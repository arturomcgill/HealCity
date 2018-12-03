package edu.umd.arturomcgill.healcity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if(type.equals("fruit")) {
            setContentView(R.layout.fruit);
        } else if(type.equals("volunteer")) {
            setContentView(R.layout.volunteer);
        } else{
            setContentView(R.layout.public_transportation);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // 80% of screen
        getWindow().setLayout((int)(width*.7),(int)(height*.7));

        if(type.equals("fruit")){
            Button submitButton = (Button) findViewById(R.id.submit_fruit);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText fruit = (EditText)findViewById(R.id.text_fruit);
                    EditText veggies = (EditText)findViewById(R.id.text_vegetables);

                    int numFruit = 0, numVeggies = 0;

                    if(fruit.getText().toString() != "") {
                        numFruit = Integer.parseInt(fruit.getText().toString());
                    }

                    if(!veggies.getText().toString().equals("")){
                        numVeggies = Integer.parseInt(veggies.getText().toString());
                    }

                    Integer result = numFruit + numVeggies;
                    int xp = result * 25;


                    User user = MainActivity.getCurrentUser();
                    HashMap<Date,Integer> dailyFruitsVeggies = user.getFruitsVeggies();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date today = null;
                    try {
                        String string = format.format(new Date());
                        today = format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(today != null) {
                        if (dailyFruitsVeggies.get(today) == null){
                            dailyFruitsVeggies.put(today, result);
                        } else {
                            dailyFruitsVeggies.put(today, dailyFruitsVeggies.get(today) + result);
                        }
                    } else {
                        Log.i("HealCity", "Couldn't get date");
                    }

                    user.addPoints(xp);
                    user.addDailyXPToDate(today, xp);
                    Toast.makeText(getApplicationContext(), xp + " points and xp added", Toast.LENGTH_LONG).show();


                    finish();

                }
            });
        } else if(type.equals("volunteer")){

            Button submitButton = (Button) findViewById(R.id.submit_volunteer);

            submitButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EditText hours = (EditText)findViewById(R.id.text_volunteer);

                    int numHours = 0;

                    if(hours.getText().toString() != null) {
                        numHours = Integer.parseInt(hours.getText().toString());
                    }

                    int xp = numHours * 200;

                    User user = MainActivity.getCurrentUser();
                    user.addlifetimeVolunteering(numHours);

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date today = null;
                    try {
                        String string = format.format(new Date());
                        today = format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    user.addPoints(xp);
                    user.addDailyXPToDate(today, xp);
                    Toast.makeText(getApplicationContext(), xp + " points and xp added", Toast.LENGTH_LONG).show();


                    finish();


                }

            });



        } else{  // public transportation

            Button submitButton = (Button) findViewById(R.id.submit_transportation);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(((RadioButton)(findViewById(R.id.radioButton))).isChecked() ||
                            ((RadioButton)(findViewById(R.id.radioButton2))).isChecked() ||
                            ((RadioButton)(findViewById(R.id.radioButton3))).isChecked()) {

                        User user = MainActivity.getCurrentUser();
                        user.addlifetimePublicTransportation(1);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Date today = null;
                        try {
                            String string = format.format(new Date());
                            today = format.parse(string);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int xp = 50;

                        user.addPoints(xp);
                        user.addDailyXPToDate(today, xp);
                        Toast.makeText(getApplicationContext(), xp + " points and xp added", Toast.LENGTH_LONG).show();


                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Nothing checked", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

}
