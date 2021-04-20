package capstone.techmatrix.beacondetector.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdUserOrdersAdapter;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Cart;
import capstone.techmatrix.beacondetector.utils.Constants;

import java.util.List;



public class QDealsOrders_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_user_orders);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.my_orders);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);

        // Get Orders From DB
        DB_Handler db_handler = new DB_Handler(this);
        SessionManager sessionManager = new SessionManager(this);
        List<Cart> orderHistory = db_handler.getOrders(sessionManager.getSessionData(Constants.SESSION_EMAIL));

        // Fill ListView
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new QdUserOrdersAdapter(this,orderHistory));
    }


}
