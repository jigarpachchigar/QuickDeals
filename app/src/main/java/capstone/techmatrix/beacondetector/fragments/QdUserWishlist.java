package capstone.techmatrix.beacondetector.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdUserWishlistAdapter;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.utils.Constants;


public class QdUserWishlist extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qdeals_prod_listview, container, false);

        List<Product> productList = db_handler.getShortListedItems(sessionManager.getSessionData(Constants.SESSION_EMAIL));

        // fill listview with data
        ListView listView= view.findViewById(R.id.listview);
        listView.setAdapter(new QdUserWishlistAdapter(getActivity(), productList));
        return view;
    }
}
