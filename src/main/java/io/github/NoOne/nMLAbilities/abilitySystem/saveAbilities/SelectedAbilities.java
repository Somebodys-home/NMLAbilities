package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SelectedAbilities {
    private String style;
    private String expertise1;
    private String expertise2;
    private String expertise3;

    public SelectedAbilities(String style, String expertise3, String expertise1, String expertise2) {
        this.style = style;
        this.expertise1 = expertise1;
        this.expertise2 = expertise2;
        this.expertise3 = expertise3;
    }

    public String[] getSelectedAbilitiesArray() {
        return new String[]{style, expertise3, expertise1, expertise2};
    }

    public void clearSelectedAbilities() {
        style = AbilityItemManager.getRawAbilityName(AbilityItemManager.emptyStyleAbilityItem());
        expertise1 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
        expertise2 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
        expertise3 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
    }

    public void syncSelectedAbilitiesToInventory(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack style = playerInventory.getItem(0);
        ItemStack expertise1 = playerInventory.getItem(1);
        ItemStack expertise2 = playerInventory.getItem(2);
        ItemStack expertise3 = playerInventory.getItem(3);

        if (AbilityItemManager.isAnAbility(style)) {
            this.style = AbilityItemManager.getRawAbilityName(style);
        } else {
            this.style = AbilityItemManager.getRawAbilityName(style);
        }

        if (AbilityItemManager.isAnAbility(expertise1)) {
            this.expertise1 = AbilityItemManager.getRawAbilityName(expertise1);
        } else {
            this.expertise1 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
        }

        if (AbilityItemManager.isAnAbility(expertise2)) {
            this.expertise2 = AbilityItemManager.getRawAbilityName(expertise2);
        } else {
            this.expertise2 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
        }

        if (AbilityItemManager.isAnAbility(expertise3)) {
            this.expertise3 = AbilityItemManager.getRawAbilityName(expertise3);
        } else {
            this.expertise3 = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
        }
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getExpertise1() {
        return expertise1;
    }

    public void setExpertise1(String expertise1) {
        this.expertise1 = expertise1;
    }

    public String getExpertise2() {
        return expertise2;
    }

    public void setExpertise2(String expertise2) {
        this.expertise2 = expertise2;
    }

    public String getExpertise3() {
        return expertise3;
    }

    public void setExpertise3(String expertise3) {
        this.expertise3 = expertise3;
    }
}