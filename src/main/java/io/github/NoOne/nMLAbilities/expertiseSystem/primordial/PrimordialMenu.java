package io.github.NoOne.nMLAbilities.expertiseSystem.primordial;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PrimordialMenu extends ExpertiseAbilityMenu {
    public PrimordialMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new PrimordialAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§2§lPrimordial Abilities";
    }
}
