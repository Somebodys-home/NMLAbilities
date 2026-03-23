package io.github.NoOne.nMLAbilities;

import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityEffectsListener;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.abilityUse.AbilityListener;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldown.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesConfig;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesListener;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilitiesManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererAbilityEffects;
import io.github.NoOne.menuSystem.MenuListener;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLShields.GuardingSystem;
import io.github.NoOne.nMLShields.NMLShields;
import io.github.NoOne.nMLSkills.NMLSkills;
import io.github.NoOne.nMLSkills.skillSetSystem.SkillSetManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLAbilities extends JavaPlugin {
    private static NMLAbilities instance;
    private ProfileManager profileManager;
    private SkillSetManager skillSetManager;
    private GuardingSystem guardingSystem;
    private SelectedAbilitiesManager selectedAbilitiesManager;
    private SelectedAbilitiesConfig selectedAbilitiesConfig;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        skillSetManager = JavaPlugin.getPlugin(NMLSkills.class).getSkillSetManager();
        guardingSystem = JavaPlugin.getPlugin(NMLShields.class).getGuardingSystem();

        selectedAbilitiesConfig = new SelectedAbilitiesConfig(this, "abilities");
        selectedAbilitiesConfig.loadConfig();

        selectedAbilitiesManager = new SelectedAbilitiesManager(this);
        selectedAbilitiesManager.loadSelectedAbilitiesFromConfig();

        cooldownManager = new CooldownManager(this);
        cooldownManager.start();

        new AbilityItemManager(this);
        new ExpertiseAbilityItemMaker(this);
        new SoldierAbilityEffects(this);
        new AssassinAbilityEffects(this);
        new MarauderAbilityEffects(this);
        new CavalierAbilityEffects(this);
        new MartialArtistAbilityEffects(this);
        new ShieldHeroAbilityEffects(this);
        new MarksmanAbilityEffects(this);
        new SorcererAbilityEffects(this);
        new PrimordialAbilityEffects(this);
        new HallowedAbilityEffects(this);
        new AnnulledAbilityEffects(this);

        getCommand("expertise").setExecutor(new ExpertiseCommand(this));
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SelectedAbilitiesListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityEffectsListener(this), this);
    }

    @Override
    public void onDisable() {
        cooldownManager.stop();
        selectedAbilitiesManager.saveAllSelectedAbilitiesToConfig();
        selectedAbilitiesConfig.saveConfig();
    }

    public static NMLAbilities getInstance() {
        return instance;
    }

    public SelectedAbilitiesManager getSelectedManager() {
        return selectedAbilitiesManager;
    }

    public SkillSetManager getSkillSetManager() {
        return skillSetManager;
    }

    public SelectedAbilitiesConfig getSelectedConfig() {
        return selectedAbilitiesConfig;
    }

    public GuardingSystem getGuardingSystem() {
        return guardingSystem;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
