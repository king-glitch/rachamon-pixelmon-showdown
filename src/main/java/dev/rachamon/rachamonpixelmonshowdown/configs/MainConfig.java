package dev.rachamon.rachamonpixelmonshowdown.configs;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * The type Main config.
 */
@ConfigSerializable
public class MainConfig {
    @Setting(value = "general", comment = "General Settings")
    private final GeneralCategorySetting mainCategorySetting = new GeneralCategorySetting();

    @Setting(value = "elo-management", comment = "Management of Elo system and how it will function")
    private final EloManagementCategorySetting eloManagementCategorySetting = new EloManagementCategorySetting();

    @Setting(value = "queue-management", comment = "Management of Queue system")
    private final QueueManagementCategorySetting queueManagementCategorySetting = new QueueManagementCategorySetting();
    @Setting(value = "database", comment = "Database Settings")
    private final DatabaseCategorySetting databaseCategorySetting = new DatabaseCategorySetting();


    /**
     * The type Queue management category setting.
     */
    @ConfigSerializable
    public static class QueueManagementCategorySetting {
        /**
         * Instantiates a new Queue management category setting.
         */
        public QueueManagementCategorySetting() {
        }

        /**
         * The matchmaker timer.
         */
        @Setting(value = "match-maker-timer", comment = "Sets the time the queue waits to attempt matching players (seconds) [default: 10]")
        protected int matchMakerTimer = 10;

        /**
         * The matchmaker bias value.
         */
        @Setting(value = "match-maker-bias-value", comment = "Value that match making will expand per check. Higher values will decrease wait time but lower match quality. [default: 25]")
        protected int matchMakerBiasValue = 25;

        /**
         * The Battle preparation time.
         */
        @Setting(value = "battle-preparation-time", comment = "Preparation time players have before battle commences in seconds [default: 30]")
        protected int battlePreparationTime = 30;

        /**
         * The Team preview time.
         */
        @Setting(value = "team-preview-time", comment = "Time preview is active for (Time included in battle preparation time -- do not set equal or higher than battle preparation!) [default: 15]")
        protected int teamPreviewTime = 15;

        /**
         * The Match threshold value.
         */
        @Setting(value = "match-threshold-value", comment = "The range people will try to be matched within. Higher values will decrease wait time but lower match quality [default: 150]")
        protected int matchThresholdValue = 150;


        /**
         * Gets matchmaker timer.
         *
         * @return the matchmaker timer
         */
        public int getMatchMakerTimer() {
            return matchMakerTimer;
        }

        /**
         * Gets matchmaker bias value.
         *
         * @return the matchmaker bias value
         */
        public int getMatchMakerBiasValue() {
            return matchMakerBiasValue;
        }

        /**
         * Gets battle preparation time.
         *
         * @return the battle preparation time
         */
        public int getBattlePreparationTime() {
            return battlePreparationTime;
        }

        /**
         * Gets team preview time.
         *
         * @return the team preview time
         */
        public int getTeamPreviewTime() {
            return teamPreviewTime;
        }

        /**
         * Gets match threshold value.
         *
         * @return the match threshold value
         */
        public int getMatchThresholdValue() {
            return matchThresholdValue;
        }
    }

    /**
     * The type Elo management category setting.
     */
    @ConfigSerializable
    public static class EloManagementCategorySetting {
        /**
         * Instantiates a new Elo management category setting.
         */
        public EloManagementCategorySetting() {
        }

        /**
         * The KFactor persistent.
         */
        @Setting(comment = "Makes K-Factor same for all elo ranges [default: false]", value = "k-factor-persistent")
        protected boolean kFactorPersistent = false;

        /**
         * The KFactor persistent value.
         */
        @Setting(comment = "Makes K-Factor value [default: 30.0]", value = "k-factor-persistent-value")
        protected double kFactorPersistentValue = 30.0;

        /**
         * The KFactor persistent high value.
         */
        @Setting(comment = "Makes K-Factor for high value [default: 30.0]", value = "k-factor-persistent-high-value")
        protected double kFactorPersistentHighValue = 30.0;

        /**
         * The KFactor persistent mid value.
         */
        @Setting(comment = "Makes K-Factor for mid value [default: = 40.0]", value = "k-factor-persistent-mid-value")
        protected double kFactorPersistentMidValue = 40.0;

        /**
         * The KFactor persistent low value.
         */
        @Setting(comment = "Makes K-Factor for low value [default: 50.0]", value = "k-factor-persistent-low-value")
        protected double kFactorPersistentLowValue = 50.0;

        /**
         * The Low elo range.
         */
        @Setting(comment = "Anything below low elo range is low elo [default: 1300]", value = "low-elo-range")
        protected int lowEloRange = 1300;

        /**
         * The High elo range.
         */
        @Setting(comment = "anything above high elo range is high elo [default: 1600]", value = "high-elo-range")
        protected int highEloRange = 1600;

        /**
         * The Default elo.
         */
        @Setting(comment = "default elo value [default: 1000]", value = "default-elo")
        protected int defaultElo = 1000;


        /**
         * Isk factor persistent boolean.
         *
         * @return the boolean
         */
        public boolean iskFactorPersistent() {
            return kFactorPersistent;
        }

        /**
         * Gets k factor persistent value.
         *
         * @return the k factor persistent value
         */
        public double getKFactorPersistentValue() {
            return kFactorPersistentValue;
        }

        /**
         * Gets k factor persistent high value.
         *
         * @return the k factor persistent high value
         */
        public double getKFactorPersistentHighValue() {
            return kFactorPersistentHighValue;
        }

        /**
         * Gets k factor persistent mid value.
         *
         * @return the k factor persistent mid value
         */
        public double getKFactorPersistentMidValue() {
            return kFactorPersistentMidValue;
        }

        /**
         * Gets k factor persistent low value.
         *
         * @return the k factor persistent low value
         */
        public double getKFactorPersistentLowValue() {
            return kFactorPersistentLowValue;
        }

        /**
         * Gets low elo range.
         *
         * @return the low elo range
         */
        public int getLowEloRange() {
            return lowEloRange;
        }

        /**
         * Gets high elo range.
         *
         * @return the high elo range
         */
        public int getHighEloRange() {
            return highEloRange;
        }

        /**
         * Gets default elo.
         *
         * @return the default elo
         */
        public int getDefaultElo() {
            return defaultElo;
        }
    }

    /**
     * The type General category setting.
     */
    @ConfigSerializable
    public static class GeneralCategorySetting {
        /**
         * Instantiates a new General category setting.
         */
        public GeneralCategorySetting() {
        }

        /**
         * The Debug.
         */
        @Setting(comment = "enable debug ? [default: false]", value = "is-debug")
        protected boolean debug = true;

        /**
         * Is debug boolean.
         *
         * @return the boolean
         */
        public boolean isDebug() {
            return debug;
        }
    }

    /**
     * The type Database category setting.
     */
    @ConfigSerializable
    public static class DatabaseCategorySetting {
        /**
         * Instantiates a new Database category setting.
         */
        public DatabaseCategorySetting() {
        }

        /**
         * Enable my sql.
         */
        @Setting(comment = "enable mysql ? [default: false]", value = "enable-mysql")
        protected boolean enableMySql = false;

        /**
         * The Host name.
         */
        @Setting(comment = "host name", value = "host-name")
        protected String hostName = "";

        /**
         * The Port.
         */
        @Setting(comment = "host port", value = "port")
        protected int port = 3306;

        /**
         * The Database name.
         */
        @Setting(comment = "database name", value = "database-name")
        protected String databaseName = "";

        /**
         * The Username.
         */
        @Setting(comment = "username", value = "username")
        protected String username = "";

        /**
         * The Password.
         */
        @Setting(comment = "database password", value = "password")
        protected String password = "";

        /**
         * Enable ssl.
         */
        @Setting(comment = "use SSL", value = "enable-ssl")
        protected boolean enableSSL = false;

        /**
         * Is enable my sql boolean.
         *
         * @return the boolean
         */
        public boolean isEnableMySql() {
            return enableMySql;
        }

        /**
         * Gets host name.
         *
         * @return the host name
         */
        public String getHostName() {
            return hostName;
        }

        /**
         * Gets database name.
         *
         * @return the database name
         */
        public String getDatabaseName() {
            return databaseName;
        }

        /**
         * Gets username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Gets password.
         *
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Gets enable ssl.
         *
         * @return enable ssl
         */
        public boolean getEnableSSL() {
            return enableSSL;
        }

        /**
         * Gets port.
         *
         * @return the port
         */
        public int getPort() {
            return port;
        }
    }

    /**
     * Gets queue management category setting.
     *
     * @return the queue management category setting
     */
    public QueueManagementCategorySetting getQueueManagementCategorySetting() {
        return queueManagementCategorySetting;
    }

    /**
     * Gets elo management category setting.
     *
     * @return the elo management category setting
     */
    public EloManagementCategorySetting getEloManagementCategorySetting() {
        return eloManagementCategorySetting;
    }

    /**
     * Gets database category setting.
     *
     * @return the database category setting
     */
    public DatabaseCategorySetting getDatabaseCategorySetting() {
        return databaseCategorySetting;
    }

    /**
     * Gets main category setting.
     *
     * @return the main category setting
     */
    public GeneralCategorySetting getMainCategorySetting() {
        return mainCategorySetting;
    }
}
