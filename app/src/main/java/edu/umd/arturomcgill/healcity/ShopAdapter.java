package edu.umd.arturomcgill.healcity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.SimpleItemVH> {

    //  Data
    private List<ShopItem> shopItems = new ArrayList<>();

    private Context context;

    public ShopAdapter(Context context) {
        this.context = context;
        getShopItems();
    }

    private void getShopItems() {

        //Real shop items will come from DB

        for (int i = 0; i < 10; i++) {
            shopItems.add(new ShopItem("Item " + i, i*10, "cool discount or something"));
        }
    }

    @Override
    public SimpleItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item_vh, parent, false);

        return new SimpleItemVH(v);
    }

    @Override
    public void onBindViewHolder(SimpleItemVH holder, int position) {
        ShopItem shopItem = shopItems.get(position);

        //TODO description only loading off screen bug

        holder.name.setText(shopItem.getName());
        holder.cost.setText("Cost: " + shopItem.getCost());
        holder.description.setText(shopItem.getDescription());

        holder.item = shopItem;

    }

    @Override
    public int getItemCount() {
        return shopItems != null ? shopItems.size() : 0;
    }

    protected static class SimpleItemVH extends RecyclerView.ViewHolder {
        ShopItem item;

        TextView name;
        TextView cost;
        TextView description;

        public SimpleItemVH(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.shop_item_name);
            cost = (TextView) itemView.findViewById(R.id.shop_item_cost);
            description = (TextView) itemView.findViewById(R.id.shop_item_description);
        }
    }
}
