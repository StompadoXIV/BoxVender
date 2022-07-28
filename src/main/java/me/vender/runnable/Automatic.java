package me.vender.runnable;

import me.vender.dao.cooldowns.AutomaticDAO;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Automatic extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(AutomaticDAO::contains).forEach(players -> {
            long time = AutomaticDAO.getRemainingTime(players);
            if (time <= 0) {
                AutomaticDAO.remove(players);
                return;
            }

            AutomaticDAO.getTime().replace(players.getName(), time, (time - 1000L));
        });
    }
}