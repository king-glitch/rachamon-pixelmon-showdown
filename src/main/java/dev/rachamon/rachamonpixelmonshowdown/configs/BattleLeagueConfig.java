package dev.rachamon.rachamonpixelmonshowdown.configs;

import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ConfigSerializable
public class BattleLeagueConfig {

    @Setting(value = "leagues", comment = "Leagues Formats")
    private final HashMap<String, League> leagues = new HashMap<String, League>();

    public HashMap<String, League> getLeagues() {
        return this.leagues;
    }

    public static class League {
        @Setting(value = "battle-rule", comment = "battle rules using in this league")
        protected BattleRule battleRule;
        @Setting(value = "complex-clause", comment = "The particular complex format you want it banned.")
        protected List<ComplexClaus> complexClaus;
        @Setting(value = "pokemon-clause", comment = "The particular pokemons you want it banned.")
        protected List<String> pokemonClaus = new ArrayList<>();
        @Setting(value = "move-clause", comment = "The particular moves you want it banned.")
        protected List<String> moveClaus = new ArrayList<>();
        @Setting(value = "held-item-clause", comment = "The particular items you want it banned.")
        protected List<String> heldItemClause = new ArrayList<>();
        @Setting(value = "ability-clause", comment = "The particular abilities you want it banned.")
        protected List<String> abilities = new ArrayList<>();

        public League(BattleRule battleRule, List<ComplexClaus> complexClaus, List<String> pokemonClaus, List<String> moveClaus, List<String> heldItemClause, List<String> abilities) {
            this.battleRule = battleRule;
            this.complexClaus = complexClaus;
            this.pokemonClaus = pokemonClaus;
            this.moveClaus = moveClaus;
            this.heldItemClause = heldItemClause;
            this.abilities = abilities;
        }

        public BattleRule getBattleRule() {
            return this.battleRule;
        }

        public List<ComplexClaus> getComplexClaus() {
            return this.complexClaus;
        }

        public List<String> getPokemonClaus() {
            return this.pokemonClaus;
        }

        public List<String> getMoveClaus() {
            return this.moveClaus;
        }

        public List<String> getHeldItemClause() {
            return this.heldItemClause;
        }

        public List<String> getAbilities() {
            return this.abilities;
        }
    }

    public static class BattleRule {

        @Setting(value = "turn-time", comment = "maximum turn time [default: 60]")
        protected int turnTime = 60;
        @Setting(value = "level-capacity", comment = "max level pokemon can use in this battle [default: 100]")
        protected int levelCapacity = 100;
        @Setting(value = "pokemon-amount", comment = "pokemon amount to use to start the battle [default: 6]")
        protected int pokemonAmount = 6;
        @Setting(value = "enable-team-preview", comment = "enable team preview before start battle [default: false]")
        protected boolean enableTeamPreview = false;
        @Setting(value = "raise-max-level", comment = "raise pokemon to max level [default: true]")
        protected boolean raiseMaxLevel = true;
        @Setting(value = "is-double-battle", comment = "enable team battle")
        protected boolean isDoubleBattle = false;
        @Setting(value = "is-full-heal", comment = "heal pokemon to full health before start [default: false]")
        protected boolean isFullHeal = false;
        @Setting(value = "is-bag-banned", comment = "banned item to be use in battle [default: true]")
        protected boolean isBagBanned = true;

        public BattleRule(int turnTime, int levelCapacity, int pokemonAmount, boolean enableTeamPreview, boolean raiseMaxLevel, boolean isDoubleBattle, boolean isFullHeal, boolean isBagBanned) {
            this.turnTime = turnTime;
            this.levelCapacity = levelCapacity;
            this.pokemonAmount = pokemonAmount;
            this.enableTeamPreview = enableTeamPreview;
            this.raiseMaxLevel = raiseMaxLevel;
            this.isDoubleBattle = isDoubleBattle;
            this.isFullHeal = isFullHeal;
            this.isBagBanned = isBagBanned;
        }

        public int getTurnTime() {
            return this.turnTime;
        }

        public int getLevelCapacity() {
            return this.levelCapacity;
        }

        public int getPokemonAmount() {
            return this.pokemonAmount;
        }

        public boolean isEnableTeamPreview() {
            return this.enableTeamPreview;
        }

        public boolean isRaiseMaxLevel() {
            return this.raiseMaxLevel;
        }

        public boolean isDoubleBattle() {
            return this.isDoubleBattle;
        }

        public boolean isFullHeal() {
            return this.isFullHeal;
        }

        public boolean isBagBanned() {
            return this.isBagBanned;
        }
    }

    public static class ComplexClaus {
        @Setting(value = "pokemon-specie", comment = "Pokemon Specie")
        protected String pokemonSpecie;
        @Nullable
        @Setting(value = "ability", comment = "pokemon ability")
        protected String ability;
        @Nullable
        @Setting(value = "held-item", comment = "held item")
        protected String heldItem;
        @Nullable
        @Setting(value = "moves", comment = "pokemon moves")
        protected List<String> moves = new ArrayList<>();

        public ComplexClaus(String pokemonSpecie, @Nullable String ability, @Nullable String heldItem, @Nullable List<String> moves) {
            this.pokemonSpecie = pokemonSpecie;
            this.ability = ability;
            this.heldItem = heldItem;
            this.moves = moves;
        }

        public String getPokemonSpecie() {
            return this.pokemonSpecie;
        }

        @Nullable
        public String getAbility() {
            return this.ability;
        }

        @Nullable
        public String getHeldItem() {
            return this.heldItem;
        }

        @Nullable
        public List<String> getMoves() {
            return this.moves;
        }
    }

}
