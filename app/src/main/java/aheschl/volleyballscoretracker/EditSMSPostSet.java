package aheschl.volleyballscoretracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditSMSPostSet extends AppCompatActivity {

    EditText messageField;

    MySharedPreferences mySharedPreferences;

    TextView messagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_smspost_set);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mySharedPreferences = new MySharedPreferences(this);

        initPreview();

        initMessageField();

        initHomeScore();

        initAwayScore();

        initHomeTeamName();

        initAwayTeamName();

        initHomeSets();

        initAwaySets();

        initSave();

        initSetOrMatch();

        initWinningTeam();

    }

    private void initSave() {

        ImageButton save = findViewById(R.id.save);

        save.setOnClickListener(view -> finish());

    }

    private void initHomeTeamName() {

        final Button homeTeamName = findViewById(R.id.homeTeamName);

        homeTeamName.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.HOME_TEAM_NAME);
        });

    }

    private void initAwayTeamName() {

        final Button awayTeamName = findViewById(R.id.awayTeamName);

        awayTeamName.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.AWAY_TEAM_NAME);
        });

    }

    private void initPreview() {

        messagePreview = findViewById(R.id.messagePreview);


        messagePreview.setText(getMessage());

    }

    private String getMessage() {

        String messageOutline = String.valueOf(mySharedPreferences.getSMSMessagePostSet());

        String messageWithHome = messageOutline.replaceAll(Constants.HOME_SCORE, String.valueOf(mySharedPreferences.getHomeScore()));

        String messageWithAway = messageWithHome.replaceAll(Constants.AWAY_SCORE, String.valueOf(mySharedPreferences.getAwayScore()));

        int setNum = getSetNum();

        String messageWithSetNum = messageWithAway.replaceAll(Constants.SET, String.valueOf(setNum));

        int homeSetsWon = getHomeSets();

        int awaySetsWon = getAwaySets();

        final String messageWithHomeSets = messageWithSetNum.replaceAll(Constants.HOME_SETS, String.valueOf(homeSetsWon));

        final String messageWithAwaySets = messageWithHomeSets.replaceAll(Constants.AWAY_SETS, String.valueOf(awaySetsWon));

        final String messageWithHomeTeamName = messageWithAwaySets.replaceAll(Constants.HOME_TEAM_NAME, String.valueOf(mySharedPreferences.getHomeTeamName()));

        return messageWithHomeTeamName.replaceAll(Constants.AWAY_TEAM_NAME, String.valueOf(mySharedPreferences.getAwayTeamName()));
    }

    String getMessage(String outline) {

        String messageWithHome = outline.replaceAll(Constants.HOME_SCORE, String.valueOf(mySharedPreferences.getHomeScore()));

        String messageWithAway = messageWithHome.replaceAll(Constants.AWAY_SCORE, String.valueOf(mySharedPreferences.getAwayScore()));

        int setNum = getSetNum();

        String messageWithSetNum = messageWithAway.replaceAll(Constants.SET, String.valueOf(setNum));

        int homeSetsWon = getHomeSets();

        int awaySetsWon = getAwaySets();

        final String messageWithHomeSets = messageWithSetNum.replaceAll(Constants.HOME_SETS, String.valueOf(homeSetsWon));

        final String messageWithAwaySets = messageWithHomeSets.replaceAll(Constants.AWAY_SETS, String.valueOf(awaySetsWon));

        final String messageWithHomeTeamName = messageWithAwaySets.replaceAll(Constants.HOME_TEAM_NAME, String.valueOf(mySharedPreferences.getHomeTeamName()));

        return messageWithHomeTeamName.replaceAll(Constants.AWAY_TEAM_NAME, String.valueOf(mySharedPreferences.getAwayTeamName()));
    }

    private int getSetNum(){

        int setNum = 1;

        setNum += mySharedPreferences.getHomeSetsWon();

        setNum += mySharedPreferences.getAwaySetsWon();

        return setNum;

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

    private void initMessageField() {

        messageField = findViewById(R.id.textMessageField);

        messageField.setText(String.valueOf(mySharedPreferences.getSMSMessagePostSet()));

        messageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                messagePreview.setText(getMessage(editable.toString()));
            }
        });

    }

    private void initAwaySets() {

        final Button awaySets = findViewById(R.id.away_sets);

        awaySets.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.AWAY_SETS);
        });

    }

    private void initHomeSets() {

        final Button homeSets = findViewById(R.id.home_sets);

        homeSets.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.HOME_SETS);
        });

    }

    private void initAwayScore() {

        final Button awayScore = findViewById(R.id.awayScore);

        awayScore.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.AWAY_SCORE);
        });

    }

    private void initHomeScore() {

        final Button homeScore = findViewById(R.id.homeScore);

        homeScore.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.HOME_SCORE);
        });

    }

    private void initSetOrMatch() {

        final Button setOrMatch = findViewById(R.id.setOrMatch);

        setOrMatch.setOnClickListener(v -> {
            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.MATCH_OR_SET);
        });

    }

    private void initWinningTeam() {

        final Button winningTeam = findViewById(R.id.winningTeam);

        winningTeam.setOnClickListener(v -> {

            int start = messageField.getSelectionStart(); //this is to get the the cursor position
            messageField.getText().insert(start, Constants.WINNING_TEAM);

        });

    }


    @Override
    protected void onPause() {

        mySharedPreferences.setSMSMessagePostSet(messageField.getText().toString());

        super.onPause();
    }

}
