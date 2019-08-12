package lubdan.petitionsplus.events;

import lubdan.petitionsplus.PetitionData;
import lubdan.petitionsplus.PetitionsPlus;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatEvent implements Listener {

   private PetitionsPlus Petitions;

    public ChatEvent(PetitionsPlus instance){
        this.Petitions = instance;
    }


    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event){
        if(this.Petitions.getContentChat().contains(event.getPlayer().getName())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.GREEN+"Success, your petition has been posted!");
            this.Petitions.removeContentChat(event.getPlayer().getName());
            PetitionData data =   this.Petitions.grabAuthorData(event.getPlayer().getName());
            data.setContent(event.getMessage());
            this.Petitions.writeNewPetition(event.getPlayer().getName());

        }

        if(this.Petitions.getTitleChat().contains(event.getPlayer().getName())){
            event.setCancelled(true);
            this.Petitions.addContentc(event.getPlayer().getName());
            event.getPlayer().sendMessage(ChatColor.GREEN+"Please enter a short description of the petition!");
            this.Petitions.removeTitleChat(event.getPlayer().getName());
            PetitionData newdata = new PetitionData();
            newdata.setArchived(0);
            newdata.setID(Petitions.countEntries() + 1);
            newdata.setAuthor(event.getPlayer().getName());
            newdata.setTitle(event.getMessage());
            SimpleDateFormat date = new SimpleDateFormat("MM.dd 'at' HH:mm");
            newdata.setTime(date.format(new Date()));
            this.Petitions.addAuthorData(event.getPlayer().getName(), newdata);


        }






    }


}
