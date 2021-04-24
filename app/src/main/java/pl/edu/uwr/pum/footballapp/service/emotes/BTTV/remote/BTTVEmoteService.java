package pl.edu.uwr.pum.footballapp.service.emotes.BTTV.remote;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BTTVEmoteService {

    private static final String BASE_URL = "https://api.betterttv.net/3/";

    private final BTTVEmoteApi api;

    public BTTVEmoteService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(BTTVEmoteApi.class);
    }


    public Single<BTTVMain> getChanelEmotes(int id) { return api.getChanelEmotes(id);}

    public Single<List<BTTVEmote>> getGlobalEmotes() {return api.getGlobalEmotes();}

}
