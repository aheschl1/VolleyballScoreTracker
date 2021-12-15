package aheschl.volleyballscoretracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WatchStream extends AppCompatActivity {

    private String gameCode = "";
    FirebaseDatabase database;

    Button homeButton;
    Button awayButton;

    Button homeButton2;
    Button awayButton2;

    CheckBox homeTimeout1;
    CheckBox homeTimeout2;
    CheckBox awayTimeout1;
    CheckBox awayTimeout2;

    TextView homeSetNumber;
    TextView awaySetNumber;

    TextView homeTeamName;
    TextView awayTeamName;

    ImageView awayIndicator;
    ImageView homeIndicator;

    TextView setOneHistory;
    TextView setTwoHistory;
    TextView setThreeHistory;
    TextView setFourHistory;
    TextView setFiveHistory;


    Animation slideDownAway;
    Animation slideUpAway;
    Animation slideUpHome;
    Animation slideDownHome;

    TextView currentSet;

    private StreamingObject streamingObject;
    StreamOnShared streamOnShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_view);
        setTitle("Spectating");

        streamOnShared = new StreamOnShared(this);
        AdView adView = findViewById(R.id.adView);

        if(PayedCheck.installedPayedVersion(this)){
            RelativeLayout parent = findViewById(R.id.parent);
            parent.removeView(adView);
        }else{
            AdRequest adRequest = new AdRequest.Builder().build();
            try{ adView.loadAd(adRequest); }catch(NullPointerException ignored){}
        }

        database = FirebaseDatabase.getInstance();
        gameCode = getIntent().getStringExtra("stream_code");

        homeButton = findViewById(R.id.homeScore);
        awayButton = findViewById(R.id.awayScore);
        homeButton2 = findViewById(R.id.homeScore2);
        awayButton2 = findViewById(R.id.awayScore2);
        homeButton.setClickable(false);
        awayButton.setClickable(false);
        homeButton2.setClickable(false);
        awayButton2.setClickable(false);

        homeTimeout1 = findViewById(R.id.homeTimeoutOne);
        homeTimeout2 = findViewById(R.id.homeTimeoutTwo);
        awayTimeout1 = findViewById(R.id.awayTimeoutOne);
        awayTimeout2 = findViewById(R.id.awayTimeoutTwo);
        homeTimeout1.setClickable(false);
        homeTimeout2.setClickable(false);
        awayTimeout1.setClickable(false);
        awayTimeout2.setClickable(false);

        homeSetNumber = findViewById(R.id.homeSetNumber);
        awaySetNumber = findViewById(R.id.awaySetNumber);

        homeTeamName = findViewById(R.id.homeTeamName);
        awayTeamName = findViewById(R.id.awayTeamName);

        homeIndicator = findViewById(R.id.leftServeIndicator);
        awayIndicator = findViewById(R.id.rightServeIndicator);

        setOneHistory = findViewById(R.id.setOneHistoryView);
        setTwoHistory = findViewById(R.id.setTwoHistoryView);
        setThreeHistory = findViewById(R.id.setThreeHistoryView);
        setFourHistory = findViewById(R.id.setFourHistoryView);
        setFiveHistory = findViewById(R.id.setFiveHistoryView);

        currentSet = findViewById(R.id.setNumber);

        animationInit();
        startListeningToStream();

    }

    private void animationInit() {
        slideUpHome = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);

        slideUpHome.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(Integer.parseInt(homeButton.getText().toString()) < streamingObject.homeScore - 1){
                    homeButton.setText(String.valueOf(streamingObject.homeScore  - 1));
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeButton.setText(String.valueOf(streamingObject.homeScore));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideDownHome = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

        slideDownHome.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(Integer.parseInt(homeButton.getText().toString()) >streamingObject.homeScore  - 1){
                    homeButton.setText(String.valueOf(streamingObject.homeScore  + 1));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeButton.setText(String.valueOf(streamingObject.homeScore ));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideUpAway = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);

        slideUpAway.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(Integer.parseInt(awayButton.getText().toString()) < streamingObject.awayScore  - 1){
                    awayButton.setText(String.valueOf(streamingObject.awayScore - 1));
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                awayButton.setText(String.valueOf(streamingObject.awayScore));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideDownAway = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

        slideDownAway.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(Integer.parseInt(awayButton.getText().toString()) > streamingObject.awayScore - 1){
                    awayButton.setText(String.valueOf(streamingObject.awayScore + 1));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                awayButton.setText(String.valueOf(streamingObject.awayScore));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startListeningToStream() {
        database.getReference(gameCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!(streamingObject != null && snapshot.getValue(StreamingObject.class) == null)){
                    streamingObject = snapshot.getValue(StreamingObject.class);
                    checkMatchStatus();
                    updateView();
                }else{
                    if(!streamingObject.homeWonMatch && !streamingObject.awayWonMatch){
                        streamEnded();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void streamEnded() {
        streamOnShared.setStreamBeingWatched(null);
        try {
            new AlertDialog.Builder(WatchStream.this)
                    .setTitle("Match ended")
                    .setMessage("The match has been ended")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        }catch(Exception ignored){

        }
    }

    private void checkMatchStatus() {
        if(streamingObject.homeWonMatch){
            notifyHomeWin();
        }else if(streamingObject.awayWonMatch){
            notifyAwayWin();
        }
    }

    private void notifyAwayWin() {
        new AlertDialog.Builder(WatchStream.this)
                .setTitle("Game over")
                .setMessage(streamingObject.awayName + " won the match")
                .setPositiveButton("Ok", (dialog, which) -> {
                    streamOnShared.setStreamBeingWatched(null);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void notifyHomeWin() {
        new AlertDialog.Builder(WatchStream.this)
                .setTitle("Game over")
                .setMessage(streamingObject.homeName + " won the match!")
                .setPositiveButton("Ok", (dialog, which) -> {
                    streamOnShared.setStreamBeingWatched(null);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void updateView() {
        scoreUpdate();
        setHistoryUpdate();
        TOUpdate();
        serverUpdate();

        teamNamesUpdate();
        buttonColors(streamingObject.homeColor, streamingObject.awayColor);

    }

    private void serverUpdate() {
        if(streamingObject.homeServing && !streamingObject.awayServing){
            homeIndicator.setVisibility(View.VISIBLE);
            awayIndicator.setVisibility(View.INVISIBLE);
        }else if(!streamingObject.homeServing && streamingObject.awayServing){
            homeIndicator.setVisibility(View.INVISIBLE);
            awayIndicator.setVisibility(View.VISIBLE);
        }else{
            homeIndicator.setVisibility(View.VISIBLE);
            awayIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void teamNamesUpdate() {
        homeTeamName.setText(streamingObject.homeName);
        awayTeamName.setText(streamingObject.awayName);
    }

    private void setHistoryUpdate() {

        homeSetNumber.setText(String.valueOf(streamingObject.homeSetsWon));
        awaySetNumber.setText(String.valueOf(streamingObject.awaySetsWon));

        if(setOneHistory != null){
            setOneHistory.setText(streamingObject.setHistory.get(0));
            setTwoHistory.setText(streamingObject.setHistory.get(1));
            setThreeHistory.setText(streamingObject.setHistory.get(2));
            setFourHistory.setText(streamingObject.setHistory.get(3));
            setFiveHistory.setText(streamingObject.setHistory.get(4));
        }

        currentSet.setText(String.valueOf(streamingObject.setNumber));

    }

    private void TOUpdate() {
        if(streamingObject.homeTimeoutsUsed == 2){
            homeTimeout1.setChecked(true);
            homeTimeout2.setChecked(true);
        }else if(streamingObject.homeTimeoutsUsed == 1){
            homeTimeout1.setChecked(true);
            homeTimeout2.setChecked(false);
        }else{
            homeTimeout1.setChecked(false);
            homeTimeout2.setChecked(false);
        }

        if(streamingObject.awayTimeoutsUsed == 2){
            awayTimeout1.setChecked(true);
            awayTimeout2.setChecked(true);
        }else if(streamingObject.awayTimeoutsUsed == 1){
            awayTimeout1.setChecked(true);
            awayTimeout2.setChecked(false);
        }else{
            awayTimeout1.setChecked(false);
            awayTimeout2.setChecked(false);
        }
    }

    private void scoreUpdate() {
        //homeButton.setText(String.valueOf(streamingObject.homeScore));
        //awayButton.setText(String.valueOf(streamingObject.awayScore));
        if(Integer.parseInt(homeButton.getText().toString()) < streamingObject.homeScore){
            //home score up
            homeButton.startAnimation(slideUpHome);
            homeButton2.setText(String.valueOf(streamingObject.homeScore));
        }else if(Integer.parseInt(homeButton.getText().toString()) > streamingObject.homeScore){
            //home score down
            homeButton.startAnimation(slideDownHome);
            homeButton2.setText(String.valueOf(streamingObject.homeScore));
        }

        if(Integer.parseInt(awayButton.getText().toString()) < streamingObject.awayScore){
            //home score up
            awayButton.startAnimation(slideUpAway);
            awayButton2.setText(String.valueOf(streamingObject.awayScore));
        }else if(Integer.parseInt(awayButton.getText().toString()) > streamingObject.awayScore){
            //home score down
            awayButton.startAnimation(slideDownAway);
            awayButton2.setText(String.valueOf(streamingObject.awayScore));
        }

    }

    private void buttonColors(int homeColor, int awayColor) {

        homeButton.setBackgroundColor(homeColor);
        awayButton.setBackgroundColor(awayColor);
        homeButton2.setBackgroundColor(homeColor);
        awayButton2.setBackgroundColor(awayColor);

        homeButton.setTextColor(streamingObject.textColor);
        awayButton.setTextColor(streamingObject.textColor);
        homeButton2.setTextColor(streamingObject.textColor);
        awayButton2.setTextColor(streamingObject.textColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_score_keeper) {
            Intent i = new Intent(this, StreamScore.class);
            i.putExtra("reopen", false);
            startActivity(i);
        }

        return true;
    }
}