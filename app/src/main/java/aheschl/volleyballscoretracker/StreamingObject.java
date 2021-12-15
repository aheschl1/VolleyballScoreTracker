package aheschl.volleyballscoretracker;

import java.util.ArrayList;

class StreamingObject {

    public int homeScore;
    public int awayScore;

    public int homeSetsWon;
    public int awaySetsWon;
    public int setNumber;

    public int homeTimeoutsUsed;
    public int awayTimeoutsUsed;

    public ArrayList<String> setHistory;

    public boolean homeServing;
    public boolean awayServing;

    public int homeColor;
    public int awayColor;
    public int textColor;

    public boolean homeWonMatch;
    public boolean awayWonMatch;

    public String homeName;
    public String awayName;

    StreamingObject(){

    }

    StreamingObject(int homeScore, int awayScore,
                    int homeSetsWon, int awaySetsWon,
                    int setNumber,
                    int homeTimeoutsUsed, int awayTimeoutsUsed,
                    ArrayList<String> setHistory,
                    boolean homeServing, boolean awayServing,
                    String homeName, String awayName,
                    int homeColor, int awayColor, int textColor,
                    boolean homeWonMatch,
                    boolean awayWonMatch){

        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeSetsWon = homeSetsWon;
        this.awaySetsWon = awaySetsWon;
        this.setNumber = setNumber;
        this.homeTimeoutsUsed = homeTimeoutsUsed;
        this.awayTimeoutsUsed = awayTimeoutsUsed;
        this.setHistory = setHistory;
        this.homeServing = homeServing;
        this.awayServing = awayServing;
        this.homeName = homeName;
        this.awayName = awayName;
        this.homeColor = homeColor;
        this.awayColor = awayColor;
        this.textColor = textColor;
        this.homeWonMatch = homeWonMatch;
        this.awayWonMatch = awayWonMatch;
    }

}
