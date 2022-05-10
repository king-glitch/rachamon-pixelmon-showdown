package dev.rachamon.rachamonpixelmonshowdown.structures;

import java.util.UUID;

public class PlayerEloProfile {
    private UUID uuid;
    private String leagueName;
    private int win;
    private int lose;
    private int elo;

    public PlayerEloProfile(UUID uuid, String leagueName) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.win = 0;
        this.lose = 0;
        this.elo = 0;
    }

    public PlayerEloProfile(UUID uuid, String leagueName, int elo) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.elo = elo;
        this.win = 0;
        this.lose = 0;
    }

    public PlayerEloProfile(UUID uuid, String leagueName, int elo, int win, int lose) {
        this.uuid = uuid;
        this.leagueName = leagueName;
        this.elo = elo;
        this.win = win;
        this.lose = lose;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
