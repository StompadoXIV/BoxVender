package me.vender.methods;

import lombok.val;
import me.vender.BoxVender;
import me.vender.ConfigManager;
import me.vender.dao.BonusDAO;
import me.vender.dao.ItemsDAO;
import me.vender.dao.cooldowns.ManuallyDAO;
import me.vender.utils.BoxUtils;
import me.vender.utils.Format;
import me.vender.utils.TimeFormatter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class SellMethod {

    private final ConfigManager settings = BoxVender.getInstance().getSettings();
    private final Economy economy = BoxVender.getInstance().getEconomy();

    public void sell(Player p) {

        if (ManuallyDAO.contains(p) && !p.hasPermission("boxvender.bypass.cooldown")) {
            val time = TimeFormatter.format(ManuallyDAO.getRemainingTime(p));

            BoxUtils.sendMessage(p, settings.getInCooldowm().replace("{tempo}", time));
            return;
        }
        double price = 0;
        int selled = 0;

        val contents = p.getInventory().getContents();
        for (int index = 0; index < contents.length; index++) {
            val item = contents[index];
            if (item == null || ItemsDAO.findItems(item) == null) continue;

            val sellable = ItemsDAO.findItems(item);

            selled += item.getAmount();
            price += sellable.getPrice() * item.getAmount();
            val bonus = BonusDAO.getBonus(p, price);
            price += bonus[0];

            economy.depositPlayer(p, price);
            p.getInventory().setItem(index, null);

        }
        if (selled > 0) {
            ManuallyDAO.add(p);
            BoxUtils.sendMessage(p, settings.getItemsSell().replace("{quantia}", Format.formatNumber(selled)).replace("{valor}", Format.formatNumber(price)));

        } else
            BoxUtils.sendMessage(p, settings.getNoHaveItems());
    }
}