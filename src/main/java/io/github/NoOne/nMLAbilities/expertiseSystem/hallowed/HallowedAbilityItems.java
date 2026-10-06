package io.github.NoOne.nMLAbilities.expertiseSystem.hallowed;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class HallowedAbilityItems extends ExpertiseAbilityItemHelper {
    public HallowedAbilityItems(Skills skills) {
        super(skills);
    }

    public ItemStack halo() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Halo",
                new HashMap<>(){{
                    put(Expertise.HALLOWED, 15);
                }},
                "Throw a ring of radiant energy that rebounds back to you, damaging anyone touching it", 
                null,
                false,
                "Area",
                20,
                0,
                17,
                25,
                List.of(
                        makeWeaponDamageString(15) + makeEverySecondString(.25),
                        makeElementalDamageString(DamageType.RADIANT, .35) + makeEverySecondString(.25)
                ),
                null,
                List.of(WAND, STAFF, CATALYST), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(halo());
    }
}