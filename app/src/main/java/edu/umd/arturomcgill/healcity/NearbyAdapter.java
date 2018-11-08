package edu.umd.arturomcgill.healcity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.SimpleItemVH> {

    //  Data
    private List<String> list = new ArrayList<>();

    private Context context;

    public NearbyAdapter(Context context) {
        this.context = context;
        prepareRandom();
    }

    private void prepareRandom() {
        String[] nameArray = context.getResources().getStringArray(R.array.dessert_names);

        final int SIZE = nameArray.length;

        for (int i = 0; i < SIZE; i++) {
            list.add("Nearby " + i);
        }
    }

    @Override
    public SimpleItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simplevh, parent, false);

        return new SimpleItemVH(v);
    }

    @Override
    public void onBindViewHolder(SimpleItemVH holder, int position) {
        String s = list.get(position);
        holder.txtTitle.setText(s);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    protected static class SimpleItemVH extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public SimpleItemVH(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.item_simplevh_txttitle);
        }
    }
}
