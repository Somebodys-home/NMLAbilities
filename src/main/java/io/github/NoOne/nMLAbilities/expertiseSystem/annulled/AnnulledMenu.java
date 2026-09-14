package io.github.NoOne.nMLAbilities.expertiseSystem.annulled;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AnnulledMenu extends ExpertiseAbilityMenu {
    public AnnulledMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new AnnulledAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§5§lAnnulled Abilities";
    }
}
