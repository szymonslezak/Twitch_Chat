package pl.edu.uwr.pum.footballapp.service.emotes.FFZ.remote;

import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZGlobal;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSet;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface FFZEmoteApi {


    @GET("room/{chanel}")
    Single<FFZMain> getChanelEmotes(@Path(value = "chanel", encoded = false) String chanel);

    @GET("set/{id}")
    Single<FFZSetMain> getChanelSet(@Path(value = "id", encoded = false) int id);

    @GET("_set/global")
    Single<FFZGlobal> getGlobalEmotes();
}
