package capstone.techmatrix.beacondetector.webservice;

import android.content.Context;

import capstone.techmatrix.beacondetector.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitBuilder {

    private Context context;

    public RetrofitBuilder(Context context) {
        this.context = context;
    }

    public OkHttpClient setClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
    }

    public Retrofit retrofitBuilder(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }
}
