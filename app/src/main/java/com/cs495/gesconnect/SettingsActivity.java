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
        vibrationsEnabledButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // set vibrations to enabled in Settings
                    // ...

                    // Do a short vibration to show vibrations are enabled
                    final Vibrator vibrator
                            = (Vibrator)(getApplicationContext().getSystemService(VIBRATOR_SERVICE));
                    vibrator.vibrate(100);

                    Toast.makeText(getApplicationContext(), "Vibrations enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // set vibrations to disabled in Settings
                    // ...
                    Toast.makeText(getApplicationContext(), "Vibrations disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
