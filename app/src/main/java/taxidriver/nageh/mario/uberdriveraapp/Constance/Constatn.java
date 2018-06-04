package taxidriver.nageh.mario.uberdriveraapp.Constance;

import taxidriver.nageh.mario.uberdriveraapp.Onlinedata.IGoogleApi;
import taxidriver.nageh.mario.uberdriveraapp.Onlinedata.RetrofitInstace;

public class Constatn {
    public static final String url ="https://maps.googleapis.com";
    public static IGoogleApi iGoogleApi(){
        return RetrofitInstace.getRetrofit(url).create(IGoogleApi.class);
    }
}
