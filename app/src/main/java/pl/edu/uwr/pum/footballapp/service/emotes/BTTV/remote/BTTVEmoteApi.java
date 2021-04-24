package pl.edu.uwr.pum.footballapp.service.emotes.BTTV.remote;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BTTVEmoteApi {


    @GET("cached/users/twitch/{id}")
    Single<BTTVMain> getChanelEmotes(@Path(value = "id", encoded = false) int id);

    @GET("cached/emotes/global")
    Single<List<BTTVEmote>> getGlobalEmotes();

}
