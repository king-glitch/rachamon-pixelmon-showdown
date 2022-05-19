package dev.rachamon.rachamonpixelmonshowdown.structures.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DuplicateItemClause extends BattleClause {
    public DuplicateItemClause(String id) {
        super(id);
        this.setDescription("Duplicate item are not allowed");

    }

    public boolean validateTeam(List<Pokemon> pokemons) {
        List<ItemStack> items = new ArrayList<>();
        for (Pokemon pokemon : pokemons) {

            if (pokemon.getHeldItem().isEmpty()) {
                continue;
            }

            if (items.contains(pokemon.getHeldItem())) {
                return false;
            }

            items.add(pokemon.getHeldItem());
        }
        return true;
    }
}
