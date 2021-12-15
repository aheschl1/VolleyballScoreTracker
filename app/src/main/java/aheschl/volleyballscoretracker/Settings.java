package aheschl.volleyballscoretracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Settings extends AppCompatActivity {

    private MySharedPreferences mySharedPreferences;

    TextView phoneNumbers;

    TextView textMessageOutlineText;

    TextView textMessageOutlineTextPostSet;

    TextView homeTeamName;

    TextView awayTeamName;

    SwitchCompat playerInfo;

    SwitchCompat tieBreakerType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mySharedPreferences = new MySharedPreferences(this);

        initTextOutline();

        initPostSetTextOutline();

        initVibrate();

        initNumbers();

        initSetNumber();

        initTieBreakerSettings();

        initEditNames();

        prepareActionBar();

        initAutoText();

        initNotifyAtEndOfSet();

        initAnimations();

        initShowSMSPopup();

        initColors();

        initChangeVolleyballType();

        initPlayerInfo();

        AdView adView = findViewById(R.id.adView);

        if(PayedCheck.installedPayedVersion(this)){
            LinearLayout parent = findViewById(R.id.main);
            parent.removeView(adView);
        }else{
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

    }

    private void initPlayerInfo() {

        playerInfo = findViewById(R.id.playerInfo);

        if(mySharedPreferences.getPlayerInfoOn() == Constants.ON){
            playerInfo.setChecked(true);
        }

        playerInfo.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                mySharedPreferences.setPlayerInfoOn(Constants.ON);
            }else{
                mySharedPreferences.setPlayerInfoOn(Constants.OFF);
            }

        });

    }

    private void initChangeVolleyballType() {

        CheckBox indoor = findViewById(R.id.indoor);
        CheckBox beach = findViewById(R.id.beach);

        if(mySharedPreferences.getTypeOfVolleyball() == Constants.INDOOR){
            indoor.setChecked(true);
        }else{
            beach.setChecked(true);
        }

        indoor.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                resetGame();
                beach.setChecked(false);
                mySharedPreferences.setTypeOfVolleyball(Constants.INDOOR);
            }
        });

        beach.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                resetGame();
                indoor.setChecked(false);
                mySharedPreferences.setTypeOfVolleyball(Constants.BEACH);
            }
        });

    }

    private void initColors() {


        Button preview2 = findViewById(R.id.homeColorPreview);
        preview2.setOnClickListener(view -> homeColorDialog());

        if(mySharedPreferences.getHomeColor() != Constants.OFF) {
            preview2.setBackgroundColor(mySharedPreferences.getHomeColor());
        }else{
            preview2.setBackgroundColor(getResources().getColor(R.color.homeColor));
        }

        Button preview = findViewById(R.id.awayColorPreview);
        preview.setOnClickListener(view -> awayColorDialog());

        if(mySharedPreferences.getAwayColor() != Constants.OFF) {
            preview.setBackgroundColor(mySharedPreferences.getAwayColor());
        }else{
            preview.setBackgroundColor(getResources().getColor(R.color.awayColor));
        }

        Button scoreColors = findViewById(R.id.scoreColorButton);
        scoreColors.setOnClickListener(view -> scoreColorDialog());

        if(mySharedPreferences.getTextColor() != Constants.OFF) {
            scoreColors.setBackgroundColor(mySharedPreferences.getTextColor());
        }else{
            scoreColors.setBackgroundColor(getResources().getColor(R.color.textColor));
        }
    }

    private void homeColorDialog() {

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, mySharedPreferences.getHomeColor(), new AmbilWarnaDialog.OnAmbilWarnaListener(){
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Button preview = findViewById(R.id.homeColorPreview);
                preview.setBackgroundColor(color);
                mySharedPreferences.setHomeColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        dialog.show();

    }

    private void scoreColorDialog() {

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, mySharedPreferences.getTextColor(), new AmbilWarnaDialog.OnAmbilWarnaListener(){
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Button scoreColor = findViewById(R.id.scoreColorButton);
                scoreColor.setBackgroundColor(color);
                mySharedPreferences.setTextColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        dialog.show();

    }

    private void awayColorDialog() {

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, mySharedPreferences.getAwayColor(), new AmbilWarnaDialog.OnAmbilWarnaListener(){
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Button preview = findViewById(R.id.awayColorPreview);
                preview.setBackgroundColor(color);
                mySharedPreferences.setAwayColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        dialog.show();

    }

    private void initShowSMSPopup() {

        SwitchCompat smsPopup = findViewById(R.id.smsPopup);

        if(mySharedPreferences.getSMSPopup() == Constants.ON){
            smsPopup.setChecked(true);
        }

        smsPopup.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                mySharedPreferences.setSMSPopup(Constants.ON);
            }else{
                mySharedPreferences.setSMSPopup(Constants.OFF);
            }
        });

    }

    private void initAnimations() {

        final SwitchCompat animations = findViewById(R.id.animationSwitch);

        if(mySharedPreferences.getAnimations() == Constants.ON){

            animations.setChecked(true);

        }

        animations.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                mySharedPreferences.setAnimations(Constants.ON);
            }else{
                mySharedPreferences.setAnimations(Constants.OFF);
            }
        });

    }

    private void initNotifyAtEndOfSet() {

        SwitchCompat notifyAtEndOfSet = findViewById(R.id.notifyAtEndOfSet);

        if(mySharedPreferences.getNotifyAtEnd() == Constants.ON){
            notifyAtEndOfSet.setChecked(true);
        }

        notifyAtEndOfSet.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                mySharedPreferences.setNotifyAtEnd(Constants.ON);
            }else{
                mySharedPreferences.setNotifyAtEnd(Constants.OFF);
            }
        });

    }

    private void initAutoText() {

        SwitchCompat autoText = findViewById(R.id.autoText);

        if(mySharedPreferences.getAutoText() == Constants.ON){
            autoText.setChecked(true);
        }

        autoText.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                mySharedPreferences.setAutoText(Constants.ON);
            }else{
                mySharedPreferences.setAutoText(Constants.OFF);
            }
        });

    }

    private void initEditNames() {

       LinearLayout homeTeamNameLinear = findViewById(R.id.homeTeamNameLinear);

       homeTeamNameLinear.setOnClickListener(view -> {
           Intent i = new Intent(Settings.this, ChangeHomeTeamName.class);

           startActivity(i);
       });

       homeTeamName = findViewById(R.id.homeTeamName);

        LinearLayout awayTeamNameLinear = findViewById(R.id.awayTeamNameLinear);

        awayTeamNameLinear.setOnClickListener(view -> {
            Intent i = new Intent(Settings.this, ChangeAwayTeamName.class);

            startActivity(i);
        });

        awayTeamName = findViewById(R.id.awayTeamName);


    }

    private void initTieBreakerSettings() {

        SwitchCompat showMessageToChangeSides = findViewById(R.id.showMessageForSideSwitch);

        showMessageToChangeSides.setChecked(mySharedPreferences.getShowMessageForSideChange() == Constants.ON);

        showMessageToChangeSides.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                mySharedPreferences.setShowMessageForSideChange(Constants.ON);
            }else{
                mySharedPreferences.setShowMessageForSideChange(Constants.OFF);
            }
        });

        tieBreakerType = findViewById(R.id.tie_breaker_15);
        tieBreakerType.setChecked(mySharedPreferences.get15PointTieBreaker());

        tieBreakerType.setOnCheckedChangeListener((compoundButton, b) -> {
            mySharedPreferences.set15PointTieBreaker(b);
        });

    }

    private void prepareActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayShowCustomEnabled(true);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_score_keeper:
                Intent i = new Intent(Settings.this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

    private void initSetNumber() {

        final CheckBox threeSets = findViewById(R.id.threeSets);

        final CheckBox fiveSets = findViewById(R.id.fiveSets);

        if(mySharedPreferences.gettNumberOfSets() == 3){

            threeSets.setChecked(true);

        }else{

            fiveSets.setChecked(true);

        }

        threeSets.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){

                fiveSets.setChecked(false);

                mySharedPreferences.setNumberOfSets(3);

                resetGame();

            }else{

                if(!fiveSets.isChecked()){

                    fiveSets.setChecked(true);

                }

            }

        });

        fiveSets.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){

                threeSets.setChecked(false);

                mySharedPreferences.setNumberOfSets(5);

                resetGame();

            }else{

                if(!threeSets.isChecked()){

                    threeSets.setChecked(true);

                }

            }

        });

    }

    private void resetGame() {

        mySharedPreferences.setAwayScore(0);
        mySharedPreferences.setHomeScore(0);
        mySharedPreferences.setHomeSetsWon(0);
        mySharedPreferences.setAwaySetsWon(0);
        mySharedPreferences.setHomeTimeoutOne(false);
        mySharedPreferences.setHomeTimeoutTwo(false);
        mySharedPreferences.setAwayTimeoutOne(false);
        mySharedPreferences.setAwayTimeoutTwo(false);
        SetHistoryShared setHistoryShared = new SetHistoryShared(this);
        setHistoryShared.setFirstSetHistory("");
        setHistoryShared.setSecondSetHistory("");
        setHistoryShared.setThirdSetHistory("");
        setHistoryShared.setFourthSetHistory("");
        setHistoryShared.setFifthSetHistory("");

        deleteStream();


    }

    private void deleteStream(){

        StreamOnShared streamOnShared = new StreamOnShared(this);

        if(streamOnShared.getStreamOn() != null){

            Intent i = new Intent(this, DeleteStreamLater.class);
            i.putExtra("name", streamOnShared.getStreamOn());
            startService(i);

            streamOnShared.setStreamOn(null);
        }
    }

    private void initNumbers() {

        phoneNumbers = findViewById(R.id.phoneNumbers);

        phoneNumbers.setText(String.valueOf(mySharedPreferences.getNumbers()));

        LinearLayout numbersLinear = findViewById(R.id.phoneNumberLinear);

        numbersLinear.setOnClickListener(view -> {
            Intent i = new Intent(Settings.this, EditNumbers.class);
            startActivity(i);
        });

    }

    private void initVibrate() {

        SwitchCompat vibrateSwitch = findViewById(R.id.vibrateSwitch);

        if(mySharedPreferences.getVibrate() == Constants.ON){
            vibrateSwitch.setChecked(true);
        }

        vibrateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                mySharedPreferences.setVibrate(Constants.ON);
            }else{
                mySharedPreferences.setVibrate(Constants.OFF);
            }

        });

    }

    private void initTextOutline() {

        textMessageOutlineText = findViewById(R.id.textMessageOutlineText);

        LinearLayout textOutlineLinearLayout = findViewById(R.id.linearLayoutTextOutline);

        textOutlineLinearLayout.setOnClickListener(view -> {
            Intent i = new Intent(Settings.this, EditSMS.class);
            startActivity(i);
        });

    }

    private void initPostSetTextOutline() {

        textMessageOutlineTextPostSet = findViewById(R.id.textMessageOutlineTextPostSet);

        LinearLayout textOutlineLinearLayoutPostSet = findViewById(R.id.linearLayoutTextOutlinePostSet);

        textOutlineLinearLayoutPostSet.setOnClickListener(view -> {
            Intent i = new Intent(Settings.this, EditSMSPostSet.class);
            startActivity(i);
        });

    }

    @Override
    protected void onResume() {

        if(mySharedPreferences.getSMSMessage().equals("")){
            textMessageOutlineText.setText("Click to set SMS outline");
        }else {
            textMessageOutlineText.setText(String.valueOf(mySharedPreferences.getSMSMessage()));
        }

        if(mySharedPreferences.getSMSMessagePostSet().equals("")){
            textMessageOutlineTextPostSet.setText("Click to set SMS outline");
        }else {
            textMessageOutlineTextPostSet.setText(String.valueOf(mySharedPreferences.getSMSMessagePostSet()));
        }

        if(mySharedPreferences.getNumbers().equals("")){
            phoneNumbers.setText("Click to add phone numbers");
        }else {
            phoneNumbers.setText(String.valueOf(mySharedPreferences.getNumbers()));
        }

        homeTeamName.setText(String.valueOf(mySharedPreferences.getHomeTeamName()));

        awayTeamName.setText(String.valueOf(mySharedPreferences.getAwayTeamName()));

        super.onResume();
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Settings.this, MainActivity.class);
        startActivity(i);

        super.onBackPressed();
    }

}
