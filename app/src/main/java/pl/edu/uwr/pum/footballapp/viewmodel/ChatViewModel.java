package pl.edu.uwr.pum.footballapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVMain;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.remote.BTTVEmoteService;
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.remote.FFZEmoteService;
import pl.edu.uwr.pum.footballapp.service.emotes.Twtich.model.TwitchResponse;
import pl.edu.uwr.pum.footballapp.service.emotes.Twtich.remote.TwitchEmoteService;
import pl.edu.uwr.pum.footballapp.service.twitch.chatConnect;
import pl.edu.uwr.pum.footballapp.service.twitch.message.IrcMessage;

public class ChatViewModel extends AndroidViewModel {

    private Disposable disposable;
    public MutableLiveData<List<IrcMessage>> chat_messages = new MutableLiveData<>();
    private final FFZEmoteService ffzService = new FFZEmoteService();
    private final BTTVEmoteService bttvService = new BTTVEmoteService();
    private final TwitchEmoteService twitchService = new TwitchEmoteService();
    public ArrayList<FFZEmote> ffzEmotes = new ArrayList<>();
    public ArrayList<BTTVEmote> bttvEmotes = new ArrayList<>();

    public final chatConnect chat_service = new chatConnect();
    public ChatViewModel(@NonNull Application application) {
        super(application);
    }
    public void createRemote(String nick, String oauth)
    {

        chat_service.connect(nick,oauth);
           /* chat_service.getReadConnection().message.observe(
                getViewLifecycleOwner(), competitions -> {
                    binding.recyclerViewList.setVisibility(View.VISIBLE);
                    adapter.updateList(competitions);
                }
            );*/

    }
    public void fetchRemote(String chanel,String oAuth) {

        disposable = ffzService.getChanelEmotes(chanel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FFZMain>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull FFZMain room) {
                       // for(FFZSet set: room.sets) {
                            //emotes.addAll(room.sets.emoticons);
                            fetch_FFZ(room.room.set);
                            fetch_BTTV(room.room.twitch_id);
                       // }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
        disposable = twitchService.getChanelEmotes("chat",oAuth)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TwitchResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TwitchResponse room) {
                        Log.d("emotes", String.valueOf(room));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
    private void fetch_FFZ(int id)
    {
        disposable = ffzService.getChanelSet(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FFZSetMain>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull FFZSetMain set) {
                        ffzEmotes.addAll(set.set.emoticons);
                        EmoteMaps.loadFFZEmotes(ffzEmotes,getApplication().getApplicationContext());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
    private void fetch_BTTV(int id)
    {
        disposable = bttvService.getChanelEmotes(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BTTVMain>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BTTVMain set) {
                        /*bttvEmotes.addAll(set.channelEmotes);
                        bttvEmotes.addAll(set.sharedEmotes);*/
                        EmoteMaps.loadBTTVEmotes(set.channelEmotes,getApplication().getApplicationContext());
                        EmoteMaps.loadBTTVEmotes(set.sharedEmotes,getApplication().getApplicationContext());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }



    /*public WebSocketCon fetchReadConnection()
    {
        //return chat_service.getReadConnection();
    }*/


    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null)
        disposable.dispose();
    }
}
