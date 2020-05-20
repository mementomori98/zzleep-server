package zzleep.core.settings;

public class EnvironmentSettings {

    private static String environment;

    static {
        String env = System.getenv("_environment");
        if (env == null)
            environment = "local";
        else
            environment = env;
    }

    public static String getEnvironment() {
        return environment;
    }

}
