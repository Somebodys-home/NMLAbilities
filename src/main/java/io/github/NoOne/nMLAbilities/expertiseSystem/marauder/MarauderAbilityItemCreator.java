package io.github.NoOne.nMLAbilities.expertiseSystem.marauder;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class MarauderAbilityItemCreator extends ExpertiseAbilityItemHelper {
    public MarauderAbilityItemCreator(Skills skills) {
        super(skills);
    }

    public static ItemStack stompingTantrum() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Stomping Tantrum",
                new HashMap<>(){{
                    put(Expertise.MARAUDER, 10);
                }},
                "DO THE EARTHQUAKE!!!",
                null,
                false,
                "Area",
                3,
                0,
                12,
                30,
                List.of(
                        makeWeaponDamageString(50) + makeTimesString(8),
                        makeElementalDamageString(DamageType.PHYSICAL, .85) + makeTimesString(8)
                ),
                null,
                List.of(AXE, HAMMER),
                skills
        );
    }

    public static ItemStack bladeTornado() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Blade Tornado",
                new HashMap<>(){{
                    put(Expertise.MARAUDER, 20);
                }},
                "Hurl yourself forwards as a whirligig of anger issues, bad intentions, and BLADES!",
                null,
                false,
                "Area",
                3,
                5,
                20,
                15,
                List.of(makeWeaponDamageString(25) + makeEverySecondString(.25)),
                null,
                List.of(SWORD, AXE), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(stompingTantrum(), bladeTornado());
    }
}