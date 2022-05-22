package dev.rachamon.rachamonpixelmonshowdown.structures.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Duplicate pokemon clause.
 */
public class DuplicatePokemonClause extends BattleClause {
    /**
     * Instantiates a new Duplicate pokemon clause.
     *
     * @param id the id
     */
    public DuplicatePokemonClause(String id) {
        super(id);
        this.setDescription("Duplicate pokemon are not allowed");
    }

    public boolean validateTeam(List<Pokemon> pokemons) {
        List<EnumSpecies> species = new ArrayList<>();
        for (Pokemon pokemon : pokemons) {
            if (species.contains(pokemon.getSpecies())) {
                return false;
            }
            species.add(pokemon.getSpecies());
        }
        return true;
    }
}
