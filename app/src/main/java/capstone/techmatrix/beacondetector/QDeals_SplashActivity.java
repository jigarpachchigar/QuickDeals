package capstone.techmatrix.beacondetector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import capstone.techmatrix.beacondetector.activities.QDealsProdDetails_Activity;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.fragments.QdUserSignIn;
import capstone.techmatrix.beacondetector.fragments.QdUserSignup;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.service.SyncDBService;
import capstone.techmatrix.beacondetector.utils.Constants;


public class QDeals_SplashActivity extends AppCompatActivity implements FinishActivity, BeaconConsumer {
    public static String TAG = "QDeals_SplashActivity";
    DB_Handler db_handler;
    Button signIn, signUp;
    Handler handler;
    TableLayout bottomLay;
    Snackbar snackbar = null;
    CoordinatorLayout coordinatorLayout;
    Product product;
    //Bluetooth
    private static final int ENABLE_BT_REQUEST_CODE = 1;

    //Beacon detector


    private BluetoothAdapter bluetoothAdapter;
    private String UUID = "";

    BeaconManager beaconManager;


//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            Toast.makeText(getBaseContext(), "This is being Called.", Toast.LENGTH_LONG).show();
//
//            beaconManager.bind(QDeals_SplashActivity.this);
//
//            Toast.makeText(getBaseContext(), "e2c56db5-dffb-48d2-b060-d0f5a7101111", Toast.LENGTH_LONG).show();
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_activity_splash);

        System.out.println("HHHHHHHHHHH");
        //bluetooth Code
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        //} else {


        // Device support Bluetooth
        if (!bluetoothAdapter.isEnabled()) {

            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(bluetoothIntent, ENABLE_BT_REQUEST_CODE);
            beaconManager = BeaconManager.getInstanceForApplication(this);
            // In this example, we will use Eddystone protocol, so we have to define it here
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            // Binds this activity to the BeaconService
            beaconManager.bind(this);


        } else {
            Toast.makeText(getBaseContext(), "Bluetooth is on!!!", Toast.LENGTH_SHORT).show();
        }

        //  }

        // Service To Fetch Data From URL
        setHandler();
        startIntentService();

        // Initialize DB Handler
        db_handler = new DB_Handler(this);

        setIds();
        setClickListeners();
//        beaconManager.setRegionStatePeristenceEnabled(false);
    }

    // Set Ids
    private void setIds() {
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup);
        bottomLay = findViewById(R.id.bottomLay);
        coordinatorLayout = findViewById(R.id.coordinatorLay);
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign In
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.fragment, new QdUserSignIn());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.fragment, new QdUserSignup());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    // Start Intent Service To Fetch Data
    private void startIntentService() {
        Intent intent = new Intent(getApplicationContext(), SyncDBService.class);
        intent.putExtra("messenger", new Messenger(handler));
        startService(intent);
    }

    // Check Session
    private void checkSession() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getSessionData(Constants.SESSION_EMAIL) != null && sessionManager.getSessionData(Constants.SESSION_EMAIL).trim().length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNextActivity();
                }
            }, 1000);
        } else {
            bottomLay.setVisibility(View.VISIBLE);
        }
    }

    // Load Next Activity
    private void loadNextActivity() {
        Intent i = new Intent(getApplicationContext(), QDeals_MainActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }

    // Handler To Receive Data From Service
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        try {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle reply = msg.getData();
                    if (reply.getString("message").equals("success")) {
                        checkSession();
                    } else {
                        // Show Error In Snack Bar
                        try {
                            String message = reply.getString("message");
                            assert message != null;
                            snackbar = Snackbar
                                    .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                            startIntentService();
                                        }
                                    });

                            // Changing message text color
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (!bluetoothAdapter.isEnabled()) {

                //Create an intent with the ACTION_REQUEST_ENABLE action, which we’ll use to display our system Activity//
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                //Pass this intent to startActivityForResult(). ENABLE_BT_REQUEST_CODE is a locally defined integer that must be greater than 0,
                //for example private static final int ENABLE_BT_REQUEST_CODE = 1//
                startActivityForResult(enableIntent, ENABLE_BT_REQUEST_CODE);
                //Toast.makeText(getApplicationContext(), "Enabling Bluetooth!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void finishActivity() {
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
//        beaconManager.unbind(this);
    }

    /*
            @Override
            public void onBeaconServiceConnect() {
                beaconManager.setMonitorNotifier(new MonitorNotifier() {
                    @Override
                    public void didEnterRegion(Region region) {
                        Toast.makeText(getBaseContext(),"Beacon detected!",Toast.LENGTH_LONG).show();
                        System.out.println("didEnterRegion");

                    }

                    @Override
                    public void didExitRegion(Region region) {
                        Toast.makeText(getBaseContext(),"Called when beacons are not in the Region specified!",Toast.LENGTH_LONG).show();
                        System.out.println("didExitRegion");
                    }

                    @Override
                    public void didDetermineStateForRegion(int state, Region region) {
                        Toast.makeText(getBaseContext(),"Called when the beacons are visible in the Region !",Toast.LENGTH_LONG).show();
                        System.out.println("didDetermineStateForRegion");
                    }
                });


                System.out.println("Inside");
                beaconManager.setRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                        if(beacons.size() > 0){
                            Identifier beacondId = beacons.iterator().next().getId1();
                            Toast.makeText(getBaseContext(),"beacondId1 :"+beacons.iterator().next().getId1(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(),"beacondId2 :"+beacons.iterator().next().getId2(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(),"beacondId3 :"+beacons.iterator().next().getId3(),Toast.LENGTH_LONG).show();

                            Double beaconDistance = beacons.iterator().next().getDistance(); // Beacon Distance
                            Toast.makeText(getBaseContext(),"beaconDistance :"+beaconDistance,Toast.LENGTH_LONG).show();

                            System.out.println("beaconId:"+beacondId);
                            System.out.println("beacondId2"+beacons.iterator().next().getId2());
                            System.out.println("beacondId3"+beacons.iterator().next().getId3());
                            System.out.println("beaconDistance"+beaconDistance);




                        }
                    }
                });
            }

    */
    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("myBeaons", Identifier.parse(UUID), null, null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.d(TAG, "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(TAG, "didExitRegion");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

                Log.d(TAG, "didDetermineStateForRegion");
                Toast.makeText(getBaseContext(),"Beacon Detected !!!",Toast.LENGTH_LONG).show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        sendNotification();
                    }
                }, 30000);

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon oneBeacon : beacons) {
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    public void sendNotification(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(sound, audioAttributes);
            notificationChannel.setLightColor(Color.GRAY);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        Intent i = new Intent(getBaseContext(), QDealsProdDetails_Activity.class);
        i.putExtra("ProductId", 13);
        //product.setId(13);


        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                .setContentTitle("Deal of the day!!!")
                .setTicker(getBaseContext().getString(R.string.app_name))
                .setContentText("Flat 50 % off on iPhone6s ")
                .setAutoCancel(true);

        builder.setContentIntent(contentIntent);
        notificationManager.notify(0, builder.build());
    }

//    @Override
//    public void onBeaconServiceConnect() {
//        // Encapsulates a beacon identifier of arbitrary byte length
//        ArrayList<Identifier> identifiers = new ArrayList<>();
//
//        // Set null to indicate that we want to match beacons with any value
//        identifiers.add(null);
//        // Represents a criteria of fields used to match beacon
//        Region region = new Region("AllBeaconsRegion",identifiers);
//        try {
//            // Tells the BeaconService to start looking for beacons that match the passed Region object
//            beaconManager.startRangingBeaconsInRegion(region);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        // Specifies a class that should be called each time the BeaconService gets ranging data, once per second by default
//        beaconManager.addRangeNotifier(this);
//    }
//
//    @Override
//    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//        if (beacons.size() > 0) {
//            Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
//        }
//    }
}
