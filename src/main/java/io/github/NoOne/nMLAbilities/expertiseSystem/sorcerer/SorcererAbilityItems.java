package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class SorcererAbilityItems extends AbilityItemManager {
    public SorcererAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack magicMissileEX(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Magic Missile EX",
                new HashMap<>() {{
                    put("sorcerer", 1);
                }},
                "Shoot your basic magic missile attack 5 times",
                null,
                false,
                "Single",
                16,
                0,
                8,
                15,
                List.of("§f§n50%" + "§r§f" + " Weapon Damage \uD83D\uDDE1 §7§o(per missile)"),
                null,
                List.of(WAND, STAFF, CATALYST), 
                skills);
    }

    public static ItemStack dragonsBreath(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Dragon's Breath",
                new HashMap<>() {{
                    put("sorcerer", 25);
                }},
                "RRRRRAAAAAAGGGHHHHH out a cone of fire from your mouth",
                null,
                false,
                "Area",
                12,
                5,
                20,
                25,
                List.of("§c§n1x§r§c Fire Damage 🔥 §7§o(every 1s)"),
                null,
                List.of(WAND, STAFF), 
                skills);
    }
}