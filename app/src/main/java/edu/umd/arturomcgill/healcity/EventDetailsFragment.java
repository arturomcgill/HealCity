package edu.umd.arturomcgill.healcity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailsFragment extends Fragment {

    public static final String TAG = EventDetailsFragment.class.getSimpleName();
    private User currentUser;
    String name, time, description, address1;
    double longitude, latitude;
    TextView textView1, textView2, textView3,  textView4;
    Button button;
    private FusedLocationProviderClient mFusedLocationClient;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = MainActivity.getCurrentUser();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        name = getArguments().getString("name");
        description = getArguments().getString("description");
        time = getArguments().getString("time");
        address1 = getArguments().getString("address1");
        longitude = getArguments().getDouble("longitude");
        latitude = getArguments().getDouble("latitude");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.eventdet, container, false);

        textView1 = rootView.findViewById(R.id.name);
        textView2 = rootView.findViewById(R.id.description);
        textView3 = rootView.findViewById(R.id.address1);
        textView4 = rootView.findViewById(R.id.time);

        textView1.setText(name);
        textView2.setText(description.trim().replaceAll(" +", " "));
        textView3.setText(address1);
        textView4.setText(time);

        button = rootView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            float distance;
                            Location loc1 = new Location("");
                            loc1.setLatitude(latitude);
                            loc1.setLongitude(longitude);
                            Log.i(TAG, "Got a location");

                            distance = location.distanceTo(loc1);
                            if (distance < 150) {
                                Toast.makeText(getContext(), "You just gained volunteer hours!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Same location");
                                currentUser.addlifetimeVolunteering(2);
                                Log.d(TAG, "Volunteering" + currentUser.getLifetimeVolunteering());



                                int xp = 2 * 200;
                                currentUser.setPercentage(currentUser.getPercentage() + xp % 100);
                                HomeFragment.addProgress(currentUser.getPercentage());
                                int level = xp/100;
                                currentUser.setLevel(currentUser.getLevel() + level);

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                Date today = null;
                                try {
                                    String string = format.format(new Date());
                                    today = format.parse(string);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                currentUser.addPoints(xp);
                                currentUser.addDailyXPToDate(today, xp);
                            }
                            else {
                                Toast.makeText(getContext(), "You are currently not at this location.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });



            }
        });

        return rootView;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

}
