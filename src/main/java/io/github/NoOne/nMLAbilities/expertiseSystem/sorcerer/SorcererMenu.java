package io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SorcererMenu extends ExpertiseAbilityMenu {
    public SorcererMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new SorcererAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§6§lSorcerer Abilities";
    }
}
