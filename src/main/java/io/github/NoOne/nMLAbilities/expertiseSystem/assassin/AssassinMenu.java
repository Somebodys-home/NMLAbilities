package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AssassinMenu extends ExpertiseAbilityMenu {
    public AssassinMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new AssassinAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§0§lAssassin Abilities";
    }
}
