package aheschl.volleyballscoretracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChangeAwayTeamName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_away_team_name);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final MySharedPreferences mySharedPreferences = new MySharedPreferences(this);

        EditText awayTeamField = findViewById(R.id.awayTeamField);

        awayTeamField.setText(String.valueOf(mySharedPreferences.getAwayTeamName()));

        awayTeamField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                mySharedPreferences.setAwayTeamName(editable.toString());

            }
        });

        ImageButton close = findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
