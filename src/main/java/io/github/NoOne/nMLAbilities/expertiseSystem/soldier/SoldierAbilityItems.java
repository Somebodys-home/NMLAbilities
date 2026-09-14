package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class SoldierAbilityItems extends ExpertiseAbilityItems {
    public SoldierAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack slash() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Slash",
                new HashMap<>() {{
                    put(Expertise.SOLDIER, 1);
                }},
                "Yep.", 
                null,
                false,
                "Area",
                2,
                0,
                2,
                15,
                List.of(makeWeaponDamageString(120)),
                null,
                List.of(SWORD, AXE, SPEAR), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(slash());
    }
}