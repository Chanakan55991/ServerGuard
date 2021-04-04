package live.chanakancloud.serverguard.check;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import live.chanakancloud.serverguard.check.impl.fly.FlyA;
import live.chanakancloud.serverguard.check.impl.speed.SpeedA;

/**
 * @author Braydon
 */
public class CheckManager {
    /**
     * A {@link List} of registered check classes
     * @see Check
     */
    public static final List<Class<? extends Check>> CHECK_CLASSES = Collections.unmodifiableList(Arrays.asList(
            // Test
            SpeedA.class,

            // Fly
            FlyA.class
    ));

    /**
     * Get the check class with the given class name
     *
     * @param name the name of the class
     * @return the check with the given name if found, otherwise null
     */
    public static Class<? extends Check> getCheckClass(String name) {
        for (Class<? extends Check> checkClass : CHECK_CLASSES) {
            if (checkClass.getSimpleName().equalsIgnoreCase(name)) {
                return checkClass;
            }
        }
        return null;
    }
}