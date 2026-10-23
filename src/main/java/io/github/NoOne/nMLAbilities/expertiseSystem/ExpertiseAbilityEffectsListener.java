package io.github.NoOne.nMLAbilities.expertiseSystem;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.UseAbilityEvent;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierAbilityItems;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererAbilityItems;
import io.github.NoOne.nMLSkills.skillSetSystem.SkillSetManager;
import io.github.NoOne.nMLSkills.skillSystem.SkillChangeEvent;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ExpertiseAbilityEffectsListener implements Listener {
    private NMLAbilities nmlAbilities;
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private SkillSetManager skillSetManager;

    public ExpertiseAbilityEffectsListener(NMLAbilities nmlAbilities) {
        this.nmlAbilities = nmlAbilities;
        selectedAbilitiesManager = nmlAbilities.getSelectedAbilitiesManager();
        skillSetManager = nmlAbilities.getSkillSetManager();
    }

    @EventHandler
    public void expertiseAbilityEffect(UseAbilityEvent event) {
        Player player = event.getPlayer();
        String[] selectedAbilities = selectedAbilitiesManager.getSelectedAbilities(event.getPlayer().getUniqueId()).getSelectedAbilitiesArray();
        ItemStack ability = event.getAbility();
        String abilityName = AbilityItemManager.getRawAbilityName(ability);
        boolean toggleState = AbilityItemManager.getToggleState(ability);

        if (event.getAbilityClass().equals("expertise") && Arrays.asList(selectedAbilities).contains(abilityName)) {
            switch (abilityName) {
                // Soldier abilities
                case "Slash" -> SoldierAbilityEffects.slash(player);
                case "X-Slash" -> SoldierAbilityEffects.xSlash(player);

                // Assassin abilities
                case "Slash & Dash" -> AssassinAbilityEffects.slashAndDash(player);

                // Marauder abilities
                case "Blade Tornado" -> MarauderAbilityEffects.bladeTornado(player);
                case "Stomping Tantrum" -> MarauderAbilityEffects.stompingTantrum(player);

                // Cavalier abilities
                case "Seismic Slam" -> CavalierAbilityEffects.seismicSlam(player);

                // Martial Artist abilities
                case "Dropkick" -> MartialArtistAbilityEffects.dropKick(player);

                // Shield Hero abilities
                case "Second Wind" -> ShieldHeroAbilityEffects.secondWind(player);
                case "Shield Bash" -> ShieldHeroAbilityEffects.shieldBash(player);
                case "Shield Punch" -> ShieldHeroAbilityEffects.shieldPunch(player);

                // Marksman abilities
                case "Arrow Hailstorm" -> MarksmanAbilityEffects.arrowHailstorm(player);
                case "Steady Aim" -> MarksmanAbilityEffects.steadyAim(player, toggleState);

                // Sorcerer abilities
                case "Magic Missile EX" -> SorcererAbilityEffects.magicMissileEX(player);
                case "Dragon's Breath" -> SorcererAbilityEffects.dragonsBreath(player);

                // Primordial abilities
                case "Chuck Rock" -> PrimordialAbilityEffects.chuckRock(player);
                case "Pumpkin Bomb" -> PrimordialAbilityEffects.pumpkinBomb(player);
                case "Air Ball" -> PrimordialAbilityEffects.airBall(player);

                // Hallowed abilities
                case "Halo" -> HallowedAbilityEffects.halo(player);

                // Annulled abilities
                case "Black Hole" -> AnnulledAbilityEffects.blackHole(player);

                default -> player.sendMessage("§c§nThis ability doesn't have an effect yet!");
            }
        }
    }

    @EventHandler
    public void giveExpertiseExp(UseAbilityEvent event) {
        int combinedLevel = 0; // the level of all the combined levels of the expertise requirements
        int expForSkill = 5;
        HashMap<Expertise, Integer> requirements = AbilityItemManager.getExpertiseRequirements(event.getAbility());
        HashMap<Expertise, Double> expertisePercents = new HashMap<>(); // a hashmap that sets the % multiplier for exp for each expertise of that ability

        for (int level : requirements.values()) {
            combinedLevel += level;
        }

        for (Map.Entry<Expertise, Integer> entry : requirements.entrySet()) { //
            expertisePercents.put(entry.getKey(), entry.getValue() / (double) combinedLevel);
        }

        for (Map.Entry<Expertise, Double> entry : expertisePercents.entrySet()) {
            Bukkit.getPluginManager().callEvent(new SkillChangeEvent(event.getPlayer(), Expertise.toString(entry.getKey()) + "exp", entry.getValue() * expForSkill));
        }
    }

    @EventHandler
    public void expertiseSkillLevelUp(SkillChangeEvent event) {
        Expertise expertise = Expertise.fromString(event.getSkill());

        if (event.getChange() > 0 && expertise != null) {
            Player player = event.getPlayer();
            Skills skills = skillSetManager.getSkillSet(player.getUniqueId()).getSkills();
            int expertiseLevel = skills.getSkillLevel(event.getSkill());
            int prevLevel = Math.max(expertiseLevel - (int) event.getChange(), 1);
            String color = Expertise.toChatColor(expertise);
            String boldColor = color + "§l";
            String unlockedLock = boldColor + "\uD83D\uDD13";
            ArrayList<ItemStack> unlockedAbilities = getUnlockedAbilities(expertise, skills, prevLevel, expertiseLevel);

            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

            // chat message
            player.sendMessage(boldColor + "---------------------------");
            player.sendMessage(boldColor + Expertise.toString(expertise).toUpperCase() + " LEVEL UP!");
            player.sendMessage("§fLv. §r§8" + prevLevel + " -> §r" + color + expertiseLevel + " §r§f" + Expertise.toString(expertise));

            if (!unlockedAbilities.isEmpty()) {
                player.sendMessage("");
                player.sendMessage(unlockedLock + " §nLEARNED ABILITIES:§r " + unlockedLock);

                for (ItemStack abi : unlockedAbilities) {
                    player.sendMessage("- §n" + AbilityItemManager.getRawAbilityName(abi));

                    for (String string : AbilityItemManager.getDescription(abi)) {
                        player.sendMessage("  " + string);
                    }

                    if (unlockedAbilities.getLast() != abi) {
                        player.sendMessage("");
                    }
                }
            }

            player.sendMessage(boldColor + "---------------------------");

            // firework
            Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.FIREWORK_ROCKET);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.addEffect(FireworkEffect.builder()
                    .withColor(getFireworkColor(expertise))
                    .withFade(Color.SILVER)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .flicker(true)
                    .withTrail()
                    .build());
            fireworkMeta.setPower(0);
            firework.setFireworkMeta(fireworkMeta);
            firework.setMetadata("no_damage", new FixedMetadataValue(nmlAbilities, true));
            firework.detonate();
        }
    }

    private ArrayList<ItemStack> getUnlockedAbilities(Expertise expertise, Skills skills, int prevLevel, int newLevel) {
        ExpertiseAbilityItemHelper expertiseAbilityItems = switch (expertise) {
            case SOLDIER -> new SoldierAbilityItems(skills);
            case ASSASSIN -> new AssassinAbilityItems(skills);
            case MARAUDER -> new MarauderAbilityItems(skills);
            case CAVALIER -> new CavalierAbilityItems(skills);
            case MARTIAL_ARTIST -> new MartialArtistAbilityItems(skills);
            case SHIELD_HERO -> new ShieldHeroAbilityItems(skills);
            case MARKSMAN -> new MarksmanAbilityItems(skills);
            case SORCERER -> new SorcererAbilityItems(skills);
            case PRIMORDIAL -> new PrimordialAbilityItems(skills);
            case HALLOWED -> new HallowedAbilityItems(skills);
            case ANNULLED -> new AnnulledAbilityItems(skills);
        };

        return new ArrayList<>(){{
            for (ItemStack ability : expertiseAbilityItems.getAllExpertiseAbilityItems()) {
                if (AbilityItemManager.meetsExpertiseRequirements(skills, ability)) { // for abilities that have multiple expertise requirements
                    int level = AbilityItemManager.getExpertiseRequirement(ability, expertise);

                    if (level > prevLevel && level <= newLevel) {
                        add(ability);
                    }
                }
            }
        }};
    }

    private Color getFireworkColor(Expertise expertise) {
        return switch (expertise) {
            case SOLDIER -> Color.RED;
            case ASSASSIN -> Color.BLACK;
            case MARAUDER, MARTIAL_ARTIST -> Color.MAROON;
            case CAVALIER -> Color.BLUE;
            case SHIELD_HERO -> Color.TEAL;
            case MARKSMAN -> Color.LIME;
            case SORCERER -> Color.YELLOW;
            case PRIMORDIAL -> Color.GREEN;
            case HALLOWED -> Color.WHITE;
            case ANNULLED -> Color.PURPLE;
        };
    }
}