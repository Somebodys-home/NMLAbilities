package io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import static io.github.NoOne.nMLItems.enums.ItemType.*;

public class MartialArtistAbilityItems extends AbilityItemManager {
    public MartialArtistAbilityItems() {
        super(NMLAbilities.getInstance());
    }

    public static ItemStack tenHitCombo(Skills skills) {
        return ExpertiseAbilityItemMaker.makeExpertiseAbilityItem(
                "Dropkick",
                new HashMap<>() {{
                    put(Expertise.MARTIAL_ARTIST, 10);
                }},
                "\"Officer I dropkicked that child in self defense.\"",
                null,
                false,
                "Single",
                -1,
                0,
                10,
                15,
                List.of("§4§n100-300%" + "§r§4" + " Physical Damage ⚔"),
                List.of("§fDamage & range based on §nvelocity"),
                List.of(GLOVE), 
                skills);
    }
}