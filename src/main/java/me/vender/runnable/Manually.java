package me.vender.runnable;

import me.vender.dao.cooldowns.ManuallyDAO;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Manually extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(ManuallyDAO::contains).forEach(players -> {
            long time = ManuallyDAO.getRemainingTime(players);

            if (time <= 0) {
                ManuallyDAO.remove(players);
                return;
            }

            ManuallyDAO.getTime().replace(players.getName(), time, (time - 1000L));
        });
    }
}