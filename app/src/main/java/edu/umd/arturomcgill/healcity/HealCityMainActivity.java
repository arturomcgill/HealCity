package edu.umd.arturomcgill.healcity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.animation.ValueAnimator;

// -------------------

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HealCityMainActivity extends AppCompatActivity implements SensorEventListener {

    private CircleProgressBar mCustomProgressBar;


    private SensorManager sensorManager;
    private CSVWriter csvWriter;
    private File path;
    private float[] prev = {0f,0f,0f};
    private File file;
    private Menu menu;
    private TextView stepView;
    private int stepCount = 0;
    private static final int ABOVE = 1;
    private static final int BELOW = 0;
    private static int CURRENT_STATE = 0;
    private static int PREVIOUS_STATE = BELOW;

    private long startTime;
    boolean SAMPLING_ACTIVE = true;
    private long streakStartTime;
    private long streakPrevTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Progress Circle
        mCustomProgressBar = (CircleProgressBar) findViewById(R.id.custom_progress5);

        mCustomProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return progress + "%";
            }
        });

        // Total Steps
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepView = findViewById(R.id.count);
        path =  this.getExternalFilesDir(null);
        file = new File(path, "raghu1.csv");
        try {
            csvWriter = new CSVWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        streakPrevTime = System.currentTimeMillis() - 500;
    }


    private void simulateProgress() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 54);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mCustomProgressBar.setProgress(progress);
            }
        });

        //animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(4000);
        animator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Progress Circle
        simulateProgress();
        mCustomProgressBar.setProgress(54);
        Log.e("PROGRESS===============", String.valueOf(mCustomProgressBar.getProgress()));
        Log.e("STEPS===============", String.valueOf(stepCount));

        // Total Steps
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                stepCount++;
            }
            PREVIOUS_STATE = CURRENT_STATE;
        }
        else if(data.R < 10.5f) {
            CURRENT_STATE = BELOW;
            PREVIOUS_STATE = CURRENT_STATE;
        }
        stepView.setText(""+(stepCount));;
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
}
