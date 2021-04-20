package capstone.techmatrix.beacondetector.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.activities.QDealsProdDetails_Activity;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.utils.Constants;

import java.util.List;


public class QdUserWishlistAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Product> productList;

    Product product;

    public QdUserWishlistAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.qdeals_user_wishlist, null);
        holder.title = rowView.findViewById(R.id.title);
        holder.price = rowView.findViewById(R.id.price);
        holder.remove = rowView.findViewById(R.id.remove);
        holder.img = rowView.findViewById(R.id.img);
        holder.title.setText(productList.get(position).getName());
        holder.price.setText(productList.get(position).getPrice_range());

        // Product Item Click
        holder.itemLay = rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QDealsProdDetails_Activity.class);
                intent.putExtra("ProductId", productList.get(position).getId());
                context.startActivity(intent);
            }
        });


        return rowView;
    }

    public class Holder {
        RelativeLayout itemLay;
        TextView title, price;
        ImageView remove,img;
    }
}
