 package edu.umd.arturomcgill.healcity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventList;
    public static final String TAG = NearbyAdapter.class.getSimpleName();

    public NearbyAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    public void update(ArrayList<Event> e) {
        this.eventList = e;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_event, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Event event = eventList.get(i);
        viewHolder.textName.setText(event.getName());
        viewHolder.textTime.setText(event.getTime());
        viewHolder.textDescription.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textDescription, textTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.main_name);
            textDescription = itemView.findViewById(R.id.main_description);
            textTime = itemView.findViewById(R.id.main_time);


        }
    }
}
