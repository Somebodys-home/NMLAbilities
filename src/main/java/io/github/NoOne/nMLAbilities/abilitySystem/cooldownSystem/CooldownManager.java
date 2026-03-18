package io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CooldownManager {
    private NMLAbilities nmlAbilities;
    private static final HashMap<UUID, HashSet<CooldownInstance>> ongoingCooldowns = new HashMap<>(); // {uuid, [cooldown1, cooldown2, cooldown3, cooldown4]}
    private BukkitTask serverCooldownTask;

    public CooldownManager(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
    }

    public void start() {
        serverCooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    if (!ongoingCooldowns.containsKey(uuid)) continue;

                    Iterator<CooldownInstance> it = ongoingCooldowns.get(uuid).iterator();
                    while (it.hasNext()) {
                        CooldownInstance ci = it.next();

                        // Decrement cooldown
                        ci.setCooldown(ci.getCooldown() - 1);

                        // Restore item when cooldown ends
                        if (ci.getCooldown() <= 0) {
                            player.getInventory().setItem(ci.getHotbarSlot(), ci.getOriginalItem());
                            it.remove();
                        } else {
                            // Still on cooldown: show cooldown item
                            player.getInventory().setItem(ci.getHotbarSlot(), AbilityItemManager.cooldownItem());
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 20L); // every second
    }

    public void stop() {
        for (Map.Entry<UUID, HashSet<CooldownInstance>> playersCooldowns : ongoingCooldowns.entrySet()) {
            Player player = Bukkit.getPlayer(playersCooldowns.getKey());

            if (player != null) resetAllCooldowns(player);
        }

        ongoingCooldowns.clear();
        serverCooldownTask.cancel();
    }

    public static void putOnCooldown(Player player, int hotbarSlot, double cooldown) {
        UUID uuid = player.getUniqueId();
        ItemStack originalItem = player.getInventory().getItem(hotbarSlot); // store original item

        CooldownInstance ci = getCooldownInstance(player, hotbarSlot);

        if (ci != null) {
            ci.setCooldown(ci.getCooldown() + cooldown); // extend existing cooldown
        } else {
            ongoingCooldowns
                    .computeIfAbsent(uuid, k -> new HashSet<>())
                    .add(new CooldownInstance(hotbarSlot, cooldown, originalItem));

            // immediately swap the item out
            player.getInventory().setItem(hotbarSlot, AbilityItemManager.cooldownItem());
        }
    }

    public static CooldownInstance getCooldownInstance(Player player, int hotbarSlot) {
        UUID uuid = player.getUniqueId();
        if (!ongoingCooldowns.containsKey(uuid)) return null;

        return ongoingCooldowns.get(uuid).stream()
                .filter(ci -> ci.getHotbarSlot() == hotbarSlot)
                .findFirst()
                .orElse(null);
    }

    public static void putAllOtherAbilitiesOnCooldown(Player player, double cooldown, int exception) {
        if (exception != 0) putOnCooldown(player, 0, cooldown);
        if (exception != 1) putOnCooldown(player, 1, cooldown);
        if (exception != 2) putOnCooldown(player, 2, cooldown);
        if (exception != 3) putOnCooldown(player, 3, cooldown);
    }

    public static void putAllAbilitiesOnCooldown(Player player, double cooldown) {
        putOnCooldown(player, 0, cooldown);
        putOnCooldown(player, 1, cooldown);
        putOnCooldown(player, 2, cooldown);
        putOnCooldown(player, 3, cooldown);
    }

    public static void resetCooldown(Player player, int hotbarSlot) {
        HashSet<CooldownInstance> cooldowns = ongoingCooldowns.get(player.getUniqueId());

        if (cooldowns != null) {
            for (CooldownInstance cooldownInstance : cooldowns) {
                if (cooldownInstance.getHotbarSlot() == hotbarSlot) {
                    player.getInventory().setItem(cooldownInstance.getHotbarSlot(), cooldownInstance.getOriginalItem());
                    cooldowns.remove(cooldownInstance);
                }
            }
        }
    }

    public static void resetAllCooldowns(Player player) {
        HashSet<CooldownInstance> cooldowns = ongoingCooldowns.get(player.getUniqueId());

        if (cooldowns != null) {
            ongoingCooldowns.remove(player.getUniqueId());

            for (CooldownInstance cooldownInstance : cooldowns) {
                player.getInventory().setItem(cooldownInstance.getHotbarSlot(), cooldownInstance.getOriginalItem());
            }
        }
    }
}
