package io.github.NoOne.nMLAbilities.abilitySystem;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class AbilityEffects { 
    public static void particleSphere(Particle particle, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) / radius;
            double y = Math.cos(i) / radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0);
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void dustSphere(Particle.DustOptions dustOptions, Location center, double radius, int particleCircles) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);

                center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, dustOptions);
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void expandingParticleSphere(Particle particle, Location center, double radius, int particleCircles, double speed) {
        for (double i = 0; i <= Math.PI; i += Math.PI / particleCircles) { // vertical circles
            double r = Math.sin(i) * radius;
            double y = Math.cos(i) * radius;

            for (double a = 0; a < Math.PI * 2; a+= Math.PI / particleCircles) { // horizontal circles
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                Location particleLocation = center.clone().add(x, y, z);
                Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

                center.getWorld().spawnParticle(particle, particleLocation, 0, velocity.getX(), velocity.getY(), velocity.getZ());
                particleLocation.subtract(x, y, z); // reset location
            }
        }
    }

    public static void horizontalParticleCircle(Particle particle, Location center, double radius, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Location particleLocation = center.clone().add(x, 0, z);

            center.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
        }
    }

    public static void expandingHorizontalParticleCircle(Particle particle, Location center, double radius, int particleCount, double speed) {
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location particleLocation = center.clone().add(x, 0, z);
            Vector velocity = particleLocation.toVector().subtract(center.toVector()).normalize().multiply(speed);

            center.getWorld().spawnParticle(particle, particleLocation,0, velocity.getX(), velocity.getY(), velocity.getZ());
        }
    }
}
