package dev.rachamon.rachamonpixelmonshowdown.managers.plugins;

import dev.rachamon.api.sponge.config.SpongeAPIConfigFactory;
import dev.rachamon.api.sponge.exception.AnnotatedCommandException;
import dev.rachamon.api.sponge.implement.plugin.IRachamonPluginManager;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdownModule;
import dev.rachamon.rachamonpixelmonshowdown.commands.PixelmonShowdownMainCommand;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.LanguageConfig;
import dev.rachamon.rachamonpixelmonshowdown.configs.MainConfig;

public class RachamonPixelmonShowdownPluginManager implements IRachamonPluginManager {
    private final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    @Override
    public void initialize() {
        this.plugin.setComponents(new RachamonPixelmonShowdown.Components());
        this.plugin.setPluginInjector(this.plugin
                .getSpongeInjector()
                .createChildInjector(new RachamonPixelmonShowdownModule()));

        this.plugin.getSpongeInjector().injectMembers(this.plugin.getComponents());

//        Sponge.getEventManager().registerListeners(this.plugin, new PokemonTokenInteract());

        this.plugin.setInitialized(true);
    }

    @Override
    public void preInitialize() {

    }

    @Override
    public void postInitialize() {
        this.reload();
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

        this.plugin.getLogger().setDebug(this.plugin.getConfig().getRoot().getMainCategorySetting().isDebug());
    }

    private void registerCommands() {
        try {
            this.plugin.getCommandService().register(new PixelmonShowdownMainCommand(), this.plugin);
        } catch (AnnotatedCommandException e) {
            e.printStackTrace();
        }
    }
}
