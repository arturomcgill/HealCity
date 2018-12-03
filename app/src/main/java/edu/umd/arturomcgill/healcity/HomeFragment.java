package edu.umd.arturomcgill.healcity;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import static android.content.Context.SENSOR_SERVICE;

// -------------------

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class HomeFragment extends Fragment implements SensorEventListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView lat, lng;
    private Button updateLocation, seeMap;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;


    // --------------------------
    public static CircleProgressBar mCustomProgressBar5;


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
    private boolean isFABOpen = false;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

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

    public static int getProgress() {
        return mCustomProgressBar5.getProgress();
    }

    public static void addProgress(int i) {
        int progress = mCustomProgressBar5.getProgress();
        mCustomProgressBar5.setProgress(progress + 10);
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
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            displayLocation();
            Log.d("onStart ", "mGoogleApiClient is built");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {



        View rootView = inflater.inflate(R.layout.home_with_profile_button, container, false);

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

        ImageButton profileButton = (ImageButton) rootView.findViewById(R.id.profile_button);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.coordinator, new ProfileFragment());
//                fragmentTransaction.commit();
                Intent intent = new Intent(getActivity(), Profile.class);
                intent.putExtra("currentUser", "true");
                startActivity(intent);
            }
        });

        View content = rootView.findViewById(R.id.content);


        FloatingActionButton fab = (FloatingActionButton) content.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) content.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) content.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) content.findViewById(R.id.fab3);
        closeFABMenu();
        fab.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {


                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogActivity.class);
                intent.putExtra("type", "fruit");
                startActivity(intent);
            }

        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogActivity.class);
                intent.putExtra("type", "volunteer");
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogActivity.class);
                intent.putExtra("type", "transportation");
                startActivity(intent);
            }
        });






        // ------------------------------------------

        //updateLocation = (Button) findViewById(R.id.buttonLocationUpdates);
        seeMap = (Button) rootView.findViewById(R.id.seeMap);
        lat = (TextView) rootView.findViewById(R.id.latitude);
        lng = (TextView) rootView.findViewById(R.id.longitude);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
//        updateLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                togglePeriodLocationUpdates();
//
//            }
//        });

        seeMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return rootView;
    }

//    private void createTestUser(){
//
//        User user = MainActivity.getCurrentUser();
//        Date today = null;
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            String string = format.format(new Date());
//            today = format.parse(string);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        HashMap<Date, Integer> fruits = new HashMap<Date, Integer>();
//        fruits.put(today, 50);
//        user.setFruitsVeggies(fruits);
//        user.setlifetimeParks(10);
//
//        ArrayList<String> achievements = new ArrayList<String>();
//        achievements.add("10 fruits and veggies");
//        achievements.add("50 fruits and veggies");
//        achievements.add("100 fruits and veggies");
//        achievements.add("500 fruits and veggies");
//
//        user.setProfilePhoto(BitmapFactory.decodeResource(getResources(),R.drawable.stickfigure));
//
//        user.setLifetimeAchievements(achievements);
//    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
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
        //mCustomProgressBar5.setProgress(80);
        mCustomProgressBar5.setProgress(currentUser.getPercentage());
        simulateProgress(0, mCustomProgressBar5.getProgress());

        // Total Steps
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        startTime = System.currentTimeMillis();

        //--------------------
        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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
    private void handleEvent(SensorEvent event) {
        int level = currentUser.getLevel();

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
            currentUser.setPercentage(progress + 10);
            simulateProgress(progress, progress + 10);
            tempStep = 1;
        }

        if (mCustomProgressBar5.getProgress() >= 100 && this.isVisible()) {
            currentUser.setLevel(currentUser.getLevel() + 1);
            final Toast toast = Toast.makeText(getActivity(), "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1500);
            mCustomProgressBar5.setProgress(currentUser.getPercentage() - 100);
            currentUser.setPercentage(currentUser.getPercentage() - 100);
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

    private void displayLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }else{
            //Do Your Stuff

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("LastLocation ", "found");
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            lat.setText("Latitude: " + latitude + "");
            lng.setText("Longitude: " + longitude + "");
            currentUser.setLatitude(latitude);
            currentUser.setLongitude(longitude);

        } else {
            currentUser.setLatitude(0.0);
            currentUser.setLongitude(0.0);
            lat.setText("Latitude: 0.0");
            lng.setText("Longitude: 0.0");
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        Log.d("GoogleApiClient", " is built");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        Log.d("LocationRequest", " is created");
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }


    protected void startLocationUpdates() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }else{
            //Do Your Stuff

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("Location was ", "updates");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }




    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getActivity().getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

}
