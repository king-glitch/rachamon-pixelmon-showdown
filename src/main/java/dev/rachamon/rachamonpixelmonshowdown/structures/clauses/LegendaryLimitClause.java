package dev.rachamon.rachamonpixelmonshowdown.structures.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Legendary limit clause.
 */
public class LegendaryLimitClause extends BattleClause {

    private int amount = 6;

    /**
     * Instantiates a new Legendary limit clause.
     *
     * @param id     the id
     * @param amount the amount
     */
    public LegendaryLimitClause(String id, int amount) {
        super(id);
        this.amount = amount;
        this.setDescription("Legendary limit, allowed: " + amount);
    }

    public boolean validateTeam(List<Pokemon> pokemons) {

        List<String> subLegendary = new ArrayList<String>() {{
            add("Articuno");
            add("Zapdos");
            add("Moltres");
            add("Raikou");
            add("Entei");
            add("Suicune");
            add("Regirock");
            add("Regice");
            add("Registeel");
            add("Latias");
            add("Latios");
            add("Uxie");
            add("Mesprit");
            add("Azelf");
            add("Heatran");
            add("Regigigas");
            add("Cresselia");
            add("Cobalion");
            add("Terrakion");
            add("Virizion");
            add("Tornadus");
            add("Prankster");
            add("Thundurus");
            add("Prankster");
            add("Landorus");
            add("TypeNull");
            add("Silvally");
            add("TapuKoko");
            add("TapuLele");
            add("TapuBulu");
            add("TapuFini");
            add("Nihilego");
            add("Buzzwole");
            add("Pheromosa");
            add("Xurkitree");
            add("Celesteela");
            add("Kartana");
            add("Guzzlord");
            add("Poipole");
            add("Naganadel");
            add("Stakataka");
            add("Blacephalon");
            add("Kubfu");
            add("Urshifu");
            add("Regieleki");
            add("Regidrago");
            add("Glastrier");
            add("Spectrier");
            add("Enamorus");
        }};

        List<String> legendary = Arrays
                .stream(EnumSpecies.LEGENDARY_ENUMS)
                .map(EnumSpecies::getPokemonName)
                .filter(pokemon -> !subLegendary.contains(pokemon))
                .collect(Collectors.toList());

        int amount = (int) pokemons
                .stream()
                .map((pokemon -> pokemon.getSpecies().getBaseStats(pokemon.getFormEnum()).getPokemonName()))
                .filter((pokemon) -> legendary.stream().anyMatch(p -> p.contains(pokemon)))
                .count();

        return amount <= this.amount;
    }

}
