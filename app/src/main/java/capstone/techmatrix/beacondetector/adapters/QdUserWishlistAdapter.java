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



        if(productList.get(position).getName().equalsIgnoreCase("Iphone 6S")) {
            holder.img.setImageResource(R.drawable.iphone6s);
        }
        else if(productList.get(position).getName().equalsIgnoreCase("Iphone 7")) {
            holder.img.setImageResource(R.drawable.iphone7);
        }else if(productList.get(position).getName().equalsIgnoreCase("Iphone 6")) {
            holder.img.setImageResource(R.drawable.iphone6);
        }else if(productList.get(position).getName().equalsIgnoreCase("Iphone 6S Plus")) {
            holder.img.setImageResource(R.drawable.iphone6splus);
        }else if(productList.get(position).getName().equalsIgnoreCase("Iphone 7 Plus")) {
            holder.img.setImageResource(R.drawable.iphone7plus);
        }
        else if(productList.get(position).getName().equalsIgnoreCase("Galaxy S7 Edge")) {
            holder.img.setImageResource(R.drawable.galaxys7edge);
        }else if(productList.get(position).getName().equalsIgnoreCase("Galaxy J5")) {
            holder.img.setImageResource(R.drawable.galaxyj5);
        }else if(productList.get(position).getName().equalsIgnoreCase("Galaxy J7")) {
            holder.img.setImageResource(R.drawable.galaxyj7);
        }else if(productList.get(position).getName().equalsIgnoreCase("Galaxy Grand Prime")) {
            holder.img.setImageResource(R.drawable.grandprime);
        }else if(productList.get(position).getName().equalsIgnoreCase("Note 4")) {
            holder.img.setImageResource(R.drawable.samsungnote4);
        }
        else if(productList.get(position).getName().equalsIgnoreCase("Dell Inspiron Core")) {
            holder.img.setImageResource(R.drawable.dellinspiron);
        }else if(productList.get(position).getName().equalsIgnoreCase("Dell Inspiron 11")) {
            holder.img.setImageResource(R.drawable.dell11);
        }
        else if(productList.get(position).getName().equalsIgnoreCase("Satellite Pro")) {
            holder.img.setImageResource(R.drawable.satpro);
        }else if(productList.get(position).getName().equalsIgnoreCase("Satellite P50")) {
            holder.img.setImageResource(R.drawable.satp50);
        }

        // Wish List Item Click
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove Item From Wish List
                DB_Handler db_handler = new DB_Handler(context);
                SessionManager sessionManager = new SessionManager(context);
                if (db_handler.removeShortlistedItem(productList.get(position).getId(), sessionManager.getSessionData(Constants.SESSION_EMAIL))) {
                    productList.remove(position);
                    notifyDataSetChanged();
                }
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
