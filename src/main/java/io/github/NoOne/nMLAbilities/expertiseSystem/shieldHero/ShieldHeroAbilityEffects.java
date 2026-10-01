package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.*;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;

public class ShieldHeroAbilityEffects extends ExpertiseEffectsHelper {
    public static void secondWind(Player player) {
        int chargeUpTime = 30;

        useEnergyAndCooldown(player, 20, 1.5);
        makeUnmovable(player, chargeUpTime);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;

                // charge up
                double radius = 5 * (1 - (timer / 30.0));
                int particleCount = 75;
                Location center = player.getLocation().clone().add(0, 0.15, 0);

                AbilityEffects.horizontalParticleCircle(Particle.END_ROD, center, radius, particleCount);

                if (timer % 10 == 0 && timer != chargeUpTime) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);
                }

                // explosion
                if (timer == chargeUpTime) {
                    AbilityEffects.expandingParticleSphere(Particle.END_ROD, player.getLocation(), 4, 30, .3);
                    player.playSound(player, Sound.ITEM_TOTEM_USE, 1f, 1f);
                    guardingSystem.fullyRegenerateGuard(player);
                    makeMovable(player);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }

    public static void shieldBash(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 1);
        World world = player.getWorld();

        // step back before dashing
        Location sbLocation = player.getLocation().clone();

        sbLocation.setPitch(0);
        sbLocation.setY(0);

        Vector stepBack = sbLocation.getDirection().multiply(-.5);

        player.setVelocity(stepBack);
        makeInvincible(player);
        useEnergyAndCooldown(player, 10, 1.5);

        // bash
        new BukkitRunnable() {
            ArrayList<LivingEntity> hitEntities = new ArrayList<>(); // make sure we can only hit an entity once
            int timer = 20;
            boolean alreadyDashed = false;
            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection();
            Location shieldLocation = playerLocation.clone().add(0, 1.5, 0).add(direction.setY(0).normalize().multiply(1.25));
            ItemDisplay shield = world.spawn(shieldLocation, ItemDisplay.class, entity -> {
                entity.setItemStack(ItemStack.of(Material.SHIELD));
                entity.setRotation(playerLocation.getYaw() + 180, 0);
                entity.setTransformationMatrix(
                        new Matrix4f()
                                .scale(2)
                                .translation(-.5f, 0, -.5f)
                );
                entity.setVisibleByDefault(false);
            });

            @Override
            public void run() {
                timer--;

                playerLocation = player.getLocation();
                direction = playerLocation.getDirection();
                shieldLocation = playerLocation.clone().add(0, 1.5, 0).add(direction.setY(0).normalize().multiply(1.25))
                        .setRotation(playerLocation.getYaw() + 180, 0);
                shield.teleport(shieldLocation);

                // dash
                if (!alreadyDashed) {
                    Vector dash = direction.multiply(2).setY(0);

                    player.setVelocity(dash);
                    alreadyDashed = true;
                    shield.setVisibleByDefault(true);
                    player.playSound(playerLocation, Sound.ITEM_TRIDENT_RETURN, 4f, 1f);
                }

                // particles
                if (timer >= 14) {
                    Particle.DustOptions white = new Particle.DustOptions(Color.WHITE, 1f);
                    double radians = -Math.toRadians(shieldLocation.getYaw());
                    Vector aLittleBehind = direction.clone().multiply(-.5).setY(0);
                    Vector xOffset = new Vector(.75, 0, 0).rotateAroundY(radians).add(aLittleBehind);
                    Vector xNegOffset = new Vector(-.75, 0, 0).rotateAroundY(radians).add(aLittleBehind);
                    Location behindParticleLocation = playerLocation.subtract(direction.multiply(.5)).add(0, .75, 0);

                    world.spawnParticle(Particle.DUST, shieldLocation.clone().add(xOffset), 10, .1, .1, .1, 0, white);
                    world.spawnParticle(Particle.DUST, shieldLocation.clone().add(xNegOffset), 10, .1, .1, .1, 0, white);
                    world.spawnParticle(Particle.DUST, behindParticleLocation, 10, .1, .1, .1, 0, white);
                }

                // damage
                for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, shieldLocation, 1.5, 1, 1.5)) {
                    if (!hitEntities.contains(livingEntity)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                        hitEntities.add(livingEntity);
                        livingEntity.setVelocity(makeKnockbackVector(livingEntity.getLocation(), playerLocation, 1.2, .35));
                        player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 2f, 1f);
                    }
                }

                if (timer == 0) {
                    this.cancel();
                    makeVincible(player);
                    shield.remove();
                }
            }
        }.runTaskTimer(nmlAbilities, 5L, 1L);
    }

    public static void shieldPunch(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 1.5);
        World world = player.getWorld();

        new BukkitRunnable() {
            ArrayList<LivingEntity> hitEntities = new ArrayList<>(); // make sure we can only hit an entity once
            int ticks = 0;
            final int windUpTicks = 15;
            Location playerLocation = player.getLocation();
            Vector direction = playerLocation.getDirection();
            Location shieldLocation = playerLocation.clone().add(0, 1.5, 0).add(direction.setY(0).normalize().multiply(.75));
            ItemDisplay shield = world.spawn(shieldLocation, ItemDisplay.class, entity -> {
                entity.setItemStack(ItemStack.of(Material.SHIELD));
                entity.setTransformationMatrix(
                        new Matrix4f()
                                .scale(2)
                                .translation(-.5f, 0, -.5f)
                                .rotateZ((float) Math.toRadians(90))
                                .rotateX((float) Math.toRadians(90))
                );
                entity.setTeleportDuration(1);
                entity.setInterpolationDuration(20);
                entity.setVisibleByDefault(false);
            });

            @Override
            public void run() {
                ticks++;

                playerLocation = player.getLocation();
                direction = playerLocation.getDirection();
                direction.setY(0);
                direction.normalize();
                shieldLocation = playerLocation.clone().add(0, .5, 0).add(direction.clone().multiply(-.4));
                shieldLocation.setRotation(playerLocation.getYaw() + 180, 0); // makes the shield upright and facing where the player looks

                double radians = -Math.toRadians(playerLocation.getYaw());
                Vector leftShoulder = new Vector(.5, 0, 0).rotateAroundY(radians);


                if (ticks < windUpTicks) { // starting location / wind up
                    shieldLocation.add(leftShoulder);
                    shield.teleport(shieldLocation);

                    if (!shield.isVisibleByDefault()) {
                        shield.setVisibleByDefault(true);
                    }
                } else { // manual punch interpolation
                    Location finalInitialPunchLocation = playerLocation.clone().add(0, 1.25, 0).add(direction.clone().multiply(.2));
                    Location finalPunchLocation = finalInitialPunchLocation.clone().add(direction.clone().multiply(1.25));
                    Matrix4f matrix = new Matrix4f()
                            .scale(2)
                            .translation(-.5f, 0, -.5f);

                    finalPunchLocation.setRotation(playerLocation.getYaw() + 180, 180); // makes the shield upright and facing where the player looks
                    shield.teleport(finalPunchLocation);
                    shield.setTransformationMatrix(matrix);

                    if (ticks == windUpTicks + 20) {
                        cancel();
                        shield.remove();
                    }

                    // damage
                    for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, shieldLocation, 2, 1, 2)) {
                        if (!hitEntities.contains(livingEntity)) {
                            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                            hitEntities.add(livingEntity);
                            livingEntity.setVelocity(makeKnockbackVector(livingEntity.getLocation(), playerLocation, 1.2, .35));
                            player.playSound(playerLocation, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 2f, 1f);
                        }
                    }
                }
            }
        }.runTaskTimer(nmlAbilities, 0, 1);
    }
}