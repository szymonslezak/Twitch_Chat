package pl.edu.uwr.pum.footballapp.service.emotes.Twtich.remote;

import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.Twtich.model.TwitchResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TwitchEmoteApi {

    //@Headers("Client-ID: {id}")
    @GET("{chanel}/emoticons")
    Single<TwitchResponse> getChanelEmotes(@Path(value = "chanel", encoded = false) String chanel,@Header("Client-ID") String id);
}
