package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.GROUNDED;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class AssassinAbilityItems extends AbilityItemManager {
    public AssassinAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack slashandDash(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Slash & Dash",
                new HashMap<>() {{
                    put("assassin", 1);
                }},
                "Dash forwards, dealing damage to anyone in your way", 
                List.of(GROUNDED),
                false,
                "Area",
                10,
                0,
                5,
                20,
                List.of("§f§n150%" + "§r§f" + " Weapon Damage \uD83D\uDDE1"),
                null,
                List.of(DAGGER), skills);
    }
}