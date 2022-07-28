package me.vender.dao;

import lombok.Getter;
import me.vender.model.Items;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsDAO {

    @Getter
    private static List<Items> items = new ArrayList<>();

    public static Items findItems(ItemStack item) {
        return items.stream().filter(items -> items.getItem().isSimilar(item)).findFirst().orElse(null);
    }

    public static int getAmount(Inventory inventory, Items item) {
        int amount = 0;

        for (ItemStack items : inventory.all(item.getItem().getType()).values()) {
            if (items == item.getItem())
                continue;

            int quantity = items.getAmount();
            amount += quantity;
        }
        return amount;
    }

    public static void remove(Inventory inventory, Items item, int amount) {
        for (Map.Entry<Integer, ? extends ItemStack> entry : inventory.all(item.getItem().getType()).entrySet()) {
            ItemStack items = entry.getValue();
            if (items.isSimilar(item.getItem())) {
                if (items.getAmount() <= amount) {
                    amount -= items.getAmount();
                    inventory.clear(entry.getKey());

                } else {
                    items.setAmount(items.getAmount() - amount);
                    amount = 0;
                }
            }
            if (amount == 0)
                break;
        }
    }
}