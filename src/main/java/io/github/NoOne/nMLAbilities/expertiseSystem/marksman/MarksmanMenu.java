package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus.ExpertiseAbilityMenu;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarksmanMenu extends ExpertiseAbilityMenu {
    public MarksmanMenu(NMLAbilities nmlAbilities, Player player, Skills skills, ItemStack clickedItem) {
        super(nmlAbilities, player, clickedItem, new MarksmanAbilityItems(skills));
    }

    @Override
    public String getMenuName() {
        return "§a§lMarksman Abilities";
    }
}
