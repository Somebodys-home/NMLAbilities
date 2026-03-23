package io.github.NoOne.nMLAbilities.abilitySystem.abilityUse;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class UseAbilityEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack weapon;
    private final ItemStack abilityItem;
    private final int hotbarSlot;
    private boolean cancelled;

    public UseAbilityEvent(@NotNull Player player, ItemStack weapon, ItemStack abilityItem, int hotbarSlot) {
        this.player = player;
        this.weapon = weapon;
        this.abilityItem = abilityItem;
        this.hotbarSlot = hotbarSlot;
        cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public ItemStack getAbilityItem() {
        return abilityItem;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}