package frc.robot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import io.github.classgraph.ClassInfo;

public class Init {
    private static void errorDuringInit(final String typename) {
        System.err.println("class " +
                typename +
                " failed to initialize, exiting");
        System.exit(1);
    }

    private static void initialize_systems(final List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            final String typename = Objects.requireNonNullElse(clazz.getCanonicalName(), "<Class>");

            try {
                if (!(Boolean) clazz.getMethod("initialize").invoke(null))
                    errorDuringInit(typename);
                System.err.println("finished initializing " + typename);
            } catch (NoSuchMethodError | NoSuchMethodException e) {
                System.err.println("initialize method for " +
                        typename +
                        " cannot be accessed, skiping");
            } catch (SecurityException e) {
                System.err.println("class " +
                        typename +
                        " cannot be accessed through reflection");
            } catch (IllegalAccessException e) {
                System.err.println("public constructor for" +
                        typename +
                        " cannot be accessed, skiping");
            } catch (IllegalArgumentException e) {
                System.err.println("initliaize method for " +
                        typename +
                        "takes a wrong amount/type of arguments, exiting");
                System.exit(1);
            } catch (InvocationTargetException e) {
                errorDuringInit(typename);
            }
        }
    }

    private static List<Class<?>> get_classes() {
        return new io.github.classgraph.ClassGraph().acceptPackages("frc.robot.system").scan().getAllClasses().stream()
                .sorted()
                .map(ClassInfo::loadClass)
                .collect(Collectors.toList());
    }
    private static void init() {
        final List<Class<?>> classes = get_classes();
        initialize_systems(classes);
    }
    public static void initialize() {
        init();
        System.gc();
    }
}
