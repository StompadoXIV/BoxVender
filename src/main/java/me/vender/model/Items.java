package me.vender.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class Items {

    private String name;
    private ItemStack item;
    private double price;
    private int id;

}