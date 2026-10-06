package io.github.NoOne.nMLAbilities.expertiseSystem.annulled;

import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityItems;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class AnnulledAbilityItems extends ExpertiseAbilityItemHelper {
    public AnnulledAbilityItems(Skills skills) {
        super(skills);
    }

    public ItemStack blackHole() {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Black Hole",
                new HashMap<>(){{
                    put(Expertise.ANNULLED, 50);
                }},
                "...it's a black hole. It pulls in and spaghettifies things; I don't need to spell this out for you.",
                null,
                false,
                "Area",
                13,
                7.75,
                30,
                50,
                List.of(makeElementalDamageString(DamageType.NECROTIC, 5)),
                null,
                List.of(WAND, STAFF, CATALYST), 
                skills
        );
    }

    @Override
    public List<ItemStack> getAllExpertiseAbilityItems() {
        PrimordialAbilityItems primordial = new PrimordialAbilityItems(skills);

        return List.of(primordial.pumpkinBomb(), blackHole());
    }
}