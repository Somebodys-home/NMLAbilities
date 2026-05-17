package io.github.NoOne.nMLAbilities.abilitySystem.cooldown;

import org.bukkit.inventory.ItemStack;

public class CooldownInstance {
    private int hotbarSlot;
    private double cooldown;
    private ItemStack originalItem; // store what was there before cooldown

    public CooldownInstance(int hotbarSlot, double cooldown, ItemStack originalItem) {
        this.hotbarSlot = hotbarSlot;
        this.cooldown = cooldown;
        this.originalItem = originalItem;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public ItemStack getOriginalItem() {
        return originalItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CooldownInstance that)) return false;

        return this.hotbarSlot == that.hotbarSlot;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(hotbarSlot);
    }
}
