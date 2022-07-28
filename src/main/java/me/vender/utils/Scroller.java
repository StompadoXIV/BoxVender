package me.vender.utils;

import me.vender.BoxVender;
import me.vender.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Scroller {

    private static final BoxVender main = BoxVender.getPlugin(BoxVender.class);

    private static ConfigManager settings = BoxVender.getInstance().getSettings();

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getInventory().getHolder() instanceof ScrollerHolder) {
                    e.setCancelled(true);

                    ScrollerHolder holder = (ScrollerHolder) e.getInventory().getHolder();
                    if (e.getSlot() == holder.getScroller().previousPage) {
                        if (holder.getScroller().hasPage(holder.getPage() - 1)) {
                            holder.getScroller().open((Player) e.getWhoClicked(), holder.getPage() - 1);

                        }
                    } else if (e.getSlot() == holder.getScroller().nextPage) {
                        if (holder.getScroller().hasPage(holder.getPage() + 1)) {
                            holder.getScroller().open((Player) e.getWhoClicked(), holder.getPage() + 1);

                        }
                    } else if (e.getSlot() == holder.getScroller().backSlot) {
                        e.getWhoClicked().closeInventory();
                        holder.getScroller().backRunnable.run((Player) e.getWhoClicked());

                    } else if (holder.getScroller().slots.contains(e.getSlot())
                            && holder.getScroller().onClickRunnable != null) {
                        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                            return;

                        holder.getScroller().onClickRunnable.run((Player) e.getWhoClicked(), e.getCurrentItem());
                    }

                }

            }
        }, main);

    }

    private Collection<ItemStack> items;
    private HashMap<Integer, Inventory> pages;
    private String name;
    private int inventorySize;
    private List<Integer> slots;
    private int backSlot, previousPage, nextPage;
    private PlayerRunnable backRunnable;
    private ChooseItemRunnable onClickRunnable;

    public Scroller(ScrollerBuilder builder) {
        this.items = builder.items;
        this.pages = new HashMap<>();
        this.name = builder.name;
        this.inventorySize = builder.inventorySize;
        this.slots = builder.slots;
        this.backSlot = builder.backSlot;
        this.backRunnable = builder.backRunnable;
        this.previousPage = builder.previousPage;
        this.nextPage = builder.nextPage;
        this.onClickRunnable = builder.clickRunnable;

        createInventories();
    }

    private void createInventories() {
        if (items.isEmpty()) {
            Inventory inventory = Bukkit.createInventory(new ScrollerHolder(this, 1), inventorySize, name);
            if (backRunnable != null)
                inventory.setItem(backSlot, getBack(1));

            pages.put(1, inventory);
            return;

        }
        List<List<ItemStack>> lists = getPages(items, slots.size());
        int page = 1;

        for (List<ItemStack> list : lists) {
            Inventory inventory = Bukkit.createInventory(new ScrollerHolder(this, page), inventorySize, name);
            int slot = 0;
            for (ItemStack it : list) {
                inventory.setItem(slots.get(slot), it);
                slot++;

            }
            if (page != 1) {
                inventory.setItem(previousPage, getBack(page - 1));

            }
            inventory.setItem(nextPage, getNextPage(page + 1));
            if (backRunnable != null)
                inventory.setItem(backSlot, getBack(page - 1));

            pages.put(page, inventory);
            page++;

        }
        pages.get(pages.size()).setItem(nextPage, new ItemStack(Material.AIR));
    }

    private ItemStack getBack(int page) {
        String name = settings.getNameBackPage();
        List<String> lore = settings.getLoreBackPage();
        lore = lore.stream().map(l -> l.replace("{pagina}", "" + page)).collect(Collectors.toList());

        Material material = Material.valueOf(settings.getMaterialBackPage().split(":")[0]);
        int data = Integer.parseInt(settings.getMaterialBackPage().split(":")[1]);

        ItemStack item = new ItemBuilder(material, 1, data).setName(name).setLore(lore).build();
        ItemStack head = new ItemBuilder(settings.getSkullUrlBackPage()).setName(name).setLore(lore).build();

        return settings.isSkullBackPage() ? head : item;
    }

    private ItemStack getNextPage(int page) {
        String name = settings.getNameNextPage();
        List<String> lore = settings.getLoreNextPage();
        lore = lore.stream().map(l -> l.replace("{pagina}", "" + page)).collect(Collectors.toList());
        ;

        Material material = Material.valueOf(settings.getMaterialNextPage().split(":")[0]);
        int data = Integer.parseInt(settings.getMaterialNextPage().split(":")[1]);

        ItemStack item = new ItemBuilder(material, 1, data).setName(name).setLore(lore).build();
        ItemStack head = new ItemBuilder(settings.getSkullUrlNextPage()).setName(name).setLore(lore).build();

        return settings.isSkullNextPage() ? head : item;
    }

    public int getPages() {
        return pages.size();
    }

    public boolean hasPage(int page) {
        return pages.containsKey(page);
    }

    public void open(Player player) {
        open(player, 1);
    }

    public void open(Player player, int page) {
        player.openInventory(pages.get(page));
    }

    private <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
        List<T> list = new ArrayList<T>(c);

        if (pageSize == null || pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();

        int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        List<List<T>> pages = new ArrayList<List<T>>(numPages);

        for (int pageNum = 0; pageNum < numPages; )
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));

        return pages;
    }

    private class ScrollerHolder implements InventoryHolder {
        private Scroller scroller;
        private int page;

        public ScrollerHolder(Scroller scroller, int page) {
            super();
            this.scroller = scroller;
            this.page = page;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        public Scroller getScroller() {
            return scroller;
        }

        public int getPage() {
            return page;
        }
    }

    public interface PlayerRunnable {
        public void run(Player player);
    }

    public interface ChooseItemRunnable {
        public void run(Player player, ItemStack item);
    }

    public static class ScrollerBuilder {
        private Collection<ItemStack> items;
        private String name;
        private int inventorySize;
        private List<Integer> slots;
        private int backSlot, previousPage, nextPage;
        private PlayerRunnable backRunnable;
        private ChooseItemRunnable clickRunnable;

        private final static List<Integer> ALLOWED_SLOTS = Arrays.asList(11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33);

        public ScrollerBuilder() {
            this.items = new ArrayList<>();
            this.name = "";
            this.inventorySize = 45;
            this.slots = ALLOWED_SLOTS;
            this.backSlot = 18;
            this.previousPage = 40;
            this.nextPage = 26;
        }

        public ScrollerBuilder withItems(Collection<ItemStack> items) {
            this.items = items;
            return this;
        }

        public ScrollerBuilder withOnClick(ChooseItemRunnable clickRunnable) {
            this.clickRunnable = clickRunnable;
            return this;
        }

        public ScrollerBuilder withName(String name) {
            this.name = name.replace("&", "§");
            return this;
        }

        public ScrollerBuilder withSize(int size) {
            this.inventorySize = size;
            return this;
        }

        public ScrollerBuilder withArrowsSlots(int previousPage, int nextPage) {
            this.previousPage = previousPage;
            this.nextPage = nextPage;
            return this;
        }

        public ScrollerBuilder withBackItem(int slot, PlayerRunnable runnable) {
            this.backSlot = slot;
            this.backRunnable = runnable;
            return this;
        }

        public ScrollerBuilder withItemsSlots(List<Integer> slots) {
            this.slots = slots;
            return this;
        }

        public Scroller build() {
            return new Scroller(this);
        }

    }
}