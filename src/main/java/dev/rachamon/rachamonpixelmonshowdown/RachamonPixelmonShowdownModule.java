package dev.rachamon.rachamonpixelmonshowdown;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownMatchMakingManager;
import dev.rachamon.rachamonpixelmonshowdown.managers.battle.RachamonPixelmonShowdownQueueManager;

/**
 * The type Rachamon pixelmon showdown module.
 */
public class RachamonPixelmonShowdownModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RachamonPixelmonShowdownQueueManager.class).in(Scopes.SINGLETON);
        bind(RachamonPixelmonShowdownMatchMakingManager.class).in(Scopes.SINGLETON);
    }

}