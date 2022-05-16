package dev.rachamon.rachamonpixelmonshowdown.managers.plugins;

import dev.rachamon.api.common.database.MySQLConnectorProvider;
import dev.rachamon.api.common.database.SQLiteConnectorProvider;
import dev.rachamon.api.sponge.config.SpongeAPIConfigFactory;
import dev.rachamon.api.sponge.exception.AnnotatedCommandException;
import dev.rachamon.api.sponge.implement.plugin.IRachamonPluginManager;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdownModule;
import dev.rachamon.rachamonpixelmonshowdown.commands.PixelmonShowdownMainCommand;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.LanguageConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.MainConfig;
import dev.rachamon.rachamonpixelmonshowdown.listeners.BattleListener;
import dev.rachamon.rachamonpixelmonshowdown.services.BattleLogDataService;
import dev.rachamon.rachamonpixelmonshowdown.services.PlayerDataService;
import org.spongepowered.api.Sponge;

/**
 * The type Rachamon pixelmon showdown plugin manager.
 */
public class RachamonPixelmonShowdownPluginManager implements IRachamonPluginManager {
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    @Override
    public void initialize() {
        this.plugin.setComponents(new RachamonPixelmonShowdown.Components());
        this.plugin.setInjector(this.plugin.getInjector().createChildInjector(new RachamonPixelmonShowdownModule()));
        this.plugin.getInjector().injectMembers(this.plugin.getComponents());
        Sponge.getEventManager().registerListeners(this.plugin, new BattleListener());


        this.plugin.setInitialized(true);
    }

    @Override
    public void preInitialize() {

    }

    @Override
    public void postInitialize() {
        this.reload();
        try {
            if (this.plugin.getConfig().getDatabaseCategorySetting().isEnableMySql()) {

                MainConfig.DatabaseCategorySetting databaseCategorySetting = this.plugin
                        .getConfig()
                        .getDatabaseCategorySetting();
                this.plugin.setDatabaseConnector(new MySQLConnectorProvider(databaseCategorySetting.getHostName(), databaseCategorySetting.getPort(), databaseCategorySetting.getDatabaseName(), databaseCategorySetting.getUsername(), databaseCategorySetting.getPassword(), databaseCategorySetting.getEnableSSL()));
                this.plugin.getLogger().info("Data handler connected using MySQL.");
            } else {
                this.plugin.setDatabaseConnector(new SQLiteConnectorProvider(this.plugin
                        .getDirectory()
                        .toAbsolutePath() + "/database.db"));
                this.plugin.getLogger().info("Data handler connected using SQLite.");
            }

            PlayerDataService.initialize();
            BattleLogDataService.initialize();
        } catch (Exception e) {
            this.plugin.getLogger().error("error on connecting to database");
        }

        try {
            this.plugin.getLogger().debug("initialize queue");
            this.plugin.getQueueManager().initializeQueue();
        } catch (Exception e) {
            this.plugin.getLogger().debug("error on initialize queue");
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void reload() {
        this.registerConfigs();
        this.registerCommands();


    }

    private void registerConfigs() {
        SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> config = new SpongeAPIConfigFactory<>(this.plugin, "main.conf");
        SpongeAPIConfigFactory<RachamonPixelmonShowdown, LanguageConfig> language = new SpongeAPIConfigFactory<>(this.plugin, "language.conf");
        SpongeAPIConfigFactory<RachamonPixelmonShowdown, BattleLeagueConfig> leagues = new SpongeAPIConfigFactory<>(this.plugin, "leagues.conf");

        this.plugin.setMainConfig(config);
        this.plugin.setMainLanguage(language);
        this.plugin.setMainBattleLeagueConfig(leagues);

        this.plugin.setConfig(config
                .setHeader("Main Config")
                .setClazz(new MainConfig())
                .setClazzType(MainConfig.class)
                .build());

        this.plugin.setLanguage(language
                .setHeader("Language Config")
                .setClazz(new LanguageConfig())
                .setClazzType(LanguageConfig.class)
                .build());

        this.plugin.setBattleLeagueConfig(leagues
                .setHeader("Leagues Config")
                .setClazz(new BattleLeagueConfig())
                .setClazzType(BattleLeagueConfig.class)
                .build());

        try {
            this.plugin.getLogger().setDebug(this.plugin.getConfig().getMainCategorySetting().isDebug());
        } catch (Exception ignore) {

        }

    }

    private void registerCommands() {
        try {
            this.plugin.getCommandService().register(new PixelmonShowdownMainCommand(), this.plugin);
        } catch (AnnotatedCommandException e) {
            e.printStackTrace();
        }
    }
}
