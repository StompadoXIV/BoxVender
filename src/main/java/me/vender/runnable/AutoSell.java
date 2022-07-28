package me.vender.runnable;

import me.vender.BoxVender;
import me.vender.methods.SellMethod;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSell extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(BoxVender.getInstance().getAutomatic()::contains).forEach(player -> {
            new SellMethod().sell(player);
        });
    }
}