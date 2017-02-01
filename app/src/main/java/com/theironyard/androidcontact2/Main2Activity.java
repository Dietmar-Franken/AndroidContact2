package com.theironyard.androidcontact2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    EditText editName;
    EditText editNumber;


    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        editName = (EditText) findViewById(R.id.editText);
        editNumber = (EditText) findViewById(R.id.editText2);

        String S = getIntent().getExtras().getString("contacts");
        String linepart[] = S.split(" - ");
        editName.setText(linepart[0]);
        editNumber.setText(linepart[1]);


        position = getIntent().getExtras().getInt("position", 0);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String item = editName.getText().toString();
        String items = editNumber.getText().toString();
        String contacts = (item + " - " + items);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contacts",contacts);
        returnIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }
}

