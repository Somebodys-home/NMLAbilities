package io.github.NoOne.nMLAbilities.expertiseSystem.marksman;

import io.github.NoOne.damagePlugin.customDamage.DamageConverter;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLEnergySystem.EnergyManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import io.github.NoOne.nMLWeapons.AttackCooldownSystem;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class MarksmanAbilityEffects {
    private static NMLAbilities nmlAbilities;
    private static ProfileManager profileManager;
    private static HashMap<UUID, HashMap<String, BukkitTask>> ongoingEffects = new HashMap<>();

    public MarksmanAbilityEffects(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        profileManager = nmlAbilities.getProfileManager();
    }

    public static void rapidShot(Player user, int hotbarSlot, ItemStack abilityItem) {
        Stats stats = profileManager.getPlayerProfile(user.getUniqueId()).getStats();
        HashMap<DamageType, Double> damage = DamageConverter.multiplyDamageMap(DamageConverter.convertPlayerStats2Damage(stats), .5);
        boolean toggle = AbilityItemManager.getToggleState(abilityItem);
        final int[] preparedArrows = {0};

        CooldownManager.putAllOtherAbilitiesOnCooldown(user, .5, hotbarSlot);
        user.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));
        AttackCooldownSystem.pauseAttackCooldown(user);

        BukkitRunnable rapidShot = new BukkitRunnable() {
            @Override
            public void run() {
                // load arrows
                preparedArrows[0]++;
                user.sendTitle("§a" + preparedArrows[0] + " \uD83C\uDFF9", "", 5, 30, 5);
                user.setMetadata("rapid_shot_arrows", new FixedMetadataValue(nmlAbilities, preparedArrows[0]));
                CooldownManager.putAllOtherAbilitiesOnCooldown(user, .75, hotbarSlot);
                user.getWorld().playSound(user, Sound.ITEM_CROSSBOW_LOADING_END, 1f, 1f);

                // fire arrows at 10
                if (preparedArrows[0] == 10) {
                    AbilityItemManager.toggleAbility(abilityItem, toggle);
                    CooldownManager.putOnCooldown(user, hotbarSlot, AbilityItemManager.getCooldown(abilityItem));
                    ongoingEffects.get(user.getUniqueId()).get("rapidShot").cancel();
                    ongoingEffects.get(user.getUniqueId()).remove("rapidShot");
                    AbilityItemManager.toggleAbility(abilityItem, false);

                    new BukkitRunnable() {
                        int arrows = user.getMetadata("rapid_shot_arrows").getFirst().asInt();

                        @Override
                        public void run() {
                            arrows--;

                            user.sendTitle("§a" + arrows + " \uD83C\uDFF9", "", 2, 20, 5);
                            shootArrow(user, 3.5, .5, damage);
                            user.getWorld().playSound(user, Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
                            user.removePotionEffect(PotionEffectType.SLOWNESS);

                            if (arrows == 0) {
                                AttackCooldownSystem.resumeAttackCooldown(user);
                                cancel();
                            }
                        }
                    }.runTaskTimer(nmlAbilities, 10L, 5L);

                    user.removeMetadata("rapid_shot_arrows", nmlAbilities);
                    cancel();
                }
            }
        };

        if (toggle) { // toggling ON
            // Make sure player's map exists
            ongoingEffects.computeIfAbsent(user.getUniqueId(), k -> new HashMap<>());

            if (!ongoingEffects.get(user.getUniqueId()).containsKey("rapidShot")) {
                EnergyManager.useEnergy(user, 25);
                ongoingEffects.get(user.getUniqueId()).put("rapidShot", rapidShot.runTaskTimer(nmlAbilities, 0L, 10L));
            }

        } else { // toggling OFF
            if (ongoingEffects.containsKey(user.getUniqueId()) && ongoingEffects.get(user.getUniqueId()).containsKey("rapidShot")) {
                ongoingEffects.get(user.getUniqueId()).get("rapidShot").cancel();
                ongoingEffects.get(user.getUniqueId()).remove("rapidShot");

                new BukkitRunnable() {
                    int arrows = user.getMetadata("rapid_shot_arrows").getFirst().asInt();

                    @Override
                    public void run() {
                        arrows--;

                        user.sendTitle("§a" + arrows + " \uD83C\uDFF9", "", 2, 20, 5);
                        shootArrow(user, 3.5, .5, damage);
                        user.getWorld().playSound(user, Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
                        user.removePotionEffect(PotionEffectType.SLOWNESS);

                        if (arrows == 0) {
                            AttackCooldownSystem.resumeAttackCooldown(user);
                            cancel();
                        }
                    }
                }.runTaskTimer(nmlAbilities, 0L, 5L);

                user.removeMetadata("rapid_shot_arrows", nmlAbilities);
            }
        }
    }

    // spread being the radius of possible blocks the arrow can hit from 10m away
    private static void shootArrow(Player shooter, double speed, double spread, HashMap<DamageType, Double> damageMap) {
        Arrow arrow = shooter.launchProjectile(Arrow.class);
        double x = (Math.random() - .5) * (spread * .5);
        double y = (Math.random() - .5) * (spread * .5);
        double z = (Math.random() - .5) * (spread * .5);
        Vector spreadVector = new Vector(x, y, z);

        arrow.setVelocity(shooter.getLocation().getDirection().multiply(speed).add(spreadVector)); // Speed multiplier
        arrow.setCritical(false);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setMetadata("ability_arrow", new FixedMetadataValue(nmlAbilities, damageMap));

        // trail particles
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isOnGround()) {
                    this.cancel();
                    return;
                }

                double speed = arrow.getVelocity().length();
                int particleCount = (int) (Math.pow(speed, 2) * 5);

                if (particleCount > 0) {
                    Location loc = arrow.getLocation();
                    shooter.getWorld().spawnParticle(Particle.CRIT, loc, particleCount,0, 0, 0, 0);
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);

        // arrow despawn task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isInBlock()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            arrow.remove();
                            cancel();
                        }
                    }.runTaskTimer(nmlAbilities, 100L, 2L);

                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 2L);
    }
}