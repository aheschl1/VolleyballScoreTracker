package aheschl.volleyballscoretracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

class MySharedPreferences {

    private final Context context;

    MySharedPreferences(Context context){
        this.context = context;
    }

    void setHomeScore(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("homeScore", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("homeScore", newValue);
        editor.apply();
    }

    int getHomeScore(){
        SharedPreferences mPrefs = context.getSharedPreferences("homeScore",0);
        return mPrefs.getInt("homeScore", 0);
    }

    void setAwayScore(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("awayScore", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("awayScore", newValue);
        editor.apply();
    }

    int getAwayScore(){
        SharedPreferences mPrefs = context.getSharedPreferences("awayScore",0);
        return mPrefs.getInt("awayScore", 0);
    }


    void setSMSMessage(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("sms", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sms", newValue);
        editor.apply();
    }

    String getSMSMessage(){
        SharedPreferences mPrefs = context.getSharedPreferences("sms",0);
        return mPrefs.getString("sms", "The score of the volleyball game is " + Constants.HOME_SCORE + " - " + Constants.AWAY_SCORE + " in set number " + Constants.SET + ".");
    }

    void setSMSMessagePostSet(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setSMSMessagePostSet", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setSMSMessagePostSet", newValue);
        editor.apply();
    }

    String getSMSMessagePostSet(){
        SharedPreferences mPrefs = context.getSharedPreferences("setSMSMessagePostSet",0);
        return mPrefs.getString("setSMSMessagePostSet", Constants.WINNING_TEAM + " won the " + Constants.MATCH_OR_SET + ".");
    }

    void setAwayTimeoutOne(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("awayTimeout1", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("awayTimeout1", newValue);
        editor.apply();
    }

    boolean getAwayTimeoutOne(){
        SharedPreferences mPrefs = context.getSharedPreferences("awayTimeout1",0);
        return mPrefs.getBoolean("awayTimeout1", false);
    }

    void setHomeTimeoutOne(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("homeTimeout1", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("homeTimeout1", newValue);
        editor.apply();
    }

    boolean getHomeTimeoutOne(){
        SharedPreferences mPrefs = context.getSharedPreferences("homeTimeout1",0);
        return mPrefs.getBoolean("homeTimeout1", false);
    }


    void setAwayTimeoutTwo(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("awayTimeout2", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("awayTimeout2", newValue);
        editor.apply();
    }

    boolean getAwayTimeoutTwo(){
        SharedPreferences mPrefs = context.getSharedPreferences("awayTimeout2",0);
        return mPrefs.getBoolean("awayTimeout2", false);
    }

    void set15PointTieBreaker(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("tieBreaker", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("tieBreaker", newValue);
        editor.apply();
    }

    boolean get15PointTieBreaker(){
        SharedPreferences mPrefs = context.getSharedPreferences("tieBreaker",0);
        return mPrefs.getBoolean("tieBreaker", true);
    }

    void setHomeTimeoutTwo(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("homeTimeout2", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("homeTimeout2", newValue);
        editor.apply();
    }

    boolean getHomeTimeoutTwo(){
        SharedPreferences mPrefs = context.getSharedPreferences("homeTimeout2",0);
        return mPrefs.getBoolean("homeTimeout2", false);
    }

    void setVibrate(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setVibrate", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setVibrate", newValue);
        editor.apply();
    }

    int getVibrate(){
        SharedPreferences mPrefs = context.getSharedPreferences("setVibrate",0);
        return mPrefs.getInt("setVibrate", Constants.OFF);
    }


    void setNumbers(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setNumbers", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setNumbers", newValue);
        editor.apply();
    }

    String getNumbers(){
        SharedPreferences mPrefs = context.getSharedPreferences("setNumbers",0);
        return mPrefs.getString("setNumbers", "");
    }

    void setHomeSetsWon(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setHomeSetsWon", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setHomeSetsWon", newValue);
        editor.apply();
    }

    int getHomeSetsWon(){
        SharedPreferences mPrefs = context.getSharedPreferences("setHomeSetsWon",0);
        return mPrefs.getInt("setHomeSetsWon", 0);
    }

    void setAwaySetsWon(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setAwaySetsWon", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setAwaySetsWon", newValue);
        editor.apply();
    }

    int getAwaySetsWon(){
        SharedPreferences mPrefs = context.getSharedPreferences("setAwaySetsWon",0);
        return mPrefs.getInt("setAwaySetsWon", 0);
    }


    void setNumberOfSets(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setNumberOfSets", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setNumberOfSets", newValue);
        editor.apply();
    }

    int gettNumberOfSets(){
        SharedPreferences mPrefs = context.getSharedPreferences("setNumberOfSets",0);
        return mPrefs.getInt("setNumberOfSets", 3);
    }

    void setHomeTeamName(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setHomeTeamName", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setHomeTeamName", newValue);
        editor.apply();
    }

    String getHomeTeamName(){
        SharedPreferences mPrefs = context.getSharedPreferences("setHomeTeamName",0);
        return mPrefs.getString("setHomeTeamName", "Home");
    }

    void setAwayTeamName(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setAwayTeamName", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setAwayTeamName", newValue);
        editor.apply();
    }

    String getAwayTeamName(){
        SharedPreferences mPrefs = context.getSharedPreferences("setAwayTeamName",0);
        return mPrefs.getString("setAwayTeamName", "Away");
    }

    void setShowMessageForSideChange(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setShowMessageForSideChange", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setShowMessageForSideChange", newValue);
        editor.apply();
    }

    int getShowMessageForSideChange(){
        SharedPreferences mPrefs = context.getSharedPreferences("setShowMessageForSideChange",0);
        return mPrefs.getInt("setShowMessageForSideChange", Constants.ON);
    }

    void setAutoText(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setAutoText", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setAutoText", newValue);
        editor.apply();
    }

    int getAutoText(){
        SharedPreferences mPrefs = context.getSharedPreferences("setAutoText",0);
        return mPrefs.getInt("setAutoText", Constants.OFF);
    }

    void setNotifyAtEnd(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setNotifyAtEnd", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setNotifyAtEnd", newValue);
        editor.apply();
    }

    int getNotifyAtEnd(){
        SharedPreferences mPrefs = context.getSharedPreferences("setNotifyAtEnd",0);
        return mPrefs.getInt("setNotifyAtEnd", Constants.ON);
    }

    void setHomeButtonIndex(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setHomeButtonIndex", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setHomeButtonIndex", newValue);
        editor.apply();
    }

    int getHomeButtonIndex(){
        SharedPreferences mPrefs = context.getSharedPreferences("setHomeButtonIndex",0);
        return mPrefs.getInt("setHomeButtonIndex", 0);
    }

    void setAnimations(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setAnimations", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setAnimations", newValue);
        editor.apply();
    }

    int getAnimations(){
        SharedPreferences mPrefs = context.getSharedPreferences("setAnimations",0);
        return mPrefs.getInt("setAnimations", Constants.ON);
    }

    void setSMSPopup(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setSMSPopup", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setSMSPopup", newValue);
        editor.apply();
    }

    int getSMSPopup(){
        SharedPreferences mPrefs = context.getSharedPreferences("setSMSPopup",0);
        return mPrefs.getInt("setSMSPopup", Constants.ON);
    }


    void setServer(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setServer", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setServer", newValue);
        editor.apply();
    }

    int getServer(){
        SharedPreferences mPrefs = context.getSharedPreferences("setServer",0);
        return mPrefs.getInt("setServer", Constants.NEUTRAL);
    }

    void setTextColor(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("textColor", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("textColor", newValue);
        editor.apply();
    }

    int getTextColor(){
        SharedPreferences mPrefs = context.getSharedPreferences("textColor",0);
        return mPrefs.getInt("textColor", Color.BLACK);
    }

    void setHomeColor(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setColor", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setColor", newValue);
        editor.apply();
    }

    int getHomeColor(){
        SharedPreferences mPrefs = context.getSharedPreferences("setColor",0);
        return mPrefs.getInt("setColor", Constants.OFF);
    }


    void setAwayColor(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setAwayColor", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setAwayColor", newValue);
        editor.apply();
    }

    int getAwayColor(){
        SharedPreferences mPrefs = context.getSharedPreferences("setAwayColor",0);
        return mPrefs.getInt("setAwayColor", Constants.OFF);
    }

    void setTypeOfVolleyball(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setTypeOfVolleyball", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setTypeOfVolleyball", newValue);
        editor.apply();
    }

    int getTypeOfVolleyball(){
        SharedPreferences mPrefs = context.getSharedPreferences("setTypeOfVolleyball",0);
        return mPrefs.getInt("setTypeOfVolleyball", Constants.INDOOR);
    }

    void setMyPlayer(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setMyPlayer", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setMyPlayer", newValue);
        editor.apply();
    }

    String getMyPlayer(){
        SharedPreferences mPrefs = context.getSharedPreferences("setMyPlayer",0);
        return mPrefs.getString("setMyPlayer", Constants.stringOFF);
    }

    void setPlayerName(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setPlayerName", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setPlayerName", newValue);
        editor.apply();
    }

    String getPlayerName(){
        SharedPreferences mPrefs = context.getSharedPreferences("setPlayerName",0);
        return mPrefs.getString("setPlayerName", "");
    }

    void setPlayerInfoOn(int newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setPlayerInfoOn", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("setPlayerInfoOn", newValue);
        editor.apply();
    }

    int getPlayerInfoOn(){
        SharedPreferences mPrefs = context.getSharedPreferences("setPlayerInfoOn",0);
        return mPrefs.getInt("setPlayerInfoOn", Constants.OFF);
    }

    void setAmazonMessageDismissed(boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences("amazonM", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("amazonM", newValue);
        editor.apply();
    }

    boolean getAmazonMessageDismissed(){
        SharedPreferences mPrefs = context.getSharedPreferences("amazonM",0);
        return mPrefs.getBoolean("amazonM", false);
    }

}
