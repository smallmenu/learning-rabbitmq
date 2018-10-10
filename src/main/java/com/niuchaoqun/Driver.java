package com.niuchaoqun;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author niuchaoqun
 */
public class Driver {
    int level = 0;

    private Map<String, ProgramDescription> programs;

    public Driver() {
        programs = new TreeMap<String, ProgramDescription>();
    }

    private void printUsage(Map<String, ProgramDescription> programs) {
        String tips = this.level == 0 ? "example" : "sub-example";

        System.out.println("Valid " + tips + " program names are:");
        for (Map.Entry<String, ProgramDescription> item : programs.entrySet()) {
            System.out.println("  " + item.getKey() + ": " + item.getValue().getDescription());
        }
    }

    public void addClass(String name, Class<?> mainClass, String description) throws Throwable {
        programs.put(name, new ProgramDescription(mainClass, description));
    }

    public int run(String[] args, int level) throws Throwable {
        this.level = level;

        String tips = level == 0 ? "example" : "sub-example";

        // Make sure they gave us a program name.
        if (args.length == 0) {
            System.out.println("An " + tips + " program must be given as the first argument.");
            printUsage(programs);
            return -1;
        }

        // And that it is good.
        ProgramDescription pgm = programs.get(args[0]);
        if (pgm == null) {
            System.out.println("Unknown " + tips + " program '" + args[0] + "' chosen.");
            printUsage(programs);
            return -1;
        }

        // Remove the leading argument and call main
        String[] new_args = new String[args.length - 1];
        for (int i = 1; i < args.length; ++i) {
            new_args[i - 1] = args[i];
        }
        pgm.invoke(new_args);
        return 0;
    }

    static private class ProgramDescription {

        private Method main;
        private String description;

        static final Class<?>[] paramTypes = new Class<?>[] { String[].class };

        public ProgramDescription(Class<?> mainClass, String description)
                throws SecurityException, NoSuchMethodException {
            this.main = mainClass.getMethod("run", paramTypes);
            this.description = description;
        }

        public void invoke(String[] args) throws Throwable {
            try {
                main.invoke(null, new Object[] { args });
            } catch (InvocationTargetException except) {
                throw except.getCause();
            }
        }

        public String getDescription() {
            return description;
        }
    }
}
