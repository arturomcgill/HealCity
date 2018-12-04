package edu.umd.arturomcgill.healcity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static edu.umd.arturomcgill.healcity.ShopFragment.TAG;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.SimpleItemVH> {

    //  Data
    private HashMap<String, ShopItem> allItems;
    private ArrayList<ShopItem> allShopItems = new ArrayList<>();

    private Context context;

    private User currentUser;

    private HashMap<String, ShopItem> userItems;

    private View rootView;

    public ShopAdapter(Context context, User currentUser, HashMap<String, ShopItem> allItems, View rootView) {
        this.context = context;
        this.currentUser = currentUser;
        this.allItems = allItems;
        this.rootView = rootView;

        //TODO create list of shop items user can buy. Store locally.
        //grab user's points. use that to determine if they can buy certain items
        // UI changes in item holders


        getShopItems();
    }

    private void getShopItems() {

        //Real shop items will come from DB

        allShopItems.addAll(allItems.values());
//        for (int i = 0; i < allShopItems.size(); i++) {
//
//        }
    }

    @Override
    public SimpleItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item_vh, parent, false);

        return new SimpleItemVH(v, currentUser);
    }

    @Override
    public void onBindViewHolder(SimpleItemVH holder, int position) {
        ShopItem shopItem = allShopItems.get(position);

//        Log.i(TAG, "BINDING ITEM NAME: " + shopItem.getName());

        //TODO description only loading off screen bug

        holder.name.setText(shopItem.getName());
        holder.cost.setText("Cost: " + shopItem.getCost());
        holder.description.setText("");

        holder.item = shopItem;

        holder.img.setImageResource(shopItem.getImgSrc());

        if (currentUser.getUpgradesPurchased().contains(shopItem.getName())) {
            holder.buy.setText("Purchased");
            holder.buy.setClickable(false);
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.holo_red_light));
        } else {
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.holo_green_light));
        }


    }

    @Override
    public int getItemCount() {
        return allShopItems.size();
    }

    protected class SimpleItemVH extends RecyclerView.ViewHolder {
        ShopItem item;
//        User currentUser;

        TextView name;
        TextView cost;
        TextView description;
        ImageView img;
        Button buy;

        TextView userPoints;

        public SimpleItemVH(View itemView, final User currentUser) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.shop_item_name);
            cost = (TextView) itemView.findViewById(R.id.shop_item_cost);
            description = (TextView) itemView.findViewById(R.id.shop_item_description);
            img = itemView.findViewById(R.id.shop_item_img);

            buy = itemView.findViewById(R.id.buy);


            userPoints = rootView.findViewById(R.id.userPoints);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser.getPoints() < item.getCost()) {
                        Toast.makeText(context, "Not enough points", Toast.LENGTH_LONG).show();
                    } else {
                        currentUser.setPoints(currentUser.getPoints() - item.getCost());
                        currentUser.addUpgradesPurchased(item.getName());
                        userPoints.setText("Points: " + currentUser.getPoints());

                        Log.i(TAG, "USER POINTS SHOP: " + currentUser.getPoints());

                        buy.setText("Purchased");
                        buy.setClickable(false);
                        buy.setBackgroundColor(context.getResources().getColor(R.color.holo_red_light));
                        Log.i(TAG, "PURCHASING");

                    }
                }
            });

        }

        public void setItem(ShopItem item) {
            this.item = item;
        }
    }
}
