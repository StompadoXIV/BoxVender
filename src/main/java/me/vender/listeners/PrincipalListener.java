package me.vender.listeners;

import lombok.val;
import me.vender.BoxVender;
import me.vender.ConfigManager;
import me.vender.dao.cooldowns.AutomaticDAO;
import me.vender.dao.cooldowns.ShiftDAO;
import me.vender.inventory.ItemsSellInventory;
import me.vender.methods.SellMethod;
import me.vender.utils.BoxUtils;
import me.vender.utils.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class PrincipalListener implements Listener {

    private final ConfigManager settings;

    private final List<String> shift;
    private final List<Player> automatic;

    public PrincipalListener(BoxVender main) {
        settings = main.getSettings();
        shift = main.getShift();
        automatic = main.getAutomatic();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void onClickEvent(InventoryClickEvent e) {
        val title = e.getView().getTitle();
        val slot = e.getSlot();
        val p = (Player) e.getWhoClicked();
        val name = p.getName();
        if (!title.equals(settings.getInventoryPrincipalName()) || title.equals(settings.getInventoryItemsSellName())) return;
        e.setCancelled(true);

        if (title.equals(settings.getInventoryPrincipalName())) {
            if (slot == settings.getSellSlot()) {
                new SellMethod().sell(p);
            } else if (slot == settings.getItemsSellSlot()) {
               new ItemsSellInventory().openItemSell(p);
            } else if (slot == settings.getShiftSlot()) {
                if (!p.hasPermission("boxvender.vender.shift")) {
                    BoxUtils.sendMessage(p, "§cVocê não possui permissão para vender no shift.");
                    return;
                }

                if (shift.contains(name)) {
                    shift.remove(name);
                    BoxUtils.sendMessage(p, settings.getMessageDisabledShift());
                    return;
                }

                if (ShiftDAO.contains(p) && !p.hasPermission("boxvender.bypass.cooldownshift")) {
                    BoxUtils.sendMessage(p, settings.getInCooldowm().replace("{tempo}", TimeFormatter.format(ShiftDAO.getRemainingTime(p))));
                    return;
                }

                shift.add(p.getName());
                ShiftDAO.add(p);
                BoxUtils.sendMessage(p, settings.getMessageActivateShift());
            } else if (slot == settings.getAutomaticSlot()) {
                if (!p.hasPermission("boxvender.vender.automatico")) {
                    BoxUtils.sendMessage(p, "§cVocê não possui permissão para vender no automatico.");
                    return;
                }

                if (automatic.contains(p)) {
                    automatic.remove(p);
                    BoxUtils.sendMessage(p, settings.getMessageDisabledAutoSell());
                    return;
                }

                if (AutomaticDAO.contains(p) && !p.hasPermission("boxvender.bypass.cooldownautomatico")) {
                    BoxUtils.sendMessage(p, settings.getInCooldowm().replace("{tempo}", TimeFormatter.format(AutomaticDAO.getRemainingTime(p))));
                    return;
                }

                automatic.add(p);
                AutomaticDAO.add(p);
                BoxUtils.sendMessage(p, settings.getMessageActivateAutoSell());
            }
        }
    }
}