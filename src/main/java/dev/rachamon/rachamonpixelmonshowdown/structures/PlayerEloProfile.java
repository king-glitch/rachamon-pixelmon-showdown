package dev.rachamon.rachamonpixelmonshowdown.structures;

import java.util.UUID;

/**
 * The type Player elo profile.
 */
public class PlayerEloProfile {
    private UUID uuid;
    private String leagueName;
    private int win;
    private int lose;
    private int elo;

    /**
     * Instantiates a new Player elo profile.
     *
     * @param uuid       the uuid
     * @param leagueName the league name
     */
    public PlayerEloProfile(UUID uuid, String leagueName) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.win = 0;
        this.lose = 0;
        this.elo = 0;
    }

    /**
     * Instantiates a new Player elo profile.
     *
     * @param uuid       the uuid
     * @param leagueName the league name
     * @param elo        the elo
     */
    public PlayerEloProfile(UUID uuid, String leagueName, int elo) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.elo = elo;
        this.win = 0;
        this.lose = 0;
    }

    /**
     * Instantiates a new Player elo profile.
     *
     * @param uuid       the uuid
     * @param leagueName the league name
     * @param elo        the elo
     * @param win        the win
     * @param lose       the lose
     */
    public PlayerEloProfile(UUID uuid, String leagueName, int elo, int win, int lose) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.elo = elo;
        this.win = win;
        this.lose = lose;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets league name.
     *
     * @return the league name
     */
    public String getLeagueName() {
        return leagueName;
    }

    /**
     * Sets league name.
     *
     * @param leagueName the league name
     */
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * Gets win.
     *
     * @return the win
     */
    public int getWin() {
        return win;
    }

    /**
     * Sets win.
     *
     * @param win the win
     */
    public void setWin(int win) {
        this.win = win;
    }

    /**
     * Gets lose.
     *
     * @return the lose
     */
    public int getLose() {
        return lose;
    }

    /**
     * Sets lose.
     *
     * @param lose the lose
     */
    public void setLose(int lose) {
        this.lose = lose;
    }

    /**
     * Gets elo.
     *
     * @return the elo
     */
    public int getElo() {
        return elo;
    }

    /**
     * Sets elo.
     *
     * @param elo the elo
     */
    public void setElo(int elo) {
        this.elo = elo;
    }
}
