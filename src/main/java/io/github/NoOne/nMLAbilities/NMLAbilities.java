package io.github.NoOne.nMLAbilities;

import io.github.NoOne.nMLAbilities.abilitySystem.AbilityEffectsListener;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityListener;
import io.github.NoOne.nMLAbilities.abilitySystem.cooldownSystem.CooldownManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedConfig;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedListener;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilitiesSystem.SelectedManager;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierAbilityEffects;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemMaker;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseManager;
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
    private SelectedManager selectedManager;
    private SelectedConfig selectedConfig;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        skillSetManager = JavaPlugin.getPlugin(NMLSkills.class).getSkillSetManager();
        guardingSystem = JavaPlugin.getPlugin(NMLShields.class).getGuardingSystem();

        selectedConfig = new SelectedConfig(this, "abilities");
        selectedConfig.loadConfig();

        selectedManager = new SelectedManager(this);
        selectedManager.loadProfilesFromConfig();

        cooldownManager = new CooldownManager(this);
        cooldownManager.start();

        new AbilityItemManager(this);
        new ExpertiseAbilityItemMaker(this);
        new ExpertiseManager(this);
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
        getServer().getPluginManager().registerEvents(new SelectedListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityEffectsListener(this), this);
    }

    @Override
    public void onDisable() {
        cooldownManager.stop();
        selectedManager.saveProfilesToConfig();
        selectedConfig.saveConfig();
    }

    public static NMLAbilities getInstance() {
        return instance;
    }

    public SelectedManager getSelectedManager() {
        return selectedManager;
    }

    public SkillSetManager getSkillSetManager() {
        return skillSetManager;
    }

    public SelectedConfig getSelectedConfig() {
        return selectedConfig;
    }

    public GuardingSystem getGuardingSystem() {
        return guardingSystem;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }
}
