package aheschl.volleyballscoretracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Telephony;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    int homeButtonIndex = 0;

    Vibrator vibrator;

    MySharedPreferences mySharedPreferences;
    SetHistoryShared setHistoryShared;
    StreamOnShared streamOnShared;

    ImageView rightIndicator;
    ImageView leftIndicator;

    CheckBox homeTimeout1;
    CheckBox homeTimeout2;
    CheckBox awayTimeout1;
    CheckBox awayTimeout2;

    LinearLayout bottomView;
    LinearLayout homeTimeoutLinear;
    LinearLayout awayTimeoutLinear;
    LinearLayout homeSets;
    LinearLayout awaySets;
    LinearLayout buttonsLayout;
    LinearLayout homeButtonLinear;
    LinearLayout awayButtonLinear;
    LinearLayout bottomLeftView;
    LinearLayout bottomRightView;

    TextView homeTeamName;
    TextView awayTeamName;

    TextView homeSetNumber;
    TextView awaySetNumber;

    GestureDetector awayButtonDetector;
    GestureDetector homeButtonDetector;

    ArrayList<String> undoHistory;

    Button awayScore2;
    Button homeButton2;
    Button homeButton;
    Button awayButton;


    Animation slideDownAway;
    Animation slideUpAway;
    Animation slideUpHome;
    Animation slideDownHome;

    TextView setNumber;

    VolleyballMatch VBMatch;

    Switch myPlayer;
    Switch serverSwitch;

    ImageButton textPlayerOnOff;
    EditText playerName;

    private boolean serverSwitchListen = true;
    boolean timeoutsReversed = false;

    private InterstitialAd mInterstitialAd;

    FirebaseDatabase database;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-5534964805685141~3781886939");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        buttonsLayout = findViewById(R.id.buttons);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mySharedPreferences = new MySharedPreferences(this);
        setHistoryShared = new SetHistoryShared(this);
        streamOnShared = new StreamOnShared(this);

        homeButtonIndex = mySharedPreferences.getHomeButtonIndex();

        undoHistory = new ArrayList<>();

        if(mySharedPreferences.getTypeOfVolleyball() == Constants.INDOOR) {
            VBMatch = new IndoorVolleyball();
        }else{
            VBMatch = new BeachVolleyball();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initNameDisplay();
        initTimeouts();
        initScoreButtons();

        initServerSwitch();

        prepareActionBar();

        initScores();

        initSwitchSides();

        initSetDisplay();

        initSetNumber();

        showSetHistory();

        initMyPlayerSwitch();

        initAdd();

        checkForStreamBeingWatched();

    }

    private void checkForStreamBeingWatched() {
        if(streamOnShared.getStreamBeingWatched() != null){

            database.getReference(streamOnShared.getStreamBeingWatched()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Snackbar.make(homeButton2, "The stream you were watching is finished", Snackbar.LENGTH_SHORT).show();
                        streamOnShared.setStreamBeingWatched(null);
                    }else{
                        Snackbar snackbar = Snackbar.make(homeButton2, "Do you want to resume spectating?", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("YES", view -> {
                            Intent i = new Intent(MainActivity.this, WatchStream.class);
                            i.putExtra("stream_code", streamOnShared.getStreamBeingWatched());
                            startActivity(i);
                        });
                        snackbar.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    private void initAdd() {

        if(!PayedCheck.installedPayedVersion(this)) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-5534964805685141/3439299675");
            //ca-app-pub-5534964805685141/3439299675  test ca-app-pub-3940256099942544/1033173712
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });
        }

    }

    private void initServerSwitch() {

        serverSwitch = findViewById(R.id.server);

        if(serverSwitch!=null) {

            serverSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (serverSwitchListen) {
                    if (isChecked) {
                        mySharedPreferences.setServer(Constants.AWAY);
                    } else {
                        mySharedPreferences.setServer(Constants.HOME);
                    }

                    setServer();
                }

            });
        }

    }

    @Override
    protected void onResume() {

        homeTeamName.setText(String.valueOf(mySharedPreferences.getHomeTeamName()));
        awayTeamName.setText(String.valueOf(mySharedPreferences.getAwayTeamName()));

        setNumber.setText(String.valueOf(getSetNum()));

        initButtonColors();

        RelativeLayout parent = null;
        LinearLayout playerLinear = null;

        boolean finish;

        try {
            parent = findViewById(R.id.parent);
            playerLinear = findViewById(R.id.playerInfoLinear);
            finish = true;
        }catch(NullPointerException e){
            finish = false;
        }

        if(finish && mySharedPreferences.getPlayerInfoOn() == Constants.OFF){
            parent.removeView(playerLinear);
        }else if(finish && parent.getChildCount() == 6){

            initMyPlayerSwitch();

        }

        super.onResume();
    }

    private void initMyPlayerSwitch() {

        try {
            myPlayer = findViewById(R.id.myPlayerOnOff);
            textPlayerOnOff = findViewById(R.id.sendOnOff);
            playerName = findViewById(R.id.playerName);
        }catch (NullPointerException ignored){}

        if(myPlayer != null && textPlayerOnOff != null && playerName != null){

            String onOff = mySharedPreferences.getMyPlayer();

            if(onOff.equals(Constants.stringON)){
                myPlayer.setChecked(true);
            }

            playerName.setText(mySharedPreferences.getPlayerName());

            playerName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    mySharedPreferences.setPlayerName(s.toString());
                }
            });

            textPlayerOnOff.setOnClickListener(v -> sendPlayerOnOff());

            myPlayer.setOnCheckedChangeListener((compoundButton, checked) -> {

                if(checked){
                    mySharedPreferences.setMyPlayer(Constants.stringON);
                }else{
                    mySharedPreferences.setMyPlayer(Constants.stringOFF);
                }

            });

        }

        updateStream(false, false);

    }

    private void sendPlayerOnOff() {

        sendText(mySharedPreferences.getPlayerName() + " - " + mySharedPreferences.getMyPlayer());

    }

    private void initButtonColors() {

        if(mySharedPreferences.getHomeColor() != Constants.OFF) {
            homeButton.setBackgroundColor(mySharedPreferences.getHomeColor());
            homeButton2.setBackgroundColor(mySharedPreferences.getHomeColor());
        }else{
            homeButton.setBackgroundColor(getResources().getColor(R.color.homeColor));
            homeButton2.setBackgroundColor(getResources().getColor(R.color.homeColor));
        }

        if(mySharedPreferences.getAwayColor() != Constants.OFF) {
            awayButton.setBackgroundColor(mySharedPreferences.getAwayColor());
            awayScore2.setBackgroundColor(mySharedPreferences.getAwayColor());
        }else{
            awayButton.setBackgroundColor(getResources().getColor(R.color.awayColor));
            awayScore2.setBackgroundColor(getResources().getColor(R.color.awayColor));
        }

        if(mySharedPreferences.getTextColor() != Constants.OFF) {
            awayButton.setTextColor(mySharedPreferences.getTextColor());
            awayScore2.setTextColor(mySharedPreferences.getTextColor());

            homeButton.setTextColor(mySharedPreferences.getTextColor());
            homeButton2.setTextColor(mySharedPreferences.getTextColor());
        }else{
            awayButton.setTextColor(getResources().getColor(R.color.textColor));
            awayScore2.setTextColor(getResources().getColor(R.color.textColor));

            homeButton.setTextColor(getResources().getColor(R.color.textColor));
            homeButton2.setTextColor(getResources().getColor(R.color.textColor));
        }

        updateStream(false, false);

    }

    private void showSetHistory() {

        TextView setHistoryOne = findViewById(R.id.setOneHistoryView);
        TextView setHistoryTwo = findViewById(R.id.setTwoHistoryView);
        TextView setHistoryThree = findViewById(R.id.setThreeHistoryView);
        TextView setHistoryFour = findViewById(R.id.setFourHistoryView);
        TextView setHistoryFive = findViewById(R.id.setFiveHistoryView);

        setHistoryOne.setText(String.valueOf(setHistoryShared.getFirstSetHistory()));
        setHistoryTwo.setText(String.valueOf(setHistoryShared.getSecondSetHistory()));
        setHistoryThree.setText(String.valueOf(setHistoryShared.getThirdSetHistory()));
        setHistoryFour.setText(String.valueOf(setHistoryShared.getFourthSetHistory()));
        setHistoryFive.setText(String.valueOf(setHistoryShared.getFifthSetHistory()));

    }

    void setServer() {

        if(mySharedPreferences.getServer() == Constants.HOME){

            if(homeButtonIndex == 0) {
                rightIndicator.setVisibility(View.INVISIBLE);

                leftIndicator.setVisibility(View.VISIBLE);
            }else{

                rightIndicator.setVisibility(View.VISIBLE);

                leftIndicator.setVisibility(View.INVISIBLE);

            }

            serverSwitchListen = false;
            if(serverSwitch != null){
                serverSwitch.setChecked(false);
            }
            serverSwitchListen = true;

        }else if(mySharedPreferences.getServer() == Constants.AWAY){

            if(homeButtonIndex == 0) {
                rightIndicator.setVisibility(View.VISIBLE);

                leftIndicator.setVisibility(View.INVISIBLE);
            }else{

                rightIndicator.setVisibility(View.INVISIBLE);

                leftIndicator.setVisibility(View.VISIBLE);

            }

            if(serverSwitch != null){
                serverSwitch.setChecked(true);
            }
            serverSwitchListen = false;
            serverSwitchListen = true;

        }

        updateStream(false, false);

    }

    private void initSetNumber() {

        setNumber = findViewById(R.id.setNumber);

        setNumber.setText(String.valueOf(getSetNum()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(PayedCheck.installedPayedVersion(this)){
            inflater.inflate(R.menu.actionbar_menu_paid, menu);
        }else{
            inflater.inflate(R.menu.actionbar_menu, menu);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_reset:

                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Reset Game")
                    .setMessage("Do you really want to reset the match?")
                    .setPositiveButton("Reset", (dialog, whichButton) -> resetGame())

                    .setNegativeButton(android.R.string.no, null).show();
                break;
            case R.id.action_switch_courts:
                changeSides();
                break;
            case R.id.action_sms:
                sendText();
                break;
            case R.id.action_undo:
                undoLastAction();
                break;
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_new_set:
                prepareNewSet();
                break;
            case R.id.action_rate:
                launchRate();
                break;
            case R.id.remove_ads:
                Intent i2 = new Intent(MainActivity.this, PaidVersion.class);
                startActivity(i2);
                break;

            case R.id.stream_game:
                Intent i4 = new Intent(MainActivity.this, StreamScore.class);
                startActivity(i4);
            default:
                break;
        }

        return true;
    }

    private void launchRate() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException error) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void initNameDisplay() {

        homeTeamName = findViewById(R.id.homeTeamName);
        awayTeamName = findViewById(R.id.awayTeamName);

        homeTeamName.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ChangeHomeTeamName.class);
            startActivity(i);
        });

        awayTeamName.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ChangeAwayTeamName.class);
            startActivity(i);
        });

    }

    private void prepareActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }

    }

    void prepareNewSet() {

        changeSides();

        if(VBMatch.getHomeScore() < VBMatch.getAwayScore()){

            int awaySets = Integer.parseInt(awaySetNumber.getText().toString());

            awaySets++;

            mySharedPreferences.setAwaySetsWon(awaySets);

            if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                sendText(getMessage(mySharedPreferences.getSMSMessagePostSet(), mySharedPreferences.getAwayTeamName(), Constants.SET));
            }else{
                sendTextNoConfirmEndOfSet(Constants.SET, mySharedPreferences.getAwayTeamName());
            }

        }else if( VBMatch.getAwayScore() < VBMatch.getHomeScore()){

            int homeSets = Integer.parseInt(homeSetNumber.getText().toString());

            homeSets++;

            mySharedPreferences.setHomeSetsWon(homeSets);

            homeSetNumber.setText(String.valueOf(homeSets));

            if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                sendText(getMessage(mySharedPreferences.getSMSMessagePostSet(), mySharedPreferences.getHomeTeamName(), Constants.SET));
            }else{
                sendTextNoConfirmEndOfSet(Constants.SET, mySharedPreferences.getHomeTeamName());
            }

        }else{

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Who Won?")
                    .setMessage("The scores are equal. Who won the set?")
                    .setPositiveButton(String.valueOf(mySharedPreferences.getHomeTeamName()), (dialog, which) -> {

                        int homeSets = Integer.parseInt(homeSetNumber.getText().toString());

                        homeSets++;

                        mySharedPreferences.setHomeSetsWon(homeSets);

                        homeSetNumber.setText(String.valueOf(homeSets));

                        if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                            sendText(mySharedPreferences.getHomeTeamName() + " won the set");
                        }else{
                            sendTextNoConfirmEndOfSet(Constants.SET, mySharedPreferences.getHomeTeamName());
                        }

                        setNumber.setText(String.valueOf(getSetNum()));


                    })
                    .setNegativeButton(String.valueOf(mySharedPreferences.getAwayTeamName()), (dialog, which) -> {

                        int awaySets = Integer.parseInt(awaySetNumber.getText().toString());

                        awaySets++;

                        mySharedPreferences.setAwaySetsWon(awaySets);

                        awaySetNumber.setText(String.valueOf(awaySets));

                        if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                            sendText(getMessage(mySharedPreferences.getSMSMessagePostSet(), mySharedPreferences.getAwayTeamName(), Constants.SET));
                        }else{
                            sendTextNoConfirmEndOfSet(Constants.SET, mySharedPreferences.getAwayTeamName());
                        }

                        setNumber.setText(String.valueOf(getSetNum()));

                    }).setCancelable(false)
                    .show();

        }

        if(getSetNum() == 2){
            setHistoryShared.setFirstSetHistory(VBMatch.getHomeScore() + "-" +  VBMatch.getAwayScore());
        }else if(getSetNum() == 3){
            setHistoryShared.setSecondSetHistory(VBMatch.getHomeScore() + "-" +  VBMatch.getAwayScore());
        }else if(getSetNum() == 4){
            setHistoryShared.setThirdSetHistory(VBMatch.getHomeScore() + "-" +  VBMatch.getAwayScore());
        }else if(getSetNum() == 5){
            setHistoryShared.setFourthSetHistory(VBMatch.getHomeScore() + "-" + VBMatch.getAwayScore());
        }

        showSetHistory();

        VBMatch.setHomeScore(0);

        VBMatch.setAwayScore(0);

        mySharedPreferences.setAwayScore(VBMatch.getAwayScore());

        mySharedPreferences.setHomeScore(VBMatch.getHomeScore());

        awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore()));

        homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore()));

        homeTimeout1.setChecked(false);

        homeTimeout2.setChecked(false);

        awayTimeout1.setChecked(false);

        awayTimeout2.setChecked(false);

        setNumber.setText(String.valueOf(getSetNum()));

        homeSetNumber.setText(String.valueOf(mySharedPreferences.getHomeSetsWon()));

        awaySetNumber.setText(String.valueOf(mySharedPreferences.getAwaySetsWon()));

        updateStream(false, false);

    }

    private String getMessage(String outline, String winningTeam, String setOrMatch){

        if(setOrMatch.equals(Constants.SET)){
            setOrMatch = "set";
        }else{
            setOrMatch = "match";
        }


        String messageWithHome = outline.replaceAll(Constants.HOME_SCORE, String.valueOf(mySharedPreferences.getHomeScore()));

        String messageWithAway = messageWithHome.replaceAll(Constants.AWAY_SCORE, String.valueOf(mySharedPreferences.getAwayScore()));

        int setNum = getSetNum();

        String messageWithSetNum = messageWithAway.replaceAll(Constants.SET, String.valueOf(setNum));

        int homeSetsWon = getHomeSets();

        int awaySetsWon = getAwaySets();

        final String messageWithHomeSets = messageWithSetNum.replaceAll(Constants.HOME_SETS, String.valueOf(homeSetsWon));

        final String messageWithAwaySets = messageWithHomeSets.replaceAll(Constants.AWAY_SETS, String.valueOf(awaySetsWon));

        final String messageWithHomeTeamName = messageWithAwaySets.replaceAll(Constants.HOME_TEAM_NAME, String.valueOf(mySharedPreferences.getHomeTeamName()));

        final String mesageWithAwayTeamName = messageWithHomeTeamName.replaceAll(Constants.AWAY_TEAM_NAME, String.valueOf(mySharedPreferences.getAwayTeamName()));

        final String mesageWithWinningTeam = mesageWithAwayTeamName.replaceAll(Constants.WINNING_TEAM, winningTeam);

        final String mesageWithMyPlayer = mesageWithWinningTeam.replaceAll(Constants.MY_PLAYER, mySharedPreferences.getMyPlayer());

        final String mesageWithMyPlayerName = mesageWithMyPlayer.replaceAll(Constants.PLAYER_NAME, mySharedPreferences.getPlayerName());

        return mesageWithMyPlayerName.replaceAll(Constants.MATCH_OR_SET, setOrMatch);
    }

    private void undoLastAction() {

        String lastAction = "";

        if(undoHistory.size() > 0){
            lastAction = undoHistory.get(undoHistory.size()-1);
            undoHistory.remove(undoHistory.size()-1);
        }

        if(lastAction.equals(Constants.HOME_SCORE)){

           VBMatch.homeScoreDown(MainActivity.this);
            updateStream(false, false);

        }else if(lastAction.equals(Constants.AWAY_SCORE)){

           VBMatch.awayScoreDown(MainActivity.this);
            updateStream(false, false);

        }

    }

    private void initSetDisplay() {

        homeSetNumber = findViewById(R.id.homeSetNumber);

        awaySetNumber = findViewById(R.id.awaySetNumber);

        homeSetNumber.setText(String.valueOf(getHomeSets()));

        awaySetNumber.setText(String.valueOf(getAwaySets()));

    }

    private void initTimeouts() {

        homeTimeout1 = findViewById(R.id.homeTimeoutOne);
        homeTimeout2 = findViewById(R.id.homeTimeoutTwo);
        awayTimeout1 = findViewById(R.id.awayTimeoutOne);
        awayTimeout2 = findViewById(R.id.awayTimeoutTwo);

        homeTimeout1.setChecked(mySharedPreferences.getHomeTimeoutOne());
        homeTimeout2.setChecked(mySharedPreferences.getHomeTimeoutTwo());

        awayTimeout1.setChecked(mySharedPreferences.getAwayTimeoutOne());
        awayTimeout2.setChecked(mySharedPreferences.getAwayTimeoutTwo());

        homeTimeout1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            mySharedPreferences.setHomeTimeoutOne(isChecked);
            updateStream(false, false);

        });

        homeTimeout2.setOnCheckedChangeListener((buttonView, isChecked) -> {

            mySharedPreferences.setHomeTimeoutTwo(isChecked);
            updateStream(false, false);

        });

        awayTimeout1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            mySharedPreferences.setAwayTimeoutOne(isChecked);
            updateStream(false, false);

        });

        awayTimeout2.setOnCheckedChangeListener((buttonView, isChecked) -> {

            mySharedPreferences.setAwayTimeoutTwo(isChecked);
            updateStream(false, false);

        });

    }

    private void initSwitchSides() {

        bottomLeftView = findViewById(R.id.bottomLeftView);

        bottomRightView = findViewById(R.id.bottomRightView);

        bottomView = findViewById(R.id.bottomOfLayout);

        awayTimeoutLinear = findViewById(R.id.awayTimeoutLinear);

        homeTimeoutLinear = findViewById(R.id.homeTimeoutLinear);

        homeSets = findViewById(R.id.homeSets);

        awaySets = findViewById(R.id.awaySets);

        homeButtonLinear = findViewById(R.id.homeButtonLinear);

        awayButtonLinear = findViewById(R.id.awayButtonLinear);

        if(homeButtonIndex != 0){
            reverseSides();
        }else{
            setServer();
        }

    }

    private void reverseSides() {

        buttonsLayout.removeViewAt(0);
        buttonsLayout.addView(homeButtonLinear, 1);

        bottomView.removeViewAt(0);

        bottomView.removeViewAt(1);

        bottomView.addView(bottomRightView, 0);

        bottomView.addView(bottomLeftView, 2);


        bottomLeftView.removeViewAt(0);

        bottomLeftView.removeViewAt(0);

        bottomLeftView.addView(homeSets, 0);

        bottomLeftView.addView(homeTimeoutLinear);


        bottomRightView.removeViewAt(0);

        bottomRightView.removeViewAt(0);

        bottomRightView.addView(awayTimeoutLinear, 0);

        bottomRightView.addView(awaySets);

        timeoutsReversed = true;

        setServer();

    }

    void changeSides() {

        if(mySharedPreferences.getVibrate() == Constants.ON) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(100);
            }
        }

        if(homeButtonIndex == 0){

            buttonsLayout.removeViewAt(0);
            buttonsLayout.addView(homeButtonLinear, 1);

            homeButtonIndex = 1;

        }else{

            buttonsLayout.removeViewAt(1);
            buttonsLayout.addView(homeButtonLinear, 0);

            homeButtonIndex = 0;

        }

        homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore()));

        if(rightIndicator.getVisibility() == View.INVISIBLE){
            rightIndicator.setVisibility(View.VISIBLE);
            leftIndicator.setVisibility(View.INVISIBLE);
        }else{
            rightIndicator.setVisibility(View.INVISIBLE);
            leftIndicator.setVisibility(View.VISIBLE);
        }

        if(!timeoutsReversed) {

            bottomView.removeViewAt(0);

            bottomView.removeViewAt(1);

            bottomView.addView(bottomRightView, 0);

            bottomView.addView(bottomLeftView, 2);


            bottomLeftView.removeViewAt(0);

            bottomLeftView.removeViewAt(0);

            bottomLeftView.addView(homeSets, 0);

            bottomLeftView.addView(homeTimeoutLinear);


            bottomRightView.removeViewAt(0);

            bottomRightView.removeViewAt(0);

            bottomRightView.addView(awayTimeoutLinear, 0);

            bottomRightView.addView(awaySets);


            timeoutsReversed = true;

        }else{

            bottomView.removeViewAt(0);

            bottomView.removeViewAt(1);

            bottomView.addView(bottomLeftView, 0);

            bottomView.addView(bottomRightView, 2);


            bottomLeftView.removeViewAt(0);

            bottomLeftView.removeViewAt(0);

            bottomLeftView.addView(homeTimeoutLinear, 0);

            bottomLeftView.addView(homeSets);


            bottomRightView.removeViewAt(0);

            bottomRightView.removeViewAt(0);

            bottomRightView.addView(awaySets, 0);

            bottomRightView.addView(awayTimeoutLinear);

            timeoutsReversed = false;

        }

    }

    //@TODO ldksjlkjl
    private void sendIntent(String text, ArrayList nums){

        String separator = "; ";

        if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            separator = ", ";
        }

        String numbers = TextUtils.join(separator, nums);

        Uri uri = Uri.parse("sms:" + numbers);
        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra("sms_body", text);
        intent.putExtra("address", numbers);
        intent.putExtra("exit_on_sent", true);

        intent.setAction(Intent.ACTION_SENDTO);
        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this);

        if (defaultSmsPackageName != null) {
            intent.setPackage(defaultSmsPackageName);
        }

        try {
            int INVITE_COMPLETED = 1;
            startActivityForResult(intent, INVITE_COMPLETED);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("message_sent", text);
        mFirebaseAnalytics.logEvent("send_message", bundle);

    }

    private void sendText() {

        PackageManager pm = this.getPackageManager();
        boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        if(hasTelephony) {

            String messageOutline = String.valueOf(mySharedPreferences.getSMSMessage());

            String messageWithHome = messageOutline.replaceAll(Constants.HOME_SCORE, String.valueOf(VBMatch.getHomeScore()));

            String messageWithAway = messageWithHome.replaceAll(Constants.AWAY_SCORE, String.valueOf(VBMatch.getAwayScore()));

            int setNum = getSetNum();

            String messageWithSetNum = messageWithAway.replaceAll(Constants.SET, String.valueOf(setNum));

            int homeSetsWon = getHomeSets();

            int awaySetsWon = getAwaySets();

            final String messageWithHomeSets = messageWithSetNum.replaceAll(Constants.HOME_SETS, String.valueOf(homeSetsWon));

            final String messageWithAwaySets = messageWithHomeSets.replaceAll(Constants.AWAY_SETS, String.valueOf(awaySetsWon));

            final String messageWithHomeTeamName = messageWithAwaySets.replaceAll(Constants.HOME_TEAM_NAME, String.valueOf(mySharedPreferences.getHomeTeamName()));

            final String messageWithMyPlayer = messageWithHomeTeamName.replaceAll(Constants.MY_PLAYER, String.valueOf(mySharedPreferences.getMyPlayer()));

            final String messageWithMyPlayerName = messageWithMyPlayer.replaceAll(Constants.PLAYER_NAME, String.valueOf(mySharedPreferences.getMyPlayer()));

            final String message = messageWithMyPlayerName.replaceAll(Constants.AWAY_TEAM_NAME, String.valueOf(mySharedPreferences.getAwayTeamName()));

            if(mySharedPreferences.getAutoText() == Constants.OFF ){

                final LinearLayout numberLinear = new LinearLayout(this);

                LinearLayout smsView = new LinearLayout(this);

                smsView.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                numberLinear.setLayoutParams(params);

                TextView text = new TextView(this);

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

                textParams.setMargins(8, 0, 0, 0);

                text.setLayoutParams(textParams);

                text.setGravity(Gravity.CENTER);

                text.setTextSize(17);

                text.setTextColor(Color.BLACK);

                text.setText(R.string.send_text_to);

                numberLinear.addView(text);

                final EditText numberField = new EditText(this);

                numberField.setLayoutParams(params);

                numberLinear.addView(numberField);

                numberField.setInputType(InputType.TYPE_CLASS_PHONE);

                numberField.setText(String.valueOf(mySharedPreferences.getNumbers()));


                LinearLayout messageLinear = new LinearLayout(this);

                messageLinear.setLayoutParams(params);

                final EditText messageEditText = new EditText(this);


                messageEditText.setText(message);

                messageEditText.setLayoutParams(params);

                messageLinear.addView(messageEditText);


                smsView.addView(messageLinear);
                smsView.addView(numberLinear);

                new AlertDialog.Builder(this)
                        .setTitle("Send Text Message")
                        .setMessage("You can edit your message below. You can change the default message outline in settings. If you want texts to be sent without this popup you can turn it off in settings.")
                        .setView(smsView)
                        .setPositiveButton("Send", (dialog, whichButton) -> {

                            mySharedPreferences.setNumbers(numberField.getText().toString());

                            if (mySharedPreferences.getNumbers().length() > 0 && messageEditText.getText().toString().length() > 0) {

                                ArrayList<String> numbers = new ArrayList<>(Arrays.asList(numberField.getText().toString().split(", *")));


                                sendIntent(messageEditText.getText().toString(), numbers);

                                Toast.makeText(MainActivity.this, "Text sent", Toast.LENGTH_SHORT).show();
                            } else {

                                if(mySharedPreferences.getNumbers().length() == 0) {

                                    Toast.makeText(MainActivity.this, "Add at least one number and try again", Toast.LENGTH_LONG).show();

                                }else{

                                    Toast.makeText(MainActivity.this, "Please create a message with at least one character and try again", Toast.LENGTH_LONG).show();

                                }

                            }


                        })

                        .setNegativeButton(android.R.string.no, (dialog, which) -> mySharedPreferences.setNumbers(numberField.getText().toString())).show();
            }else{

                if (mySharedPreferences.getNumbers().length() > 0 && String.valueOf(mySharedPreferences.getSMSMessage()).length() > 0) {

                    ArrayList<String> numbers = new ArrayList<>(Arrays.asList(String.valueOf(mySharedPreferences.getNumbers()).split(", *")));
                    sendIntent(message, numbers);

                    Toast.makeText(MainActivity.this, "Text sent", Toast.LENGTH_SHORT).show();

                } else {

                    if(mySharedPreferences.getNumbers().length() == 0) {

                        Toast.makeText(MainActivity.this, "Add at least one number and try again", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(MainActivity.this, EditNumbers.class);

                        startActivity(i);

                    }else{

                        Toast.makeText(MainActivity.this, "Please create a message with at least one character and try again", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(MainActivity.this, EditSMS.class);

                        startActivity(i);

                    }
                }

            }

        }else{


            new AlertDialog.Builder(this)
                    .setTitle("Feature Unavailable")
                    .setMessage("You device does not seem to be able to send sms messages. If it can, you can contact the developer by emailing ajheschl@gmail.com " +
                            "to report this as an error")
                    .setPositiveButton("Ok", null)
                    .show();

        }

    }

    //@TODO change to intent
    private void sendText(String message) {

        PackageManager pm = this.getPackageManager();
        boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        if(hasTelephony) {

            final LinearLayout numberLinear = new LinearLayout(this);

            LinearLayout smsView = new LinearLayout(this);

            smsView.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            numberLinear.setLayoutParams(params);

            TextView text = new TextView(this);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

            textParams.setMargins(8, 0, 0, 0);

            text.setLayoutParams(textParams);

            text.setGravity(Gravity.CENTER);

            text.setTextSize(17);

            text.setTextColor(Color.BLACK);

            text.setText(R.string.send_text_to);

            numberLinear.addView(text);

            final EditText numberField = new EditText(this);

            numberField.setLayoutParams(params);

            numberLinear.addView(numberField);

            numberField.setInputType(InputType.TYPE_CLASS_PHONE);

            numberField.setText(String.valueOf(mySharedPreferences.getNumbers()));


            LinearLayout messageLinear = new LinearLayout(this);

            messageLinear.setLayoutParams(params);

            final EditText messageEditText = new EditText(this);

            messageEditText.setText(message);

            messageEditText.setLayoutParams(params);

            messageLinear.addView(messageEditText);


            smsView.addView(messageLinear);
            smsView.addView(numberLinear);

            new AlertDialog.Builder(this)
                    .setTitle("Send Text Message")
                    .setMessage("You can stop this popup by turning it off in settings. You can edit your message below.")
                    .setView(smsView)
                    .setPositiveButton("Send", (dialog, whichButton) -> {

                        mySharedPreferences.setNumbers(numberField.getText().toString());

                        if (mySharedPreferences.getNumbers().length() > 0) {

                            ArrayList<String> numbers = new ArrayList<>(Arrays.asList(numberField.getText().toString().split(", *")));


                            sendIntent(messageEditText.getText().toString(), numbers);


                            Toast.makeText(MainActivity.this, "Text sent", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(MainActivity.this, "Add at least one number and try again", Toast.LENGTH_LONG).show();

                        }


                    })

                    .setNegativeButton(android.R.string.no, (dialog, which) -> mySharedPreferences.setNumbers(numberField.getText().toString())).show();

        }else{


            new AlertDialog.Builder(this)
                    .setTitle("Feature Unavailable")
                    .setMessage("You device does not seem to be able to send sms messages. If it can, you can contact the developer by emailing ajheschl@gmail.com " +
                            "to report this as an error")
                    .setPositiveButton("Ok", null)
                    .show();

        }

    }

    //@TODO remove
    private void sendTextNoConfirmEndOfSet(String setOrMatch, String winningTeam){

        String messageOutline = String.valueOf(mySharedPreferences.getSMSMessagePostSet());

        final String message = getMessage(messageOutline, winningTeam, setOrMatch);

        PackageManager pm = this.getPackageManager();
        boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        if(hasTelephony) {

                if (mySharedPreferences.getNumbers().length() > 0) {

                    ArrayList<String> numbers = new ArrayList<>(Arrays.asList(mySharedPreferences.getNumbers().split(", *")));

                    for (String ignored : numbers) {
                        sendIntent(message, numbers);
                    }

                    Toast.makeText(MainActivity.this, "Text sent", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Add at least one number and try again", Toast.LENGTH_LONG).show();

                }


        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Feature Unavailable")
                    .setMessage("You device does not seem to be able to send sms messages. If it can, you can contact the developer by emailing ajheschl@gmail.com " +
                            "to report this as an error")
                    .setPositiveButton("Ok", null)
                    .show();

        }

    }

    private int getAwaySets() {

        int sets;

        sets = mySharedPreferences.getAwaySetsWon();

        return sets;
    }

    private int getHomeSets() {

        int sets;

        sets = mySharedPreferences.getHomeSetsWon();

        return sets;

    }

    private void resetGame() {

        if(!PayedCheck.installedPayedVersion(this)){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }


        VBMatch.setHomeScore(0);
        VBMatch.setAwayScore(0);

        mySharedPreferences.setAwayScore(VBMatch.getAwayScore());

        mySharedPreferences.setHomeScore(VBMatch.getHomeScore());

        homeButton.setText(String.valueOf(VBMatch.getHomeScore()));

        awayButton.setText(String.valueOf(VBMatch.getAwayScore()));

        if(homeButtonIndex != 0){
            changeSides();
        }

        rightIndicator.setVisibility(View.VISIBLE);

        leftIndicator.setVisibility(View.VISIBLE);

        homeTimeout1.setChecked(false);

        homeTimeout2.setChecked(false);

        awayTimeout1.setChecked(false);

        awayTimeout2.setChecked(false);

        mySharedPreferences.setHomeSetsWon(0);

        mySharedPreferences.setAwaySetsWon(0);

        homeSetNumber.setText(String.valueOf(mySharedPreferences.getHomeScore()));

        awaySetNumber.setText(String.valueOf(mySharedPreferences.getAwayScore()));

        setNumber.setText(String.valueOf(getSetNum()));

        mySharedPreferences.setServer(Constants.NEUTRAL);

        setHistoryShared.setFirstSetHistory("");
        setHistoryShared.setSecondSetHistory("");
        setHistoryShared.setThirdSetHistory("");
        setHistoryShared.setFourthSetHistory("");
        setHistoryShared.setFifthSetHistory("");

        showSetHistory();

        deleteStream();

    }

    private void initScores() {

        VBMatch.setHomeScore(mySharedPreferences.getHomeScore());
        VBMatch.setAwayScore(mySharedPreferences.getAwayScore());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initScoreButtons() {

        homeButton2 = findViewById(R.id.homeScore2);

        awayScore2 = findViewById(R.id.awayScore2);

        homeButton = findViewById(R.id.homeScore);
        awayButton = findViewById(R.id.awayScore);

        leftIndicator = findViewById(R.id.leftServeIndicator);

        rightIndicator = findViewById(R.id.rightServeIndicator);

        homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore()));
        awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore()));

        homeButtonDetector = new GestureDetector(this, new OnSwipeListener(){

            @Override
            boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    VBMatch.homeScoreUp(MainActivity.this);
                    updateStream(false, false);
                }

                if (direction==Direction.down){
                    VBMatch.homeScoreDown(MainActivity.this);
                    updateStream(false, false);
                }

                return true;
            }

            @Override
            public boolean onTapConfirmed(MotionEvent event){

                VBMatch.homeScoreUp(MainActivity.this);
                updateStream(false, false);
                return true;
            }

            @Override
            public boolean onLongPressConfirmed(MotionEvent event){

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(MainActivity.this, mySharedPreferences.getHomeColor(), new AmbilWarnaDialog.OnAmbilWarnaListener(){
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mySharedPreferences.setHomeColor(color);
                        initButtonColors();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });

                dialog.show();

                return true;
            }


        });

        awayButtonDetector = new GestureDetector(this, new OnSwipeListener(){

            @Override
            boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    VBMatch.awayScoreUp(MainActivity.this);
                    updateStream(false, false);

                }

                if (direction==Direction.down){
                    VBMatch.awayScoreDown(MainActivity.this);
                    updateStream(false, false);

                }

                return true;
            }
            
            @Override
            public boolean onTapConfirmed(MotionEvent event){
                VBMatch.awayScoreUp(MainActivity.this);
                updateStream(false, false);

                return true;
            }

            @Override
            public boolean onLongPressConfirmed(MotionEvent event){

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(MainActivity.this, mySharedPreferences.getAwayColor(), new AmbilWarnaDialog.OnAmbilWarnaListener(){
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mySharedPreferences.setAwayColor(color);
                        initButtonColors();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });

                dialog.show();
                return true;
            }


        });

        homeButton.setOnTouchListener(this);

        awayButton.setOnTouchListener(this);

        slideUpHome = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);

        slideUpHome.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(Integer.parseInt(homeButton.getText().toString()) < VBMatch.getHomeScore() - 1){
                    homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore() - 1));
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideDownHome = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

        slideDownHome.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(Integer.parseInt(homeButton.getText().toString()) > VBMatch.getHomeScore() - 1){
                    homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore() + 1));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                homeButton.setText(String.valueOf(mySharedPreferences.getHomeScore()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideUpAway = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);

        slideUpAway.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(Integer.parseInt(awayButton.getText().toString()) < VBMatch.getAwayScore() - 1){
                    awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore() - 1));
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        slideDownAway = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

        slideDownAway.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(Integer.parseInt(awayButton.getText().toString()) > VBMatch.getAwayScore() - 1){
                    awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore() + 1));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                awayButton.setText(String.valueOf(mySharedPreferences.getAwayScore()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    boolean isTieBreaker() {

        boolean isTieBreaker = false;
        if(mySharedPreferences.gettNumberOfSets() == 5){

            if(getSetNum() == 5){
                isTieBreaker = true;
            }

        }else{

            if(getSetNum() == 3){
                isTieBreaker = true;
            }

        }

        return isTieBreaker;

    }

    void displayWin(final String team) {

        if(team.equals("away")) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Game over")
                    .setMessage(mySharedPreferences.getAwayTeamName() + " won the match")
                    .setPositiveButton("Prepare new match", (dialog, which) -> {

                        if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                            sendText(getMessage(mySharedPreferences.getSMSMessagePostSet(), mySharedPreferences.getAwayTeamName(), Constants.MATCH));
                        }else{
                            sendTextNoConfirmEndOfSet(Constants.MATCH, mySharedPreferences.getAwayTeamName());
                        }

                        updateStream(false, true);

                        resetGame();

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                        if(mySharedPreferences.gettNumberOfSets() == 3){

                            awaySetNumber.setText("1");

                            mySharedPreferences.setAwaySetsWon(1);

                        }else{

                            awaySetNumber.setText("2");

                            mySharedPreferences.setAwaySetsWon(2);

                        }
                        updateStream(false, false);

                    }).setCancelable(false)
                    .show();
        }else{

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Game over")
                    .setMessage(mySharedPreferences.getHomeTeamName() + " won the match!")
                    .setPositiveButton("Prepare new match", (dialog, which) -> {

                        if(mySharedPreferences.getSMSPopup() == Constants.ON) {
                            sendText(getMessage(mySharedPreferences.getSMSMessagePostSet(), mySharedPreferences.getHomeTeamName(), Constants.MATCH));
                        }else{
                            sendTextNoConfirmEndOfSet(Constants.MATCH, mySharedPreferences.getHomeTeamName());
                        }

                        updateStream(true, false);

                        resetGame();

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                        if(mySharedPreferences.gettNumberOfSets() == 3){
                            homeSetNumber.setText("1");

                            mySharedPreferences.setHomeSetsWon(1);
                        }else{

                            homeSetNumber.setText("2");

                            mySharedPreferences.setHomeSetsWon(2);

                        }

                        updateStream(false, false);


                    }).setCancelable(false)
                    .show();

        }

    }

    private int getSetNum(){

        int setNum = 1;

        setNum += mySharedPreferences.getHomeSetsWon();

        setNum += mySharedPreferences.getAwaySetsWon();

        return setNum;

    }

    @Override
    protected void onPause() {

        mySharedPreferences.setHomeButtonIndex(homeButtonIndex);
        super.onPause();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        
        if(view == homeButton){
            return homeButtonDetector.onTouchEvent(event);
        } else{
            return awayButtonDetector.onTouchEvent(event);
        }
    
    }


    private void updateStream(boolean homeWon, boolean awayWon){

        int homeTimeoutsUsed = 0;
        if(homeTimeout1.isChecked()){
            homeTimeoutsUsed++;
        }
        if(homeTimeout2.isChecked()){
            homeTimeoutsUsed++;
        }

        int awayTimeoutsUsed = 0;
        if(awayTimeout1.isChecked()){
            awayTimeoutsUsed++;
        }
        if(awayTimeout2.isChecked()){
            awayTimeoutsUsed++;
        }

        ArrayList<String> setHistory = new ArrayList<>();

        setHistory.add(setHistoryShared.getFirstSetHistory());
        setHistory.add(setHistoryShared.getSecondSetHistory());
        setHistory.add(setHistoryShared.getThirdSetHistory());
        setHistory.add(setHistoryShared.getFourthSetHistory());
        setHistory.add(setHistoryShared.getFifthSetHistory());

        StreamingObject streamingObject = new StreamingObject(VBMatch.getHomeScore(), VBMatch.getAwayScore(),
                getHomeSets(), getAwaySets(),
                getSetNum(),
                homeTimeoutsUsed, awayTimeoutsUsed,
                setHistory,
                mySharedPreferences.getServer() == Constants.HOME, mySharedPreferences.getServer() == Constants.AWAY,
                mySharedPreferences.getHomeTeamName(), mySharedPreferences.getAwayTeamName(),
                mySharedPreferences.getHomeColor(), mySharedPreferences.getAwayColor(), mySharedPreferences.getTextColor(),
                homeWon, awayWon);

        if(streamOnShared.getStreamOn() != null && mAuth.getCurrentUser() != null){
            database.getReference(streamOnShared.getStreamOn())
                    .setValue(streamingObject);
        }

    }

    private void deleteStream(){

        if(streamOnShared.getStreamOn() != null){

            Intent i = new Intent(this, DeleteStreamLater.class);
            i.putExtra("name", streamOnShared.getStreamOn());
            startService(i);

            streamOnShared.setStreamOn(null);
        }
    }


}
