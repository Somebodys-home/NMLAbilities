package io.github.NoOne.nMLAbilities.expertiseSystem.soldier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenuTemplate;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoldierMenu extends ExpertiseAbilityMenuTemplate {
    public SoldierMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new SoldierAbilityItemCreator(skills));
    }

    @Override
    public String getMenuName() {
        return "§c§lSoldier Abilities";
    }
}
