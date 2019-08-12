package lubdan.petitionsplus;

public class PetitionData {

    private int ID;
    private String content;
    private String title;
    private String Author;
    private String time;
    private String VotedList;
    private int Archived;
    private int upvotes;
    private int downvotes;
    private int UD;

    public String getVotedList() {
        return VotedList;
    }

    public void setVotedList(String votedList) {
        VotedList = votedList;
    }


    public int getArchived() {
        return Archived;
    }

    public void setArchived(int archived) {
        Archived = archived;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getUD() {
        return UD;
    }

    public void setUD(int UD) {
        this.UD = UD;
    }
}
