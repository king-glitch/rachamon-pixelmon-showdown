package dev.rachamon.rachamonpixelmonshowdown.utils;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;

public class BattleUtil {

    public static int calculateWinElo(int me, int opponent) {
        double kFactor = BattleUtil.getKFactor(opponent);
        int newEloValue = (int) (Math.round((me + kFactor * (1.0 - BattleUtil.getExpectedOutcome(me, opponent)))));

        int defaultElo = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getEloManagementCategorySetting()
                .getDefaultElo();

        return Math.max(newEloValue, defaultElo);
    }

    public static int calculateLoseElo(int me, int opponent) {
        double kFactor = BattleUtil.getKFactor(opponent);
        int newEloValue = (int) (Math.round((me + kFactor * (0.0 - BattleUtil.getExpectedOutcome(me, opponent)))));

        int defaultElo = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getEloManagementCategorySetting()
                .getDefaultElo();

        return Math.max(newEloValue, defaultElo);
    }

    private static double getExpectedOutcome(int me, int opponent) {
        return 1 / (1 + Math.pow(10, (opponent - me) / 400.0));
    }

    public static double getKFactor(int elo) {

        if (RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getEloManagementCategorySetting()
                .iskFactorPersistent()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getRoot()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentValue();
        }

        if (elo < RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getEloManagementCategorySetting()
                .getLowEloRange()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getRoot()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentLowValue();
        } else if (elo < RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getRoot()
                .getEloManagementCategorySetting()
                .getHighEloRange()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getRoot()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentMidValue();
        } else {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getRoot()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentHighValue();
        }

    }

}
