package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class MarksmanAbilityItems extends AbilityItemManager {
    public MarksmanAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack steadyAim(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Steady Aim",
                new HashMap<>() {{
                    put("marksman", 1);
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
                skills);
    }

    public static ItemStack rapidShot(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Arrow Hailstorm",
                new HashMap<>() {{
                    put("marksman", 30);
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
                skills);
    }
}