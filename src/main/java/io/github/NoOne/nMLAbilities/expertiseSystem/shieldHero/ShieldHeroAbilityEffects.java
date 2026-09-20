package io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseEffectsHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldHeroAbilityEffects extends ExpertiseEffectsHelper {
    public static void secondWind(Player player) {
        useEnergyAndCooldown(player, 20, 1.5);
        makeUnmovable(player);
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

                if (timer % 10 == 0 && timer != 30) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1f);
                }

                // explosion
                if (timer == 30) {
                    AbilityEffects.expandingParticleSphere(Particle.END_ROD, player.getLocation(), 4, 30, .3);
                    player.playSound(player, Sound.ITEM_TOTEM_USE, 1f, 1f);
                    guardingSystem.fullyRegenerateGuard(player);
                    makeMovable(player);
                    cancel();
                }
            }
        }.runTaskTimer(nmlAbilities, 0L, 1L);
    }
}