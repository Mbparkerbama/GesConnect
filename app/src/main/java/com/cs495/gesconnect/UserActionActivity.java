package com.cs495.gesconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidget;
import com.myscript.atk.sltw.SingleLineWidgetApi;
import com.myscript.certificate.MyCertificate;

import java.util.ArrayList;
import java.util.List;

public class UserActionActivity extends AppCompatActivity implements
        SingleLineWidgetApi.OnConfiguredListener,
        SingleLineWidgetApi.OnTextChangedListener,
        SingleLineWidgetApi.OnPenMoveListener
{
    private static final String TAG = "UserActionActivity";
    private SingleLineWidgetApi widget;
    private PointSet pointSet = new PointSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_surface);

        widget = (SingleLineWidget) findViewById(R.id.singleLine_widget);
        if (!widget.registerCertificate(MyCertificate.getBytes()))
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }

        widget.setOnConfiguredListener(this);
        widget.setOnTextChangedListener(this);
        widget.setAutoScrollEnabled(false);
        widget.setOnPenMoveListener(this);

        // references assets directly from the APK to avoid extraction in application
        // file system
        widget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf");

        // The configuration is an asynchronous operation. Callbacks are provided to
        // monitor the beginning and end of the configuration process and update the UI
        // of the input method accordingly.
        //
        // "en_US" references the en_US bundle name in conf/en_US.conf file in your assets.
        // "cur_text" references the configuration name in en_US.conf
        widget.configure("en_US", "cur_text");

        /**
         * Configure Buttons
         */

        final Button clearButton = (Button) findViewById(R.id.drawing_button_left);
        clearButton.setText("Clear");
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.clear();
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "# Points: " + pointSet.getSize());
                    Log.d(TAG, "Points: " + pointSet.toString());
                }
                pointSet.clear();
            }
        });

        final Button settingsButton = (Button) findViewById(R.id.drawing_button_right);
        settingsButton.setText("Settings");

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings(v);
            }
        });


        final Button submitButton = (Button) findViewById(R.id.drawing_button_middle);
        submitButton.setText("Submit");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClickListener(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        widget.setOnTextChangedListener(null);
        widget.setOnConfiguredListener(null);

        super.onDestroy();
    }

    @Override
    public void onConfigured(SingleLineWidgetApi singleLineWidgetApi, boolean success) {
        if(!success)
        {
            Toast.makeText(getApplicationContext(), widget.getErrorString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to configure the Single Line Widget: " + widget.getErrorString());
            return;
        }
        Toast.makeText(getApplicationContext(), "Single Line Widget Configured", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Single Line Widget configured!");
    }

    @Override
    public void onTextChanged(SingleLineWidgetApi singleLineWidgetApi, String s, boolean b) {
        Toast.makeText(getApplicationContext(), "Recognition update", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
        {
            Log.d(TAG, "Single Line Widget recognition: " + widget.getText());
        }
    }

    @Override
    public void onPenMove(SingleLineWidgetApi singleLineWidgetApi, CaptureInfo captureInfo) {
        Point p = new Point();
        p.setX(captureInfo.getX());
        p.setY(captureInfo.getY());

        pointSet.appendPoint(p);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onSubmitButtonClickListener(View v) {
        String text = widget.getText().trim();

        List<String> contactNames = new ArrayList<String>();
        List<String> contactIds = new ArrayList<String>();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Text: " + text);
        }

        Cursor cursor = getContentResolver().query(
                android.provider.ContactsContract.Contacts.CONTENT_URI,
                new String[] { ContactsContract.Contacts.PHOTO_ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts._ID },
                ContactsContract.Contacts.HAS_PHONE_NUMBER, null,
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor == null) {
            return;
        }

        cursor.moveToFirst();

         do {
             contactNames.add(cursor.getString(1));
             contactIds.add(cursor.getString((2)));
             Log.d(TAG, cursor.getString(1));
        } while (cursor.moveToNext());

        if (contactNames.contains(text)) {
            // Exact Match Found
            int position = contactNames.indexOf(text);

            if (BuildConfig.DEBUG) { Log.d(TAG, "CONTACT MATCH FOUND"); }

            cursor = getContentResolver()
                    .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[] {
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?",
                            new String[] { contactIds.get(position) }, null);

            List <String> contactNumbers = new ArrayList<String>();

            cursor.moveToFirst();

             do {
                contactNumbers.add(cursor.getString(0));
                Log.d(TAG, cursor.getString(0));
            } while (cursor.moveToNext());

            call(contactNumbers.get(0));

        }
    }

    private void call(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(phone.trim())));
        try {
            startActivity(intent);

        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(),"Invalid permission",Toast.LENGTH_SHORT).show();
        }
    }
}
