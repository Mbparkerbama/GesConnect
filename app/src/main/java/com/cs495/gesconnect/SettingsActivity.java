package com.cs495.gesconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.cs495.gesconnect.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button addGestureButton = (Button) findViewById(R.id.addGestureButton);
        addGestureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddCustomGestureActivity
                Intent intent = new Intent(getApplicationContext(), AddCustomGestureActivity.class);
                startActivity(intent);
            }
        });

        final Button editGesturesButton = (Button) findViewById(R.id.editGesturesButton);
        editGesturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start ManageGestureActivity
                Intent intent = new Intent(getApplicationContext(), ManageGestureActivity.class);
                startActivity(intent);
            }
        });

        final ToggleButton vibrationsEnabledButton
                = (ToggleButton) findViewById(R.id.vibrationsEnabledButton);
        // Set button state based on application settings (default: true)
        if (Settings.getSetting(getApplicationContext(),
                Settings.vibrationEnabledString,
                Settings.trueString).equals(Settings.trueString)) {
            vibrationsEnabledButton.setChecked(true);
        }
        else {
            vibrationsEnabledButton.setChecked(false);
        }
        vibrationsEnabledButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Set vibrations to enabled in Settings
                    Settings.setSetting(getApplicationContext(),
                                        Settings.vibrationEnabledString,
                                        Settings.trueString);

                    // Do a short vibration to show vibrations are enabled
                    final Vibrator vibrator
                            = (Vibrator)(getApplicationContext().getSystemService(VIBRATOR_SERVICE));
                    vibrator.vibrate(100);

                    Toast.makeText(getApplicationContext(), Settings.getSetting(
                            getApplicationContext(), Settings.vibrationEnabledString)
                        , Toast.LENGTH_SHORT).show();
                } else {
                    // Set vibrations to disabled in com.cs495.gesconnect.Settings
                    Settings.setSetting(getApplicationContext(),
                            Settings.vibrationEnabledString,
                            Settings.falseString);

                    Toast.makeText(getApplicationContext(), Settings.getSetting(
                            getApplicationContext(), Settings.vibrationEnabledString)
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
