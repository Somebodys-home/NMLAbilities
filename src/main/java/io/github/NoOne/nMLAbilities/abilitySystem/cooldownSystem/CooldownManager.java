package io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.Expertise;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CooldownManager {
    private static NMLAbilities nmlAbilities = NMLAbilities.getInstance();
    private static HashMap<UUID, HashSet<CooldownInstance>> ongoingCooldowns = new HashMap<>(); // {uuid, [cooldown1, cooldown2, cooldown3, cooldown4]}
    private static HashMap<UUID, Integer> hardCooldowns = new HashMap<>();

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    if (!ongoingCooldowns.containsKey(uuid)) continue;

                    Iterator<CooldownInstance> it = ongoingCooldowns.get(uuid).iterator();
                    while (it.hasNext()) {
                        CooldownInstance ci = it.next();

                        // decrement cooldown
                        ci.setCooldown(ci.getCooldown() - 1);

                        // restore item when cooldown ends
                        if (ci.getCooldown() <= 0) {
                            ItemStack originalItem = ci.getOriginalItem();

                            player.getInventory().setItem(ci.getHotbarSlot(), originalItem);
                            it.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 20);
    }

    public void stop() {
        for (Map.Entry<UUID, HashSet<CooldownInstance>> playersCooldowns : ongoingCooldowns.entrySet()) {
            Player player = Bukkit.getPlayer(playersCooldowns.getKey());

            if (player != null) {
                resetAllCooldowns(player);
            }
        }

        ongoingCooldowns.clear();
    }

    public static void putOnSoftCooldown(Player player, int hotbarSlot, double seconds) {
        UUID uuid = player.getUniqueId();
        ItemStack originalItem = player.getInventory().getItem(hotbarSlot); // store original item
        CooldownInstance ci = getCooldownInstance(player, hotbarSlot);

        if (ci != null) {
            ci.setCooldown(ci.getCooldown() + seconds); // extend existing seconds
        } else {
            ongoingCooldowns.computeIfAbsent(uuid, _ -> new HashSet<>()).add(new CooldownInstance(hotbarSlot, seconds, originalItem));
            player.getInventory().setItem(hotbarSlot, AbilityItemManager.cooldownItem()); // immediately swap the item out
        }
    }

    public static void putOnHardCooldown(Player player, double seconds) {
        for (Expertise expertise : Expertise.values()) {
            player.setCooldown(Expertise.toMaterial(expertise), (int) (seconds * 20));
        }

        player.setCooldown(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem(), (int) (seconds * 20));
        player.setCooldown(AbilityItemManager.cooldownItem(), (int) (seconds * 20));
        player.setCooldown(AbilityItemManager.emptyStyleAbilityItem(), (int) (seconds * 20));
    }

    public static void putOnInfiniteHardCooldown(Player player) {
        hardCooldowns.put(player.getUniqueId(), player.getCooldown(ExpertiseAbilityItemMaker.emptyExpertiseAbilityItem()));
        putOnHardCooldown(player, 9999);
    }

    public static void removeHardCooldown(Player player) {
        putOnHardCooldown(player, 0);
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

    public static int getSavedHardCooldown(Player player) {
        return hardCooldowns.get(player.getUniqueId());
    }

    public static CooldownInstance getCooldownInstance(Player player, int hotbarSlot) {
        UUID uuid = player.getUniqueId();

        if (!ongoingCooldowns.containsKey(uuid)) {
            return null;
        }

        return ongoingCooldowns.get(uuid).stream()
                .filter(ci -> ci.getHotbarSlot() == hotbarSlot)
                .findFirst()
                .orElse(null);
    }
}
