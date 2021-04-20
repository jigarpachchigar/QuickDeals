package capstone.techmatrix.beacondetector.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import capstone.techmatrix.beacondetector.pojo.Cart;
import capstone.techmatrix.beacondetector.pojo.Category;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.pojo.Tax;
import capstone.techmatrix.beacondetector.pojo.User;
import capstone.techmatrix.beacondetector.pojo.Variant;
import capstone.techmatrix.beacondetector.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DB_Handler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "QuickDeals";

    // Column Names Static Variables
    private static final String ID = "id";
    private static final String CAT_ID = "cat_id";
    private static final String SUB_ID = "subcat_id";
    private static final String PDT_ID = "product_id";
    private static final String VAR_ID = "var_id";
    private static final String NAME = "prd_name";
    private static final String DATE = "date_add";
    private static final String SIZE = "prd_size";
    private static final String COLOR = "prd_color";
    private static final String PRICE = "prd_price";
    private static final String TAX_NAME = "tax_name";
    private static final String TAX_VALUE = "tax_value";
    private static final String EMAIL = "user_email";
    private static final String PASSWORD = "user_password";
    private static final String MOBILE = "mobile_no";
    private static final String QUANTITY = "prd_quantity";
    public static final String VIEW_COUNT = "view_count";
    public static final String ORDER_COUNT = "order_count";
    public static final String SHARE_COUNT = "share_count";

    // Table Names Static Variables
    private static final String QDUsers = "qdeals_user";
    private static final String QDCategories = "listview";
    private static final String QDSubCategories = "subcategories_mapping";
    private static final String QDProdcuts = "products";
    private static final String QDVar = "variants";
    private static final String QDWishlist = "wishlist";
    private static final String QDUserOrder = "order_history";
    private static final String QDUserCart = "shopping_cart";


}
