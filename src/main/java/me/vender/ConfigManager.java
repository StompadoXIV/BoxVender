package me.vender;

import lombok.Getter;
import lombok.val;
import me.vender.utils.DateManager;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private String noHaveItems;
    private String itemsSell;
    private String messageActivateShift;
    private String messageActivateAutoSell;
    private String messageDisabledShift;
    private String messageDisabledAutoSell;
    private String inCooldowm;

    private long manually;
    private long shift;
    private long automatic;

    private String formatSellName;
    private List<String> formatSellLore;

    private String inventoryPrincipalName;
    private String inventoryItemsSellName;
    private int inventoryPrincipalRows;

    private String sellName;
    private String sellMaterial;
    public boolean sellIsSkull;
    private String sellSkullUrl;
    private List<String> sellLore;
    private int sellSlot;

    private String itemsSellName;
    private String itemsSellMaterial;
    public boolean itemsSellIsSkull;
    private String itemsSellSkullUrl;
    private List<String> itemsSellLore;
    private int itemsSellSlot;

    private String shiftName;
    private String shiftMaterial;
    public boolean shiftIsSkull;
    private String shiftSkullUrl;
    private List<String> shiftLore;
    private int shiftSlot;

    private String automaticName;
    private String automaticMaterial;
    public boolean automaticIsSkull;
    private String automaticSkullUrl;
    private List<String> automaticLore;
    private int automaticSlot;

    private String nameNextPage;
    private String materialNextPage;
    private boolean isSkullNextPage;
    private String skullUrlNextPage;
    private List<String> loreNextPage;

    private String nameBackPage;
    private String materialBackPage;
    private boolean isSkullBackPage;
    private String skullUrlBackPage;
    private List<String> loreBackPage;

    public void loadConfig() {

        val config = BoxVender.getInstance().getConfig();
        val inv = DateManager.getConfig("inventarios");

        noHaveItems = config.getString("Mensagens.SemItems");
        itemsSell = config.getString("Mensagens.ItemsVendidos");
        messageActivateShift = config.getString("Mensagens.VendaComShiftAtivada");
        messageActivateAutoSell = config.getString("Mensagens.VendaAutomaticaAtivada");
        messageDisabledShift = config.getString("Mensagens.VendaComShiftDesativada");
        messageDisabledAutoSell = config.getString("Mensagens.VendaAutomaticaDesativada");
        inCooldowm = config.getString("Mensagens.EmCooldown");

        manually = config.getLong("Configuracoes.Cooldowns.Manualmente");
        shift = config.getLong("Configuracoes.Cooldowns.Shift");
        automatic = config.getLong("Configuracoes.Cooldowns.Automatico");

        formatSellName = config.getString("Configuracoes.Formatos.ItemsAVenda.Nome");
        formatSellLore = config.getStringList("Configuracoes.Formatos.ItemsAVenda.Lore");
        formatSellLore = formatSellLore.stream().map(l -> l.replace("&", "§")).collect(Collectors.toList());

        inventoryPrincipalName = inv.getString("Inventarios.MenuPrincipal.Nome").replace("&", "§");
        inventoryPrincipalRows = inv.getInt("Inventarios.MenuPrincipal.Tamanho");
        inventoryItemsSellName = inv.getString("Inventarios.ItemsAVenda.Nome").replace("&", "§");

        sellName = inv.getString("Inventarios.MenuPrincipal.Items.Vender.Nome").replace("&", "§");
        sellMaterial = inv.getString("Inventarios.MenuPrincipal.Items.Vender.Material");
        sellIsSkull = inv.getBoolean("Inventarios.MenuPrincipal.Items.Vender.Skull.Ativar");
        sellSkullUrl = inv.getString("Inventarios.MenuPrincipal.Items.Vender.Skull.Skull-URL");
        sellLore = inv.getStringList("Inventarios.MenuPrincipal.Items.Vender.Lore");
        sellLore = sellLore.stream().map(l -> l.replace("&", "§")).collect(Collectors.toList());
        sellSlot = inv.getInt("Inventarios.MenuPrincipal.Items.Vender.Slot");

        itemsSellName = inv.getString("Inventarios.MenuPrincipal.Items.ItemsAVenda.Nome").replace("&", "§");
        itemsSellMaterial = inv.getString("Inventarios.MenuPrincipal.Items.ItemsAVenda.Material");
        itemsSellIsSkull = inv.getBoolean("Inventarios.MenuPrincipal.Items.ItemsAVenda.Skull.Ativar");
        itemsSellSkullUrl = inv.getString("Inventarios.MenuPrincipal.Items.ItemsAVenda.Skull.Skull-URL");
        itemsSellLore = inv.getStringList("Inventarios.MenuPrincipal.Items.ItemsAVenda.Lore");
        itemsSellLore = itemsSellLore.stream().map(l -> l.replace("&", "§")).collect(Collectors.toList());
        itemsSellSlot = inv.getInt("Inventarios.MenuPrincipal.Items.ItemsAVenda.Slot");

        shiftName = inv.getString("Inventarios.MenuPrincipal.Items.VendaComShift.Nome").replace("&", "§");
        shiftMaterial = inv.getString("Inventarios.MenuPrincipal.Items.VendaComShift.Material");
        shiftIsSkull = inv.getBoolean("Inventarios.MenuPrincipal.Items.VendaComShift.Skull.Ativar");
        shiftSkullUrl = inv.getString("Inventarios.MenuPrincipal.Items.VendaComShift.Skull.Skull-URL");
        shiftLore = inv.getStringList("Inventarios.MenuPrincipal.Items.VendaComShift.Lore");
        shiftLore = shiftLore.stream().map(l -> l.replace("&", "§")).collect(Collectors.toList());
        shiftSlot = inv.getInt("Inventarios.MenuPrincipal.Items.VendaComShift.Slot");

        automaticName = inv.getString("Inventarios.MenuPrincipal.Items.VendaAutomatica.Nome").replace("&", "§");
        automaticMaterial = inv.getString("Inventarios.MenuPrincipal.Items.VendaAutomatica.Material");
        automaticIsSkull = inv.getBoolean("Inventarios.MenuPrincipal.Items.VendaAutomatica.Skull.Ativar");
        automaticSkullUrl = inv.getString("Inventarios.MenuPrincipal.Items.VendaAutomatica.Skull.Skull-URL");
        automaticLore = inv.getStringList("Inventarios.MenuPrincipal.Items.VendaAutomatica.Lore");
        automaticLore = automaticLore.stream().map(l -> l.replace("&", "§")).collect(Collectors.toList());
        automaticSlot = inv.getInt("Inventarios.MenuPrincipal.Items.VendaAutomatica.Slot");

        nameNextPage = inv.getString("Inventarios.ItemsAVenda.Items.ProximaPagina.Nome");
        materialNextPage = inv.getString("Inventarios.ItemsAVenda.Items.ProximaPagina.Material");
        isSkullNextPage = inv.getBoolean("Inventarios.ItemsAVenda.Items.ProximaPagina.Ativar");
        skullUrlNextPage = inv.getString("Inventarios.ItemsAVenda.Items.ProximaPagina.Skull-URL");
        loreNextPage = inv.getStringList("Inventarios.ItemsAVenda.Items.ProximaPagina.Lore");

        nameBackPage = inv.getString("Inventarios.ItemsAVenda.Items.PaginaAnterior.Nome");
        materialBackPage = inv.getString("Inventarios.ItemsAVenda.Items.PaginaAnterior.Material");
        isSkullBackPage = inv.getBoolean("Inventarios.ItemsAVenda.Items.PaginaAnterior.Skull.Ativar");
        skullUrlBackPage = inv.getString("Inventarios.ItemsAVenda.Items.PaginaAnterior.Skull.Skull-URL");
        loreBackPage = inv.getStringList("Inventarios.ItemsAVenda.Items.PaginaAnterior.Lore");
    }
}