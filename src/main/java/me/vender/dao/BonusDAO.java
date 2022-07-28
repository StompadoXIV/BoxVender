package me.vender.dao;

import me.vender.model.Bonus;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BonusDAO {

    private static List<Bonus> bonuses = new ArrayList<>();

    public static void addBonus(Bonus bonus) {
        bonuses.add(bonus);
    }

    public static Bonus getBonusByPermission(Player p) {
        return bonuses.stream().filter(bonus -> p.hasPermission(bonus.getPermission())).findFirst().orElse(null);
    }

    public static double[] getBonus(Player p, double value) {
        Bonus bonus = getBonusByPermission(p);
        return bonus != null ? new double[]{(value * bonus.getPercentage()) / 100, bonus.getPercentage()} : new double[]{0, 0};
    }

    public static String getGroup(Player p) {
        Bonus bonus = getBonusByPermission(p);
        return bonus != null ? bonus.getGroup() : "";
    }
}