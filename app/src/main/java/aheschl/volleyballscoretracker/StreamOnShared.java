package aheschl.volleyballscoretracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

class StreamOnShared {

    private final Context context;

    StreamOnShared(Context context){
        this.context = context;
    }

    void setStreamOn(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setStreamOn", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setStreamOn", newValue);
        editor.apply();
    }

    String getStreamOn(){
        SharedPreferences mPrefs = context.getSharedPreferences("setStreamOn",0);
        return mPrefs.getString("setStreamOn", null);
    }

    void setStreamBeingWatched(String newValue) {
        SharedPreferences prefs = context.getSharedPreferences("setStreamWatch", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("setStreamWatch", newValue);
        editor.apply();

        sendUpdateIntent(context);
    }

    String getStreamBeingWatched(){
        SharedPreferences mPrefs = context.getSharedPreferences("setStreamWatch",0);
        return mPrefs.getString("setStreamWatch", null);
    }

    public void sendUpdateIntent(Context context)
    {
        Intent i = new Intent(context, StreamWidget.class);
        i.setAction(StreamWidget.MATCH_CHANGE);
        context.sendBroadcast(i);
    }

}
