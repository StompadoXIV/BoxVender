package me.vender.runnable;

import me.vender.dao.cooldowns.ShiftDAO;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Shift extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(ShiftDAO::contains).forEach(players -> {
            long time = ShiftDAO.getRemainingTime(players);

            if (time <= 0) {
                ShiftDAO.remove(players);
                return;
            }

            ShiftDAO.getTime().replace(players.getName(), time, (time - 1000L));
        });
    }

}
