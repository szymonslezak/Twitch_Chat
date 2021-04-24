package pl.edu.uwr.pum.footballapp.service.emotes.Twtich.remote;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.Twtich.model.TwitchResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitchEmoteService {

    private static final String BASE_URL = "https://api.twitch.tv/kraken/";

    private final TwitchEmoteApi api;

    public TwitchEmoteService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(TwitchEmoteApi.class);
    }


    public Single<TwitchResponse> getChanelEmotes(String chanel, String id){return api.getChanelEmotes(chanel,id);};


}
