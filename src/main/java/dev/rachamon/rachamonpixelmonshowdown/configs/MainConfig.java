package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MainConfig {
    @Setting(value = "general", comment = "General Settings")
    private final GeneralCategorySetting mainCategorySetting = new GeneralCategorySetting();
    @Setting(value = "database", comment = "Database Settings")
    private final DatabaseCategorySetting databaseCategorySetting = new DatabaseCategorySetting();



    @ConfigSerializable
    public static class GeneralCategorySetting {

        @Setting(comment = "enable debug ? [default: false]", value = "is-debug")
        protected boolean debug = false;

        public boolean isDebug() {
            return debug;
        }
    }

    @ConfigSerializable
    public static class DatabaseCategorySetting {

        @Setting(comment = "enable mysql ? [default: false]", value = "enable-mysql")
        protected boolean enableMySql = false;

        @Setting(comment = "host name", value = "host-name")
        protected String hostName = "";

        @Setting(comment = "host port", value = "port")
        protected int port = 3306;

        @Setting(comment = "database name", value = "database-name")
        protected String databaseName = "";

        @Setting(comment = "username", value = "username")
        protected String username = "";

        @Setting(comment = "database password", value = "password")
        protected String password = "";

        @Setting(comment = "use SSL", value = "enable-ssl")
        protected boolean enableSSL = false;

        public boolean isEnableMySql() {
            return enableMySql;
        }

        public String getHostName() {
            return hostName;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean getEnableSSL() {
            return enableSSL;
        }

        public int getPort() {
            return port;
        }
    }

    public DatabaseCategorySetting getDatabaseCategorySetting() {
        return databaseCategorySetting;
    }
    public GeneralCategorySetting getMainCategorySetting() {
        return mainCategorySetting;
    }
}
