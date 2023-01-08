package aheschl.volleyballscoretracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class EditNumbers extends AppCompatActivity {

    MySharedPreferences mySharedPreferences;

    EditText numberField;
    private final int PICK_CONTACT = 999;
    ImageButton close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_numbers);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mySharedPreferences = new MySharedPreferences(this);
        initEditNumberField();
        initAddContact();
        initClose();
    }

    private void initClose(){
        close = findViewById(R.id.close);
        close.setOnClickListener(view -> finish());
    }

    private void initAddContact() {

        Button addContact = findViewById(R.id.addContact);

        addContact.setOnClickListener((view)-> loadFromContacts());

    }

    private void initEditNumberField() {

        numberField = findViewById(R.id.editNumbersField);

        numberField.setText(String.valueOf(mySharedPreferences.getNumbers()));

    }


    private void loadFromContacts(){
        if (ContextCompat.checkSelfPermission(EditNumbers.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("Allow Permissions")
                    .setMessage("You need to allow the permission read contacts to use this feature. " +
                            "The permission will only be used to add numbers. " +
                            "Allow the permission and click the load contact button again.").setPositiveButton("Ok", (dialogInterface, i) ->
                    ActivityCompat.requestPermissions(EditNumbers.this, new String[]{Manifest.permission.READ_CONTACTS}, 101))
                    .show();
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    private void handleContactNumber(String number){
        String existingNumbers = String.valueOf(mySharedPreferences.getNumbers());
        if(existingNumbers.length() > 0) {
            existingNumbers = existingNumbers + ", " + number;
        }else{
            existingNumbers = existingNumbers + number;
        }
        mySharedPreferences.setNumbers(existingNumbers);
        numberField.setText(existingNumbers);
    }

    @Override public void onActivityResult(int reqCode, int resultCode, Intent data){

        if (resultCode == RESULT_OK) {
            if (reqCode == PICK_CONTACT) {
                Uri result = data.getData();
                String id = Objects.requireNonNull(result).getLastPathSegment();

                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{id}, null);

                if (Objects.requireNonNull(phones).moveToFirst()) {

                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    handleContactNumber(number);

                }

                phones.close();
            }

        }

        super.onActivityResult(reqCode, resultCode, data);

    }


    @Override
    protected void onPause() {
        mySharedPreferences.setNumbers(numberField.getText().toString());
        super.onPause();
    }
}

