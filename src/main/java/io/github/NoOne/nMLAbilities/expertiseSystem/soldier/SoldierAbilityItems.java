package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class SoldierAbilityItems extends AbilityItemManager {
    public SoldierAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack slash(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Slash",
                new HashMap<>() {{
                    put("soldier", 1);
                }},
                "Yep.", 
                null,
                false,
                "Area",
                2,
                0,
                2,
                15,
                List.of("§f§n120%" + "§r§f" + " Weapon Damage \uD83D\uDDE1"),
                null,
                List.of(SWORD, AXE, SPEAR), skills);
    }
}