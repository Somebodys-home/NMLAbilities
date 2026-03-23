package io.github.NoOne.nMLAbilities.abilitySystem.cooldown;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

                        // decrement cooldown
                        ci.setCooldown(ci.getCooldown() - 1);

                        // restore item when cooldown ends
                        if (ci.getCooldown() <= 0) {
                            ItemStack originalItem = ci.getOriginalItem();

                            player.getInventory().setItem(ci.getHotbarSlot(), originalItem);
                            it.remove();

                            if (player.hasCooldown(originalItem.getType())) { // remove cooldown on the original ability item
                                player.setCooldown(originalItem.getType(), 0);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 20L); // every second
    }

    public void stop() {
        for (Map.Entry<UUID, HashSet<CooldownInstance>> playersCooldowns : ongoingCooldowns.entrySet()) {
            Player player = Bukkit.getPlayer(playersCooldowns.getKey());

            if (player != null) {
                resetAllCooldowns(player);
            }
        }

        ongoingCooldowns.clear();
        serverCooldownTask.cancel();
    }

    public static void putOnCooldown(Player player, int hotbarSlot, double seconds) {
        UUID uuid = player.getUniqueId();
        ItemStack originalItem = player.getInventory().getItem(hotbarSlot); // store original item
        CooldownInstance ci = getCooldownInstance(player, hotbarSlot);

        if (ci != null) {
            ci.setCooldown(ci.getCooldown() + seconds); // extend existing seconds
        } else {
            ongoingCooldowns
                    .computeIfAbsent(uuid, k -> new HashSet<>())
                    .add(new CooldownInstance(hotbarSlot, seconds, originalItem));

            // immediately swap the item out
            player.getInventory().setItem(hotbarSlot, AbilityItemManager.cooldownItem());
        }
    }

    public static void putOnHardCooldown(Player player, double seconds) {
        PlayerInventory playerInventory = player.getInventory();

        player.setCooldown(playerInventory.getItem(0), (int) (seconds * 20));
        player.setCooldown(playerInventory.getItem(1), (int) (seconds * 20));
        player.setCooldown(playerInventory.getItem(2), (int) (seconds * 20));
        player.setCooldown(playerInventory.getItem(3), (int) (seconds * 20));
        player.setCooldown(AbilityItemManager.cooldownItem(), (int) (seconds * 20));
    }

    public static void putOnInfiniteHardCooldown(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        Material originalStyle = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(0));
        Material originalExpertise1 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(1));
        Material originalExpertise2 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(2));
        Material originalExpertise3 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(3));

        player.setCooldown(playerInventory.getItem(0), 9999);
        player.setCooldown(playerInventory.getItem(1), 9999);
        player.setCooldown(playerInventory.getItem(2), 9999);
        player.setCooldown(playerInventory.getItem(3), 9999);
        player.setCooldown(AbilityItemManager.cooldownItem(), 999);

        if (originalStyle != null) player.setCooldown(originalStyle, 9999);
        if (originalExpertise1 != null) player.setCooldown(originalExpertise1, 9999);
        if (originalExpertise2 != null) player.setCooldown(originalExpertise2, 9999);
        if (originalExpertise3 != null) player.setCooldown(originalExpertise3, 9999);
    }

    public static void putOnInfiniteHardCooldown(Player player, int exception) {
        PlayerInventory playerInventory = player.getInventory();
        Material originalStyle = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(0));
        Material originalExpertise1 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(1));
        Material originalExpertise2 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(2));
        Material originalExpertise3 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(3));

        if (exception != 0) player.setCooldown(playerInventory.getItem(0), 9999);
        if (exception != 1) player.setCooldown(playerInventory.getItem(1), 9999);
        if (exception != 2) player.setCooldown(playerInventory.getItem(2), 9999);
        if (exception != 3) player.setCooldown(playerInventory.getItem(3), 9999);
        player.setCooldown(AbilityItemManager.cooldownItem(), 999);

        if (originalStyle != null && exception != 0) player.setCooldown(originalStyle, 9999);
        if (originalExpertise1 != null && exception != 1) player.setCooldown(originalExpertise1, 9999);
        if (originalExpertise2 != null && exception != 2) player.setCooldown(originalExpertise2, 9999);
        if (originalExpertise3 != null && exception != 3) player.setCooldown(originalExpertise3, 9999);
    }

    public static void removeHardCooldown(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        Material originalStyle = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(0));
        Material originalExpertise1 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(1));
        Material originalExpertise2 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(2));
        Material originalExpertise3 = AbilityItemManager.getOriginalItemMaterial(playerInventory.getItem(3));

        player.setCooldown(playerInventory.getItem(0), 1);
        player.setCooldown(playerInventory.getItem(1), 1);
        player.setCooldown(playerInventory.getItem(2), 1);
        player.setCooldown(playerInventory.getItem(3), 1);
        player.setCooldown(AbilityItemManager.cooldownItem(), 1);

        if (originalStyle != null) player.setCooldown(originalStyle, 1);
        if (originalExpertise1 != null) player.setCooldown(originalExpertise1, 1);
        if (originalExpertise2 != null) player.setCooldown(originalExpertise2, 1);
        if (originalExpertise3 != null) player.setCooldown(originalExpertise3, 1);
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
