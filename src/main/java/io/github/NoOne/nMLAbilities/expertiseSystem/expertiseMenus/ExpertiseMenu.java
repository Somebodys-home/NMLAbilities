package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.menuSystem.Menu;
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
import io.github.NoOne.nMLItems.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ExpertiseMenu extends Menu {
    private NMLAbilities nmlAbilities;
    private ExpertiseMenuItems expertiseMenuItems;
    private ItemStack changeLoadout;

    public ExpertiseMenu(NMLAbilities nmlAbilities, Player player) {
        super(player);
        this.nmlAbilities = nmlAbilities;
        expertiseMenuItems = new ExpertiseMenuItems(nmlAbilities.getSkillSetManager().getSkillSet(playerMenuUtility.getOwner().getUniqueId()).getSkills());
        changeLoadout = ItemCreator.createItem(
                Material.STRUCTURE_BLOCK,
                1,
                "§7§lChange Ability Loadout",
                null
        );
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
            case 10 -> new SoldierMenu(nmlAbilities, player).open();
            case 11 -> new AssassinMenu(nmlAbilities, player).open();
            case 12 -> new MarauderMenu(nmlAbilities, player).open();
            case 14 -> new CavalierMenu(nmlAbilities, player).open();
            case 15 -> new MartialArtistMenu(nmlAbilities, player).open();
            case 16 -> new ShieldHeroMenu(nmlAbilities, player).open();
            case 22 -> new MarksmanMenu(nmlAbilities, player).open();
            case 29 -> new SorcererMenu(nmlAbilities, player).open();
            case 30 -> new PrimordialMenu(nmlAbilities, player).open();
            case 32 -> new HallowedMenu(nmlAbilities, player).open();
            case 33 -> new AnnulledMenu(nmlAbilities, player).open();
            case 44 -> new ExpertiseLoadoutMenu(nmlAbilities, player).open();
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
