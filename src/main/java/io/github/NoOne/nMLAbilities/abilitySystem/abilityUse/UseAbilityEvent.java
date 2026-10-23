package io.github.NoOne.nMLAbilities.abilitySystem.abilityUse;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class UseAbilityEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack ability;
    private String abilityClass; // for if the ability is an expertise or style ability
    private boolean cancelled;

    public UseAbilityEvent(@NotNull Player player, ItemStack ability, String abilityClass) {
        this.player = player;
        this.ability = ability;
        this.abilityClass = abilityClass;
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

    public ItemStack getAbility() {
        return ability;
    }

    public String getAbilityClass() {
        return abilityClass;
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