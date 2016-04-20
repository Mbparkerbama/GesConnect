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
    GestureList gestureList;
    private Map<ContactTarget, String> contacts;
    private List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gesture);

        gestureList = Settings.getGestureList();
        Set<ContactTarget> contactTargets = gestureList.getGestures().keySet();

        contactsManager = new ContactsManager(getApplicationContext());

        contacts = new HashMap<>();

        for (ContactTarget ct : contactTargets) {
            String name = contactsManager.findName(ct.getLookupKey());
            contacts.put(ct, name);
            names.add(name);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        );

        listView = (ListView) findViewById(R.id.customGestureListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String contactName = tv.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), EditCustomGestureActivity.class);

                for (Map.Entry<ContactTarget, String> i : contacts.entrySet()) {
                    if (i.getValue().equals(contactName)) {
                        intent.putExtra("ContactTarget", i.getKey());
                        break;
                    }
                }

                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Item clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditCustomGestureActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view;
                String contactName = tv.getText().toString().trim();

                for (Map.Entry<ContactTarget, String> i : contacts.entrySet()) {
                    if (i.getValue().equals(contactName)) {
                        gestureList.removeGesture(i.getKey());
                        contacts.remove(i.getKey());
                        Settings.saveGestureList(getApplicationContext());
                        break;
                    }
                }

                names.remove(position);
                adapter.notifyDataSetChanged();

                return true;

            }
        });
    }



}
