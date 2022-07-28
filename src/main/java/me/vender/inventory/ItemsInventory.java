package me.vender.inventory;

import lombok.val;
import me.vender.BoxVender;
import me.vender.ConfigManager;
import me.vender.utils.BoxUtils;
import me.vender.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ItemsInventory {

    private final ConfigManager settings = BoxVender.getInstance().getSettings();

    public void openPrincipal(Player p) {

        val inv = Bukkit.createInventory(null, 9 * settings.getInventoryPrincipalRows(), settings.getInventoryPrincipalName());

        val sellMaterial = Material.valueOf(settings.getSellMaterial().split(":")[0]);
        val sellData = Integer.parseInt(settings.getSellMaterial().split(":")[1]);

        val sellItem = new ItemBuilder(sellMaterial, 1, sellData).setName(settings.getSellName()).setLore(settings.getSellLore()).build();
        val sellSkull = new ItemBuilder(settings.getSellSkullUrl()).setName(settings.getSellName()).setLore(settings.getSellLore()).build();

        val sellResult = settings.isSellIsSkull() ? sellSkull : sellItem;

        inv.setItem(settings.getSellSlot(), sellResult);

        val itemsSellMaterial = Material.valueOf(settings.getItemsSellMaterial().split(":")[0]);
        val itemsSellData = Integer.parseInt(settings.getItemsSellMaterial().split(":")[1]);

        val itemsSellItem = new ItemBuilder(itemsSellMaterial, 1, itemsSellData).setName(settings.getItemsSellName()).setLore(settings.getItemsSellLore()).build();
        val itemsSellSkull = new ItemBuilder(settings.getItemsSellSkullUrl()).setName(settings.getItemsSellName()).setLore(settings.getItemsSellLore()).build();

        val itemsSellResult = settings.isItemsSellIsSkull() ? itemsSellSkull : itemsSellItem;

        inv.setItem(settings.getItemsSellSlot(), itemsSellResult);

        val shiftMaterial = Material.valueOf(settings.getShiftMaterial().split(":")[0]);
        val shiftData = Integer.parseInt(settings.getShiftMaterial().split(":")[1]);

        val shiftItem = new ItemBuilder(shiftMaterial, 1, shiftData).setName(settings.getShiftName()).setLore(settings.getShiftLore()).build();
        val shiftSkull = new ItemBuilder(settings.getShiftSkullUrl()).setName(settings.getShiftName()).setLore(settings.getShiftLore()).build();

        val shiftResult = settings.isShiftIsSkull() ? shiftSkull : shiftItem;

        inv.setItem(settings.getShiftSlot(), shiftResult);

        val automaticMaterial = Material.valueOf(settings.getAutomaticMaterial().split(":")[0]);
        val automaticData = Integer.parseInt(settings.getAutomaticMaterial().split(":")[1]);

        val automaticItem = new ItemBuilder(automaticMaterial, 1, automaticData).setName(settings.getAutomaticName()).setLore(settings.getAutomaticLore()).build();
        val automaticSkull = new ItemBuilder(settings.getAutomaticSkullUrl()).setName(settings.getAutomaticName()).setLore(settings.getAutomaticLore()).build();

        val automaticResult = settings.isAutomaticIsSkull() ? automaticSkull : automaticItem;

        inv.setItem(settings.getAutomaticSlot(), automaticResult);

        p.openInventory(inv);
        BoxUtils.playSound(p, Sound.CHEST_OPEN);

    }
}