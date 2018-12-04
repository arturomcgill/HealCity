package edu.umd.arturomcgill.healcity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ShopFragment extends Fragment {

    public static final String TAG = ShopFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR = "color";

    // TODO: Rename and change types of parameters
    private int color;

    private RecyclerView recyclerView;

    private User currentUser;

    private HashMap<String, ShopItem> allItems = new HashMap<>();

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }

        currentUser = MainActivity.getCurrentUser();
//        currentUser.addPoints(5000);

        Log.i(TAG, "USER: POINTS: " + currentUser.getPoints() + " NAME: " + currentUser.getFirstName());

        fillShop();
    }

    private void fillShop() {
        allItems.put("Remove Ads", new ShopItem("Remove Ads", R.drawable.remove_ads, 10000, "Remove Ads"));
        allItems.put("$10 Zipcar Credit", new ShopItem("$10 Zipcar Credit", R.drawable.zipcar, 7500, "$10 Zipcar Credit"));
        allItems.put("$5 Metro Credit", new ShopItem("$5 Metro Credit", R.drawable.metro_card, 5000, "$5 Metro Credit"));
        allItems.put("50% off Apples", new ShopItem("50% off Apples", R.drawable.apple, 4000, "50% off Apples"));
        allItems.put("25% off Salad", new ShopItem("25% off Salad", R.drawable.salad, 3000, "25% off Salad"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.shop_ui, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.shop_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setBackgroundColor(getLighterColor(color));

        ShopAdapter adapter = new ShopAdapter(getContext(), currentUser, allItems, rootView);
        recyclerView.setAdapter(adapter);

        TextView userName = rootView.findViewById(R.id.user_first_name);
        TextView userPoints = rootView.findViewById(R.id.userPoints);

        userName.setText(currentUser.getFirstName());
        userPoints.setText("Points: " + currentUser.getPoints());

        return rootView;
    }


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
