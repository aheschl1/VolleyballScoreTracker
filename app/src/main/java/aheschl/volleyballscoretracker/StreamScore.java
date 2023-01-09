package aheschl.volleyballscoretracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StreamScore extends AppCompatActivity {

    Button streamScore;
    Button watchStream;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    TextView streamStatus;
    ImageButton shareCode;
    StreamOnShared streamOnShared;
    boolean reopen;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_score);

        reopen = getIntent().getBooleanExtra("reopen", true);
        streamOnShared = new StreamOnShared(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        streamScore = findViewById(R.id.startstream);
        watchStream = findViewById(R.id.joinstream);

        streamStatus = findViewById(R.id.streamStatus);
        shareCode = findViewById(R.id.shareCode);

        checkForActiveStreamWatch();

        if(streamOnShared.getStreamOn() != null){
            streamStatus.setText("Stream code: " + streamOnShared.getStreamOn());
            streamScore.setText("End Stream");
        }

        streamScore.setOnClickListener((v)->{

           if(streamOnShared.getStreamOn() != null){
               new android.app.AlertDialog.Builder(StreamScore.this)
                       .setTitle("End Stream")
                       .setMessage("Are you sure you want to stop streaming?")
                       .setPositiveButton("Yes", (dialog, which) -> {
                           deleteStream();
                           streamStatus.setText("Stream not on");
                           streamScore.setText("Start Stream");
                       })
                       .setNegativeButton("No", (dialogInterface, i) -> {

                       })
                       .setCancelable(true)
                       .show();

           }else{
               if(isNetworkAvailable()) {

                   if (mAuth.getCurrentUser() == null) {
                       mAuth.signInAnonymously().addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               startStream();
                           } else {
                               Snackbar.make(streamScore, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                           }
                       });
                   } else {
                       startStream();
                   }
               }else{
                   Snackbar.make(streamScore, "You are not online", Snackbar.LENGTH_SHORT).show();
               }
           }
        });

        if(streamOnShared.getStreamBeingWatched() == null){
            watchStream.setOnClickListener((v)->{

                if(isNetworkAvailable()){
                    startWatching();
                }else{
                    Snackbar.make(streamScore, "You are not online", Snackbar.LENGTH_SHORT).show();
                }

            });
        }else{
            watchStream.setText("Stop Spectating");
            watchStream.setOnClickListener((v)->{
                if(isNetworkAvailable()){
                    streamOnShared.setStreamBeingWatched(null);
                    watchStream.setText("Spectate");
                    watchStream.setOnClickListener((w)->{

                        if(isNetworkAvailable()){
                            startWatching();
                        }else{
                            Snackbar.make(streamScore, "You are not online", Snackbar.LENGTH_SHORT).show();
                        }

                    });
                }else{
                    Snackbar.make(streamScore, "You are not online", Snackbar.LENGTH_SHORT).show();
                }

            });
        }

        shareCode.setOnClickListener((v)->{
            if(streamOnShared.getStreamOn() != null){
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, streamOnShared.getStreamOn());
                startActivity(Intent.createChooser(share, "Share Code"));
            }else{
                Snackbar.make(streamScore, "No stream to share", Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void startStream(){
        if(PayedCheck.installedPayedVersion(this)){
            streamOnShared.setStreamOn(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            streamStatus.setText("Stream code: " + streamOnShared.getStreamOn());
            streamScore.setText("End Stream");
            Snackbar.make(streamScore, "Stream Started", Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar snackbar = Snackbar.make(streamScore, "Pro is required to stream a match", Snackbar.LENGTH_SHORT);
            snackbar.setAction("GET PRO", view -> {
                Intent i = new Intent(StreamScore.this, PaidVersion.class);
                startActivity(i);
                snackbar.dismiss();
            });
            snackbar.show();
        }

    }

    private void checkForActiveStreamWatch() {

        if(streamOnShared.getStreamBeingWatched() != null && reopen){

            database.getReference(streamOnShared.getStreamBeingWatched()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Snackbar.make(streamScore, "The stream you were watching is finished", Snackbar.LENGTH_SHORT).show();
                        streamOnShared.setStreamBeingWatched(null);
                    }else{
                        Intent i = new Intent(StreamScore.this, WatchStream.class);
                        i.putExtra("stream_code", streamOnShared.getStreamBeingWatched());
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void startWatching() {

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 8, 16, 8);
        final EditText input = new EditText(this);
        input.setLayoutParams(lp);
        input.setGravity(android.view.Gravity.TOP| Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setHint("Game Code");
        container.addView(input, lp);

        new AlertDialog.Builder(this)
                .setTitle("Enter code")
                .setMessage("Ask the person who started the stream to send you the game code, and paste it here to watch.")
                .setView(container)
                .setPositiveButton("Join", (dialog, whichButton) -> {
                    
                    if(input.getText().length() > 0){
                        String gameCode = input.getText().toString().trim();
                        try{
                            database.getReference(gameCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        Snackbar.make(streamScore, "This stream has not yet started", Snackbar.LENGTH_SHORT).show();
                                    }else{
                                        streamOnShared.setStreamBeingWatched(gameCode);
                                        Intent i = new Intent(StreamScore.this, WatchStream.class);
                                        i.putExtra("stream_code", gameCode);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }catch(DatabaseException e){
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this, "Enter at least one character", Toast.LENGTH_SHORT).show();
                    }
                   


                })
        .show();
    }

    private void deleteStream(){

        if(streamOnShared.getStreamOn() != null){

            Intent i = new Intent(this, DeleteStreamLater.class);
            i.putExtra("name", streamOnShared.getStreamOn());
            startService(i);

            streamOnShared.setStreamOn(null);
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
        if (item.getItemId() == R.id.action_score_keeper) {
            Intent i = new Intent(StreamScore.this, MainActivity.class);
            startActivity(i);
        }

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Network is present and connected
        return networkInfo != null && networkInfo.isConnected();
    }
}