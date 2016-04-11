package com.cs495.gesconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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

import java.sql.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UserActionActivity extends AppCompatActivity implements
        SingleLineWidgetApi.OnConfiguredListener,
        SingleLineWidgetApi.OnTextChangedListener,
        SingleLineWidgetApi.OnPenMoveListener
{
    private static final String TAG = "UserActionActivity";
    private SingleLineWidgetApi widget;
    private PointSet pointSet = new PointSet();
    private Vibrator vibrator;

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

                submitButton.setEnabled(false);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                submitButton.setEnabled(true);
                            }
                        });
                    }
                }, 2000);
            }
        });

        vibrator = (Vibrator)(getApplicationContext().getSystemService(VIBRATOR_SERVICE));
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
           // Toast.makeText(getApplicationContext(), widget.getErrorString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to configure the Single Line Widget: " + widget.getErrorString());
            return;
        }
        //Toast.makeText(getApplicationContext(), "Single Line Widget Configured", Toast.LENGTH_SHORT).show();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "Single Line Widget configured!");
    }

    @Override
    public void onTextChanged(SingleLineWidgetApi singleLineWidgetApi, String s, boolean b) {
        //Toast.makeText(getApplicationContext(), "Recognition update", Toast.LENGTH_SHORT).show();
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
        String nameCandidate = widget.getText().trim();

        ContactsManager contacts = new ContactsManager(getApplicationContext());


        if(!pointSet.getPoints().isEmpty()) {
            if (contacts.countMatches(nameCandidate) == 1) {
                call(contacts.findPhoneNumber(nameCandidate));
            } else {
                Log.d(TAG, "NO CONTACT FOUND");
                Toast.makeText(getApplicationContext(), "No Contact Found!", Toast.LENGTH_SHORT).show();

                // Todo: Process as Custom Gesture
                ContactTarget match = getMatch(pointSet);
                if (match != null) {
                    Log.d(TAG, "MATCH PHONE TYPE: " + match.getPhoneType());
                    Log.d(TAG, "MATCH NUMBER IS " + contacts.findPhoneByType(match.getLookupKey(), match.getPhoneType()));
                    call(contacts.findPhoneByType(match.getLookupKey(), match.getPhoneType()));
                    return;
                }
                long[] pattern = {0, 100, 100, 100};
                vibrate(pattern, -1);
            }
        }
    }

    private ContactTarget getMatch(PointSet points)
    {
        boolean hashMatch = false;

        HashMap<ContactTarget, Gesture> gestures = Settings.getGestureList().getGestures();

        for (Map.Entry entry : gestures.entrySet()) {
            if (entry.getValue().equals(points)) {
                Log.d(TAG, "MATCHING GESTURE FOUND.");
                return (ContactTarget) entry.getKey();
            }
        }

        return null;
    }

    private void call(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(phone.trim())));
        try {
            long[] pattern = {0, 300};
            vibrate(pattern, -1);

//            startActivity(intent);
            Log.d(TAG, "Calling: " + phone);
            Toast.makeText(getApplicationContext(),"Calling " + phone, Toast.LENGTH_SHORT).show();

        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(),"Invalid permission",Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrate(long[] pattern, int pulse){
        if (Settings.getSetting(getApplicationContext(),
                Settings.vibrationEnabledString,
                Settings.trueString).equals(Settings.trueString)) {
            vibrator.vibrate(pattern, pulse);
        }
    }
}
