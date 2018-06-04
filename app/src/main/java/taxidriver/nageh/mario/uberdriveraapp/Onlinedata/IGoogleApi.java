package taxidriver.nageh.mario.uberdriveraapp.Onlinedata;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {
    @GET
    Call<String> getpath(@Url String url);
}
