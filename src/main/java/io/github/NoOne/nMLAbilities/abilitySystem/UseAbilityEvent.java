package io.github.NoOne.nMLAbilities.abilitySystem;

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
    private final ItemStack ability;
    private final int hotbarSlot;

    public UseAbilityEvent(@NotNull Player player, ItemStack weapon, ItemStack ability, int hotbarSlot) {
        this.player = player;
        this.weapon = weapon;
        this.ability = ability;
        this.hotbarSlot = hotbarSlot;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; } // deleting this breaks things, apparently

    public Player getPlayer() { return player; }

    public ItemStack getWeapon() { return weapon; }

    public ItemStack getAbility() { return ability; }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}