package pl.edu.uwr.pum.footballapp.service.emotes.FFZ.remote;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZGlobal;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSet;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FFZEmoteService {

    private static final String BASE_URL = "https://api.frankerfacez.com/v1/";

    private final FFZEmoteApi api;

    public FFZEmoteService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(FFZEmoteApi.class);
    }


    public Single<FFZMain> getChanelEmotes(String chanel) { return api.getChanelEmotes(chanel);}

    public Single<FFZSetMain> getChanelSet(int id) { return api.getChanelSet(id);}

    public Single<FFZGlobal> getGlobalSet() {return api.getGlobalEmotes();}

}
