package aheschl.volleyballscoretracker;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Implementation of App Widget functionality.
 */
public class StreamWidget extends AppWidgetProvider{

    public static final String MATCH_CHANGE = "NEW_GAME";

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static StreamingObject streamingObject = new StreamingObject();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stream_widget);
        StreamOnShared streamOnShared = new StreamOnShared(context);
        String streamWatched = streamOnShared.getStreamBeingWatched();

        if(streamWatched == null || streamWatched.length() == 0){

            views.setTextViewText(R.id.appwidget_text, "You are not spectating");
            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);

            views.setViewVisibility(R.id.homeScore, View.INVISIBLE);
            views.setViewVisibility(R.id.awayScore, View.INVISIBLE);
            views.setViewVisibility(R.id.homeSets,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySets,  View.INVISIBLE);
            views.setViewVisibility(R.id.homeSetTitle,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySetTitle,  View.INVISIBLE);

        }else{

            views.setViewVisibility(R.id.appwidget_text, View.INVISIBLE);
            views.setTextViewText(R.id.appwidget_text, "");

            views.setViewVisibility(R.id.homeScore, View.VISIBLE);
            views.setViewVisibility(R.id.awayScore, View.VISIBLE);
            views.setViewVisibility(R.id.homeSets,  View.VISIBLE);
            views.setViewVisibility(R.id.awaySets,  View.VISIBLE);
            views.setViewVisibility(R.id.homeSetTitle,  View.VISIBLE);
            views.setViewVisibility(R.id.awaySetTitle,  View.VISIBLE);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            database.getReference(streamWatched).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(!(streamingObject != null && snapshot.getValue(StreamingObject.class) == null)){
                        streamingObject = snapshot.getValue(StreamingObject.class);
                        updateWidgetData(context, appWidgetManager, views, appWidgetId);
                    }else{
                        if(!streamingObject.homeWonMatch && !streamingObject.awayWonMatch){
                            streamEnded(context, appWidgetManager, views, appWidgetId);
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }



    private static void streamEnded(Context context, AppWidgetManager appWidgetManager, RemoteViews views, int appWidgetId) {

        views.setViewVisibility(R.id.homeScore, View.INVISIBLE);
        views.setViewVisibility(R.id.awayScore, View.INVISIBLE);
        views.setViewVisibility(R.id.homeSets,  View.INVISIBLE);
        views.setViewVisibility(R.id.awaySets,  View.INVISIBLE);
        views.setViewVisibility(R.id.homeSetTitle,  View.INVISIBLE);
        views.setViewVisibility(R.id.awaySetTitle,  View.INVISIBLE);

        views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
        views.setTextViewText(R.id.appwidget_text, "Match ended");

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private static void updateWidgetData(Context context, AppWidgetManager appWidgetManager, RemoteViews views, int appWidgetId) {

        if(streamingObject.homeWonMatch){

            views.setViewVisibility(R.id.homeScore, View.INVISIBLE);
            views.setViewVisibility(R.id.awayScore, View.INVISIBLE);
            views.setViewVisibility(R.id.homeSets,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySets,  View.INVISIBLE);
            views.setViewVisibility(R.id.homeSetTitle,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySetTitle,  View.INVISIBLE);

            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
            views.setTextViewText(R.id.appwidget_text, streamingObject.homeName + " won the match!");

        }else if(streamingObject.awayWonMatch){

            views.setViewVisibility(R.id.homeScore, View.INVISIBLE);
            views.setViewVisibility(R.id.awayScore, View.INVISIBLE);
            views.setViewVisibility(R.id.homeSets,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySets,  View.INVISIBLE);
            views.setViewVisibility(R.id.homeSetTitle,  View.INVISIBLE);
            views.setViewVisibility(R.id.awaySetTitle,  View.INVISIBLE);

            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
            views.setTextViewText(R.id.appwidget_text, streamingObject.awayName + " won the match");
        }
        else{

            views.setViewVisibility(R.id.appwidget_text, View.INVISIBLE);
            views.setTextViewText(R.id.appwidget_text, "");

            views.setViewVisibility(R.id.homeScore, View.VISIBLE);
            views.setViewVisibility(R.id.awayScore, View.VISIBLE);
            views.setViewVisibility(R.id.homeSets,  View.VISIBLE);
            views.setViewVisibility(R.id.awaySets,  View.VISIBLE);
            views.setViewVisibility(R.id.homeSetTitle,  View.VISIBLE);
            views.setViewVisibility(R.id.awaySetTitle,  View.VISIBLE);


            views.setTextViewText(R.id.homeScore, String.valueOf(streamingObject.homeScore));
            views.setTextViewText(R.id.awayScore, String.valueOf(streamingObject.awayScore));

            views.setTextViewText(R.id.homeSets, String.valueOf(streamingObject.homeSetsWon));
            views.setTextViewText(R.id.awaySets, String.valueOf(streamingObject.awaySetsWon));

            views.setTextViewText(R.id.homeSetTitle, streamingObject.homeName);
            views.setTextViewText(R.id.awaySetTitle, streamingObject.awayName);

        }

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(MATCH_CHANGE))
        {
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, StreamWidget.class));
            this.onUpdate(context, gm, ids);
        }
    }
}