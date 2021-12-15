package aheschl.volleyballscoretracker;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

public class DeleteStreamLater extends IntentService {


    public DeleteStreamLater() {
        super("DeleteStreamLater");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String path = intent.getStringExtra("name");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FirebaseDatabase.getInstance().getReference(path)
                .setValue(null);

    }
}