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

    // Create User Table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + QDUsers + "("
            + EMAIL + " TEXT PRIMARY KEY,"
            + NAME + " TEXT NOT NULL,"
            + MOBILE + " TEXT NOT NULL,"
            + PASSWORD + " TEXT NOT NULL" + ")";

    // Create QdProdCategory Table
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + QDCategories + "("
            + ID + " INTEGER PRIMARY KEY,"
            + NAME + " TEXT NOT NULL" + ")";

    // Create QdProdSubCategory Mapping Table
    private static final String CREATE_SUBCATEGORIES_MAPPING_TABLE = "CREATE TABLE " + QDSubCategories + "("
            + ID + " INTEGER PRIMARY KEY,"
            + CAT_ID + " INTEGER NOT NULL,"
            + SUB_ID + " INTEGER NOT NULL" + ")";

    // Create QdProducts Table
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + QDProdcuts + "("
            + ID + " INTEGER PRIMARY KEY,"
            + CAT_ID + " INTEGER NOT NULL,"
            + NAME + " TEXT NOT NULL,"
            + DATE + " TEXT NOT NULL,"
            + TAX_NAME + " TEXT NOT NULL,"
            + TAX_VALUE + " REAL NOT NULL,"
            + VIEW_COUNT + " INTEGER NOT NULL,"
            + ORDER_COUNT + " INTEGER NOT NULL,"
            + SHARE_COUNT + " INTEGER NOT NULL" + ")";

    // Create Variants Table
    private static final String CREATE_VARIANTS_TABLE = "CREATE TABLE " + QDVar + "("
            + ID + " INTEGER PRIMARY KEY,"
            + SIZE + " TEXT,"
            + COLOR + " TEXT NOT NULL,"
            + PRICE + " TEXT NOT NULL,"
            + PDT_ID + " INTEGER NOT NULL" + ")";

    // Create Order History Table
    private static final String CREATE_ORDER_HISTORY_TABLE = "CREATE TABLE " + QDUserOrder + "("
            + ID + " INTEGER PRIMARY KEY,"
            + PDT_ID + " INTEGER NOT NULL,"
            + VAR_ID + " INTEGER NOT NULL,"
            + QUANTITY + " INTEGER NOT NULL,"
            + EMAIL + " TEXT NOT NULL" + ")";

    // Create Shopping Cart Table
    private static final String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + QDUserCart + "("
            + ID + " INTEGER PRIMARY KEY,"
            + PDT_ID + " INTEGER NOT NULL,"
            + VAR_ID + " INTEGER NOT NULL,"
            + QUANTITY + " INTEGER NOT NULL,"
            + EMAIL + " TEXT NOT NULL" + ")";

    // Create Wish List Table
    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE " + QDWishlist + "("
            + ID + " INTEGER PRIMARY KEY,"
            + PDT_ID + " INTEGER NOT NULL,"
            + EMAIL + " TEXT NOT NULL" + ")";

    public DB_Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_VARIANTS_TABLE);
        db.execSQL(CREATE_SUBCATEGORIES_MAPPING_TABLE);
        db.execSQL(CREATE_ORDER_HISTORY_TABLE);
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + QDUsers);
        db.execSQL("DROP TABLE IF EXISTS " + QDCategories);
        db.execSQL("DROP TABLE IF EXISTS " + QDProdcuts);
        db.execSQL("DROP TABLE IF EXISTS " + QDVar);
        db.execSQL("DROP TABLE IF EXISTS " + QDSubCategories);
        db.execSQL("DROP TABLE IF EXISTS " + QDSubCategories);
        db.execSQL("DROP TABLE IF EXISTS " + QDUserCart);
        db.execSQL("DROP TABLE IF EXISTS " + QDWishlist);

        // Create tables
        onCreate(db);
    }


}
