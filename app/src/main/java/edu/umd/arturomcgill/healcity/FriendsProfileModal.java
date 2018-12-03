package edu.umd.arturomcgill.healcity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class FriendsProfileModal extends DialogFragment {

    private User friend;

    private RecyclerView recyclerView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        String name = getArguments().getString("name");
        int level = getArguments().getInt("level");
        ArrayList<String> achievements = getArguments().getStringArrayList("achievements");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View friendProfile = inflater.inflate(R.layout.friend_profile, null);

        //add recycler view

        recyclerView = (RecyclerView) friendProfile.findViewById(R.id.friend_achievements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));





        builder.setIcon(R.drawable.ic_friends);
        builder.setTitle(name + " Level: " + level);
        builder.setView(friendProfile);
        //TODO: List of Friend achievements

        return builder.create();
    }
}
