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
    private String emptyExpertiseAbilityName;

    public SelectedAbilities(String style, String expertise3, String expertise1, String expertise2) {
        this.style = style;
        this.expertise1 = expertise1;
        this.expertise2 = expertise2;
        this.expertise3 = expertise3;
        emptyExpertiseAbilityName = AbilityItemManager.getRawAbilityName(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem());
    }

    public String[] getSelectedAbilitiesArray() {
        return new String[]{style, expertise3, expertise1, expertise2};
    }

    public void clearSelectedAbilities() {
        style = AbilityItemManager.getRawAbilityName(AbilityItemManager.emptyStyleAbilityItem());
        expertise1 = emptyExpertiseAbilityName;
        expertise2 = emptyExpertiseAbilityName;
        expertise3 = emptyExpertiseAbilityName;
    }

    public void syncSelectedAbilitiesToInventory(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack style = playerInventory.getItem(0);
        ItemStack expertise1 = playerInventory.getItem(1);
        ItemStack expertise2 = playerInventory.getItem(2);
        ItemStack expertise3 = playerInventory.getItem(3);

        this.style = AbilityItemManager.getRawAbilityName(style);

        if (AbilityItemManager.isAnAbility(expertise1)) {
            this.expertise1 = AbilityItemManager.getRawAbilityName(expertise1);
        } else {
            this.expertise1 = emptyExpertiseAbilityName;
        }

        if (AbilityItemManager.isAnAbility(expertise2)) {
            this.expertise2 = AbilityItemManager.getRawAbilityName(expertise2);
        } else {
            this.expertise2 = emptyExpertiseAbilityName;
        }

        if (AbilityItemManager.isAnAbility(expertise3)) {
            this.expertise3 = AbilityItemManager.getRawAbilityName(expertise3);
        } else {
            this.expertise3 = emptyExpertiseAbilityName;
        }
    }

    // method that checks if any saved abilities are of the AbilityItemManager.errorLoadingAbilityItem()
    public void errorAbilityCheck() {
        String errorName = AbilityItemManager.getRawAbilityName(AbilityItemManager.errorLoadingAbilityItem());

        System.out.println(errorName);
        System.out.println(expertise1);
        System.out.println(expertise2);
        System.out.println(expertise3);
        if (expertise1.equals(errorName)) expertise1 = emptyExpertiseAbilityName;
        if (expertise2.equals(errorName)) expertise2 = emptyExpertiseAbilityName;
        if (expertise3.equals(errorName)) expertise3 = emptyExpertiseAbilityName;
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