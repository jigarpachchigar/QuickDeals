package capstone.techmatrix.beacondetector.webservice;

import capstone.techmatrix.beacondetector.pojo.ResponseJSON;

import retrofit2.Call;
import retrofit2.http.GET;



public interface RetrofitInterface {

    // Method To Fetch Data From URL
    @GET("data.json?alt=media&token=0185eb39-c6f4-4f46-ab72-01936037d0db")
    Call<ResponseJSON> fetchData();
}
