package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.SHIELD;

public class ShieldHeroAbilityItems extends ExpertiseAbilityItems {
    public ShieldHeroAbilityItems(Skills skills) {
        super(skills);
    }

    public static ItemStack secondWind() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Second Wind",
                new HashMap<>() {{
                    put(Expertise.SHIELD_HERO, 1);
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
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(secondWind());
    }
}