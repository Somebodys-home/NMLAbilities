package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.GROUNDED;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class PrimordialAbilityItems extends ExpertiseAbilityItems {
    public PrimordialAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack chuckRock() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Chuck Rock",
                new HashMap<>(){{
                    put(Expertise.PRIMORDIAL, 1);
                }},
                "Pick up and chuck a rock. It's not magical or anything, you just find a rock and throw it.", 
                List.of(GROUNDED),
                false,
                "Single",
                20,
                0,
                5,
                10,
                List.of(makeElementalDamageString(DamageType.PHYSICAL, 1.5)),
                null,
                List.of(),
                skills
        );
    }

    public static ItemStack airBall() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Air Ball",
                new HashMap<>(){{
                    put(Expertise.PRIMORDIAL, 10);
                }},
                "Start balling so hard you dunk an air ball!",
                null,
                false,
                "Area",
                13,
                0,
                10,
                15,
                List.of(
                        makeWeaponDamageString(50),
                        makeElementalDamageString(DamageType.AIR, 2)
                ),
                null,
                List.of(),
                skills
        );
    }

    public static ItemStack pumpkinBomb() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Pumpkin Bomb",
                new LinkedHashMap<>(){{
                    put(Expertise.PRIMORDIAL, 15);
                    put(Expertise.ANNULLED, 5);
                }},
                "Summon and throw a pumpkin that explodes on contact. Spooky!", 
                null,
                false,
                "Area",
                20,
                0,
                1,
                30,
                List.of(
                        makeWeaponDamageString(80),
                        makeElementalDamageString(DamageType.FIRE, 1.5),
                        makeElementalDamageString(DamageType.EARTH, 1.5)
                ),
                null,
                List.of(WAND, STAFF, CATALYST),
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(chuckRock(), airBall(), pumpkinBomb());
    }
}