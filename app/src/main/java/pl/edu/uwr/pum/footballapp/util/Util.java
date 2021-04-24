package pl.edu.uwr.pum.footballapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.caverock.androidsvg.SVG;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


import io.reactivex.rxjava3.internal.util.ErrorMode;
import pl.edu.uwr.pum.footballapp.R;
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.util.SVG.SvgSoftwareLayerSetter;

public class Util {

    public static long refreshTime = 20 * 1000 * 1000 * 1000L;

    public static final int NUM_OF_THREADS = 4;
    public static void loadGif(String url,String name, Context context
    ) {
        RequestOptions options = new RequestOptions();
        Glide.with(context)
                //.setDefaultRequestOptions(options)
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.load("https://res.cloudinary.com/demo/image/upload/bored_animation.gif")
                .into(new CustomTarget<GifDrawable>() {

                   /* @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                       // Drawable draw = (Drawable)resource;
                       // GifDrawable dran = (GifDrawable) resource;
                        EmoteMaps.Gifs.put(name,(GifDrawable)resource);
                        //EmoteMaps.Gifs.get(name).start();
                    }*/

                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        resource.start();
                        resource.setBounds(0,0,75,75);
                        EmoteMaps.Gifs.put(name,resource);
                        EmoteMaps.Gifs.get(name).start();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public static void loadImage(String url,String name, int type, Context context,int size
                                 ) {
        RequestOptions options = new RequestOptions();

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if(size < 3)
                        {

                            // resource.recycle();
                            resource = Bitmap.createScaledBitmap(
                                    resource,  Math.round((float)resource.getWidth()*3/size), Math.round((float)resource.getHeight()*3/size), false);
                        }
                        switch(type)
                        {
                            case 2:
                            {
                                EmoteMaps.FFZEmotes.put(name,resource);
                                break;
                            }
                            case 3:
                            {
                                EmoteMaps.FFZGlobalEmotes.put(name,resource);
                                break;
                            }
                            case 4:
                            {
                                EmoteMaps.BttvEmotes.put(name,resource);
                                break;
                            }
                            case 5:
                            {
                                EmoteMaps.BttvGlobalEmotes.put(name,resource);
                                break;
                            }
                            default:
                            {
                                break;
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
                //.into(imageView);
    }

    /*public static CircularProgressDrawable getProgressDrawable(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(15f);
        circularProgressDrawable.setCenterRadius(20f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }*/

   /* @BindingAdapter("image_url")
    public static void loadImage(ImageView imageView, String url){
        loadImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }*/
}
