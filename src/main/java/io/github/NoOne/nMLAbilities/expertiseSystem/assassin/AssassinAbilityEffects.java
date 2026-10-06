package io.github.NoOne.nMLAbilities.expertiseSystem.assassin;

import io.github.NoOne.damagePlugin.customDamage.CustomDamageEvent;
import io.github.NoOne.damagePlugin.customDamage.DamageType;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityEffects.AbilityEffectsHelper;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class AssassinAbilityEffects extends AbilityEffectsHelper {
    public static void slashAndDash(Player player) {
        HashMap<DamageType, Double> damage = getDamageForAbility(player, 1.5);
        World world = player.getWorld();

        useEnergyAndCooldown(player, 15, 1.2);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        // dash
        Vector dash = player.getLocation().getDirection().multiply(4).setY(-2);

        player.setVelocity(dash);
        makeInvincible(player);

        // slash
        new BukkitRunnable() {
            ArrayList<LivingEntity> hitEntities = new ArrayList<>(); // make sure we can only hit an entity once
            int dashTicks = 6;

            @Override
            public void run() {
                dashTicks--;

                Location particleLocation = player.getLocation().add(0, 1, 0);
                Vector direction = particleLocation.getDirection().multiply(1.2);

                particleLocation.add(direction); // so that particle is a little in front of the player
                world.spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 0, 0, 0, 0, 0);

                for (LivingEntity livingEntity : getNearbyEntitiesExcludingPlayer(player, player.getLocation(), 2, 1, 2)) {
                    if (!hitEntities.contains(livingEntity)) {
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(livingEntity, player, damage));
                        hitEntities.add(livingEntity);
                    }
                }

                if (dashTicks == 0) {
                    this.cancel();
                    makeVincible(player);
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}