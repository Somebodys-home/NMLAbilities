package io.github.NoOne.nMLAbilities.expertiseSystem.annulled;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenuTemplate;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AnnulledMenu extends ExpertiseAbilityMenuTemplate {
    public AnnulledMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new AnnulledAbilityItemCreator(skills));
    }

    @Override
    public String getMenuName() {
        return "§5§lAnnulled Abilities";
    }
}
