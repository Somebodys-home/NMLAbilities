package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityPrerequisite;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.SHIELD;

public class ShieldHeroAbilityItemCreator extends ExpertiseAbilityItemHelper {
    public ShieldHeroAbilityItemCreator(Skills skills) {
        super(skills);
    }

    public static ItemStack secondWind() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Second Wind",
                new HashMap<>(){{
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

    public static ItemStack shieldBash() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Shield Bash",
                new HashMap<>(){{
                    put(Expertise.SHIELD_HERO, 5);
                }},
                "Not to be confused with a shield punch, a shield bash is for ramming into people to knock them upside",
                List.of(AbilityPrerequisite.GROUNDED),
                false,
                "Area",
                7,
                0,
                7, 
                10,
                List.of(makeWeaponDamageString(100)),
                List.of(makeInvincibleString(1)),
                List.of(SHIELD),
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(secondWind(), shieldBash());
    }
}