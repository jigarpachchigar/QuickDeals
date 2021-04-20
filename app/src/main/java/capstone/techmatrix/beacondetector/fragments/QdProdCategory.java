package capstone.techmatrix.beacondetector.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdCartListAdapter;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.pojo.Category;

import java.util.List;


public class QdProdCategory extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qdeals_prod_listview, container, false);

        // load products
        DB_Handler db_handler = new DB_Handler(getActivity());
        List<Category> categoryList = db_handler.getCategoryList();

        // fill listview with data
        ListView listView= view.findViewById(R.id.listview);
        listView.setAdapter(new QdCartListAdapter(getActivity(), categoryList));

        return view;
    }
}
