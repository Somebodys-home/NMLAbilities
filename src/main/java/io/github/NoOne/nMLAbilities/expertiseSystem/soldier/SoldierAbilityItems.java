package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class SoldierAbilityItems extends ExpertiseAbilityItemHelper {
    public SoldierAbilityItems(Skills skills) {
        super(skills);
    }

    public ItemStack slash() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Slash",
                new HashMap<>(){{
                    put(Expertise.SOLDIER, 1);
                }},
                "Yep.",
                null,
                false,
                "Area",
                3,
                0,
                2,
                15,
                List.of(makeWeaponDamageString(120)),
                null,
                List.of(SWORD, AXE, SPEAR),
                skills
        );
    }

    public ItemStack xSlash() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "X-Slash",
                new HashMap<>(){{
                    put(Expertise.SOLDIER, 10);
                }},
                "It's a slash in an X shape!\n...\nI'm so bad at describing soldier abilities I promise it's cool.",
                null,
                false,
                "Area",
                3,
                0,
                0,
                15,
                List.of(makeWeaponDamageString(80) + makeTimesString(2)),
                null,
                List.of(SWORD),
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(slash(), xSlash());
    }
}