package capstone.techmatrix.beacondetector.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import capstone.techmatrix.beacondetector.QDeals_MainActivity;
import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.fragments.QdProdSubCategory;
import capstone.techmatrix.beacondetector.fragments.QdProducts;
import capstone.techmatrix.beacondetector.pojo.Category;
import capstone.techmatrix.beacondetector.utils.Constants;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.Serializable;
import java.util.List;


public class QdProdSubCatAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Category> subCategoryList;
    private DB_Handler db_handler;

    public QdProdSubCatAdapter(Context context, List<Category> subCategoryList) {
        this.context = context;
        this.subCategoryList = subCategoryList;
        db_handler = new DB_Handler(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subCategoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return subCategoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.categories_grid_item, null);
        holder.category = rowView.findViewById(R.id.name);
        holder.circleImageView = rowView.findViewById(R.id.image);

        System.out.println("Sub Category Name :"+subCategoryList.get(position).getName());
        if(subCategoryList.get(position).getName().equalsIgnoreCase("Mobiles")) {
            holder.circleImageView.setImageResource(R.drawable.smartphone);
            holder.category.setText("Phone");
        }else if(subCategoryList.get(position).getName().equalsIgnoreCase("Laptops")){
            holder.circleImageView.setImageResource(R.drawable.laptop);
            holder.category.setText("Laptop & Desktop");
        } else if (subCategoryList.get(position).getName().equalsIgnoreCase("Apple")){
            holder.circleImageView.setImageResource(R.drawable.apple_logo);
            holder.category.setText(subCategoryList.get(position).getName());
        } else if(subCategoryList.get(position).getName().equalsIgnoreCase("Samsung")){
            holder.circleImageView.setImageResource(R.drawable.samsung);
            holder.category.setText(subCategoryList.get(position).getName());
        }else if(subCategoryList.get(position).getName().equalsIgnoreCase("Dell")){
            holder.circleImageView.setImageResource(R.drawable.dellchromebook);
            holder.category.setText(subCategoryList.get(position).getName());
        }else if(subCategoryList.get(position).getName().equalsIgnoreCase("Toshiba")){
            holder.circleImageView.setImageResource(R.drawable.dell11);
            holder.category.setText(subCategoryList.get(position).getName());
        } else{
            holder.circleImageView.setImageResource(R.drawable.ic_account_grey600_24dp);
            holder.category.setText(subCategoryList.get(position).getName());
        }



        holder.gridItemLayout = rowView.findViewById(R.id.gridItemLayouut);
        holder.gridItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = subCategoryList.get(position).getId();

                // get subcategories by id
                List<Category> childCategories = db_handler.getSubcategoryList(id);

                // initialize bundle and fragment manager
                Bundle bundle = new Bundle();
                FragmentManager fm = ((QDeals_MainActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                // Check If QdProdSubCategory Are There Else Call QdProducts List
                if (childCategories.size() > 0) {

                    // add bundle arguments
                    bundle.putString(Constants.TITLE,subCategoryList.get(position).getName());
                    bundle.putSerializable(Constants.CAT_KEY, (Serializable) childCategories);

                    QdProdSubCategory subcategories = new QdProdSubCategory();
                    subcategories.setArguments(bundle);

                    ft.replace(R.id.content, subcategories, Constants.FRAG_SUBCAT);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    // add bundle arguments
                    bundle.putInt(Constants.CAT_ID_KEY, id);
                    bundle.putString(Constants.TITLE,subCategoryList.get(position).getName());

                    QdProducts products = new QdProducts();
                    products.setArguments(bundle);

                    ft.replace(R.id.content, products, Constants.FRAG_PDT);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        return rowView;
    }

    public class Holder {
        TextView category;
        RelativeLayout gridItemLayout;
        CircleImageView circleImageView;
    }
}
