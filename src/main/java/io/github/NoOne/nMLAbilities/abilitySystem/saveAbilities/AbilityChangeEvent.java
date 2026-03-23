package io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbilityChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String changedAbility;
    private final ItemStack newAbility;
    private final boolean resettingAbilities;

    public AbilityChangeEvent(@NotNull Player player, String changedAbility, ItemStack newAbility) {
        this.player = player;
        this.changedAbility = changedAbility;
        this.newAbility = newAbility;
        resettingAbilities = false;
    }

    public AbilityChangeEvent(@NotNull Player player) {
        this.player = player;
        this.changedAbility = null;
        this.newAbility = null;
        resettingAbilities = true;
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

    public String getChangedAbility() {
        return changedAbility;
    }

    public ItemStack getNewAbility() {
        return newAbility;
    }

    public boolean isResettingAbilities() {
        return resettingAbilities;
    }
}