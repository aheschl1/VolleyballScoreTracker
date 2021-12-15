package aheschl.volleyballscoretracker;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class BuyMeACoffee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_me_acoffee);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://buymeacoff.ee/andrewheschl"));
            startActivity(browserIntent);

        });

    }
}
