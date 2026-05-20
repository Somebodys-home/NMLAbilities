package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.*;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class MarauderAbilityItems extends AbilityItemManager {
    public MarauderAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack bladeTornado(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Blade Tornado",
                new HashMap<>() {{
                    put(Expertise.MARAUDER, 20);
                }},
                "Hurl yourself forwards as a whirligig of anger issues, bad intentions, and BLADES!",
                null,
                false,
                "Self",
                3,
                5,
                20,
                30,
                List.of("§f§n50%" + "§r§f" + " Weapon Damage \uD83D\uDDE1 §7§o(every .25s)"),
                null,
                List.of(SWORD, AXE), 
                skills);
    }
}