package edu.umd.arturomcgill.healcity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// -------------------
// -------------------

public class MainActivity extends AppCompatActivity {

    private final int[] colors = {R.color.bottomtab_0, R.color.bottomtab_4, R.color.bottomtab_1, R.color.bottomtab_2, R.color.colorPrimaryNight};
    private Toolbar toolbar;
    private NoSwipePager viewPager;
    private AHBottomNavigation bottomNavigation;
    private BottomBarAdapter pagerAdapter;

    private FirebaseAuth mAuth;

    public static MainActivity ma;
    private Bundle bundle;

    private static ArrayList<User> allUsers;

    private final String TAG = "FirebaseDebugging";


    private boolean notificationVisible = false;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }



    @Override
    protected void onPause()
    {
        super.onPause();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //Will only happen if you're kicked out of main activity due to not being logged in
        if(user != null)
        {
            String userId = user.getUid();
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            //   mRef.child("users").child(userId).setValue(currentUser);

            mRef.removeValue();
            for (int i = 0; i < allUsers.size(); i++) {
                //Update db
                User u = allUsers.get(i);
                mRef.child("users").child(u.getUid()).setValue(u);
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "In MainActivity.onCreate()");
        //Get the step count from database
        ma = this;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        // getActivity().finish();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users");
        allUsers = new ArrayList<User>();
        this.bundle = savedInstanceState;

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User u = userSnapshot.getValue(User.class);
                    allUsers.add(u);
                }

                onCreateAux(bundle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static User queryForUser(String userId)
    {
        for(int i = 0; i < allUsers.size(); i++)
        {
            if(userId.equals(allUsers.get(i).getUid()))
                return allUsers.get(i);
        }

        return null;
    }

    public static User getCurrentUser()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();

        return queryForUser(userId);
    }

    public static ArrayList<User> getAllUsers()
    {
        return allUsers;
    }

    private boolean userDoesNotExist(FirebaseUser user)
    {
        final String userId = user.getUid();
        return (queryForUser(userId) == null);
    }

    protected void onCreateAux(Bundle savedInstanceState)
    {
        Log.i(TAG, "In onCreateAux");
        ma = this;
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        //If not logged in, go to log in activity
        if(user == null)
        {
            Log.i(TAG, "Firebase user is null");
            Intent intent = new Intent(MainActivity.this, HealCityLoginActivity.class);
            startActivity(intent);
        }
        //Firebase user exists, but corresponding user in database does not
        else if(userDoesNotExist(user))
        {
            Log.i(TAG, "Database user is null");
            mAuth.signOut();
            HealCityLoginActivity.endEverything();
        }
        else
        {
            try

            {
                HealCityLoginActivity.hcla.finish();
            }
            catch(NullPointerException npe)
            {
                //Activity was already killed by android, no need to kill it.
            }
        }

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        setContentView(R.layout.activity_main);

        setupViewPager();

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setupBottomNavBehaviors();
        setupBottomNavStyle();

        createFakeNotification();

        addBottomNavigationItems();
        bottomNavigation.setCurrentItem(0);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                fragment.updateColor(ContextCompat.getColor(MainActivity.this, colors[position]));

                if (!wasSelected)
                    viewPager.setCurrentItem(position);

                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (notificationVisible && position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);

                return true;
            }
        });

    }

    private void setupViewPager() {
        viewPager = (NoSwipePager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(createHomeFragment(R.color.bottomtab_0));
        pagerAdapter.addFragments(createFriendsFragment(R.color.bottomtab_4));
        pagerAdapter.addFragments(createGoalsFragment(R.color.bottomtab_1));
        pagerAdapter.addFragments(createNearbyFragment(R.color.bottomtab_2));
        pagerAdapter.addFragments(createShopFragment(R.color.colorPrimaryNight));

        viewPager.setAdapter(pagerAdapter);
    }

    @NonNull
    private HomeFragment createHomeFragment(int color) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(passFragmentArguments(fetchColor(color)));
        return fragment;
    }

    @NonNull
    private FriendsFragment createFriendsFragment(int color) {
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(passFragmentArguments(fetchColor(color)));
        return fragment;
    }

    @NonNull
    private GoalsFragment createGoalsFragment(int color) {
        GoalsFragment fragment = new GoalsFragment();
        fragment.setArguments(passFragmentArguments(fetchColor(color)));
        return fragment;
    }

    @NonNull
    private NearbyFragment createNearbyFragment(int color) {
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(passFragmentArguments(fetchColor(color)));
        return fragment;
    }

    @NonNull
    private ShopFragment createShopFragment(int color) {
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(passFragmentArguments(fetchColor(color)));
        return fragment;
    }

    @NonNull
    private Bundle passFragmentArguments(int color) {
        Bundle bundle = new Bundle();
        bundle.putInt("color", color);
        return bundle;
    }

    private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.

                bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);

                notificationVisible = true;
            }
        }, 1000);
    }


    public void setupBottomNavBehaviors() {
        bottomNavigation.setBehaviorTranslationEnabled(false);

        /*
        Before enabling this. Change MainActivity theme to MyTheme.TranslucentNavigation in
        AndroidManifest.

        Warning: Toolbar Clipping might occur. Solve this by wrapping it in a LinearLayout with a top
        View of 24dp (status bar size) height.
         */
        bottomNavigation.setTranslucentNavigationEnabled(false);
    }

    /**
     * Adds styling properties to {@link AHBottomNavigation}
     */
    private void setupBottomNavStyle() {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.

        Will not be visible if setColored(true) and default current item is set.
         */
        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(fetchColor(R.color.bottomtab_0));
        bottomNavigation.setInactiveColor(fetchColor(R.color.bottomtab_item_resting));

        // Colors for selected (active) and non-selected items.
        bottomNavigation.setColoredModeColors(Color.WHITE,
                fetchColor(R.color.bottomtab_item_resting));

        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }


    /**
     * Adds (items) {@link AHBottomNavigationItem} to {@link AHBottomNavigation}
     * Also assigns a distinct color to each Bottom Navigation item, used for the color ripple.
     */
    private void addBottomNavigationItems() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_home, colors[0]);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_friends, colors[1]);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_goals, colors[2]);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_nearby, colors[3]);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_shop, colors[4]);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
    }


    /**
     * Simple facade to fetch color resource, so I avoid writing a huge line every time.
     *
     * @param color to fetch
     * @return int color value.
     */
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }
}

