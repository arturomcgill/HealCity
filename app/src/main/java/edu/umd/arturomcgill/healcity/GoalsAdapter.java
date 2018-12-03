package edu.umd.arturomcgill.healcity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.SimpleItemVH> {

    //  Data
    private List<Dessert> desserts = new ArrayList<>();

    private Context context;
    private User currentUser;

    public GoalsAdapter(Context context, User currentUser) {
        this.context = context;
        this.currentUser = currentUser;

        prepareDesserts();
    }

    private void prepareDesserts() {
//        String[] nameArray = context.getResources().getStringArray(R.array.dessert_names);
//        String[] descArray = context.getResources().getStringArray(R.array.dessert_descriptions);
        String[] nameArray = new String[5];
        String[] descArray = new String[5];
        nameArray[0] = "Adventuring Pedestrian";
        descArray[0] = "Walk 10 steps";
        nameArray[1] = "Pinch of Potassium";
        descArray[1] = "Eat a banana";
        nameArray[2] = "Mark the Park";
        descArray[2] = "Visit a Nearby Park";
        nameArray[3] = "Mass Transit Mass Savings";
        descArray[3] = "Use public transportation";
        nameArray[4] = "Never Refuse to Reuse";
        descArray[4] = "Recycle plastic";

        final int SIZE = nameArray.length;

        for (int i = 0; i < SIZE; i++) {
            Dessert dessert = new Dessert(
                    nameArray[i],
                    descArray[i]
            );

            desserts.add(dessert);
        }
    }

    @Override
    public SimpleItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goals, parent, false);

        return new SimpleItemVH(v, currentUser);
    }

    @Override
    public void onBindViewHolder(SimpleItemVH holder, int position) {
        Dessert dessert = desserts.get(position);

        holder.txtTitle.setText(dessert.getName());
        holder.txtDesc.setText(dessert.getDescription());

        if (holder.txtTitle.getText().equals("Adventuring Pedestrian")) {
            if (currentUser.getDailyGoals().get(0)) {
                holder.button.setText("DONE!");
                holder.button.setClickable(false);
                holder.button.setBackgroundColor(Color.parseColor("#6CCC3D"));
            }
        } else if (holder.txtTitle.getText().equals("Pinch of Potassium")) {
            if (currentUser.getDailyGoals().get(1)) {
                holder.button.setText("DONE!");
                holder.button.setClickable(false);
                holder.button.setBackgroundColor(Color.parseColor("#6CCC3D"));
            }
        } else if (holder.txtTitle.getText().equals("Mark the Park")) {
            if (currentUser.getDailyGoals().get(2)) {
                holder.button.setText("DONE!");
                holder.button.setClickable(false);
                holder.button.setBackgroundColor(Color.parseColor("#6CCC3D"));
            }
        } else if (holder.txtTitle.getText().equals("Mass Transit Mass Savings")) {
            if (currentUser.getDailyGoals().get(3)) {
                holder.button.setText("DONE!");
                holder.button.setClickable(false);
                holder.button.setBackgroundColor(Color.parseColor("#6CCC3D"));
            }
        } else { // Recycle
            if (currentUser.getDailyGoals().get(4)) {
                holder.button.setText("DONE!");
                holder.button.setClickable(false);
                holder.button.setBackgroundColor(Color.parseColor("#6CCC3D"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return desserts != null ? desserts.size() : 0;
    }

    protected class SimpleItemVH extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDesc;
        Button button;


        public SimpleItemVH(View itemView, final User currentUser) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.item_simplevh_txttitle);
            txtDesc = (TextView) itemView.findViewById(R.id.item_simplevh_txtdescription);

            button = itemView.findViewById(R.id.button);



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtTitle.getText().equals("Adventuring Pedestrian")) {
                        if (currentUser.getTotalSteps() >= 10) {
                            currentUser.finishGoal("Adventuring Pedestrian");
                            Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
                            HomeFragment.addProgress(16);
                            currentUser.setPercentage(currentUser.getPercentage() + 16);
                            button.setText("DONE!");
                            button.setClickable(false);
                            button.setBackgroundColor(Color.parseColor("#6CCC3D"));

                            if (currentUser.getPercentage() >= 100) {
                                currentUser.setLevel(currentUser.getLevel() + 1);
                                final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
                                toast.show();
                                currentUser.setPercentage(currentUser.getPercentage() - 100);
                            }
                        } else {
                            Toast.makeText(context, "Insufficient Steps. Only " + currentUser.getTotalSteps() + " steps taken.", Toast.LENGTH_SHORT).show();
                        }

                    } else if (txtTitle.getText().equals("Pinch of Potassium")) {
                        currentUser.finishGoal("Pinch of Potassium");
                        Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
                        HomeFragment.addProgress(16);
                        currentUser.setPercentage(currentUser.getPercentage() + 16);
                        button.setText("DONE!");
                        button.setClickable(false);
                        button.setBackgroundColor(Color.parseColor("#6CCC3D"));

                        if (currentUser.getPercentage() >= 100) {
                            currentUser.setLevel(currentUser.getLevel() + 1);
                            final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
                            toast.show();
                            currentUser.setPercentage(currentUser.getPercentage() - 100);
                        }
                    } else if (txtTitle.getText().equals("Mark the Park")) {

//                        currentUser.finishGoal("Mark the Park");
//                        Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
//                        HomeFragment.addProgress(16);
//                        currentUser.setPercentage(currentUser.getPercentage() + 16);
//                        button.setText("DONE!");
//                        button.setClickable(false);
//                        button.setBackgroundColor(Color.parseColor("#6CCC3D"));
//
//                        if (currentUser.getPercentage() >= 100) {
//                            currentUser.setLevel(currentUser.getLevel() + 1);
//                            final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
//                            toast.show();
//                            currentUser.setPercentage(currentUser.getPercentage() - 100);
//                        }


                        if (currentUser.getLatitude() > 30 && currentUser.getLatitude() < 45 && currentUser.getLongitude() > -80 && currentUser.getLongitude() < -70) {
                            currentUser.finishGoal("Mark the Park");
                            Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
                            HomeFragment.addProgress(16);
                            currentUser.setPercentage(currentUser.getPercentage() + 16);
                            button.setText("DONE!");
                            button.setClickable(false);
                            button.setBackgroundColor(Color.parseColor("#6CCC3D"));

                            if (currentUser.getPercentage() >= 100) {
                                currentUser.setLevel(currentUser.getLevel() + 1);
                                final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
                                toast.show();
                                currentUser.setPercentage(currentUser.getPercentage() - 100);
                            }
                        } else {
                            Toast.makeText(context, "You are not at a park.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (txtTitle.getText().equals("Mass Transit Mass Savings")) {

//                        currentUser.finishGoal("Mass Transit Mass Savings");
//                        Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
//                        HomeFragment.addProgress(16);
//                        currentUser.setPercentage(currentUser.getPercentage() + 16);
//                        button.setText("DONE!");
//                        button.setClickable(false);
//                        button.setBackgroundColor(Color.parseColor("#6CCC3D"));
//
//                        if (currentUser.getPercentage() >= 100) {
//                            currentUser.setLevel(currentUser.getLevel() + 1);
//                            final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
//                            toast.show();
//                            currentUser.setPercentage(currentUser.getPercentage() - 100);
//                        }


                        if (currentUser.getLatitude() > 30 && currentUser.getLatitude() < 45 && currentUser.getLongitude() > -80 && currentUser.getLongitude() < -70) {
                            currentUser.finishGoal("Mass Transit Mass Savings");
                            Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
                            HomeFragment.addProgress(16);
                            currentUser.setPercentage(currentUser.getPercentage() + 16);
                            button.setText("DONE!");
                            button.setClickable(false);
                            button.setBackgroundColor(Color.parseColor("#6CCC3D"));

                            if (currentUser.getPercentage() >= 100) {
                                currentUser.setLevel(currentUser.getLevel() + 1);
                                final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
                                toast.show();
                                currentUser.setPercentage(currentUser.getPercentage() - 100);
                            }
                        } else {
                            Toast.makeText(context, "You are not currently at a public transportation area", Toast.LENGTH_SHORT).show();
                        }
                    } else { // Recycle
                        currentUser.finishGoal("Recycle plastic");
                        Toast.makeText(context, "+18% EXP", Toast.LENGTH_SHORT).show();
                        HomeFragment.addProgress(16);
                        currentUser.setPercentage(currentUser.getPercentage() + 16);
                        button.setText("DONE!");
                        button.setClickable(false);
                        button.setBackgroundColor(Color.parseColor("#6CCC3D"));

                        if (currentUser.getPercentage() >= 100) {

                            currentUser.setLevel(currentUser.getLevel() + 1);
                            final Toast toast = Toast.makeText(context, "Leveled up to level " + currentUser.getLevel(), Toast.LENGTH_SHORT);
                            toast.show();
                            currentUser.setPercentage(currentUser.getPercentage() - 100);
                        }
                    }


                    //Toast.makeText(context, "" + HomeFragment.getProgress(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
