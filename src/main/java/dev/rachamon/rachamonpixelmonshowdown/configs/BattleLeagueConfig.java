package dev.rachamon.rachamonpixelmonshowdown.configs;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Battle league config.
 */
@ConfigSerializable
public class BattleLeagueConfig {

    @Setting(value = "leagues", comment = "Leagues Formats")
    private final Map<String, League> leagues = this.initialize();


    private Map<String, League> initialize() {
        Map<String, League> leagues = new HashMap<>();
        BattleRule battleRule = new BattleRule(45, 50, 4, true, true, true, true, true);
        leagues.put("vcg-series-12", new League(battleRule, new ArrayList<>(), new ArrayList<String>() {{
            add("Mew");
            add("Celebi");
            add("Jirachi");
            add("Victini");
            add("Keldeo");
            add("Genesect");
            add("Diancie");
            add("Volcanion");
            add("Magearna");
            add("Marshadow");
            add("Zeraora");
            add("Meltan");
            add("Melmetal");
            add("Zarude");


        }}, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, 2, false, false));
        return leagues;
    }

    /**
     * Gets leagues.
     *
     * @return the leagues
     */
    public Map<String, League> getLeagues() {
        return this.leagues;
    }

    /**
     * The type League.
     */
    @ConfigSerializable
    public static class League {

        /**
         * Instantiates a new League.
         */
        public League() {
        }

        /**
         * The Battle rule.
         */
        @Setting(value = "battle-rule", comment = "battle rules using in this league")
        protected BattleRule battleRule;
        /**
         * The Complex claus.
         */
        @Setting(value = "complex-clause", comment = "The particular complex format you want it banned.")
        protected List<ComplexClaus> complexClaus;
        /**
         * The Pokemon claus.
         */
        @Setting(value = "pokemon-clause", comment = "The particular pokemons you want it banned.")
        protected List<String> pokemonClaus = new ArrayList<>();
        /**
         * The Move claus.
         */
        @Setting(value = "move-clause", comment = "The particular moves you want it banned.")
        protected List<String> moveClaus = new ArrayList<>();
        /**
         * The Held item clause.
         */
        @Setting(value = "held-item-clause", comment = "The particular items you want it banned.")
        protected List<String> heldItemClause = new ArrayList<>();
        /**
         * The Abilities.
         */
        @Setting(value = "ability-clause", comment = "The particular abilities you want it banned.")
        protected List<String> abilityClause = new ArrayList<>();

        /**
         * The Monotype clause.
         */
        @Nullable
        @Setting(value = "monotype-clause", comment = "The particular type you want it banned.")
        protected String monotypeClause = null;

        /**
         * The Legendary limit clause.
         */
        @Setting(value = "legendary-limit-clause", comment = "Limit the amount of legendary.")
        protected int legendaryLimitClause = 6;

        /**
         * The Allow duplicate item clause.
         */
        @Setting(value = "allow-duplicate-item-clause", comment = "Allow same item in the party.")
        protected boolean allowDuplicateItemClause = false;

        /**
         * The Allow duplicate pokemon clause.
         */
        @Setting(value = "allow-duplicate-pokemon-clause", comment = "Allow same pokemon in the party.")
        protected boolean allowDuplicatePokemonClause = false;


        /**
         * Instantiates a new League.
         *
         * @param battleRule                  the battle rule
         * @param complexClaus                the complex claus
         * @param pokemonClaus                the pokemon claus
         * @param moveClaus                   the move claus
         * @param heldItemClause              the held item clause
         * @param abilities                   the abilities
         * @param monotypeClause              the monotype clause
         * @param legendaryLimitClause        the legendary limit clause
         * @param allowDuplicateItemClause    the allow duplicate item clause
         * @param allowDuplicatePokemonClause the allow duplicate pokemon clause
         */
        public League(BattleRule battleRule, List<ComplexClaus> complexClaus, List<String> pokemonClaus, List<String> moveClaus, List<String> heldItemClause, List<String> abilities, @Nullable String monotypeClause, int legendaryLimitClause, boolean allowDuplicateItemClause, boolean allowDuplicatePokemonClause) {
            this.battleRule = battleRule;
            this.complexClaus = complexClaus;
            this.pokemonClaus = pokemonClaus;
            this.moveClaus = moveClaus;
            this.heldItemClause = heldItemClause;
            this.abilityClause = abilities;
            this.monotypeClause = monotypeClause;
            this.legendaryLimitClause = legendaryLimitClause;
            this.allowDuplicateItemClause = allowDuplicateItemClause;
            this.allowDuplicatePokemonClause = allowDuplicatePokemonClause;
        }

        /**
         * Gets battle rule.
         *
         * @return the battle rule
         */
        public BattleRule getBattleRule() {
            return this.battleRule;
        }

        /**
         * Gets complex claus.
         *
         * @return the complex claus
         */
        public List<ComplexClaus> getComplexClaus() {
            return this.complexClaus;
        }

        /**
         * Gets pokemon claus.
         *
         * @return the pokemon claus
         */
        public List<String> getPokemonClaus() {
            return this.pokemonClaus;
        }

        /**
         * Gets move claus.
         *
         * @return the move claus
         */
        public List<String> getMoveClaus() {
            return this.moveClaus;
        }

        /**
         * Gets held item clause.
         *
         * @return the held item clause
         */
        public List<String> getHeldItemClause() {
            return this.heldItemClause;
        }

        /**
         * Gets abilities.
         *
         * @return the abilities
         */
        public List<String> getAbilityClause() {
            return this.abilityClause;
        }

        /**
         * Gets monotype clause.
         *
         * @return the monotype clause
         */
        @Nullable
        public String getMonotypeClause() {
            return monotypeClause;
        }

        /**
         * Gets legendary limit clause.
         *
         * @return the legendary limit clause
         */
        public int getLegendaryLimitClause() {
            return legendaryLimitClause;
        }

        /**
         * Is allow duplicate item clause boolean.
         *
         * @return the boolean
         */
        public boolean isAllowDuplicateItemClause() {
            return allowDuplicateItemClause;
        }

        /**
         * Is allow duplicate pokemon clause boolean.
         *
         * @return the boolean
         */
        public boolean isAllowDuplicatePokemonClause() {
            return allowDuplicatePokemonClause;
        }
    }

    /**
     * The type Battle rule.
     */
    @ConfigSerializable
    public static class BattleRule {
        /**
         * Instantiates a new Battle rule.
         */
        public BattleRule() {
        }

        /**
         * The Turn time.
         */
        @Setting(value = "turn-time", comment = "maximum turn time [default: 60]")
        protected int turnTime = 60;
        /**
         * The Level capacity.
         */
        @Setting(value = "level-capacity", comment = "max level pokemon can use in this battle [default: 100]")
        protected int levelCapacity = 100;
        /**
         * The Pokemon amount.
         */
        @Setting(value = "pokemon-amount", comment = "pokemon amount to use to start the battle [default: 6]")
        protected int pokemonAmount = 6;
        /**
         * The Enable team preview.
         */
        @Setting(value = "enable-team-preview", comment = "enable team preview before start battle [default: false]")
        protected boolean enableTeamPreview = false;
        /**
         * The Raise max level.
         */
        @Setting(value = "raise-max-level", comment = "raise pokemon to max level [default: true]")
        protected boolean raiseMaxLevel = true;
        /**
         * The Is double battle.
         */
        @Setting(value = "is-double-battle", comment = "enable team battle")
        protected boolean isDoubleBattle = false;
        /**
         * The Is full heal.
         */
        @Setting(value = "is-full-heal", comment = "heal pokemon to full health before start [default: false]")
        protected boolean isFullHeal = false;
        /**
         * The Is bag banned.
         */
        @Setting(value = "is-bag-banned", comment = "banned item to be use in battle [default: true]")
        protected boolean isBagBanned = true;

        /**
         * Instantiates a new Battle rule.
         *
         * @param turnTime          the turn time
         * @param levelCapacity     the level capacity
         * @param pokemonAmount     the Pokémon amount
         * @param enableTeamPreview the enable team preview
         * @param raiseMaxLevel     the raise max level
         * @param isDoubleBattle    the is double battle
         * @param isFullHeal        the is full heal
         * @param isBagBanned       the is bag banned
         */
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

        /**
         * Gets turn time.
         *
         * @return the turn time
         */
        public int getTurnTime() {
            return this.turnTime;
        }

        /**
         * Gets level capacity.
         *
         * @return the level capacity
         */
        public int getLevelCapacity() {
            return this.levelCapacity;
        }

        /**
         * Gets pokemon amount.
         *
         * @return the Pokémon amount
         */
        public int getPokemonAmount() {
            return this.pokemonAmount;
        }

        /**
         * Is enable team preview boolean.
         *
         * @return the boolean
         */
        public boolean isEnableTeamPreview() {
            return this.enableTeamPreview;
        }

        /**
         * Is raise max level boolean.
         *
         * @return the boolean
         */
        public boolean isRaiseMaxLevel() {
            return this.raiseMaxLevel;
        }

        /**
         * Is double battle boolean.
         *
         * @return the boolean
         */
        public boolean isDoubleBattle() {
            return this.isDoubleBattle;
        }

        /**
         * Is full heal boolean.
         *
         * @return the boolean
         */
        public boolean isFullHeal() {
            return this.isFullHeal;
        }

        /**
         * Is bag banned boolean.
         *
         * @return the boolean
         */
        public boolean isBagBanned() {
            return this.isBagBanned;
        }
    }

    /**
     * The type Complex claus.
     */
    @ConfigSerializable
    public static class ComplexClaus {
        /**
         * Instantiates a new Complex claus.
         */
        public ComplexClaus() {
        }

        /**
         * The Pokemon specie.
         */
        @Setting(value = "pokemon-specie", comment = "Pokemon Specie")
        protected String pokemonSpecie;
        /**
         * The Ability.
         */
        @Nullable
        @Setting(value = "ability", comment = "pokemon ability")
        protected String ability;
        /**
         * The Held item.
         */
        @Nullable
        @Setting(value = "held-item", comment = "held item")
        protected String heldItem;
        /**
         * The Moves.
         */
        @Nullable
        @Setting(value = "moves", comment = "pokemon moves")
        protected List<String> moves = new ArrayList<>();

        /**
         * Instantiates a new Complex claus.
         *
         * @param pokemonSpecie the pokemon specie
         * @param ability       the ability
         * @param heldItem      the held item
         * @param moves         the moves
         */
        public ComplexClaus(String pokemonSpecie, @Nullable String ability, @Nullable String heldItem, @Nullable List<String> moves) {
            this.pokemonSpecie = pokemonSpecie;
            this.ability = ability;
            this.heldItem = heldItem;
            this.moves = moves;
        }

        /**
         * Gets pokemon specie.
         *
         * @return the pokemon specie
         */
        public String getPokemonSpecie() {
            return this.pokemonSpecie;
        }

        /**
         * Gets ability.
         *
         * @return the ability
         */
        @Nullable
        public String getAbility() {
            return this.ability;
        }

        /**
         * Gets held item.
         *
         * @return the held item
         */
        @Nullable
        public String getHeldItem() {
            return this.heldItem;
        }

        /**
         * Gets moves.
         *
         * @return the moves
         */
        @Nullable
        public List<String> getMoves() {
            return this.moves;
        }
    }

}
