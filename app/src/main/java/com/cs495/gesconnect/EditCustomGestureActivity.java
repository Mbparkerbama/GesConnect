package com.cs495.gesconnect;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myscript.atk.core.CaptureInfo;
import com.myscript.atk.sltw.SingleLineWidget;
import com.myscript.atk.sltw.SingleLineWidgetApi;
import com.myscript.certificate.MyCertificate;

public class EditCustomGestureActivity extends AppCompatActivity implements
        SingleLineWidgetApi.OnConfiguredListener,
        SingleLineWidgetApi.OnTextChangedListener,
        SingleLineWidgetApi.OnPenMoveListener {
    private SingleLineWidgetApi widget;
    private PointSet pointSet = new PointSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_gesture);

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
                pointSet.clear();
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.drawing_button_right);
        cancelButton.setText("Cancel");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //openSettings(v);
            }
        });


        final Button submitButton = (Button) findViewById(R.id.drawing_button_middle);
        submitButton.setText("Submit");
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
        //
    }

    @Override
    public void onTextChanged(SingleLineWidgetApi singleLineWidgetApi, String s, boolean b) {
        //
    }

    @Override
    public void onPenMove(SingleLineWidgetApi singleLineWidgetApi, CaptureInfo captureInfo) {
        Point p = new Point();
        p.setX(captureInfo.getX());
        p.setY(captureInfo.getY());

        pointSet.appendPoint(p);
    }



}
