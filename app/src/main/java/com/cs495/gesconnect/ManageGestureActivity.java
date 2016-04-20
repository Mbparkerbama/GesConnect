package com.cs495.gesconnect;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageGestureActivity extends AppCompatActivity {

    private ListView listView;
    private ContactsManager contactsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gesture);

        GestureList gestureList = Settings.getGestureList();
        Set<ContactTarget> contactTargets = gestureList.getGestures().keySet();

        contactsManager = new ContactsManager(getApplicationContext());

        final Map<ContactTarget, String> contacts = new HashMap<>();

        for (ContactTarget ct : contactTargets) {
            contacts.put(ct, contactsManager.findName(ct.getLookupKey()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                contacts.values().toArray(new String[contacts.size()])
        );

        listView = (ListView) findViewById(R.id.customGestureListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Intent to Edit Activity
                Toast.makeText(getApplicationContext(), "Item clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditCustomGestureActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view;
                Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                return true;

            }
        });
    }



}
