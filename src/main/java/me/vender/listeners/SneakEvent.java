package me.vender.listeners;

import lombok.val;
import me.vender.BoxVender;
import me.vender.methods.SellMethod;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.List;

public class SneakEvent implements Listener {

    private final List<String> shift;

    public SneakEvent(BoxVender main) {
        shift = main.getShift();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void sneakEvent(PlayerToggleSneakEvent e) {
        val p = e.getPlayer();

        if (shift.contains(p.getName())) {
            new SellMethod().sell(p);
        }
    }
}