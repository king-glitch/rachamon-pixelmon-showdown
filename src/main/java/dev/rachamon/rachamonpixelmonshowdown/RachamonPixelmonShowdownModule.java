package dev.rachamon.rachamonpixelmonshowdown;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownBattleManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownMatchMakingManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownQueueManager;

public class RachamonPixelmonShowdownModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RachamonPixelmonShowdownBattleManager.class).in(Scopes.SINGLETON);
        bind(RachamonPixelmonShowdownQueueManager.class).in(Scopes.SINGLETON);
        bind(RachamonPixelmonShowdownMatchMakingManager.class).in(Scopes.SINGLETON);
    }

}
