package edu.umd.arturomcgill.healcity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static edu.umd.arturomcgill.healcity.ShopFragment.TAG;

public class FriendsFragment extends Fragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR = "color";

    // TODO: Rename and change types of parameters
    private int color;

    private RecyclerView recyclerView;

    private User currentUser;

    //mimic db call for user list for now
    private ArrayList<String> allUsersEmails = new ArrayList<>();
    private ArrayList<User> allUsers;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }

        currentUser = MainActivity.getCurrentUser();
        allUsers = MainActivity.getAllUsers();

        fillFriendsList();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        Log.i(TAG, mDatabase.child("users").toString());



    }

    private void fillFriendsList() {
        //TODO get list from DB OR do a query search in DB

//        currentUser.resetFriendEmails();

//        Log.i(TAG, "ALL USERS");
        for (int i = 0; i < allUsers.size(); i++) {
//            Log.i(TAG, "EMAIL: " + allUsers.get(i).getEmail());
            allUsersEmails.add(allUsers.get(i).getEmail());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.friend_ui, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setBackgroundColor(getLighterColor(color));

        final FriendsAdapter adapter = new FriendsAdapter(getContext(), getFragmentManager(), currentUser);
        recyclerView.setAdapter(adapter);


        final Button addFriend = rootView.findViewById(R.id.addFriend);
        final TextView friendSearch = rootView.findViewById(R.id.searchFriend);

        friendSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    addFriend.performClick();
                    return true;
                }
                return false;
            }
        });
//        Log.i(TAG, "USER POINTS SHOP: " + currentUser.getPoints());

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendSearchInput = friendSearch.getText().toString();

                if (currentUser.getFriendEmails().contains(friendSearchInput)) {
                    Toast.makeText(getContext(), friendSearchInput + " is already your friend", Toast.LENGTH_LONG).show();
                } else {
                    //TODO check with DB list or with query
                    if (allUsersEmails.contains(friendSearchInput)) {
                        Toast.makeText(getContext(), "Added " + friendSearchInput, Toast.LENGTH_LONG).show();
                        currentUser.addFriendEmail(friendSearchInput);

                        adapter.getFriendsData();
                        adapter.notifyDataSetChanged();
//                        adapter.notifyItemInserted(currentUser.getFriendEmails().size() - 1);

                    } else {
                        Toast.makeText(getContext(), "Could not find user with email " + friendSearchInput, Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

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
