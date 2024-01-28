package xyz.nikgub.pyromancer.animations.from_text;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.POSITION, kfT));
                resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.ROTATION, kfR));
                resMap.get(currKey).add(new AnimationChannel(AnimationChannel.Targets.SCALE, kfS));
                rotationKeyframes = new ArrayList<>();
                translationKeyframes = new ArrayList<>();
                scaleKeyframes = new ArrayList<>();
                currKey = "";
                continue;
            }
            if(!currKey.equals(""))
            {
                if(line.matches("[RST]@[0-9]+(.[0-9]+)?@[(][0-9]+(.[0-9]+)? [0-9]+(.[0-9]+)? [0-9]+(.[0-9]+)?[)]"))
                {
                    vector3f = parseVector(line.substring(line.indexOf("@(") + 2, line.indexOf(")")) + ')');
                    float moment = Float.parseFloat(line.substring(line.indexOf("T@") + 3, line.indexOf("@(")));
                    switch (line.charAt(0))
                    {
                        case ('T') -> translationKeyframes.add(new Keyframe(
                                moment,
                                KeyframeAnimations.posVec(vector3f.x, vector3f.y, vector3f.z),
                                AnimationChannel.Interpolations.LINEAR
                        ));
                        case ('R') -> rotationKeyframes.add(new Keyframe(
                                moment,
                                KeyframeAnimations.degreeVec(vector3f.x, vector3f.y, vector3f.z),
                                AnimationChannel.Interpolations.LINEAR
                        ));
                        case ('S') -> scaleKeyframes.add(new Keyframe(
                                moment,
                                KeyframeAnimations.scaleVec(vector3f.x, vector3f.y, vector3f.z),
                                AnimationChannel.Interpolations.LINEAR
                        ));
                    }
                    continue;
                }
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
                builder.addAnimation(part, channel);
            }
        }
        return builder.build();
    }
}
