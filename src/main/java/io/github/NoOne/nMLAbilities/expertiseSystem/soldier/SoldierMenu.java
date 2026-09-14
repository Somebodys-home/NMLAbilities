package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoldierMenu extends ExpertiseAbilityMenu {
    public SoldierMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new SoldierAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§c§lSoldier Abilities";
    }
}
