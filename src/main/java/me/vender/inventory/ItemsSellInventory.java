package me.vender.inventory;

import lombok.val;
import me.vender.BoxVender;
import me.vender.ConfigManager;
import me.vender.dao.ItemsDAO;
import me.vender.utils.BoxUtils;
import me.vender.utils.Format;
import me.vender.utils.ItemBuilder;
import me.vender.utils.Scroller;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsSellInventory {

    private final ConfigManager settings = BoxVender.getInstance().getSettings();

    public void openItemSell(Player p) {

        List<ItemStack> itemList = new ArrayList<>();
        ItemsDAO.getItems().forEach(items -> {

            val name = settings.getFormatSellName().replace("{item_nome}", items.getName());

            val itemUnity = Format.formatNumber(items.getPrice());
            val itemPack = Format.formatNumber(items.getPrice() * 64);
            val itemInv = Format.formatNumber(items.getPrice() * 2304);

            List<String> lore = settings.getFormatSellLore();
            lore = lore.stream().map(l -> l.replace("{preco_unidade}", itemUnity).replace("{preco_pack}", itemPack).replace("{preco_inventario}", itemInv)).collect(Collectors.toList());

            ItemStack item = new ItemBuilder(items.getItem().getType(), 1).setName(name).setLore(lore).build();
            itemList.add(item);

        });

        Scroller scroller = new Scroller.ScrollerBuilder().withName(settings.getInventoryItemsSellName()).withArrowsSlots(18, 26).withItems(itemList).build();
        scroller.open(p);
        BoxUtils.playSound(p, Sound.CHEST_OPEN);

    }
}