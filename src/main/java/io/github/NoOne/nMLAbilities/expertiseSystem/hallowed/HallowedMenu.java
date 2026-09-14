package io.github.NoOne.nMLAbilities.expertiseSystem.hallowed;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HallowedMenu extends ExpertiseAbilityMenu {
    public HallowedMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new HallowedAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§f§lHallowed Abilities";
    }
}
