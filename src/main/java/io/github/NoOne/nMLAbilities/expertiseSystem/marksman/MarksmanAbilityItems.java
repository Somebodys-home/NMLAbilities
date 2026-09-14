package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.BOW;

public class MarksmanAbilityItems extends ExpertiseAbilityItems {
    public MarksmanAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack steadyAim() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Steady Aim",
                new HashMap<>() {{
                    put(Expertise.MARKSMAN, 1);
                }},
                "Slow your movements to increase your accuracy, making critical shots land easier.",
                null,
                true,
                "Self",
                0,
                0,
                10,
                15,
                null,
                List.of("§7-50% §nSpeed§r§7 ✦", "§9+30% §nCrit Damage§r§9 ☠"),
                List.of(BOW),
                skills
        );
    }

    public static ItemStack rapidShot() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Arrow Hailstorm",
                new HashMap<>() {{
                    put(Expertise.MARKSMAN, 30);
                }},
                "EXTREME WEATHER WARNING: A storm of hail-sized arrows has been forecasted in your area. Find shelter immediately.",
                null,
                false,
                "Area",
                22,
                5,
                25,
                30,
                List.of("§f§n35%" + "§r§f" + " Weapon Damage \uD83D\uDDE1 §7§o(per arrow)"),
                null,
                List.of(BOW), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(steadyAim(), rapidShot());
    }
}