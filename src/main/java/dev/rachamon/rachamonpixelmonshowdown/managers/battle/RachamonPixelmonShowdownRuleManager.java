package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.*;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Rachamon pixelmon showdown rule manager.
 */
public class RachamonPixelmonShowdownRuleManager {

    /**
     * The Plugin.
     */
    public final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    private final BattleRules battleRules = new BattleRules();
    private final String leagueName;
    private BattleLeagueConfig.League league = null;
    private int complexNum = 0;
    private final List<PokemonClause> pokemonClauses = new ArrayList<>();
    private final List<MoveClause> moveClauses = new ArrayList<>();
    private final List<ItemPreventClause> itemPreventClauses = new ArrayList<>();
    private final List<AbilityClause> abilityClauses = new ArrayList<>();
    private final List<BattleClauseAll> complexClauses = new ArrayList<>();

    /**
     * Instantiates a new Rachamon pixelmon showdown rule manager.
     *
     * @param leagueName the league name
     * @throws Exception the exception
     */
    public RachamonPixelmonShowdownRuleManager(String leagueName) throws Exception {

        this.leagueName = leagueName;
        this.league = this.plugin.getLeague().getLeagues().get(this.leagueName);

        if (this.league == null) {
            this.plugin.getLogger().error("error on build battle rules, " + leagueName);
            throw new Exception("error on build battle rules, " + leagueName);
        }

    }

    private PokemonClause getPokemonClause(String pokemonName) {

        if (pokemonName.contains("-")) {

            Optional<PokemonForm> pokemonForm = PokemonForm.getFromName(pokemonName);

            if (!pokemonForm.isPresent()) {
                return null;
            }

            String[] pokemonNameSplit = pokemonName.split("-");

            String pokemon = pokemonNameSplit[0];
            String form = pokemonNameSplit[1];

            Optional<EnumSpecies> enumSpecies = EnumSpecies.getFromName(pokemon);

            if (!enumSpecies.isPresent()) {
                return null;
            }

            for (Object formsList : enumSpecies.get().getPossibleForms(false).toArray()) {
                IEnumForm castForm = (IEnumForm) formsList;

                if (!castForm.getFormSuffix().equalsIgnoreCase(form)) {
                    continue;
                }

                pokemonForm.get().form = castForm.getForm();
                return new PokemonClause(pokemonName, pokemonForm.get());
            }
        }

        if (EnumSpecies.getFromNameAnyCase(pokemonName) == null || pokemonName.contains("-")) {
            this.plugin
                    .getLogger()
                    .error("Error Getting Pokemon Clause: " + pokemonName + ". please check config error.");
            return null;
        }

        return new PokemonClause(pokemonName, EnumSpecies.getFromNameAnyCase(pokemonName));
    }

    /**
     * Gets held item clause.
     *
     * @param itemName the item name
     * @return the held item clause
     */
    public ItemPreventClause getHeldItemClause(String itemName) {
        try {
            if (itemName.equalsIgnoreCase("megastone")) {
                return new ItemPreventClause(itemName, EnumHeldItems.megaStone);
            }

            if (itemName.equalsIgnoreCase("z-crystals")) {
                return new ItemPreventClause(itemName, EnumHeldItems.zCrystal);
            }

            return new ItemPreventClause(itemName, EnumHeldItems.valueOf(itemName.toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.getLogger().error("Error adding held item " + itemName + " please check config.");
        }

        return null;
    }

    /**
     * Add complex clause.
     *
     * @param claus the claus
     */
    public void addComplexClause(BattleLeagueConfig.ComplexClaus claus) {

        ArrayList<BattleClause> complexClause = new ArrayList<>();

        if (claus.getPokemonSpecie() != null) {
            PokemonClause pokemonClause = this.getPokemonClause(claus.getPokemonSpecie());
            if (pokemonClause != null) {
                complexClause.add(pokemonClause);
            }
        }

        if (claus.getMoves() != null) {
            for (String move : claus.getMoves()) {
                MoveClause moveClause = this.getMoveClause(move);
                if (moveClause != null) {
                    complexClause.add(moveClause);
                }
            }
        }

        if (claus.getAbility() != null) {
            AbilityClause abilityClause = this.getAbilityClause(claus.getAbility());
            if (abilityClause != null) {
                complexClause.add(abilityClause);
            }
        }

        if (claus.getHeldItem() != null) {
            ItemPreventClause heldItemClause = this.getHeldItemClause(claus.getHeldItem());
            if (heldItemClause != null) {
                complexClause.add(heldItemClause);
            }
        }

        BattleClause[] battleClauses = new BattleClause[complexClause.size()];

        battleClauses = complexClause.toArray(battleClauses);
        BattleClauseSingleAll comboClause = new BattleClauseSingleAll("ComplexClause" + complexNum, battleClauses);
        complexNum++;
        complexClauses.add(comboClause);
    }

    /**
     * Gets ability clause.
     *
     * @param abilityName the ability name
     * @return the ability clause
     */
    public AbilityClause getAbilityClause(String abilityName) {

        Optional<AbilityBase> abilityBase = AbilityBase.getAbility(abilityName);

        if (!abilityBase.isPresent()) {
            this.plugin.getLogger().error("Error adding Ability " + abilityName + ". please check config.");
            return null;
        }

        return new AbilityClause(abilityName, abilityBase.get().getClass());
    }

    /**
     * Gets move clause.
     *
     * @param moveName the move name
     * @return the move clause
     */
    public MoveClause getMoveClause(String moveName) {
        try {
            return new MoveClause(moveName, moveName);
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.getLogger().error("Error adding move " + moveName + ". please check config.");
        }
        return null;
    }

    /**
     * Add pokemon clause.
     *
     * @param pokemonName the pokemon name
     */
    public void addPokemonClause(String pokemonName) {
        if (pokemonName.equalsIgnoreCase("legendary")) {
            for (EnumSpecies legendary : EnumSpecies.LEGENDARY_ENUMS) {
                pokemonClauses.add(this.getPokemonClause(legendary.getPokemonName()));
            }
            return;
        }

        if (pokemonName.equalsIgnoreCase("ultrabreast")) {
            for (EnumSpecies legendary : EnumSpecies.ultrabeasts) {
                pokemonClauses.add(this.getPokemonClause(legendary.getPokemonName()));
            }
            return;
        }

        EnumSpecies species = EnumSpecies.getFromNameAnyCase(pokemonName);

        Optional<PokemonForm> form = PokemonForm.getFromName(pokemonName);

        if (species == null || !form.isPresent()) {
            this.plugin.getLogger().error("Error adding pokemon " + pokemonName + ". please check config.");
            return;
        }

        PokemonClause clause = this.getPokemonClause(pokemonName);

        if (clause == null) {
            this.plugin.getLogger().error("Error adding pokemon " + pokemonName + ". please check config.");
            return;
        }

        pokemonClauses.add(clause);
    }

    /**
     * Add held item clause.
     *
     * @param itemName the item name
     */
    public void addHeldItemClause(String itemName) {
        ItemPreventClause clause = this.getHeldItemClause(itemName);
        if (clause == null) {
            this.plugin.getLogger().error("Error adding held item " + itemName + ". please check config.");
            return;
        }

        this.itemPreventClauses.add(clause);
    }

    /**
     * Add move clause.
     *
     * @param moveName the move name
     */
    public void addMoveClause(String moveName) {
        MoveClause clause = this.getMoveClause(moveName);

        if (clause == null) {
            this.plugin.getLogger().error("Error adding move " + moveName + ". please check config.");
            return;
        }

        this.moveClauses.add(clause);
    }

    /**
     * Add ability clause.
     *
     * @param abilityName the ability name
     */
    public void addAbilityClause(String abilityName) {
        AbilityClause clause = this.getAbilityClause(abilityName);

        if (clause == null) {
            this.plugin.getLogger().error("Error adding ability " + abilityName + ". please check config.");
            return;
        }

        this.abilityClauses.add(clause);
    }

    /**
     * Load rachamon pixelmon showdown rule manager.
     *
     * @return the rachamon pixelmon showdown rule manager
     */
    public RachamonPixelmonShowdownRuleManager load() {

        for (String pokemon : this.league.getPokemonClaus()) {
            this.addPokemonClause(pokemon);
        }

        for (String pokemon : this.league.getHeldItemClause()) {
            this.addHeldItemClause(pokemon);
        }

        for (String pokemon : this.league.getMoveClaus()) {
            this.addMoveClause(pokemon);
        }

        for (String pokemon : this.league.getAbilities()) {
            this.addAbilityClause(pokemon);
        }

        for (BattleLeagueConfig.ComplexClaus complex : this.league.getComplexClaus()) {
            this.addComplexClause(complex);
        }

        return this;

    }

    /**
     * Build rachamon pixelmon showdown rule manager.
     *
     * @return the rachamon pixelmon showdown rule manager
     */
    public RachamonPixelmonShowdownRuleManager build() {
        battleRules.fullHeal = this.league.getBattleRule().isFullHeal();
        battleRules.battleType = league
                .getBattleRule()
                .isDoubleBattle() ? EnumBattleType.Double : EnumBattleType.Single;
        battleRules.levelCap = this.league.getBattleRule().getLevelCapacity();
        battleRules.raiseToCap = this.league.getBattleRule().isRaiseMaxLevel();
        battleRules.teamPreview = this.league.getBattleRule().isEnableTeamPreview();
        battleRules.turnTime = this.league.getBattleRule().getTurnTime();
        battleRules.numPokemon = this.league.getBattleRule().getPokemonAmount();

        List<BattleClause> clauses = battleRules.getClauseList();

        if (this.league.getBattleRule().isBagBanned()) {
            clauses.add(new BattleClause("bag"));
        }

        clauses.addAll(pokemonClauses);
        clauses.addAll(moveClauses);
        clauses.addAll(abilityClauses);
        clauses.addAll(complexClauses);
        clauses.addAll(itemPreventClauses);

        return this;

    }

    /**
     * Gets battle rules.
     *
     * @return the battle rules
     */
    public BattleRules getBattleRules() {
        return battleRules;
    }

    /**
     * Gets league.
     *
     * @return the league
     */
    public BattleLeagueConfig.League getLeague() {
        return league;
    }

    /**
     * Gets league name.
     *
     * @return the league name
     */
    public String getLeagueName() {
        return leagueName;
    }
}
