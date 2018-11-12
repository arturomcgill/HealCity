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
import java.util.List;

import static edu.umd.arturomcgill.healcity.FriendsFragment.TAG;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.SimpleItemVH> {

    //  Data

    private List<Friend> friends = new ArrayList<>();

    private Context context;
    private  FragmentManager fragmentManager;

    public FriendsAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        getFriendsData();
    }

    private void getFriendsData() {
        //Get friends from DB. For now, from array.

        for (int i = 0; i < 10; i++) {
            friends.add(new Friend("Steve", i, i*100));
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
        Friend f = friends.get(position);

        holder.friendLevel.setText("Level " + f.getLevel());
        holder.friendName.setText(f.getName());
        holder.friend = f;
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    protected static class SimpleItemVH extends RecyclerView.ViewHolder {
        Friend friend;
        TextView friendName;
        TextView friendLevel;

        public SimpleItemVH(View itemView, final FragmentManager fragmentManager) {
            super(itemView);

            friendName = (TextView) itemView.findViewById(R.id.friend_name);
            friendLevel = (TextView) itemView.findViewById(R.id.friend_level);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment friendProfile = new FriendsProfileModal();
                    Bundle friendInfo = new Bundle();

                    String name = friend.getName();
                    int level = friend.getLevel();


                    friendInfo.putString("name", name);
                    friendInfo.putInt("level", level);
                    friendInfo.putStringArrayList("achievements", friend.getAchievements());
                    //Just need name (or id) to do query of DB
                    // TODO: OR send Friend object to fragment


                    friendProfile.setArguments(friendInfo);

                    friendProfile.show(fragmentManager, "friend");
                }
            });

        }


    }
}
