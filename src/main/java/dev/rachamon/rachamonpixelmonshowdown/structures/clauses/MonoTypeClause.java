package dev.rachamon.rachamonpixelmonshowdown.structures.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.enums.EnumType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The type Mono type clause.
 */
public class MonoTypeClause extends BattleClause {

    /**
     * The Type.
     */
    EnumType type = null;

    /**
     * Instantiates a new Mono type clause.
     *
     * @param id   the id
     * @param type the type
     */
    public MonoTypeClause(String id, @Nullable EnumType type) {
        super(id);
        this.type = type;
    }

    public boolean validateTeam(List<Pokemon> pokemons) {

        if (type == null) {

            EnumType type1 = pokemons.get(0).getSpecies().getBaseStats(pokemons.get(0).getFormEnum()).getType1();
            EnumType type2 = pokemons.get(0).getSpecies().getBaseStats(pokemons.get(0).getFormEnum()).getType2();

            for (Pokemon pokemon : pokemons) {
                boolean isTypeOneDifferent = pokemon
                        .getSpecies()
                        .getBaseStats(pokemon.getFormEnum())
                        .getType1() != type1 && pokemon
                        .getSpecies()
                        .getBaseStats(pokemon.getFormEnum())
                        .getType1() != type2;
                boolean isTypeTwoDifferent = pokemon
                        .getSpecies()
                        .getBaseStats(pokemon.getFormEnum())
                        .getType2() != type1 && pokemon
                        .getSpecies()
                        .getBaseStats(pokemon.getFormEnum())
                        .getType2() != type2;

                if (isTypeOneDifferent && isTypeTwoDifferent) {
                    return false;
                }
            }
            return true;
        }


        for (Pokemon pokemon : pokemons) {
            if (pokemon.getSpecies().getBaseStats(pokemon.getFormEnum()).getType1() != type && (pokemon
                    .getSpecies()
                    .getBaseStats(pokemon.getFormEnum())
                    .getType2() != type || pokemon
                    .getSpecies()
                    .getBaseStats(pokemon.getFormEnum())
                    .getType2() == null)) {
                return false;
            }
        }

        return true;
    }
}




