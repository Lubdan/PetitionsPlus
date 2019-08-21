package lubdan.petitionsplus.commands;

import lubdan.petitionsplus.PetitionData;
import lubdan.petitionsplus.PetitionsPlus;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;


public class Petition implements CommandExecutor {

    private PetitionsPlus plugin;


    public Petition(PetitionsPlus instance) {
        this.plugin = instance;

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.plugin.enabled) {

            if (sender instanceof Player) {
                Player player = (Player) sender;

                try {
                    if (args.length > 0) {


                        if(args[0].equalsIgnoreCase("help")){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-1")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-2")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-3")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-4")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-5")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-6")));

                            if(player.hasPermission("Petitions.Staff")){
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Staff-1")));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Staff-2")));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Staff-3")));
                            }

                        }


                        if (args[0].equalsIgnoreCase("create")) {

                            if (player.hasPermission("Petitions.Create")) {

                                if(!this.plugin.getTitleChat().contains(sender.getName()) && !this.plugin.getContentChat().contains(sender.getName())) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Petition-Started")));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Enter-title")));
                                    this.plugin.addTitlec(player.getName());
                                }

                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("No-Permission")));
                            }

                        }
                        if (args[0].equalsIgnoreCase("delete")) {
                            if (args.length == 2) {
                                if (player.hasPermission("Petitions.delete")) {
                                    sender.sendMessage(ChatColor.DARK_RED + "Deleted");
                                    this.plugin.deletePetition(Integer.valueOf(args[1]));
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("No-Permission")));
                                }
                            }
                        }


                        if(args[0].equalsIgnoreCase("GUI")){

                          Inventory inv = Bukkit.createInventory(null,27,"Petitions");


                          ArrayList<PetitionData> list = this.plugin.playerViewPetitions();







                          for(int i = 0; i < list.size(); i++){


                              if(list.get(i).getUpvotes() - list.get(i).getDownvotes() > 0){
                                  Wool m = new Wool(DyeColor.LIME);
                                  ItemStack wool = m.toItemStack(1);
                                  ItemMeta meta = wool.getItemMeta();
                                  meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("GUI-Name-Style").replace("[title]",list.get(i).getTitle()) + " " +list.get(i).getID()));
                                  ArrayList<String> lore = new ArrayList<>();
                                  lore.add(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("Lore-1") + String.valueOf(list.get(i).getUpvotes())));
                                  lore.add(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("Lore-2") + String.valueOf(list.get(i).getDownvotes())));
                                  meta.setLore(lore);
                                  wool.setItemMeta(meta);
                                  inv.setItem(i,wool);

                              }
                              else{
                                  Wool m = new Wool(DyeColor.RED);
                                  ItemStack wool = m.toItemStack(1);
                                  ItemMeta meta = wool.getItemMeta();
                                  meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("GUI-Name-Style").replace("[title]",list.get(i).getTitle()) + " " +list.get(i).getID()));
                                  ArrayList<String> lore = new ArrayList<>();
                                  lore.add(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("Lore-1") + String.valueOf(list.get(i).getUpvotes())));
                                  lore.add(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("Lore-2") + String.valueOf(list.get(i).getDownvotes())));
                                  meta.setLore(lore);
                                  wool.setItemMeta(meta);
                                  inv.setItem(i,wool);
                              }





                          }

                            player.openInventory(inv);


                        }



                        if (args[0].equalsIgnoreCase("archive")) {

                            if (args.length == 2) {
                                if (player.hasPermission("Petitions.archive")) {

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("Archived")));
                                    this.plugin.archivePetition(Integer.valueOf(args[1]));

                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("No-Permission")));
                                }
                            }
                        }

                        if (args[0].equalsIgnoreCase("unarchive")) {

                            if (args.length == 2) {
                                if (player.hasPermission("Petitions.unarchive")) {

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("unArchive")));
                                    this.plugin.unarchivePetition(Integer.valueOf(args[1]));

                                }
                                else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.plugin.getConfig().getString("No-Permission")));
                                }
                            }
                        }

                        if(args[0].equalsIgnoreCase("view")){
                            PetitionData data = this.plugin.ViewPetition(Integer.valueOf(args[1]));
                            String decoration = this.plugin.getConfig().getString("Single-View-Decoration");
                            String extra = this.plugin.getConfig().getString("Single-View-Extra-Data").replace("[author]", data.getAuthor());
                            extra = extra.replace("[time]", data.getTime());
                            String title = this.plugin.getConfig().getString("Single-View-Title").replace("[title]", data.getTitle());
                            String description = this.plugin.getConfig().getString("Single-View-Description").replace("[description]", data.getContent());
                            String votes = this.plugin.getConfig().getString("Single-View-Votes").replace("[upvotes]",String.valueOf(data.getUpvotes()));
                            votes = votes.replace("[downvotes]", String.valueOf(data.getDownvotes()));
                            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Agree")));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/petition vote " + data.getID() + " agree"));
                            TextComponent messag = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Disagree")));
                            messag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/petition vote " + data.getID() + " disagree"));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', extra));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', description));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', votes));
                            player.spigot().sendMessage(message);
                            player.spigot().sendMessage(messag);


                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));

                        }


                        if(args[0].equalsIgnoreCase("vote")){

                            if(args.length == 3){

                                if(args[2].equalsIgnoreCase("agree")){
                                    if(this.plugin.Vote(Integer.valueOf(args[1]), 1, player.getName())){
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Thanks-For-Voting")));
                                    }
                                    else{
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Already-Voted")));
                                    }

                                }
                                else{
                                    if(this.plugin.Vote(Integer.valueOf(args[1]), 0, player.getName())){
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Thanks-For-Voting")));
                                    }
                                    else{
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Already-Voted")));
                                    }
                                }



                            }

                            else{
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Incorrect-Parameters")));
                            }




                        }



                        if (args[0].equalsIgnoreCase("list")) {

                            if (args.length == 1) {
                                if (player.hasPermission("Petitions.Staff")) {

                                    ArrayList<PetitionData> data = this.plugin.staffViewPetitions();
                                    for (int i = 0; i < data.size(); i++) {
                                        String format = this.plugin.getConfig().getString("List-View-Format");
                                        format = format.replace("[id]", String.valueOf(data.get(i).getID()));
                                        format = format.replace("[title]", data.get(i).getTitle());


                                        if(data.get(i).getUpvotes() - data.get(i).getDownvotes()  > 0){

                                            format = format + " Votes: &a" + String.valueOf(data.get(i).getUpvotes() - data.get(i).getDownvotes());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                                        }
                                        else{

                                            format = format + " Votes: &4" + String.valueOf(data.get(i).getUpvotes() - data.get(i).getDownvotes());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                                        }

                                    }
                                    if (data.size() == 0) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("No-Petitions")));
                                    }


                                } else {


                                    ArrayList<PetitionData> data = this.plugin.playerViewPetitions();
                                    System.out.println(data.size());
                                    for (int i = 0; i < data.size(); i++) {
                                        String format = this.plugin.getConfig().getString("List-View-Format");
                                        format = format.replace("[id]", String.valueOf(data.get(i).getID()));
                                        format = format.replace("[title]", data.get(i).getTitle());


                                        if(data.get(i).getUpvotes() - data.get(i).getDownvotes()  > 0){

                                            format = format + " Votes: &a" + String.valueOf(data.get(i).getUpvotes() - data.get(i).getDownvotes());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                                        }
                                        else{

                                            format = format + " Votes: &4" + String.valueOf(data.get(i).getUpvotes() - data.get(i).getDownvotes());
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', format));
                                        }

                                    }
                                    if (data.size() == 0) {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("No-Petitions")));
                                    }


                                }
                            }

                            if (args.length == 2) {

                                PetitionData data = this.plugin.ViewPetition(Integer.valueOf(args[1]));
                                String decoration = this.plugin.getConfig().getString("Single-View-Decoration");
                                String extra = this.plugin.getConfig().getString("Single-View-Extra-Data").replace("[author]", data.getAuthor());
                                extra = extra.replace("[time]", data.getTime());
                                String title = this.plugin.getConfig().getString("Single-View-Title").replace("[title]", data.getTitle());
                                String description = this.plugin.getConfig().getString("Single-View-Description").replace("[description]", data.getContent());
                                String votes = this.plugin.getConfig().getString("Single-View-Votes").replace("[upvotes]",String.valueOf(data.getUpvotes()));
                                votes = votes.replace("[downvotes]", String.valueOf(data.getDownvotes()));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', extra));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', description));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', votes));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));


                            }

                            if (args.length > 2) {
                                sender.sendMessage(ChatColor.RED + "Too many arguments..");
                            }


                        }


                    }
                    else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-1")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-2")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-3")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-4")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-5")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Line-6")));

                    }


                } catch (Exception e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Incorrect-Parameters")));
                }
            }

        }
        else{
            sender.sendMessage(ChatColor.RED+"Plugin is disabled. Please refer to the staff team for assistance");
        }

        return false;
    }

}
