package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShieldHeroMenu extends ExpertiseAbilityMenu {
    public ShieldHeroMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new ShieldHeroAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§3§lShield Hero Abilities";
    }
}
