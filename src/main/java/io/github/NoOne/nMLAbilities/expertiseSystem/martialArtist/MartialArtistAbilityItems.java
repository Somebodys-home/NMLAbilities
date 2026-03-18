package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class MartialArtistAbilityItems extends AbilityItemManager {
    public MartialArtistAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack tenHitCombo(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "10-Hit Combo",
                new HashMap<>() {{
                    put("martialartist", 30);
                }},
                "Perform a devastating 10 hit combo, ending with an uppercut, so long as you can land every hit. (Energy cost is per hit)", 
                null,
                false,
                "Single",
                5,
                0,
                15,
                10,
                List.of("§f§n300%" + "§r§f" + " Weapon Damage \uD83D\uDDE1"),
                null,
                List.of(GLOVE), skills);
    }
}