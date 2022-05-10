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
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownBattleManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.plugins.RachamonPixelmonShowdownPluginManager;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(id = "rachamonpixelmonshowdown", name = RachamonPixelmonShowdown.PLUGIN_NAME, dependencies = {@Dependency(id = "after:pixelmon")})
public class RachamonPixelmonShowdown extends RachamonSpongePluginProvider implements IRachamonPlugin {

    public static final String PLUGIN_NAME = "RachamonPixelmonShowdown";

    // injects
    @Inject
    private Game game;
    @Inject
    private GuiceObjectMapperFactory factory;
    @Inject
    private Injector injector;
    @Inject
    private Injector pluginInjector;
    @Inject
    private PluginContainer container;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path directory;

    private static RachamonPixelmonShowdown instance;
    private Components components;
    private RachamonPixelmonShowdownPluginManager pluginManager;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> config;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, LanguageConfig> language;
    private SpongeAPIConfigFactory<RachamonPixelmonShowdown, BattleLeagueConfig> league;

    private IDatabaseConnector databaseConnector;
    private boolean isInitialized = false;

    public RachamonPixelmonShowdown() {
        super(RachamonPixelmonShowdown.PLUGIN_NAME, Sponge.getServer());
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

    @Override
    public Injector getPluginInjector() {
        return this.pluginInjector;
    }

    @Override
    public Injector getSpongeInjector() {
        return this.injector;
    }

    public void setPluginInjector(Injector injector) {
        this.pluginInjector = injector;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public Components getComponents() {
        return this.components;
    }

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
        this.getLogger().info("On Pre Initialize RachamonTextureToken...");
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
     * Gets config.
     *
     * @return the config
     */
    public SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> getConfig() {
        return this.config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    public void setConfig(MainConfig config) {
        this.config.setClazz(config);
    }

    public void setMainConfig(SpongeAPIConfigFactory<RachamonPixelmonShowdown, MainConfig> config) {
        this.config = config;
    }

    public void setBattleLeagueConfig(BattleLeagueConfig league) {
        this.league.setClazz(league);
    }

    public void setMainBattleLeagueConfig(SpongeAPIConfigFactory<RachamonPixelmonShowdown, BattleLeagueConfig> league) {
        this.league = league;
    }

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

    public BattleLeagueConfig getLeague() {
        return this.league.getRoot();
    }

    public IDatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public void setDatabaseConnector(IDatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * The type Components.
     */
    public static class Components {
        @Inject
        private RachamonPixelmonShowdownBattleManager battleManager;
    }
}
