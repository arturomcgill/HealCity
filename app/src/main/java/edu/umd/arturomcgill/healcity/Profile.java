package edu.umd.arturomcgill.healcity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Profile extends Activity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        int index = MainActivity.getAllUsers().indexOf(MainActivity.getCurrentUser());

        Log.i("TEST", "AAAAAAAAAAAAAAAAAAAAA");
  //      MainActivity.getAllUsers().set(index, createTestUser());



        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lifetime_achievements);

        if(getIntent().getStringExtra("currentUser") == "true"){
            Log.i("SARA", "HERE");
            user = MainActivity.getCurrentUser();
        } else {
            //TODO: the intent that creates this activity then should have two extras, one with tag "currentUser" that would be
            // set to false and another one that sends the email of the user that your trying to get
            // so from that email, set the user reference
            ArrayList<User> allUsers = MainActivity.getAllUsers();
            String email = getIntent().getStringExtra("email");
            for(int i = 0; i < allUsers.size(); i++){
                if(allUsers.get(i).getEmail().equals(email)) {
                    user = allUsers.get(i);
                    break;
                }
            }

        }

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(user.getFirstName() + " " + user.getLastName());

        ImageView profilePic = (ImageView)findViewById(R.id.profile_picture);
        Bitmap userPic = user.extractBitmap();

        if(userPic == null){
            profilePic.setImageResource(R.drawable.ic_profile);
        } else {
            profilePic.setImageBitmap(userPic);
        }




        ArrayList<String> achievements = user.getLifetimeAchievements();

        if(achievements == null || achievements.size() == 0) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText("No lifetime achievements :( ");
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView);
        } else {
            for(int i = 0; i < achievements.size(); i++){
                LinearLayout innerLayout = new LinearLayout(getApplicationContext());
                innerLayout.setOrientation(LinearLayout.VERTICAL);
                innerLayout.setPadding(0,0,50, 0);
                TextView textView = new TextView(getApplicationContext());
                textView.setText(achievements.get(i));
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                ImageView imageView = new ImageView(getApplicationContext());
//                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
                imageView.setPadding(0,40, 0,0);
                String text = achievements.get(i);
                if(text.equals("10 fruits and veggies")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fruit1, null));
                } else if(text.equals("50 fruits and veggies")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fruitbadge2, null));
                } else if(text.equals("100 fruits and veggies")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fruitbadge3, null));
                } else if(text.equals("500 fruits and veggies")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.fruitbadge4, null));
                } else if(text.equals("100000 steps")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.walking1, null));
                } else if(text.equals("500000 steps")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.walking2, null));
                } else if(text.equals("1000000 steps")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.run, null));
                } else if(text.equals("2000000 steps")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.steps2, null));
                } else if(text.equals("1 park")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.park1, null));
                } else if(text.equals("10 parks")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.park3, null));
                } else if(text.equals("50 parks")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.park, null));
                } else if(text.equals("1 use of transportation")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bus, null));
                } else if(text.equals("50 uses of transportation")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bus2, null));
                } else if(text.equals("100 uses of transportation")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bus3, null));
                } else if(text.equals("Volunteered 10 hours")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.volunteer4, null));
                } else if(text.equals("Volunteered 25 hours")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.volunteer2, null));
                } else if(text.equals("Volunteered 100 hours")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.volunteer3, null));
                }


                innerLayout.addView(textView);
                innerLayout.addView(imageView);
                linearLayout.addView(innerLayout);


            }

        }




        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // 80% of screen
        getWindow().setLayout((int)(width*.7),(int)(height*.7));

    }

    private User createTestUser(){

        User user = MainActivity.getCurrentUser();
        Date today = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String string = format.format(new Date());
            today = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HashMap<String, Integer> fruits = new HashMap<String, Integer>();
        fruits.put(today.toString(), 50);
        user.setFruitsVeggies(fruits);
        user.addLifetimeParks(10);

        ArrayList<String> achievements = new ArrayList<String>();
        achievements.add("10 fruits and veggies");
        achievements.add("50 fruits and veggies");
        achievements.add("100 fruits and veggies");
        achievements.add("500 fruits and veggies");
        achievements.add("100000 steps");
        achievements.add("500000 steps");
        achievements.add("1000000 steps");
        achievements.add("2000000 steps");
        achievements.add("1 park");
        achievements.add("10 parks");
        achievements.add("50 parks");
        achievements.add("1 use of transportation");
        achievements.add("50 uses of transportation");
        achievements.add("100 uses of transportation");
        achievements.add("Volunteered 10 hours");
        achievements.add("Volunteered 25 hours");
        achievements.add("Volunteered 100 hours");


        user.setProfilePhoto(BitmapFactory.decodeResource(getResources(),R.drawable.zipcar));

        user.setLifetimeAchievements(achievements);

        return user;
    }

}
