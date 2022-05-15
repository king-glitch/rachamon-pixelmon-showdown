package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MainConfig {
    @Setting(value = "general", comment = "General Settings")
    private final GeneralCategorySetting mainCategorySetting = new GeneralCategorySetting();

    @Setting(value = "elo-management", comment = "Management of Elo system and how it will function")
    private final EloManagementCategorySetting eloManagementCategorySetting = new EloManagementCategorySetting();
    @Setting(value = "database", comment = "Database Settings")
    private final DatabaseCategorySetting databaseCategorySetting = new DatabaseCategorySetting();


    @ConfigSerializable
    public static class EloManagementCategorySetting {

        @Setting(comment = "Makes K-Factor same for all elo ranges [default: false]", value = "k-factor-persistent")
        protected boolean kFactorPersistent = false;

        @Setting(comment = "Makes K-Factor value [default: 30.0]", value = "k-factor-persistent-value")
        protected double kFactorPersistentValue = 30.0;

        @Setting(comment = "Makes K-Factor for high value [default: 30.0]", value = "k-factor-persistent-high-value")
        protected double kFactorPersistentHighValue = 30.0;

        @Setting(comment = "Makes K-Factor for mid value [default: = 40.0]", value = "k-factor-persistent-mid-value")
        protected double kFactorPersistentMidValue = 40.0;

        @Setting(comment = "Makes K-Factor for low value [default: 50.0]", value = "k-factor-persistent-low-value")
        protected double kFactorPersistentLowValue = 50.0;

        @Setting(comment = "Anything below low elo range is low elo [default: 1300]", value = "low-elo-range")
        protected int lowEloRange = 1300;

        @Setting(comment = "anything above high elo range is high elo [default: 1600]", value = "high-elo-range")
        protected int highEloRange = 1600;

        @Setting(comment = "default elo value [default: 1000]", value = "default-elo")
        protected int defaultElo = 1000;


        public boolean iskFactorPersistent() {
            return kFactorPersistent;
        }

        public double getKFactorPersistentValue() {
            return kFactorPersistentValue;
        }

        public double getKFactorPersistentHighValue() {
            return kFactorPersistentHighValue;
        }

        public double getKFactorPersistentMidValue() {
            return kFactorPersistentMidValue;
        }

        public double getKFactorPersistentLowValue() {
            return kFactorPersistentLowValue;
        }

        public int getLowEloRange() {
            return lowEloRange;
        }

        public int getHighEloRange() {
            return highEloRange;
        }

        public int getDefaultElo() {
            return defaultElo;
        }
    }

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

    public EloManagementCategorySetting getEloManagementCategorySetting() {
        return eloManagementCategorySetting;
    }

    public DatabaseCategorySetting getDatabaseCategorySetting() {
        return databaseCategorySetting;
    }

    public GeneralCategorySetting getMainCategorySetting() {
        return mainCategorySetting;
    }
}
