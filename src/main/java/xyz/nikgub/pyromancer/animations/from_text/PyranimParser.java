package xyz.nikgub.pyromancer.animations.from_text;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import org.joml.Vector3f;
import xyz.nikgub.pyromancer.PyromancerMod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * <h1>Class to convert .pyranim format files into {@link AnimationDefinition}</h1>
 * <p>.pyranim describes an animation and consists of total animation length and model parts' keyframes.<p/>
 * <h2>Syntax of .pyranim</h2>
 * NOTE: in the following text %something% represents either a number or a string
 * <h3>Total animation length</h3>
 * <p>Announced as !_%somefloatnumber%_!, must be a first line</p>
 * <h3>Model parts</h3>
 * <p>Announced with $_%somemodelpart%_$, must be terminated with $$ at the end</p>
 * <h3>Keyframes</h3>
 * <p>Consist of 3 parts in 1 line: type-defining character (T, R or S), moment in time in which the keyframe is placed (@%somefloatnumber%)
 * and a vector (a b c), where a, b and c are floats. All parts must be written in one line back-to-back without any symbols in between</p>
 *
 * <p>For further reference, see test.pyranim in resources</p>
 */
public class PyranimParser {

    private final List<String> contents;
    private Map<String, List<AnimationChannel>> map = new HashMap<>();
    private float animLength = 0f;

    public PyranimParser(String location)
    {
        float t = 0f;
        this.contents = new ArrayList<>();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try(InputStream inputStream = classloader.getResourceAsStream(location))
        {
            if(inputStream == null) return;
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            for (String s; (s = bufferedReader.readLine()) != null;)
            {
                PyromancerMod.LOGGER.info(">> | " + s);
                if(s.matches("!_[0-9]+(.[0-9]+)?_!"))
                {
                    t = Float.parseFloat(s.substring(s.indexOf("!_") + 2, s.indexOf("_!")));
                    s = "";
                }
                if(!s.equals("")) this.contents.add(s);
            }
            this.animLength = t;

        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to locate a file '" + location + "' in resources");
        }
    }

    private static Vector3f parseVector(String s)
    {
        Vector3f vector3f = new Vector3f();
        String nb = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        vector3f.x = Float.parseFloat(nb.substring(0, nb.indexOf(' ')));
        nb = nb.substring(nb.indexOf(" ") + 1);
        vector3f.y = Float.parseFloat(nb.substring(0, nb.indexOf(' ')));
        nb = nb.substring(nb.indexOf(" ") + 1);
        vector3f.z = Float.parseFloat(nb);
        return vector3f;
    }

    private void toMap()
    {
        PyromancerMod.LOGGER.info("---------------------------------------");
        Vector3f vector3f;
        Map<String, List<AnimationChannel>> resMap = new HashMap<>();
        List<Keyframe> rotationKeyframes = new ArrayList<>();
        List<Keyframe> translationKeyframes = new ArrayList<>();
        List<Keyframe> scaleKeyframes = new ArrayList<>();
        Keyframe[] kfR, kfT, kfS;
        String currKey = "";
        for(String line : this.contents)
        {
            if(line.equals("$$"))
            {
                kfR = new Keyframe[rotationKeyframes.size()];
                kfT = new Keyframe[translationKeyframes.size()];
                kfS = new Keyframe[scaleKeyframes.size()];
                kfR = rotationKeyframes.toArray(kfR);
                kfT = translationKeyframes.toArray(kfT);
                kfS = scaleKeyframes.toArray(kfS);
                if(kfT.length > 0) resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.POSITION, kfT));
                if(kfR.length > 0) resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.ROTATION, kfR));
                if(kfS.length > 0) resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.SCALE, kfS));
                rotationKeyframes = new ArrayList<>();
                translationKeyframes = new ArrayList<>();
                scaleKeyframes = new ArrayList<>();
                continue;
            }
            if(line.matches("[RST]@[0-9]+(.[0-9]+)?@[(]-?[0-9]+(.[0-9]+)? -?[0-9]+(.[0-9]+)? -?[0-9]+(.[0-9]+)?[)][LC]?"))
            {
                PyromancerMod.LOGGER.info(">> | " + line);
                vector3f = parseVector(line.substring(line.indexOf("@(") + 2, line.indexOf(")")) + ')');
                float moment = Float.parseFloat(line.substring(line.indexOf("T@") + 3, line.indexOf("@(")));
                AnimationChannel.Interpolation interpolation = line.charAt(line.length()-1) == 'C' ? AnimationChannel.Interpolations.CATMULLROM : AnimationChannel.Interpolations.LINEAR;
                switch (line.charAt(0))
                {
                    case ('T') -> translationKeyframes.add(new Keyframe(
                            moment,
                            KeyframeAnimations.posVec(vector3f.x, vector3f.y, vector3f.z),
                            interpolation
                    ));
                    case ('R') -> rotationKeyframes.add(new Keyframe(
                            moment,
                            KeyframeAnimations.degreeVec(vector3f.x, vector3f.y, vector3f.z),
                            interpolation
                    ));
                    case ('S') -> scaleKeyframes.add(new Keyframe(
                            moment,
                            KeyframeAnimations.scaleVec(vector3f.x, vector3f.y, vector3f.z),
                            interpolation
                    ));
                }
                continue;
            }
            if(line.matches("[$]_[a-zA-Z]+_[$]"))
            {
                currKey = line.substring(line.indexOf("$_") + 2, line.indexOf("_$"));
                resMap.put(currKey, new ArrayList<>());
            }
        }
        this.map = resMap;
    }

    public AnimationDefinition create()
    {
        this.toMap();
        AnimationDefinition.Builder builder = AnimationDefinition.Builder.withLength(this.animLength);
        for(String part : map.keySet())
        {
            for(AnimationChannel channel : map.get(part))
            {
                //PyromancerMod.LOGGER.info(">> | " + Arrays.stream(channel.keyframes()).sequential() + " | " + channel.keyframes().length);
                builder.addAnimation(part, channel);
            }
        }
        return builder.build();
    }
}
