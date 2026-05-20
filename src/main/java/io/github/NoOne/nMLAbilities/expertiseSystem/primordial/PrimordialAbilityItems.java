package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite.GROUNDED;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class PrimordialAbilityItems extends AbilityItemManager {
    public PrimordialAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack chuckRock(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Chuck Rock",
                new HashMap<>() {{
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
                List.of("§4§n1.5x§r§4 Physical Damage ⚔"),
                null,
                List.of(),
                skills);
    }

    public static ItemStack pumpkinBomb(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Pumpkin Bomb",
                new HashMap<>() {{
                    put(Expertise.PRIMORDIAL, 15);
                    put(Expertise.ANNULLED, 5);
                }},
                "Summon and throw a pumpkin that explodes on contact. Spooky!", 
                null,
                false,
                "Area",
                20,
                0,
                15,
                30,
                List.of("§f§n100%§r§f Weapon Damage \uD83D\uDDE1", "§c§n1.5x§r§c Fire Damage 🔥", "§2§n1.5x§r§2 Earth Damage 🪨"),
                null,
                List.of(WAND, STAFF, CATALYST),
                skills);
    }

    public static ItemStack airBall(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Air Ball",
                new HashMap<>() {{
                    put(Expertise.PRIMORDIAL, 10);
                }},
                "Dunk on your foes with a compressed ball of air. Ball game.",
                null,
                false,
                "Area",
                15,
                0,
                10,
                15,
                List.of("§f§n50%§r§f Weapon Damage \uD83D\uDDE1", "§7§n2.5x§r§7 Air Damage ☁"),
                null,
                List.of(), 
                skills);
    }
}