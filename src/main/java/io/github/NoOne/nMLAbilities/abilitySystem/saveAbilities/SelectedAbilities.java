package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SelectedAbilities {
    private ItemStack style;
    private ItemStack expertise1;
    private ItemStack expertise2;
    private ItemStack expertise3;

    public SelectedAbilities(ItemStack style, ItemStack expertise1, ItemStack expertise2, ItemStack expertise3) {
        this.style = style;
        this.expertise1 = expertise1;
        this.expertise2 = expertise2;
        this.expertise3 = expertise3;
    }

    public SelectedAbilities(PlayerInventory playerInventory) {
        this.style = playerInventory.getItem(0);
        this.expertise1 = playerInventory.getItem(1);
        this.expertise2 = playerInventory.getItem(2);
        this.expertise3 = playerInventory.getItem(3);
    }

    public void clearSelectedAbilities() {
        style = AbilityItemManager.emptyStyleAbilityItem();
        expertise1 = ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem();
        expertise2 = ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem();
        expertise3 = ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem();
    }

    public boolean hasAbility(ItemStack ability) {
       return style.isSimilar(ability) || expertise1.isSimilar(ability) || expertise2.isSimilar(ability) || expertise3.isSimilar(ability);
    }

    public ItemStack[] getSelectedAbilitiesArray() {
        return new ItemStack[]{style, expertise3, expertise1, expertise2};
    }

    public ItemStack getStyle() {
        return style;
    }

    public void setStyle(ItemStack style) {
        this.style = style;
    }

    public ItemStack getExpertise1() {
        return expertise1;
    }

    public void setExpertise1(ItemStack expertise1) {
        this.expertise1 = expertise1;
    }

    public ItemStack getExpertise2() {
        return expertise2;
    }

    public void setExpertise2(ItemStack expertise2) {
        this.expertise2 = expertise2;
    }

    public ItemStack getExpertise3() {
        return expertise3;
    }

    public void setExpertise3(ItemStack expertise3) {
        this.expertise3 = expertise3;
    }
}