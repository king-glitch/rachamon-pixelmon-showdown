package dev.rachamon.rachamonpixelmonshowdown;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownBattleManager;

public class RachamonPixelmonShowdownModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RachamonPixelmonShowdownBattleManager.class).in(Scopes.SINGLETON);
    }
}
