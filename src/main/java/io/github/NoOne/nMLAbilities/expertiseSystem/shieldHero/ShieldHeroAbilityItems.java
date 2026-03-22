package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class ShieldHeroAbilityItems extends AbilityItemManager {
    public ShieldHeroAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack secondWind(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Second Wind",
                new HashMap<>() {{
                    put("shieldhero", 1);
                }},
                "Take a moment to steel your resolve to fully regain your guard", 
                null,
                false,
                "Self",
                0,
                0,
                20,
                10,
                null,
                List.of("§fRestore your §nGuard§r§f ⛨"),
                List.of(SHIELD), 
                skills);
    }
}