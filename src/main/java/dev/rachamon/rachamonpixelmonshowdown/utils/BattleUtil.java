package dev.rachamon.rachamonpixelmonshowdown.utils;

import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;

/**
 * The type Battle util.
 */
public class BattleUtil {

    /**
     * Calculate win elo int.
     *
     * @param me       the me
     * @param opponent the opponent
     * @return the int
     */
    public static int calculateWinElo(int me, int opponent) {
        double kFactor = BattleUtil.getKFactor(opponent);
        int newEloValue = (int) (Math.round((me + kFactor * (1.0 - BattleUtil.getExpectedOutcome(me, opponent)))));

        int defaultElo = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getEloManagementCategorySetting()
                .getDefaultElo();

        return Math.max(newEloValue, defaultElo);
    }

    /**
     * Calculate lose elo int.
     *
     * @param me       the me
     * @param opponent the opponent
     * @return the int
     */
    public static int calculateLoseElo(int me, int opponent) {
        double kFactor = BattleUtil.getKFactor(opponent);
        int newEloValue = (int) (Math.round((me + kFactor * (0.0 - BattleUtil.getExpectedOutcome(me, opponent)))));

        int defaultElo = RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getEloManagementCategorySetting()
                .getDefaultElo();

        return Math.max(newEloValue, defaultElo);
    }

    private static double getExpectedOutcome(int me, int opponent) {
        return 1 / (1 + Math.pow(10, (opponent - me) / 400.0));
    }

    /**
     * Gets k factor.
     *
     * @param elo the elo
     * @return the k factor
     */
    public static double getKFactor(int elo) {

        if (RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getEloManagementCategorySetting()
                .iskFactorPersistent()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentValue();
        }

        if (elo < RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getEloManagementCategorySetting()
                .getLowEloRange()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentLowValue();
        } else if (elo < RachamonPixelmonShowdown
                .getInstance()
                .getConfig()
                .getEloManagementCategorySetting()
                .getHighEloRange()) {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentMidValue();
        } else {
            return RachamonPixelmonShowdown
                    .getInstance()
                    .getConfig()
                    .getEloManagementCategorySetting()
                    .getKFactorPersistentHighValue();
        }

    }

}
