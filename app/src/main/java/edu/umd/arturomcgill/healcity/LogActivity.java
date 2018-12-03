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

                    int num = user.getLifetimeFruitsVeggies();

                    if(num < 50 && num + result >= 50){
                        user.addLifetimeAchievement("50 fruits and veggies");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    } else if(num < 100 && num + result >= 100){
                        user.addLifetimeAchievement("100 fruits and veggies");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    } else if(num < 500 && num + result >= 500){
                        user.addLifetimeAchievement("500 fruits and veggies");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    }

                    HashMap<String,Integer> dailyFruitsVeggies = user.getFruitsVeggies();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date today = null;
                    try {
                        String string = format.format(new Date());
                        today = format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(today != null) {
                        if (dailyFruitsVeggies.get(today.toString()) == null){
                            dailyFruitsVeggies.put(today.toString(), result);
                        } else {
                            dailyFruitsVeggies.put(today.toString(), dailyFruitsVeggies.get(today) + result);
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
                    int totalHours = user.getLifetimeVolunteering();

                    if(totalHours < 10 && totalHours + numHours >= 10){
                        user.addLifetimeAchievement("Volunteered 10 hours");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    } else if(totalHours < 25 && totalHours + numHours >= 25){
                        user.addLifetimeAchievement("Volunteered 25 hours");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    } else if(totalHours < 100 && totalHours + numHours >= 100){
                        user.addLifetimeAchievement("Volunteered 100 hours");
                        Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                    }

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
                    user.addDailyXPToDate(today.toString(), xp);
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

                        int num = user.getlifetimePublicTransportation();

                        if(num < 1 && num + 1 >= 1){
                            user.addLifetimeAchievement("1 use of transportation");
                            Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                        } else if(num < 50 && num + 1 > 50) {
                            user.addLifetimeAchievement("50 uses of transportation");
                            Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                        } else if(num < 100 && num + 1 > 100){
                            user.addLifetimeAchievement("100 uses of transportation");
                            Toast.makeText(getApplicationContext(), "New badge added", Toast.LENGTH_LONG).show();
                        }

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
                        user.addDailyXPToDate(today.toString(), xp);
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
