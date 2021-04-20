package capstone.techmatrix.beacondetector.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdSortProdAdapter;
import capstone.techmatrix.beacondetector.adapters.QdProdtListAdapter;
import capstone.techmatrix.beacondetector.adapters.QdProdSortAdapter;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.interfaces.ShowBackButton;
import capstone.techmatrix.beacondetector.interfaces.ToolbarTitle;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QdProducts extends Fragment {

    RelativeLayout sort, filter;
    TextView sortByText;
    String[] sortByArray = {"Most Recent", "Most Orders", "Most Shares", "Most Viewed"};
    int sortById = 0, cat_id = 0;
    GridView productsGrid;
    List<String> sizeFilter = new ArrayList<>();
    List<String> colorFilter = new ArrayList<>();
    QdProdtListAdapter productListAdapter;
    List<Product> productList;

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

    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.qdeals_prod_listview);

                ListView listView = dialog.findViewById(R.id.listview);
                listView.setAdapter(new QdProdSortAdapter(getActivity(), sortByArray, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        sortByText.setText(sortByArray[sortById]);

                        // Reload QdProducts List
                        fillGridView();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    // Set Filter Listener
    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterlayout);

                // Get Colors and Get Sizes
                DB_Handler db_handler = new DB_Handler(getActivity());
                final List<String> colors = db_handler.getAllColors();
                final List<String> sizes = db_handler.getAllSizes();

                // Add into hash map
                HashMap<String, List<String>> listHashMap = new HashMap<>();
                listHashMap.put("Size", sizes);
                listHashMap.put("Color", colors);

                // Add Headers
                List<String> headers = new ArrayList<>();
                headers.add("Size");
                headers.add("Color");

                final ExpandableListView listView = dialog.findViewById(R.id.expandableList);
                final QdSortProdAdapter filterItemListAdapter = new QdSortProdAdapter(getActivity(), headers, listHashMap, sizeFilter, colorFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case 0: // Size
                                if (!sizeFilter.contains(sizes.get(childPosition))) {
                                    sizeFilter.add(sizes.get(childPosition));
                                } else {
                                    sizeFilter.remove(sizes.get(childPosition));
                                }
                                break;

                            case 1: // Color
                                if (!colorFilter.contains("'" + colors.get(childPosition) + "'")) {
                                    colorFilter.add("'" + colors.get(childPosition) + "'");
                                } else {
                                    colorFilter.remove("'" + colors.get(childPosition) + "'");
                                }
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

                // Filter Apply Button Click
                Button apply = dialog.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Reload QdProducts List By Filter
                        fillGridView();
                        dialog.dismiss();
                    }
                });

                // Clear All Button Click
                Button clear = dialog.findViewById(R.id.clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            sizeFilter.clear();
                        } catch (NullPointerException ignore) {

                        }

                        try {
                            colorFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });

                // Close Button
                final ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
