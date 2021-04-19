package capstone.techmatrix.beacondetector.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdProdSortAdapter;
import capstone.techmatrix.beacondetector.adapters.QdProdtListAdapter;
import capstone.techmatrix.beacondetector.adapters.QdSortProdAdapter;

import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.interfaces.ShowBackButton;
import capstone.techmatrix.beacondetector.interfaces.ToolbarTitle;
import capstone.techmatrix.beacondetector.utils.Constants;

public class QdProducts extends Fragment {

    RelativeLayout sort, filter;

    int sortById = 0, cat_id = 0;
    GridView productsGrid;
    List<String> sizeFilter = new ArrayList<>();
    List<String> colorFilter = new ArrayList<>();

    ToolbarTitle toolbarTitleCallback;
    ShowBackButton showBackButtonCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        toolbarTitleCallback = (ToolbarTitle) context;
        showBackButtonCallback = (ShowBackButton) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qdeals_product_list, container, false);

        setIds(view);
        setSortListener();
        setFilterListener();

        // get category id
        Bundle args = getArguments();
        assert args != null;
        cat_id = args.getInt(Constants.CAT_ID_KEY);

        if (cat_id > 0) {
            // Show Back Button and Set Title
            showBackButtonCallback.showBackButton();
            toolbarTitleCallback.setToolbarTitle(args.getString(Constants.TITLE));
        }

        // Get Data and Fill Grid
        sortByText.setText(sortByArray[0]);
        fillGridView();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // Update Items
        try {
            DB_Handler db_handler = new DB_Handler(getActivity());
            SessionManager sessionManager = new SessionManager(getActivity());
            List<Product> productList = db_handler.getProductsList(sortById, sizeFilter, colorFilter, cat_id, sessionManager.getSessionData(Constants.SESSION_EMAIL));
            this.productList.clear();
            this.productList.addAll(productList);
            productListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set Ids
    private void setIds(View view) {
        sort = view.findViewById(R.id.sortLay);
        filter = view.findViewById(R.id.filterLay);
        sortByText = view.findViewById(R.id.sortBy);
        productsGrid = view.findViewById(R.id.productsGrid);
    }

    // Fill GridView With Data
    private void fillGridView() {
        SessionManager sessionManager = new SessionManager(getActivity());
        DB_Handler db_handler = new DB_Handler(getActivity());
        productList = db_handler.getProductsList(sortById, sizeFilter, colorFilter, cat_id, sessionManager.getSessionData(Constants.SESSION_EMAIL));
        productsGrid.setNumColumns(2);
        productListAdapter = new QdProdtListAdapter(getActivity(), productList);
        productsGrid.setAdapter(productListAdapter);
    }


}
