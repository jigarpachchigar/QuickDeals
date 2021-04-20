package capstone.techmatrix.beacondetector.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.pojo.Category;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.pojo.ProductRank;
import capstone.techmatrix.beacondetector.pojo.Ranking;
import capstone.techmatrix.beacondetector.pojo.ResponseJSON;
import capstone.techmatrix.beacondetector.pojo.Variant;
import capstone.techmatrix.beacondetector.utils.Util;
import capstone.techmatrix.beacondetector.webservice.RetrofitBuilder;
import capstone.techmatrix.beacondetector.webservice.RetrofitInterface;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SyncDBService extends IntentService {

    DB_Handler db_handler;
    Intent intent;

    public SyncDBService(String name) {
        super(name);
    }

    public SyncDBService() {
        super("SyncDBService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        db_handler = new DB_Handler(this);
        fetchData();
        this.intent = intent;
    }

    // Fetch Data From URL
    private void fetchData() {
        // Initialize Retrofit
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(this);
        OkHttpClient httpClient = retrofitBuilder.setClient();
        Retrofit retrofit = retrofitBuilder.retrofitBuilder(httpClient);
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Call Web Service
        Call<ResponseJSON> call = retrofitInterface.fetchData();
        call.enqueue(new Callback<ResponseJSON>() {
            @Override
            public void onResponse(Call<ResponseJSON> call, Response<ResponseJSON> response) {
                try {
                    if (response.body() != null) {
                        processData(response.body());
                    }
                } catch (Exception ignore) {
                    reply(getResources().getString(R.string.Error500));
                }
            }

            @Override
            public void onFailure(Call<ResponseJSON> call, Throwable t) {
                reply(Util.getErrorMessage(t,SyncDBService.this));
            }
        });
    }



    // Reply Message
    private void reply(String value)
    {
        Bundle bundle = intent.getExtras();
        bundle.putString("message",value);
        if (bundle != null) {
            Messenger messenger = (Messenger) bundle.get("messenger");
            Message msg = Message.obtain();
            msg.setData(bundle); //put the data here
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                Log.i("error", "error");
            }
        }
    }
}
