package lubdan.petitionsplus.events;

import lubdan.petitionsplus.PetitionData;
import lubdan.petitionsplus.PetitionsPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InventoryClick implements Listener {

 private   PetitionsPlus plugin;
    public InventoryClick(PetitionsPlus instance){
        this.plugin = instance;

    }



    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event){


        if(event.getInventory().getName() != null){

            if(event.getInventory().getName().contains("Petition")){
                event.setCancelled(true);
                if(event.getCurrentItem() != null){
                    if(event.getCurrentItem().getItemMeta() != null){
                        if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Description")){
                            PetitionData data =  this.plugin.ViewPetition(Integer.valueOf(event.getInventory().getName().split("")[event.getInventory().getName().split("").length - 1]));
                            event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("GUI-Description").replace("[description]",data.getContent())));
                        }

                    }

                }
            }



            if(event.getInventory().getName().equalsIgnoreCase("Petitions")){
                event.setCancelled(true);


                if(event.getCurrentItem() != null){

                    if(event.getCurrentItem().getAmount() == 1){

                        System.out.println("I ran");

                        String[] split = event.getCurrentItem().getItemMeta().getDisplayName().split(" ");
                        Inventory inv = Bukkit.createInventory(null, 27, "Petition " + split[split.length - 1]);
                        PetitionData data = this.plugin.ViewPetition(Integer.valueOf(split[split.length - 1]));
                        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
                        ItemMeta meta = pearl.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + data.getTitle());
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(ChatColor.AQUA+"Author: " + data.getAuthor());
                        lore.add(ChatColor.AQUA+"Posted At: " + data.getTime());
                        meta.setLore(lore);
                        pearl.setItemMeta(meta);
                        inv.setItem(4,pearl);



                        ItemStack pearl1 = new ItemStack(Material.ENDER_PEARL);
                        ItemMeta meta1 = pearl1.getItemMeta();
                        meta1.setDisplayName(ChatColor.RED + "Description:");
                        ArrayList<String> lore1 = new ArrayList<>();
                        lore1.add(ChatColor.AQUA+"Click me to read the description");
                        meta1.setLore(lore1);
                        pearl1.setItemMeta(meta1);
                        inv.setItem(13,pearl1);


                        ItemStack pearl2 = new ItemStack(Material.ENDER_PEARL);
                        ItemMeta meta2 = pearl2.getItemMeta();
                        meta2.setDisplayName(ChatColor.BLUE+"Votes");
                        ArrayList<String> lore2 = new ArrayList<>();
                        lore2.add(ChatColor.AQUA+"Up-Votes: " + data.getUpvotes());
                        lore2.add(ChatColor.AQUA+"Down-Votes: " + data.getDownvotes());
                        meta2.setLore(lore2);
                        pearl2.setItemMeta(meta2);
                        inv.setItem(22,pearl2);






                        event.getWhoClicked().openInventory(inv);




                    }

                }



            }

        }


    }

}
