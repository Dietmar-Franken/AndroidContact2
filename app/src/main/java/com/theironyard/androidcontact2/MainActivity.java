package com.theironyard.androidcontact2;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;



import static android.provider.Telephony.Mms.Part.FILENAME;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    ArrayAdapter<String> contacts;

    ListView list;
    EditText name;
    EditText number;
    Button addButton;


    public static final int CONTACTS_REQUEST = 1;

    final String FILENAME = "contacts.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list = (ListView) findViewById(R.id.listView);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        addButton = (Button) findViewById(R.id.addButton);

        contacts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        loadContactsList();
        list.setAdapter(contacts);

        addButton.setOnClickListener(this);
        list.setOnItemLongClickListener(this);
        list.setOnItemClickListener(this);


    }

    @Override
    public void onClick(View view) {
        String contact = name.getText().toString();
        String contactNum = number.getText().toString();
        contacts.add(contact + " - " + contactNum);
        name.setText(" ");
        number.setText(" ");
        saveContactList();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final String contact = contacts.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact List");
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                contacts.remove(contact);
                saveContactList();
            }

        });
        builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //does nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("contacts", contacts.getItem(position));
        intent.putExtra("position", position);

        startActivityForResult(intent, CONTACTS_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACTS_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                String contact = data.getStringExtra("contacts");
                //String contactNumber = data.getStringExtra("numbers");
                contacts.remove(contacts.getItem(position));
                contacts.notifyDataSetChanged();
                contacts.add(contact);


                saveContactList();
            }
        }
    }

    private void saveContactList() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contacts.getCount(); i++) {
                String phoneList = contacts.getItem(i);

                sb.append(phoneList + "\n");
            }
            fos.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContactsList() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while (br.ready()) {
                String contactLine = br.readLine();
                contacts.add(contactLine);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

