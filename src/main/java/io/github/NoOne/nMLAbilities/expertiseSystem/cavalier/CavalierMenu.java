package io.github.NoOne.nMLAbilities.expertiseSystem.cavalier;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CavalierMenu extends ExpertiseAbilityMenu {
    public CavalierMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new CavalierAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§1§lCavalier Abilities";
    }
}
