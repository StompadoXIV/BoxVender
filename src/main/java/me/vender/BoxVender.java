package me.vender;

import lombok.Getter;
import lombok.val;
import me.vender.commands.SellCommand;
import me.vender.dao.BonusDAO;
import me.vender.dao.ItemsDAO;
import me.vender.listeners.PrincipalListener;
import me.vender.listeners.SneakEvent;
import me.vender.model.Bonus;
import me.vender.model.Items;
import me.vender.runnable.AutoSell;
import me.vender.runnable.Automatic;
import me.vender.runnable.Manually;
import me.vender.runnable.Shift;
import me.vender.utils.DateManager;
import me.vender.utils.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoxVender extends JavaPlugin {

    @Getter
    private static BoxVender Instance;
    private ConfigManager settings;

    private Economy economy;

    private List<String> shift = new ArrayList<>();
    private List<Player> automatic = new ArrayList<>();

    public void onEnable() {
        Instance = this;
        registerYaml();
        setupEconomy();
        registerItems();
        registerBonus();
        registerCommands();
        registerEvents();
        sendMessage();

        new Manually().runTaskTimerAsynchronously(this, 20L, 20L);
        new Automatic().runTaskTimerAsynchronously(this, 20L, 20L);
        new Shift().runTaskTimerAsynchronously(this, 20L, 20L);
        new AutoSell().runTaskTimerAsynchronously(this, 20L, 20L);
    }

    private void registerCommands() {
        getCommand("vender").setExecutor(new SellCommand());
    }

    private void registerEvents() {
        new PrincipalListener(this);
        new SneakEvent(this);
    }

    private void registerYaml() {
        settings = new ConfigManager();
        saveDefaultConfig();
        DateManager.createConfig("items");
        DateManager.createConfig("bonus");
        DateManager.createConfig("inventarios");
        settings.loadConfig();
    }

    private void sendMessage() {
        Bukkit.getConsoleSender().sendMessage("§e[BoxVender] §fCriado por §b[Stompado]");
        Bukkit.getConsoleSender().sendMessage("§b[Discord] §fhttps://discord.gg/Z6PbQgdweB");
        Bukkit.getConsoleSender().sendMessage("§e[BoxVender] §aO plugin §eBoxVender §afoi iniciado com sucesso.");
    }

    private void registerItems() {
        int id = 0;

        for (String path : DateManager.getConfig("items").getConfigurationSection("Items").getKeys(false)) {

            val key = DateManager.getConfig("items").getConfigurationSection("Items." + path);

            val name = key.getString("Nome").replace("&", "§");

            val material = Material.valueOf(key.getString("Item").split(":")[0]);
            val data = Integer.parseInt(key.getString("Item").split(":")[1]);

            val price = key.getDouble("Preco");

            val item = new ItemBuilder(material, 1 , data).build();

            val nmsItem = CraftItemStack.asNMSCopy(item);
            val itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
            itemCompound.setInt("Item-ID", id);
            nmsItem.setTag(itemCompound);

            Items items = new Items(name, item, price, id);
            ItemsDAO.getItems().add(items);
            id++;

        }
    }

    private void registerBonus() {

        for (String path : DateManager.getConfig("bonus").getConfigurationSection("Bonus").getKeys(false)) {

            val key = DateManager.getConfig("bonus").getConfigurationSection("Bonus." + path);

            val permission = key.getString("Permissao");
            val group = key.getString("Grupo");
            val percentage = key.getDouble("Porcentagem");

            Bonus bonus = new Bonus(permission, group, percentage);
            BonusDAO.addBonus(bonus);
        }
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> econ = getServer().getServicesManager().getRegistration(Economy.class);
        if (econ == null)
            return;

        economy = econ.getProvider();

    }
}