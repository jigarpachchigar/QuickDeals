package capstone.techmatrix.beacondetector.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.pojo.Variant;
import capstone.techmatrix.beacondetector.utils.Constants;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;


public class QDealsProdDetails_Activity extends AppCompatActivity {

    Product product;
    DB_Handler db_handler;

    String selectedSize = null;
    String selectedColor = null;
    String selectedItemPrice = null;
    int selectedItemQuantity = 1;
    int selectedItemVariantId = 0;
    String userEmail = null;

    LinearLayout colorParentLay, sizeParentLay;
    FlowLayout colorsLay, sizeLay;
    TextView price, quantityValue;
    ImageView minus, plus;
    Button cart, buyNow;
    SessionManager sessionManager;
    int cartCount = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_prod_details);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Get User Email
        sessionManager = new SessionManager(this);
        userEmail = sessionManager.getSessionData(Constants.SESSION_EMAIL);

        // Get Product Id
        int id = getIntent().getIntExtra("ProductId", 0);

        // Get Product Details By Id
        db_handler = new DB_Handler(this);
        product = db_handler.getProductDetailsById(id, userEmail);

        setIds();
        setValues();
        setToolbarIconsClickListeners();
        setQuantityUpdateListeners();
        setBottomPanelClickListeners();
    }

    // Set Ids
    private void setIds() {
        buyNow = findViewById(R.id.buyNow);
        cart = findViewById(R.id.cartButton);
        colorParentLay = findViewById(R.id.colorParentLay);
        sizeParentLay = findViewById(R.id.sizeParentLay);
        colorsLay = findViewById(R.id.colorsLay);
        sizeLay = findViewById(R.id.sizesLay);
        price = findViewById(R.id.price);
        quantityValue = findViewById(R.id.quantityValue);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
    }

    // Set Toolbar Icons Click Listeners
    private void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), QDealsCart_Activity.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Set Bottom Panel Click Listeners
    private void setBottomPanelClickListeners() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccessAddingToCart(false)) {
                    Toast.makeText(getApplicationContext(), R.string.add_success, Toast.LENGTH_LONG).show();
                    updateCartCount();
                }
            }
        });

        // Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccessAddingToCart(true)) {
                    startActivity(new Intent(getApplicationContext(), QDealsCart_Activity.class));
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    // Get Item Adding To Cart Status
    private boolean isSuccessAddingToCart(boolean isBuyNow) {
        try {
            // Get Selected Item Price
            if (selectedSize.equals("-") || selectedSize != null) {
                if (selectedColor != null) {
                    long result = db_handler.insertIntoCart(product.getId(), selectedItemVariantId, selectedItemQuantity, userEmail);
                    if (result > 0 || isBuyNow) {
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.item_exists, Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.color_select, Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // Set Values
    private void setValues() {

        // QdUserWishlist Icon
        final ImageView heart = findViewById(R.id.heart);
        if (product.getShortlisted()) {
            heart.setImageResource(R.drawable.ic_heart_grey);
        }

        // Wishlist Icon Click
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add / Remove Item From Wish List
                if (!product.getShortlisted()) {
                    heart.setImageResource(R.drawable.ic_heart_grey);
                    if (db_handler.shortlistItem(product.getId(), userEmail) > 0) {
                        product.setShortlisted(true);
                        Toast.makeText(getApplicationContext(), R.string.item_add_wishlist, Toast.LENGTH_LONG).show();
                    }
                } else {
                    heart.setImageResource(R.drawable.ic_heart_grey600_24dp);
                    if (db_handler.removeShortlistedItem(product.getId(), userEmail)) {
                        product.setShortlisted(false);
                        Toast.makeText(getApplicationContext(), R.string.item_rem_wishlist, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Title
        TextView Title = findViewById(R.id.title);
        Title.setText(product.getName());

        // Size
        List<String> sizeList = db_handler.getSizeByProductId(product.getId());
        setSizeLayout(sizeList);

        // Color
        List<String> colorList = db_handler.getProductColorsById(product.getId());
        setColorLayout(colorList);

        // Price Range
        price.setText(db_handler.getProductPriceRangeById(product.getId()));

        ImageView productImage = findViewById(R.id.image);
        if(product.getName().equalsIgnoreCase("Iphone 6S")) {
            productImage.setImageResource(R.drawable.iphone6s);
        }
        else if(product.getName().equalsIgnoreCase("Iphone 7")) {
            productImage.setImageResource(R.drawable.iphone7);
        }else if(product.getName().equalsIgnoreCase("Iphone 6")) {
            productImage.setImageResource(R.drawable.iphone6);
        }else if(product.getName().equalsIgnoreCase("Iphone 6S Plus")) {
            productImage.setImageResource(R.drawable.iphone6splus);
        }else if(product.getName().equalsIgnoreCase("Iphone 7 Plus")) {
            productImage.setImageResource(R.drawable.iphone7plus);
        }
        else if(product.getName().equalsIgnoreCase("Galaxy S7 Edge")) {
            productImage.setImageResource(R.drawable.galaxys7edge);
        }else if(product.getName().equalsIgnoreCase("Galaxy J5")) {
            productImage.setImageResource(R.drawable.galaxyj5);
        }else if(product.getName().equalsIgnoreCase("Galaxy J7")) {
            productImage.setImageResource(R.drawable.galaxyj7);
        }else if(product.getName().equalsIgnoreCase("Galaxy Grand Prime")) {
            productImage.setImageResource(R.drawable.grandprime);
        }else if(product.getName().equalsIgnoreCase("Note 4")) {
            productImage.setImageResource(R.drawable.samsungnote4);
        }
        else if(product.getName().equalsIgnoreCase("Dell Inspiron Core")) {
            productImage.setImageResource(R.drawable.dellinspiron);
        }else if(product.getName().equalsIgnoreCase("Dell Inspiron 11")) {
            productImage.setImageResource(R.drawable.dell11);
        }
        else if(product.getName().equalsIgnoreCase("Satellite Pro")) {
            productImage.setImageResource(R.drawable.satpro);
        }else if(product.getName().equalsIgnoreCase("Satellite P50")) {
            productImage.setImageResource(R.drawable.satp50);
        }

    }

    private void setSizeLayout(final List<String> sizeList) {
        sizeLay.removeAllViews();
        try {
            if (sizeList.size() > 0) {
                for (int i = 0; i < sizeList.size(); i++) {
                    final TextView size = new TextView(this);
                    size.setText(sizeList.get(i));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        size.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }

                    // Change Border To Blue If Selected
                    try {
                        if (selectedSize.equals(sizeList.get(i))) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                size.setBackground(getResources().getDrawable(R.drawable.border_blue));
                            } else {
                                size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_blue));
                            }
                        }
                    } catch (NullPointerException ignore) {
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        size.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        size.setTextColor(getResources().getColor(R.color.black));
                    }
                    size.setFocusableInTouchMode(false);
                    size.setFocusable(true);
                    size.setClickable(true);
                    size.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    size.setLayoutParams(params);
                    sizeLay.addView(size);

                    // Size Click Listener
                    size.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView textView = (TextView) view;
                            selectedSize = textView.getText().toString();
                            selectedColor = null;
                            selectedItemPrice = null;
                            setSizeLayout(sizeList); // refresh to set selected

                            // Get Color of Selected Size & Set Color Layout
                            List<String> colorList = db_handler.getColorBySelectedSize(product.getId(), selectedSize);
                            setColorLayout(colorList);
                        }
                    });
                }
            } else {
                sizeParentLay.setVisibility(View.GONE);
                selectedSize = "-";
            }
        } catch (NullPointerException e) {
            sizeParentLay.setVisibility(View.GONE);
            selectedSize = "-";
        }
    }

    private void setColorLayout(final List<String> colorList) {
        colorsLay.removeAllViews();
        try {
            if (colorList.size() > 0) {
                for (int i = 0; i < colorList.size(); i++) {
                    final TextView color = new TextView(this);
                    color.setText(colorList.get(i));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        color.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }

                    // Change Border To Blue If Selected
                    try {
                        if (selectedColor.equals(colorList.get(i))) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                color.setBackground(getResources().getDrawable(R.drawable.border_blue));
                            } else {
                                color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_blue));
                            }
                        }
                    } catch (NullPointerException ignore) {
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        color.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        color.setTextColor(getResources().getColor(R.color.black));
                    }
                    color.setFocusableInTouchMode(false);
                    color.setFocusable(true);
                    color.setClickable(true);
                    color.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    color.setLayoutParams(params);
                    colorsLay.addView(color);

                    // Size Click Listener
                    color.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View view) {

                            try {
                                // Get Selected Item Price
                                if (selectedSize.equals("-") || selectedSize != null) {
                                    TextView textView = (TextView) view;
                                    selectedColor = textView.getText().toString();

                                    Variant variant;
                                    if (selectedSize.equals("-")) // no size for product
                                    {
                                        variant = db_handler.getProductVariant(product.getId(), null, selectedColor);
                                        selectedItemPrice = variant.getPrice();
                                    } else {
                                        variant = db_handler.getProductVariant(product.getId(), selectedSize, selectedColor);
                                        selectedItemPrice = variant.getPrice();
                                    }

                                    selectedItemVariantId = variant.getId();
                                    price.setText("CAD " + selectedItemPrice);
                                    setColorLayout(colorList); // reload to refresh background
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else {
                colorParentLay.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            colorParentLay.setVisibility(View.GONE);
        }
    }

    // Quantity Update Listeners
    private void setQuantityUpdateListeners() {
        // Decrement Listener
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemQuantity != 1) {
                    selectedItemQuantity--;
                    quantityValue.setText(String.valueOf(selectedItemQuantity));
                }
            }
        });

        // Increment Listener
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemQuantity++;
                quantityValue.setText(String.valueOf(selectedItemQuantity));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCartCount();
    }

    // Update Cart Item Count In Toolbar
    private void updateCartCount() {
        cartCount = db_handler.getCartItemCount(sessionManager.getSessionData(Constants.SESSION_EMAIL));
        TextView count = findViewById(R.id.count);
        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
