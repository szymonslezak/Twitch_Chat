package pl.edu.uwr.pum.footballapp.service.emotes;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import pl.edu.uwr.pum.footballapp.service.emotes.BTTV.model.BTTVEmote;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZEmote;
import pl.edu.uwr.pum.footballapp.kotlin.GifDownloader;
import pl.edu.uwr.pum.footballapp.util.Util;


public class EmoteMaps {

    public static Map<String, Bitmap> FFZEmotes = new HashMap<>();
    public static Map<String, Bitmap> FFZGlobalEmotes = new HashMap<>();
    public static Map<String, Bitmap> BttvGlobalEmotes = new HashMap<>();
    public static Map<String, Bitmap> BttvEmotes = new HashMap<>();
    public static Map<String, GifDrawable> Gifs = new HashMap<>();
    public static ArrayList<String> overlay_emotes = new ArrayList<>();


    public static void clearEmotes()
    {
        FFZEmotes.clear();
        BttvEmotes.clear();
        Gifs.clear();
    }
    public static void loadFFZEmotes(List<FFZEmote> emotes, Context context)
    {
        for(FFZEmote emote:emotes)
        {
            int size = 4;
            String url = emote.urls.Times4;
            if(url == null)
            {
                url = emote.urls.Times2;
                size = 2;
                if(url == null)
                {
                    url = emote.urls.Times1;
                    size = 1;
                }

            }
            Util.loadImage("https:"+ url,emote.name,2,context,size);
        }
    }
    public static void loadGlobalFFZEmotes(List<FFZEmote> emotes, Context context)
    {
        for(FFZEmote emote:emotes)
        {
            int size = 4;
            String url = emote.urls.Times4;
            if(url == null)
            {
                url = emote.urls.Times2;
                size = 2;
                if(url == null)
                {
                    url = emote.urls.Times1;
                    size = 1;
                }

            }
            Util.loadImage("https:"+ url,emote.name,3,context,size);
        }
    }
    public static void loadBTTVEmotes(List<BTTVEmote> emotes, Context context)
    {
        for(BTTVEmote emote:emotes)
        {

            String url = emote.id;
           /* if(emote.imageType.equals("gif"))
            {
                Util.loadGif("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,context);
            }*/
            if(emote.imageType.equals("gif"))
            {

                GifDownloader downloader = new GifDownloader(context);
               // downloader.get_gif("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,context);
                Util.loadGif("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,context);
            }
            else
            Util.loadImage("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,4,context,3);
        }
    }
    public static void loadGlobalBTTVEmotes(List<BTTVEmote> emotes, Context context)
    {
        for(BTTVEmote emote:emotes)
        {
            String url = emote.id;
            if(emote.imageType.equals("gif"))
            {

                GifDownloader downloader = new GifDownloader(context);
              //  downloader.get_gif("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,context);
                Util.loadGif("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,context);
            }
            else
            Util.loadImage("https://cdn.betterttv.net/emote/"+ url+"/3x",emote.code,5,context,3);
        }
    }




}
