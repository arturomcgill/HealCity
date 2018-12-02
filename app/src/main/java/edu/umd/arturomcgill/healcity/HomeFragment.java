package edu.umd.arturomcgill.healcity;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

// -------------------

public class HomeFragment extends Fragment implements SensorEventListener{
    private CircleProgressBar mCustomProgressBar5;


    private SensorManager sensorManager;
    private CSVWriter csvWriter;
    private File path;
    private float[] prev = {0f,0f,0f};
    private File file;
    private Menu menu;
    private TextView stepView, levelView;
    private static final int ABOVE = 1;
    private static final int BELOW = 0;
    private static int CURRENT_STATE = 0;
    private static int PREVIOUS_STATE = BELOW;

    private long startTime;
    boolean SAMPLING_ACTIVE = true;
    private long streakStartTime;
    private long streakPrevTime;

    private User currentUser;

    public static final String TAG = HomeFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR = "color";

    // TODO: Rename and change types of parameters
    private int color;

    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }

        currentUser = MainActivity.getCurrentUser();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {



        View rootView = inflater.inflate(R.layout.home, container, false);

        // Progress Circle
        mCustomProgressBar5 = (CircleProgressBar) rootView.findViewById(R.id.custom_progress5);

        mCustomProgressBar5.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return progress + "%";
            }
        });

        //Logout button
        Button logoutButton = (Button) rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                mRef.child("users").child(userId).setValue(currentUser);
                mAuth.signOut();
                Toast.makeText(getContext(), "Logging out.", Toast.LENGTH_SHORT).show();
                MainActivity.ma.endEverything();
            }
        });


        //recyclerView = (RecyclerView) rootView.findViewById(R.id.count);
        stepView = rootView.findViewById(R.id.count);
        levelView = rootView.findViewById(R.id.level);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        //stepView = findViewById(R.id.count);
        path =  getActivity().getExternalFilesDir(null);
        file = new File(path, "raghu1.csv");
        try {
            csvWriter = new CSVWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        streakPrevTime = System.currentTimeMillis() - 500;

        return rootView;
    }

    private void simulateProgress(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mCustomProgressBar5.setProgress(progress);
            }
        });

        //animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Progress Circle
        //testing
        mCustomProgressBar5.setProgress(80);
        mCustomProgressBar5.setProgress(mCustomProgressBar5.getProgress());
        simulateProgress(0, mCustomProgressBar5.getProgress());

        // Total Steps
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.instrumentation) {
            SAMPLING_ACTIVE = true;
            startTime = System.currentTimeMillis();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int tempStep = 1;
    int level = 1;

    private void handleEvent(SensorEvent event) {
        prev = lowPassFilter(event.values,prev);
        Accelerometer raw = new Accelerometer(event.values);
        Accelerometer data = new Accelerometer(prev);
        StringBuilder text = new StringBuilder();
        text.append("X: " + data.X);
        text.append("Y: " + data.Y);
        text.append("Z: " + data.Z);
        text.append("R: " + data.R);
        if(data.R > 10.5f){
            CURRENT_STATE = ABOVE;
            if(PREVIOUS_STATE != CURRENT_STATE) {
                streakStartTime = System.currentTimeMillis();
                if ((streakStartTime - streakPrevTime) <= 250f) {
                    streakPrevTime = System.currentTimeMillis();
                    return;
                }
                streakPrevTime = streakStartTime;
                Log.d("STATES:", "" + streakPrevTime + " " + streakStartTime);
                currentUser.addTotalSteps(1);
                ArrayList<String> emailTest = new ArrayList<String>();
                currentUser.setFriendEmails(emailTest);

            }
            PREVIOUS_STATE = CURRENT_STATE;
        }
        else if(data.R < 10.5f) {
            CURRENT_STATE = BELOW;
            PREVIOUS_STATE = CURRENT_STATE;
        }
        tempStep = currentUser.getTotalSteps();
        stepView.setText("" + currentUser.getTotalSteps());
        levelView.setText("Level: " + level);

        if ((tempStep + 1) % 10 == 0) {
            int progress = mCustomProgressBar5.getProgress();
            mCustomProgressBar5.setProgress(progress + 10);
            simulateProgress(progress, progress + 10);
            tempStep = 1;
        }

        if (mCustomProgressBar5.getProgress() >= 100) {
            level++;
            final Toast toast = Toast.makeText(getActivity(), "Leveled up to level " + level, Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1500);
            mCustomProgressBar5.setProgress(0);
        }
    }

    private float[] lowPassFilter(float[] input, float[] prev) {
        float ALPHA = 0.1f;
        if(input == null || prev == null) {
            return null;
        }
        for (int i=0; i< input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleEvent(event);
            if(SAMPLING_ACTIVE) {
                long now = System.currentTimeMillis();
                if (now >= startTime + 5000) {
                    SAMPLING_ACTIVE = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


    /**
     * Updates {@link RecyclerView} background color upon changing Bottom Navigation item.
     *
     * @param color to apply to {@link RecyclerView} background.
     */
    public void updateColor(int color) {
        recyclerView.setBackgroundColor(getLighterColor(color));
    }

    /**
     * Facade to return colors at 30% opacity.
     *
     * @param color
     * @return
     */
    private int getLighterColor(int color) {
        return Color.argb(30,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

}
