package edu.umd.arturomcgill.healcity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.umd.arturomcgill.healcity.FriendsFragment.TAG;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.SimpleItemVH> {

    //  Data

    private User currentUser;
    private ArrayList<User> allUsers;
    private HashMap<String, User> allUsersMap = new HashMap<>();
    private ArrayList<String> friendsEmails;
    private ArrayList<User> friends = new ArrayList<>();

    private Context context;
    private  FragmentManager fragmentManager;

    public FriendsAdapter(Context context, FragmentManager fragmentManager, User currentUser) {
        this.context = context;
        this.fragmentManager = fragmentManager;

        this.currentUser = currentUser;
        this.allUsers = MainActivity.getAllUsers();

        //Mapping of email to user for all
        for (int i = 0; i < allUsers.size(); i++) {
            allUsersMap.put(allUsers.get(i).getEmail(), allUsers.get(i));
        }

        friendsEmails = currentUser.getFriendEmails();

        getFriendsData();
    }

    public void getFriendsData() {
        //Get friends from DB. For now, from array.

        Log.i(TAG, "GETTING FRIEND DATA");
        Log.i(TAG, friendsEmails.toString());

        friends.clear();

        for (int i = 0; i < friendsEmails.size(); i++) {
            friends.add(allUsersMap.get(friendsEmails.get(i)));
        }


    }

    @Override
    public SimpleItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_vh, parent, false);


        return new SimpleItemVH(v, fragmentManager);
    }

    @Override
    public void onBindViewHolder(SimpleItemVH holder, int position) {
        User friend = friends.get(position);

//        holder.friendLevel.setText("Level: " + friend.get);
        holder.friendEmail.setText(friend.getEmail());
        holder.friend = friend;
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    protected static class SimpleItemVH extends RecyclerView.ViewHolder {
        User friend;
        TextView friendEmail;
//        TextView friendLevel;

        public SimpleItemVH(View itemView, final FragmentManager fragmentManager) {
            super(itemView);

            friendEmail = (TextView) itemView.findViewById(R.id.friend_name);
//            friendLevel = (TextView) itemView.findViewById(R.id.friend_level);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment friendProfile = new FriendsProfileModal();
                    Bundle friendInfo = new Bundle();

                    String name = friend.getEmail();
//                    int level = friend.getLevel();


                    friendInfo.putString("name", name);
//                    friendInfo.putInt("level", level);
//                    friendInfo.putStringArrayList("achievements", friend.getAchievements());
                    //Just need name (or id) to do query of DB
                    // TODO: OR send Friend object to fragment


                    friendProfile.setArguments(friendInfo);

                    //TODO Change to user profile popup
                    friendProfile.show(fragmentManager, "friend");
                }
            });

        }


    }
}
