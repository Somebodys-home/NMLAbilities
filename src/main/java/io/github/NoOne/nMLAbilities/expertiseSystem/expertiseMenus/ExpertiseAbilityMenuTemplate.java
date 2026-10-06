package io.github.NoOne.nMLAbilities.expertiseSystem.expertiseMenus;

import io.github.NoOne.menuSystem.Menu;
import io.github.NoOne.nMLAbilities.NMLAbilities;
import io.github.NoOne.nMLAbilities.abilitySystem.AbilityItemManager;
import io.github.NoOne.nMLAbilities.abilitySystem.saveAbilities.SelectedAbilities;
import io.github.NoOne.nMLAbilities.expertiseSystem.ExpertiseAbilityItemHelper;
import io.github.NoOne.nMLItems.ItemCreator;
import io.github.NoOne.nMLSkills.skillSystem.Skills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// the parent for the other ability selection menus
public class ExpertiseAbilityMenuTemplate extends Menu {
    protected NMLAbilities nmlAbilities;
    protected SelectedAbilities selectedAbilities;
    protected Skills skills;
    private ItemStack clickedItem;
    private ExpertiseAbilityItemHelper expertiseAbilityItemHelper;

    public ExpertiseAbilityMenuTemplate(NMLAbilities nmlAbilities, Player player, ItemStack clickedItem, ExpertiseAbilityItemHelper expertiseAbilityItemHelper) {
        super(player);

        this.nmlAbilities = nmlAbilities;
        this.clickedItem = clickedItem;
        this.expertiseAbilityItemHelper = expertiseAbilityItemHelper;
        selectedAbilities = nmlAbilities.getSelectedAbilitiesManager().getSelectedAbilities(playerMenuUtility.getOwner().getUniqueId());
        skills = nmlAbilities.getSkillSetManager().getSkillSet(playerMenuUtility.getOwner().getUniqueId()).getSkills();
    }

    @Override
    public String getMenuName() {
        return "";
    }

    @Override
    public final int getSlots() {
        return 9 * 6;
    }

    @Override
    public final void handleMenu(InventoryClickEvent event) {
        ItemStack selected = event.getCurrentItem();
        assert selected != null;

        event.setCancelled(true);

        if (event.getSlot() == 53) {
            new ExpertiseMainMenu(nmlAbilities, player).open();
        } else if (AbilityItemManager.isAnAbility(selected)) {
            if (Arrays.stream(selectedAbilities.getSelectedAbilitiesArray()).anyMatch(element -> element.equals(ChatColor.stripColor(Objects.requireNonNull(selected.getItemMeta()).getDisplayName())))) {
                playerMenuUtility.getOwner().sendMessage("§c⚠ §nYou already have this ability selected!§r§c ⚠");
                playerMenuUtility.getOwner().playSound(playerMenuUtility.getOwner(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, .5f);
                return;
            }

            if (!AbilityItemManager.meetsExpertiseRequirements(skills, selected)) {
                playerMenuUtility.getOwner().sendMessage("§c⚠ §nYou are too inexperienced for this ability!§r§c ⚠");
                playerMenuUtility.getOwner().playSound(playerMenuUtility.getOwner(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, .5f);
                return;
            }

            new ExpertiseConfirmMenu(player, selected, this).open();
        }
    }

    @Override
    public final void handlePlayerMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public final void setMenuItems() {
        ItemStack abilityBreakdown = ItemCreator.createItem(
                Material.IRON_SWORD,
                "§d§l§nAbility Breakdown:",
                List.of(
                        "",
                        "§c§lDamages:",
                        "  §7- §r§f§n(#)% Weapon Damage \uD83D\uDDE1§r§f =§r§7 % of your damages",
                        "  §7- §b§nElemental Damage (#x)§r§f =§r§7 your damage for that element",
                        "      §8§o- Elemental damage multipliers override",
                        "        §8§oweapon damage multipliers",
                        "",
                        "§c§lWeapons: §r§fThe weapon type to hold to use the ability",
                        "  §7- Gloves require both hands",
                        "  §7- Shields can be held in either hand",
                        "  §7- You can't use abilities while guarding",
                        "  §7- Bows require a quiver"
                )
        );

        abilityBreakdown.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        for (int slot : getBorderSlots()) {
            inventory.setItem(slot, ItemCreator.createMenuBorder());
        }

        inventory.setItem(4, clickedItem);
        inventory.setItem(45, abilityBreakdown);
        inventory.setItem(53, ItemCreator.createBackoutButton());

        for (ItemStack abilityItem : expertiseAbilityItemHelper.getAllExpertiseAbilityItems()) {
            inventory.setItem(inventory.firstEmpty(), abilityItem);
        }
    }
}
