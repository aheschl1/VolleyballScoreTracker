package aheschl.volleyballscoretracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaidVersion extends AppCompatActivity {

    TextView getProMessage;
    Button getPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_version);

        getProMessage = findViewById(R.id.get_pro_message);
        getPro = findViewById(R.id.getPro);

        if(PayedCheck.installedPayedVersion(this)){
            getProMessage.setText("Thank you for buying the pro version. You have access to all content without ads.");
            getPro.setText("Open app in store");
        }

        getPro.setOnClickListener(v -> {
            PayedCheck.openProKey(this);
        });

    }
}
