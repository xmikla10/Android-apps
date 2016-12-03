package com.flatmate.flatmate;

import com.flatmate.flatmate.Firebase.NewWork;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.flatmate.flatmate.Firebase.FirebaseHelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by xmikla10 on 29.10.2016.
 */

public class AddNewWorkActivity extends AppCompatActivity {

    public static final int ADD_FINISHED = 128;
    public static final String SELECTED_ADD_KEY = "date_result";
    public static final String TAG = AddNewWorkActivity.class.getSimpleName();
    String dur;
    int value;
    int a;
    String str1;
    String str2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_work);

        Spinner dropdown1 = (Spinner) findViewById(R.id.deadline_spinner);
        String[] items = new String[]
                {
                        "1 hour",
                        "3 hours",
                        "today",
                        "tomorrow",
                        "week",
                        "2 weeks",
                        "month"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown1.setAdapter(adapter);

        Button plus = (Button) findViewById(R.id.buttonplus);
        Button minus = (Button) findViewById(R.id.buttonminus);
        //final EditText duraction = (EditText) findViewById(R.id.editTextDuraction);

        plus.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                EditText duraction = (EditText) findViewById(R.id.duraction);
                String b = duraction.getText().toString();
                int c = Integer.parseInt(b);
                if ( c < 60)
                    c = c + 5;
                else
                    c = c + 10;
                String a = Integer.toString(c);
                duraction.setText(a);
            }
        });

        CheckBox checkbox = (CheckBox) findViewById(R.id.duration_of_auction_checkBox);
        final Switch sw2 = (Switch) findViewById(R.id.switchDate);
        final Switch sw1 = (Switch) findViewById(R.id.switchTime);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    sw1.setVisibility(View.VISIBLE);
                    sw2.setVisibility(View.VISIBLE);
                }
                else
                {
                    sw1.setVisibility(View.INVISIBLE);
                    sw2.setVisibility(View.INVISIBLE);
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                EditText duraction = (EditText) findViewById(R.id.duraction);
                String b = duraction.getText().toString();
                int c = Integer.parseInt(b);
                if ( c <= 60)
                    c = c - 5;
                else
                    c = c - 10;

                if ( c < 0)
                    c = 0;
                String a = Integer.toString(c);
                duraction.setText(a);
            }
        });

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    startActivityForResult(new Intent(AddNewWorkActivity.this, TimePopUp.class), TimePopUp.TIME_POPUP_FINISHED);
                }
                else
                {
                    Switch switch1 = (Switch) findViewById(R.id.switchTime);
                    switch1.setText("Time");
                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    startActivityForResult(new Intent(AddNewWorkActivity.this, DatePopUp.class), DatePopUp.DATE_POPUP_FINISHED);
                }
                else
                {
                    Switch switch2 = (Switch) findViewById(R.id.switchDate);
                    switch2.setText("Date");
                }
            }
        });

    }

    public void saveTask(View view)
    {
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        EditText work_name_text = (EditText) findViewById(R.id.work_name);
        EditText duration_text = (EditText) findViewById(R.id.duraction);
        Spinner deadline_spinner_text = (Spinner) findViewById(R.id.deadline_spinner);


        String work_name = work_name_text.getText().toString().trim();
        String duration = duration_text.getText().toString().trim();
        String deadline_spinner = deadline_spinner_text.getSelectedItem().toString().trim();
        String date = str2;
        String time = str1;

        if ( work_name_text.getText().toString().trim().length() == 0)
        {
            Toast.makeText( getBaseContext(), "Please, enter a name of work" ,Toast.LENGTH_SHORT).show();
        }
        else {
            String selectedDate = (work_name + "#"
                    + duration + "#"
                    + deadline_spinner + "#"
                    + date + "#"
                    + time + "#");
            Intent intent = new Intent();
            intent.putExtra(SELECTED_ADD_KEY, selectedDate);
            setResult(ADD_FINISHED, intent);

            /*NewWork newWork = new NewWork();

            newWork.set_work_name(work_name);
            newWork.set_duration(duration);
            newWork.set_deadline(deadline_spinner);
            newWork.set_date(date);
            newWork.set_time(time);

            ref.child("todo").push().setValue(newWork);*/

            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data ==  null)
        {
            Switch switch1 = (Switch) findViewById(R.id.switchTime);
            Switch switch2 = (Switch) findViewById(R.id.switchDate);
            switch1.setChecked(false);
            switch2.setChecked(false);
            return;
        }
        switch (requestCode) {
            case TimePopUp.TIME_POPUP_FINISHED:

                Switch switch1 = (Switch) findViewById(R.id.switchTime);
                String[] split1 = data.getStringExtra(TimePopUp.SELETECTED_TIME_KEY).split("-");
                str1 = Arrays.toString(split1).replace("[", "").replace("]", "");
                ;
                switch1.setText("Time    -    " +  str1);
                Toast.makeText(this, str1 , Toast.LENGTH_SHORT).show();
                break;

            case DatePopUp.DATE_POPUP_FINISHED:

                Switch switch2 = (Switch) findViewById(R.id.switchDate);
                String[] split2 = data.getStringExtra(DatePopUp.SELECTED_DATE_KEY).split("-");
                str2 = Arrays.toString(split2).replace("[", "").replace("]", "");;
                switch2.setText("Date    -    " + str2);
                Toast.makeText(this, str2 , Toast.LENGTH_SHORT).show();
                break;

            default:
                Log.d(TAG, "onActivityResult: uknown request code " + requestCode);
        }
    }
}