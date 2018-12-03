package edu.umd.arturomcgill.healcity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class NearbyFragment extends Fragment {

    public static final String TAG = NearbyFragment.class.getSimpleName();
    public static final String eventURL = "https://www.eventbriteapi.com/v3/events/search/?location.address=Hyattsville&expand=venue&sort_by=date&token=74FTCKSNQEF2GVG6WR2T";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR = "color";

    // TODO: Rename and change types of parameters
    private int color;
    private RecyclerView mRecyclerView;
    private NearbyAdapter mAdapter;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }
        Log.i(TAG, "Nearby Fragment created");
        EventAsyncTask task = new EventAsyncTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_square, container, false);
        mRecyclerView = rootView.findViewById(R.id.fragment_square_recycler);
        mRecyclerView.setBackgroundColor(getLighterColor(color));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new NearbyAdapter(getContext(), new ArrayList<Event>());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    private class EventAsyncTask extends AsyncTask<URL, Void, ArrayList<Event>> {
        @Override
        protected ArrayList<Event> doInBackground(URL... urls) {
            HttpURLConnection httpURLConnection = null;
            String jsonResponse = "";
            try {
                httpURLConnection = (HttpURLConnection) new URL(eventURL).openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                jsonResponse = readStream(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return parseJSON(jsonResponse);
        }


        private String readStream(InputStream inputStream) {
            BufferedReader reader = null;
            StringBuilder data = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }
                }
            }
            return data.toString();
        }

        private ArrayList<Event> parseJSON(String response) {
            ArrayList<Event> events = new ArrayList<>();
            try {
                JSONObject base = new JSONObject(response);
                JSONArray eventsArray = base.getJSONArray("events");
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject n = eventsArray.getJSONObject(i);
                    JSONObject nameObject = n.getJSONObject("name");

                    String name = nameObject.getString("text");

                    JSONObject d = n.getJSONObject("description");
                    String description = d.getString("text");

                    JSONObject localtime = n.getJSONObject("start");
                    String time = localtime.getString("local");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
                    Date date = format.parse(time);
                    format = new SimpleDateFormat("MMMM dd yyyy hh:mm aaa");
                    time = format.format(date);
                    JSONObject venue = n.getJSONObject("venue");
                    JSONObject address = venue.getJSONObject("address");
                    double latitude = Double.parseDouble(address.getString("latitude"));
                    double longitude = Double.parseDouble(address.getString("longitude"));
                    String address1 = address.getString("localized_address_display");

                    Event e = new Event(name, time, description, longitude, latitude, address1);
                    events.add(e);
                }


            } catch (JSONException e) {
                Log.e(TAG, "JSONException");
            } finally {
                return events;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Event> eventArr) {
            if (eventArr == null) {
                return;
            }
            mAdapter.update(eventArr);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates {@link RecyclerView} background color upon changing Bottom Navigation item.
     *
     * @param color to apply to {@link RecyclerView} background.
     */
    public void updateColor(int color) {
        mRecyclerView.setBackgroundColor(getLighterColor(color));
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
