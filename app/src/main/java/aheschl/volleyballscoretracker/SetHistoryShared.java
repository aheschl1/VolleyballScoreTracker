package aheschl.volleyballscoretracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SetHistoryShared {

    private final Context context;

    SetHistoryShared(Context context){
        this.context = context;
    }

    void setFirstSetHistory(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setFirstSetHistory", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setFirstSetHistory", newValue);
        editor.apply();
    }

    String getFirstSetHistory(){
        SharedPreferences mPrefs = context.getSharedPreferences("setFirstSetHistory",0);
        return mPrefs.getString("setFirstSetHistory", "");
    }


    void setSecondSetHistory(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setSecondSetHistory", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setSecondSetHistory", newValue);
        editor.apply();
    }

    String getSecondSetHistory(){
        SharedPreferences mPrefs = context.getSharedPreferences("setSecondSetHistory",0);
        return mPrefs.getString("setSecondSetHistory", "");
    }


    void setThirdSetHistory(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setThirdSetHistory", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setThirdSetHistory", newValue);
        editor.apply();
    }

    String getThirdSetHistory(){
        SharedPreferences mPrefs = context.getSharedPreferences("setThirdSetHistory",0);
        return mPrefs.getString("setThirdSetHistory", "");
    }

    void setFourthSetHistory(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setFourthSetHistory", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setFourthSetHistory", newValue);
        editor.apply();
    }

    String getFourthSetHistory(){
        SharedPreferences mPrefs = context.getSharedPreferences("setFourthSetHistory",0);
        return mPrefs.getString("setFourthSetHistory", "");
    }

    void setFifthSetHistory(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setFifthSetHistory", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setFifthSetHistory", newValue);
        editor.apply();
    }

    String getFifthSetHistory(){
        SharedPreferences mPrefs = context.getSharedPreferences("setFifthSetHistory",0);
        return mPrefs.getString("setFifthSetHistory", "");
    }

}
