package pl.edu.uwr.pum.footballapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.edu.uwr.pum.footballapp.R;
import pl.edu.uwr.pum.footballapp.databinding.ActivityMainBinding;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVMain;
import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.remote.BTTVEmoteService;
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZGlobal;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZSetMain;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.remote.FFZEmoteApi;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.remote.FFZEmoteService;
import pl.edu.uwr.pum.footballapp.service.twitch.chatConnect;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    FFZEmoteService ffzService = new FFZEmoteService();
    BTTVEmoteService bttvService = new BTTVEmoteService();
    Disposable disposable;
    //private chatConnect chat = new chatConnect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fetchRemote();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //chat.connect();
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(navHostFragment != null){
            navController = navHostFragment.getNavController();
        }
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, binding.drawerLayout);
    }


    public void fetchRemote() {


        disposable = ffzService.getGlobalSet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FFZGlobal>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull FFZGlobal global) {
                        for (int set : global.default_sets) {
                            fetch_FFZ(set);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
        fetch_BTTV();
    }
    private void fetch_FFZ(int id)
    {
       disposable =  ffzService.getChanelSet(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FFZSetMain>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull FFZSetMain set) {
                        EmoteMaps.loadGlobalFFZEmotes(set.set.emoticons,getApplicationContext());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
    private void fetch_BTTV()
    {
        disposable = bttvService.getGlobalEmotes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<BTTVEmote>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<BTTVEmote> emotes) {
                     /*   bttvEmotes.addAll(set.channelEmotes);
                        bttvEmotes.addAll(set.sharedEmotes);*/
                        EmoteMaps.loadGlobalBTTVEmotes(emotes,getApplicationContext());
                        EmoteMaps.overlay_emotes.add("cvHazmat");
                        EmoteMaps.overlay_emotes.add("cvMask");
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


}