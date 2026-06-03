package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.*;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class CavalierAbilityItems extends AbilityItemManager {
    public CavalierAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack seismicSlam(Skills skills) {
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
                List.of("§f§n250%" + "§r§f" + " Weapon Damage \uD83D\uDDE1"),
                null,
                List.of(SPEAR, HAMMER), 
                skills);
    }
}