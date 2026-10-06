package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityPrerequisite;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.SHIELD;

public class ShieldHeroAbilityItems extends ExpertiseAbilityItemHelper {
    public ShieldHeroAbilityItems(Skills skills) {
        super(skills);
    }

    public ItemStack secondWind() {
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
                30,
                10,
                null,
                List.of("§fRestore your §nGuard§r§f ⛨"),
                List.of(SHIELD), 
                skills
        );
    }

    public ItemStack shieldBash() {
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
                13,
                15,
                List.of(makeWeaponDamageString(100)),
                List.of(makeInvincibleString(1)),
                List.of(SHIELD),
                skills
        );
    }

    public ItemStack shieldPunch() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Shield Punch",
                new HashMap<>(){{
                    put(Expertise.SHIELD_HERO, 5);
                }},
                "Not to be confused with a shield bash, a shield punch is for clocking the everloving &kshit out of somebody",
                null,
                false,
                "Single",
                3,
                0,
                7,
                20,
                List.of(makeWeaponDamageString(200)),
                null,
                List.of(SHIELD),
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        return List.of(secondWind(), shieldBash(), shieldPunch());
    }
}