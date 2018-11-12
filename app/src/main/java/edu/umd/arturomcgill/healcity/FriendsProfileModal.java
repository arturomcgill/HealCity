package edu.umd.arturomcgill.healcity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class FriendsProfileModal extends DialogFragment {

    private Friend friend;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        String name = getArguments().getString("name");
        int level = getArguments().getInt("level");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setIcon(R.drawable.ic_friends);
        builder.setTitle(name + ": Level " + String.valueOf(level));
        builder.setView(inflater.inflate(R.layout.friend_profile, null));
        //TODO: List of Friend achievements

        return builder.create();
    }
}
