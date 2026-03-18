package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem;

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
        style = "§bEmpty Style Ability";
        expertise1 = "§dEmpty Expertise Ability";
        expertise2 = "§dEmpty Expertise Ability";
        expertise3 = "§dEmpty Expertise Ability";
    }

    public void setSelectedAbilitiesFromInventory(PlayerInventory playerInventory) {
        style = playerInventory.getItem(0).getItemMeta().getDisplayName();
        expertise1 = playerInventory.getItem(1).getItemMeta().getDisplayName();
        expertise2 = playerInventory.getItem(2).getItemMeta().getDisplayName();
        expertise3 = playerInventory.getItem(3).getItemMeta().getDisplayName();
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