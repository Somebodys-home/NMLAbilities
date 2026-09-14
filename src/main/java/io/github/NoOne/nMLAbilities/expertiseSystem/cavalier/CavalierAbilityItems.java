package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.GROUNDED;
import static io.github.NoOne.nMLItems.enums.ItemType.HAMMER;
import static io.github.NoOne.nMLItems.enums.ItemType.SPEAR;

public class CavalierAbilityItems extends ExpertiseAbilityItems {
    public CavalierAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack seismicSlam() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Seismic Slam",
                new HashMap<>() {{
                    put(Expertise.CAVALIER, 10);
                }},
                "Jump into the air, then crash into the ground, launching anyone nearby away from you", 
                List.of(GROUNDED),
                false,
                "Area",
                16,
                0,
                20,
                30,
                List.of(makeWeaponDamageString(250)),
                null,
                List.of(SPEAR, HAMMER), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(seismicSlam());
    }
}