package lubdan.petitionsplus;

import lubdan.petitionsplus.commands.Petition;
import lubdan.petitionsplus.events.ChatEvent;
import lubdan.petitionsplus.events.InventoryClick;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class PetitionsPlus extends JavaPlugin {

    private Connection DBconnection;
    public boolean enabled;
    private HashMap<Integer, String> Petitions;
    private HashMap<String, PetitionData> AuthorLink;
    private ArrayList<String> pTitleChat;
    private ArrayList<String> pContentChat;








    @Override
    public void onEnable() {
        try{
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            this.enabled = true;
            this.pTitleChat = new ArrayList<>();
            this.pContentChat = new ArrayList<>();
            this.AuthorLink = new HashMap<>();
            File file = new File("");
            initializeDatabase(file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB");

            getCommand("petition").setExecutor(new Petition(this));
            this.getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
            this.getServer().getPluginManager().registerEvents(new InventoryClick(this), this);





        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Connection getDBConnection(){
        return DBconnection;
    }

    private void initializeDatabase(String path){

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "CREATE TABLE IF NOT EXISTS Petitions(\n"
                        + "PetitionNumber integer PRIMARYKEY, \n"
                        + "Content text NOT NULL, \n"
                        + "Title text NOT NULL, \n"
                        + "Author text NOT NULL, \n"
                        + "Date text NOT NULL, \n"
                        + "VotedList text, \n"
                        + "Archived integer, \n"
                        + "Upvotes integer, \n"
                        + "Downvotes integer \n"
                        + ");";
                Statement statement = DBconnection.createStatement();
                statement.execute(sql);

            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
        }




    }

    public int countEntries(){
        File file = new File("");
       String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "SELECT * FROM Petitions";
              Statement stm = this.DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                int counter = 0;
                while(rs.next()){
                    ++counter;
                }
                return counter;

            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
                return -1;
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
            return -1;
        }




    }

    public void addTitlec(String name){
        this.pTitleChat.add(name);
    }

    public void addContentc(String name){

        this.pContentChat.add(name);
    }


    public ArrayList getTitleChat(){
        return pTitleChat;
    }

    public ArrayList getContentChat(){
        return pContentChat;
    }


    public void removeTitleChat(String name){
        this.pTitleChat.remove(name);
    }

    public void removeContentChat(String name){
        this.pContentChat.remove(name);
    }


    public void writeNewPetition(String name){

        try{
            System.out.println("Attempting to initialize a DB update");
            File file = new File("");
            String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";
            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "INSERT INTO Petitions(PetitionNumber,Content,Title,Author,Date,VotedList,Archived,Upvotes,Downvotes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = DBconnection.prepareStatement(sql);
                PetitionData data = this.AuthorLink.get(name);
                preparedStatement.setInt(1,data.getID());
                preparedStatement.setString(2,data.getContent());
                preparedStatement.setString(3,data.getTitle());
                preparedStatement.setString(4,data.getAuthor());
                preparedStatement.setString(5,data.getTime());
                preparedStatement.setString(6,"");
                preparedStatement.setInt(7,data.getArchived());
                preparedStatement.setInt(8,0);
                preparedStatement.setInt(9,0);
                preparedStatement.executeUpdate();




                if(getConfig().getBoolean("Announce")){
                    String decoration = this.getConfig().getString("Single-View-Decoration");
                    String extra = this.getConfig().getString("Single-View-Extra-Data").replace("[author]", data.getAuthor());
                    extra = extra.replace("[time]", data.getTime());
                    String title = this.getConfig().getString("Single-View-Title").replace("[title]", data.getTitle());
                    String description = this.getConfig().getString("Single-View-Description").replace("[description]", data.getContent());
                    String votes = this.getConfig().getString("Single-View-Votes").replace("[upvotes]",String.valueOf(data.getUpvotes()));
                    votes = votes.replace("[downvotes]", String.valueOf(data.getDownvotes()));
                    TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Agree")));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/petition vote " + data.getID() + " agree"));
                    TextComponent messag = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Disagree")));
                    messag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/petition vote " + data.getID() + " disagree"));

                    for(Player player : Bukkit.getOnlinePlayers()){


                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', extra));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', description));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', votes));
                        player.spigot().sendMessage(message);
                        player.spigot().sendMessage(messag);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', decoration));

                    }


                }










            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
        }

    }

    public void addAuthorData(String name, PetitionData data){
        this.AuthorLink.put(name,data);
    }
    public PetitionData grabAuthorData(String name){
        return this.AuthorLink.get(name);
    }

    public void deletePetition(int id){

        try{
            System.out.println("Attempting to initialize a DB update");
            File file = new File("");
            String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";
            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "DELETE FROM Petitions WHERE PetitionNumber = ?";
                PreparedStatement preparedStatement = DBconnection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                preparedStatement.executeUpdate();
            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
        }

    }



    public void archivePetition(int id){

        try{
            System.out.println("Attempting to initialize a DB update");
            File file = new File("");
            String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";
            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "UPDATE Petitions SET ARCHIVED = 1 WHERE PetitionNumber = ?";
                PreparedStatement preparedStatement = DBconnection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                preparedStatement.executeUpdate();
            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
        }

    }





    public void unarchivePetition(int id){

        try{
            System.out.println("Attempting to initialize a DB update");
            File file = new File("");
            String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";
            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){
                String sql = "UPDATE Petitions SET ARCHIVED = 0 WHERE PetitionNumber = ?";
                PreparedStatement preparedStatement = DBconnection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                preparedStatement.executeUpdate();
            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
        }

    }


    public ArrayList staffViewPetitions(){

        File file = new File("");
        String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){

                String sql = "SELECT * FROM Petitions";
                Statement stm = DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ArrayList<PetitionData> data = new ArrayList<>();
                while(rs.next()){
                    PetitionData temp = new PetitionData();
                    temp.setID(rs.getInt("PetitionNumber"));
                    temp.setContent(rs.getString("Content"));
                    temp.setTitle(rs.getString("Title"));
                    temp.setAuthor(rs.getString("Author"));
                    temp.setTime(rs.getString("Date"));
                    temp.setArchived(rs.getInt("Archived"));
                    temp.setUpvotes(rs.getInt("Upvotes"));
                    temp.setDownvotes(rs.getInt("Downvotes"));
                    temp.setUD(temp.getUpvotes() - temp.getDownvotes());
                    data.add(temp);

                }



                return data;


            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
                return new ArrayList<>();
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
            return new ArrayList<>();
        }


    }

       public ArrayList playerViewPetitions(){

        File file = new File("");
        String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){

                String sql = "SELECT * FROM Petitions WHERE Archived = 0";
                Statement stm = DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                ArrayList<PetitionData> data = new ArrayList<>();
                while(rs.next()){
                    PetitionData temp = new PetitionData();
                    temp.setID(rs.getInt("PetitionNumber"));
                    temp.setContent(rs.getString("Content"));
                    temp.setTitle(rs.getString("Title"));
                    temp.setAuthor(rs.getString("Author"));
                    temp.setTime(rs.getString("Date"));
                    temp.setArchived(rs.getInt("Archived"));
                    temp.setUpvotes(rs.getInt("Upvotes"));
                    temp.setDownvotes(rs.getInt("Downvotes"));
                    temp.setUD(temp.getUpvotes() - temp.getDownvotes());
                    data.add(temp);

                }



                return data;


            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
                return new ArrayList<>();
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
            return new ArrayList<>();
        }


    }



    public PetitionData ViewPetition(int id){

        File file = new File("");
        String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){

                String sql = "SELECT * FROM Petitions WHERE PetitionNumber = " + id;
                Statement stm = DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                PetitionData temp = new PetitionData();
                while(rs.next()){

                    temp.setID(rs.getInt("PetitionNumber"));
                    temp.setContent(rs.getString("Content"));
                    temp.setTitle(rs.getString("Title"));
                    temp.setAuthor(rs.getString("Author"));
                    temp.setTime(rs.getString("Date"));
                    temp.setArchived(rs.getInt("Archived"));
                    temp.setUpvotes(rs.getInt("Upvotes"));
                    temp.setDownvotes(rs.getInt("Downvotes"));
                    temp.setUD(temp.getUpvotes() - temp.getDownvotes());


                }



                return temp;


            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
                return null;
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
            return null;
        }


    }




    public boolean Vote(int id, int vote, String name){

        File file = new File("");
        String path = file.getAbsolutePath() + "/plugins/PetitionsPlus/Petitions.DB";

        try{

            String url = "jdbc:sqlite:"+path;
            DBconnection = DriverManager.getConnection(url);
            if(DBconnection != null){

                String sql = "SELECT * FROM Petitions WHERE PetitionNumber = " + id;
                Statement stm = DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                PetitionData temp = new PetitionData();
                while(rs.next()){

                    temp.setID(rs.getInt("PetitionNumber"));
                    temp.setContent(rs.getString("Content"));
                    temp.setTitle(rs.getString("Title"));
                    temp.setAuthor(rs.getString("Author"));
                    temp.setTime(rs.getString("Date"));
                    temp.setVotedList(rs.getString("VotedList"));
                    temp.setArchived(rs.getInt("Archived"));
                    temp.setUpvotes(rs.getInt("Upvotes"));
                    temp.setDownvotes(rs.getInt("Downvotes"));
                    temp.setUD(temp.getUpvotes() - temp.getDownvotes());


                }
                if(temp.getArchived() == 1){
                    return false;
                }



                String[] voted = temp.getVotedList().split(" ");
                for(int i = 0; i < voted.length; i++){
                    if(voted[i].equalsIgnoreCase(name)){
                        return false;
                    }
                }

                temp.setVotedList(temp.getVotedList() + " " + name);

                String sql2 = "UPDATE Petitions SET VotedList = ?, Upvotes = ?, Downvotes = ? WHERE PetitionNumber = ?";

                // 0 is bad, 1 is good
                if(vote == 0){
                    temp.setDownvotes(temp.getDownvotes() + 1);


                }
                else{
                    temp.setUpvotes(temp.getUpvotes()+1);


                }
                PreparedStatement preparedStatement = DBconnection.prepareStatement(sql2);
                preparedStatement.setString(1,temp.getVotedList());
                preparedStatement.setInt(2,temp.getUpvotes());
                preparedStatement.setInt(3,temp.getDownvotes());
                preparedStatement.setInt(4,id);



                preparedStatement.executeUpdate();

                return true;



            }
            else{
                System.out.println("PetitionsPlus was unable to initialize a database");
                return false;
            }

        }
        catch (Exception e){
            System.out.println("Petitions Plus hit a fatal error..The plugin will be disabled!");
            e.printStackTrace();
            this.enabled = false;
            return false;

        }



    }






















}
