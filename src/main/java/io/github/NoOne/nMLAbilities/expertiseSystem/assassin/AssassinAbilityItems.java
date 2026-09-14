package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.GROUNDED;
import static io.github.NoOne.nMLItems.enums.ItemType.DAGGER;

public class AssassinAbilityItems extends ExpertiseAbilityItems {
    public AssassinAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack slashAndDash() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Slash & Dash",
                new HashMap<>() {{
                    put(Expertise.ASSASSIN, 1);
                }},
                "Dash forwards, dealing damage to anyone in your way", 
                List.of(GROUNDED),
                false,
                "Area",
                10,
                0,
                5,
                20,
                List.of(makeWeaponDamageString(150)),
                null,
                List.of(DAGGER), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(slashAndDash());
    }
}