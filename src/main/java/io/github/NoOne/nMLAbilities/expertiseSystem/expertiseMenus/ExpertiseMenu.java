package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.annulled.AnnulledMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.assassin.AssassinMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.cavalier.CavalierMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.hallowed.HallowedMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.marauder.MarauderMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.marksman.MarksmanMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.martialArtist.MartialArtistMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.primordial.PrimordialMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.shieldHero.ShieldHeroMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.soldier.SoldierMenu;
import io.github.NoOne.nMLAbilities.expertiseSystem.sorcerer.SorcererMenu;
import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.menuSystem.PlayerMenuUtility;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExpertiseMenu extends Menu {
    private NMLAbilities nmlAbilities;
    private Player player;
    private Skills skills;
    private ExpertiseMenuItems expertiseMenuItems;
    private final ItemStack changeLoadout;

    public ExpertiseMenu(NMLAbilities nmlAbilities, PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.nmlAbilities = nmlAbilities;
        player = playerMenuUtility.getOwner();
        skills = nmlAbilities.getSkillSetManager().getSkillSet(player.getUniqueId()).getSkills();
        expertiseMenuItems = new ExpertiseMenuItems(skills);

        changeLoadout = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta meta = changeLoadout.getItemMeta();
        meta.setDisplayName("§7§lChange Ability Loadout");
        changeLoadout.setItemMeta(meta);
    }

    @Override
    public String getMenuName() {
        return "§d§lCHOOSE YOUR EXPERTISE!";
    }

    @Override
    public int getSlots() {
        return 9 * 5;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10 -> new SoldierMenu(nmlAbilities, playerMenuUtility).open();
            case 11 -> new AssassinMenu(nmlAbilities, playerMenuUtility).open();
            case 12 -> new MarauderMenu(nmlAbilities, playerMenuUtility).open();
            case 14 -> new CavalierMenu(nmlAbilities, playerMenuUtility).open();
            case 15 -> new MartialArtistMenu(nmlAbilities, playerMenuUtility).open();
            case 16 -> new ShieldHeroMenu(nmlAbilities, playerMenuUtility).open();
            case 22 -> new MarksmanMenu(nmlAbilities, playerMenuUtility).open();
            case 29 -> new SorcererMenu(nmlAbilities, playerMenuUtility).open();
            case 30 -> new PrimordialMenu(nmlAbilities, playerMenuUtility).open();
            case 32 -> new HallowedMenu(nmlAbilities, playerMenuUtility).open();
            case 33 -> new AnnulledMenu(nmlAbilities, playerMenuUtility).open();
            case 44 -> new ExpertiseLoadoutMenu(nmlAbilities, playerMenuUtility).open();
        }
    }

    @Override
    public void handlePlayerMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, expertiseMenuItems.soldier());
        inventory.setItem(11, expertiseMenuItems.assassin());
        inventory.setItem(12, expertiseMenuItems.marauder());
        inventory.setItem(14, expertiseMenuItems.cavalier());
        inventory.setItem(15, expertiseMenuItems.martialArtist());
        inventory.setItem(16, expertiseMenuItems.shieldHero());
        inventory.setItem(22, expertiseMenuItems.marksman());
        inventory.setItem(29, expertiseMenuItems.sorcerer());
        inventory.setItem(30, expertiseMenuItems.primordial());
        inventory.setItem(32, expertiseMenuItems.hallowed());
        inventory.setItem(33, expertiseMenuItems.annulled());
        inventory.setItem(44, changeLoadout);
    }
}
