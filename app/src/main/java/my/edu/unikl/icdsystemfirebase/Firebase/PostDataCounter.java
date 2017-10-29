package my.edu.unikl.icdsystemfirebase.Firebase;

/**
 * Created by Mirolhusni95 on 05-Oct-17.
 */

public class PostDataCounter  {

    public String numbPeople,dateTaken,timeTaken;

    public PostDataCounter(){
    }

    public PostDataCounter(String numbPeople){
        this.numbPeople = numbPeople;
    }

    public PostDataCounter(String numbPeople, String dateTaken, String timeTaken){
        this.numbPeople = numbPeople;
        this.dateTaken = dateTaken;
        this.timeTaken = timeTaken;
    }

    public String getNumbPeople() {
        return numbPeople;
    }

    public void setNumbPeople(String numbPeople) {
        this.numbPeople = numbPeople;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }
}
