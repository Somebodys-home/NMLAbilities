package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class SorcererAbilityItems extends ExpertiseAbilityItems {
    public SorcererAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack magicMissileEX() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Magic Missile EX",
                new HashMap<>() {{
                    put(Expertise.SORCERER, 1);
                }},
                "Shoot your basic magic missile attack 5 times",
                null,
                false,
                "Single",
                16,
                0,
                8,
                15,
                List.of(makeWeaponDamageString(50) + makePerString("missile")),
                null,
                List.of(WAND, STAFF, CATALYST), 
                skills
        );
    }

    public static ItemStack dragonsBreath() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Dragon's Breath",
                new HashMap<>() {{
                    put(Expertise.SORCERER, 25);
                }},
                "RRRRRAAAAAAGGGHHHHH out a cone of fire from your mouth",
                null,
                false,
                "Area",
                12,
                5,
                20,
                25,
                List.of(makeElementalDamageString(DamageType.FIRE, 1) + makeEverySecondString(1)),
                null,
                List.of(WAND, STAFF), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(magicMissileEX(), dragonsBreath());
    }
}