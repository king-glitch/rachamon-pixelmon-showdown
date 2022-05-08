package dev.rachamon.rachamonpixelmonshowdown.managers.battle;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.*;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import dev.rachamon.rachamonpixelmonshowdown.RachamonPixelmonShowdown;
import dev.rachamon.rachamonpixelmonshowdown.configs.BattleLeagueConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RachamonPixelmonShowdownRuleManager {

    public final RachamonPixelmonShowdown plugin = RachamonPixelmonShowdown.getInstance();

    private final String leagueName;
    private final List<PokemonClause> pokemonClauses = new ArrayList<>();
    private final List<MoveClause> moveClauses = new ArrayList<>();
    private final List<ItemPreventClause> itemPreventClauses = new ArrayList<>();
    private final List<AbilityClause> abilityClauses = new ArrayList<>();
    private final List<BattleClauseAll> complexClauses = new ArrayList<>();

    public RachamonPixelmonShowdownRuleManager(String leagueName) {
        this.leagueName = leagueName;
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

    public void addPokemonClause(String pokemonName) {

    }

    public void build() {
        BattleRules battleRules = new BattleRules();
        BattleLeagueConfig.League league = this.plugin.getLeague().getLeagues().get(this.leagueName);

        if (league == null) {
            this.plugin.getLogger().error("error on build battle rules, " + leagueName);
            return;
        }

        battleRules.fullHeal = league.getBattleRule().isFullHeal();
        battleRules.battleType = league
                .getBattleRule()
                .isDoubleBattle() ? EnumBattleType.Double : EnumBattleType.Single;
        battleRules.levelCap = league.getBattleRule().getLevelCapacity();
        battleRules.raiseToCap = league.getBattleRule().isRaiseMaxLevel();
        battleRules.teamPreview = league.getBattleRule().isEnableTeamPreview();
        battleRules.turnTime = league.getBattleRule().getTurnTime();
        battleRules.numPokemon = league.getBattleRule().getPokemonAmount();

        List<BattleClause> clauses = battleRules.getClauseList();
        if (league.getBattleRule().isBagBanned()) {
            clauses.add(new BattleClause("bag"));
        }

        clauses.addAll(pokemonClauses);
        clauses.addAll(moveClauses);
        clauses.addAll(abilityClauses);
        clauses.addAll(complexClauses);
        clauses.addAll(itemPreventClauses);

    }

    public boolean isValidated() {

        return false;
    }


}
