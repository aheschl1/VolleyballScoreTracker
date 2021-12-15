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

public class ChangeHomeTeamName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_home_team_name);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final MySharedPreferences mySharedPreferences = new MySharedPreferences(this);

        EditText homeTeamField = findViewById(R.id.homeTeamField);

        homeTeamField.setText(String.valueOf(mySharedPreferences.getHomeTeamName()));

        homeTeamField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                mySharedPreferences.setHomeTeamName(editable.toString());

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
