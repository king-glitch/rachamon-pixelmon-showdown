package dev.rachamon.rachamonpixelmonshowdown;

import com.google.inject.Inject;
import com.google.inject.Injector;
import dev.rachamon.api.common.database.IDatabaseConnector;
import dev.rachamon.api.sponge.command.SpongeCommandService;
import dev.rachamon.api.sponge.config.SpongeAPIConfigFactory;
import dev.rachamon.api.sponge.implement.plugin.IRachamonPlugin;
import dev.rachamon.api.sponge.implement.plugin.IRachamonPluginManager;
import dev.rachamon.api.sponge.provider.RachamonSpongePluginProvider;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.LanguageConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.MainConfig;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownMatchMakingManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownQueueManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.plugins.RachamonPixelmonShowdownPluginManager;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

/**
 * The type Rachamon pixelmon showdown.
 */
@Plugin(id = "rachamonpixelmonshowdown", name = RachamonPixelmonShowdown.PLUGIN_NAME, dependencies = {@Dependency(id = "after:pixelmon")})
public class RachamonPixelmonShowdown extends RachamonSpongePluginProvider implements IRachamonPlugin {

    /**
     * The constant PLUGIN_NAME.
     */
    public static final String PLUGIN_NAME = "RachamonPixelmonShowdown";
    @Inject
    private Game game;
    @Inject
    private GuiceObjectMapperFactory factory;
    @Inject
    private PluginContainer container;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path directory;

    @Inject
    private Injector injector;
    private static RachamonPixelmonShowdown instance;
    private RachamonPixelmonShowdownPluginManager pluginManager;
    private Components components;
    private IDatabaseConnector databaseConnector;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> config;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, LanguageConfig> language;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, BattleLeagueConfig> league;


    private boolean isInitialized = false;

    /**
     * Instantiates a new Rachamon pixelmon showdown.
     */
    public RachamonPixelmonShowdown() {
        super(PLUGIN_NAME, Sponge.getServer());
    }

    @Override
    public GuiceObjectMapperFactory getFactory() {
        return this.factory;
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public Path getDirectory() {
        return this.directory;
    }

    @Override
    public PluginContainer getContainer() {
        return this.container;
    }

    @Override
    public SpongeCommandService getCommandService() {
        return SpongeCommandService.getInstance();
    }

    @Override
    public IRachamonPluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void setInitialized(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RachamonPixelmonShowdown getInstance() {
        return RachamonPixelmonShowdown.instance;
    }

    /**
     * On pre initialize.
     *
     * @param event the event
     */
    @Listener
    public void onPreInitialize(GamePreInitializationEvent event) {
        instance = this;
        this.pluginManager = new RachamonPixelmonShowdownPluginManager();
        this.getLogger().info("On Pre Initialize RachamonPixelmonShowdown...");
    }

    /**
     * On initialize.
     *
     * @param event the event
     */
    @Listener(order = Order.EARLY)
    public void onInitialize(GameInitializationEvent event) {
        RachamonPixelmonShowdown.getInstance().getLogger().info("On Initialize RachamonPixelmonShowdown...");
        RachamonPixelmonShowdown.getInstance().getPluginManager().initialize();
    }

    /**
     * On start.
     *
     * @param event the event
     */
    @Listener
    public void onStart(GameStartedServerEvent event) {
        if (!this.isInitialized()) return;
        RachamonPixelmonShowdown.getInstance().getLogger().info("On Start RachamonPixelmonShowdown...");
        RachamonPixelmonShowdown.getInstance().getPluginManager().start();
    }

    /**
     * On post initialize.
     *
     * @param event the event
     */
    @Listener
    public void onPostInitialize(GamePostInitializationEvent event) {
        RachamonPixelmonShowdown.getInstance().getLogger().info("On Post Initialize RachamonPixelmonShowdown");
        RachamonPixelmonShowdown.getInstance().getPluginManager().postInitialize();
    }

    /**
     * On stop.
     *
     * @param event the event
     */
    @Listener
    public void onStop(GameStoppingServerEvent event) {
        if (!this.isInitialized()) return;
        RachamonPixelmonShowdown.getInstance().getLogger().info("On stop RachamonPixelmonShowdown...");
        this.getDatabaseConnector().closeConnection();
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public MainConfig getConfig() {
        return this.config.getRoot();
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    public void setConfig(MainConfig config) {
        this.config.setClazz(config);
    }

    /**
     * Sets main config.
     *
     * @param config the config
     */
    public void setMainConfig(SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> config) {
        this.config = config;
    }

    /**
     * Sets battle league config.
     *
     * @param league the league
     */
    public void setBattleLeagueConfig(BattleLeagueConfig league) {
        this.league.setClazz(league);
    }

    /**
     * Sets main battle league config.
     *
     * @param league the league
     */
    public void setMainBattleLeagueConfig(SpongeAPIConfigFactory<RachamonPixelmonShowdown, BattleLeagueConfig> league) {
        this.league = league;
    }

    /**
     * Sets main language.
     *
     * @param language the language
     */
    public void setMainLanguage(SpongeAPIConfigFactory<RachamonPixelmonShowdown, LanguageConfig> language) {
        this.language = language;
    }

    /**
     * Sets language.
     *
     * @param language the language
     */
    public void setLanguage(LanguageConfig language) {
        this.language.setClazz(language);
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public LanguageConfig getLanguage() {
        return this.language.getRoot();
    }

    /**
     * Gets league.
     *
     * @return the league
     */
    public BattleLeagueConfig getLeague() {
        return this.league.getRoot();
    }

    /**
     * Gets database connector.
     *
     * @return the database connector
     */
    public IDatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    /**
     * Sets database connector.
     *
     * @param databaseConnector the database connector
     */
    public void setDatabaseConnector(IDatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Gets components.
     *
     * @return the components
     */
    public Components getComponents() {
        return this.components;
    }

    /**
     * Sets components.
     *
     * @param components the components
     */
    public void setComponents(Components components) {
        this.components = components;
    }

    /**
     * Gets queue manager.
     *
     * @return the queue manager
     */
    public RachamonPixelmonShowdownQueueManager getQueueManager() {
        return this.getComponents().queueManager;
    }

    /**
     * Gets match making manager.
     *
     * @return the match making manager
     */
    public RachamonPixelmonShowdownMatchMakingManager getMatchMakingManager() {
        return this.getComponents().matchMakingManager;
    }

    /**
     * Gets injector.
     *
     * @return the injector
     */
    public Injector getInjector() {
        return injector;
    }

    /**
     * Sets injector.
     *
     * @param injector the injector
     */
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    /**
     * The type Components.
     */
    public static class Components {
        @Inject
        private RachamonPixelmonShowdownQueueManager queueManager;
        @Inject
        private RachamonPixelmonShowdownMatchMakingManager matchMakingManager;
    }
}
