package pl.edu.uwr.pum.footballapp.service.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;


import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.service.emotes.FFZ.model.FFZEmote;
import pl.edu.uwr.pum.footballapp.service.twitch.message.IrcMessage;

public class ChatMessage {
    public Spannable mess;
    public boolean mention;
    public String user;
    public GifDrawable gi;
    public String gif_name;

    public ChatMessage(IrcMessage message, Context context)
    {
        Bitmap overlay_emote = null;
        int overlay_start = 0;
        int overlay_end = 0;

        int start_index = 0;
        mention = message.mention;
        user = message.user;
        String m = message.message.substring(0,message.message.length()-2);
        String[] words = m.split(" ");
        for(String word:words)
        {
            start_index += word.length()+1;
        }
       /* if(words.length > 2)
            start_index-=4;
        else if(words.length == 2)
            start_index-=3;
        else*/
            start_index-=2;
        Collections.reverse(Arrays.asList(words));
     //   words.reverse();
        mess = new SpannableString(message.message);
        for(String word:words)
        {
            GifDrawable gif =  EmoteMaps.Gifs.get(word);


            if(gif == null) {

                Bitmap emote = EmoteMaps.FFZGlobalEmotes.get(word);

                if (emote == null) {
                    emote = EmoteMaps.FFZEmotes.get(word);
                    if (emote == null) {
                        emote = EmoteMaps.BttvGlobalEmotes.get(word);
                        if (emote == null) {
                            emote = EmoteMaps.BttvEmotes.get(word);
                        }
                    }
                }
                if (emote != null) {
                    if(overlay_emote != null)
                    {
                        overlay_emote = overlay(emote,overlay_emote);
                        overlay_start -= word.length()+1;
                    }
                    if(EmoteMaps.overlay_emotes.contains(word))
                    {
                            if(overlay_emote == null)
                            {
                                overlay_emote = emote;
                                overlay_end = start_index;
                                overlay_start = start_index - word.length();
                            }
                    }
                    else {
                        ImageSpan image = null;
                        if (overlay_emote != null) {

                            image = new ImageSpan(context, overlay_emote, ImageSpan.ALIGN_BASELINE);
                            overlay_emote = null;
                            mess.setSpan(image, overlay_start+1, overlay_end+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        } else {
                            image = new ImageSpan(context, emote, ImageSpan.ALIGN_BASELINE);
                            mess.setSpan(image, start_index - word.length()+1, start_index+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                    }

                }
                else if(overlay_emote != null)
                {
                    ImageSpan image = new ImageSpan(context, overlay_emote, ImageSpan.ALIGN_BASELINE);
                    mess.setSpan(image, overlay_start+1,overlay_end+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    overlay_emote = null;

                }
            }
            else
            {
                //gif.start();
                /*if(!gif.isRunning())*/
                //gif.stop();
                gi = gif;
                ImageSpan image = new ImageSpan(gi, ImageSpan.ALIGN_BASELINE);

                gif_name = word;
           //     image.getDrawable().setBounds(0,0,50,50);
                mess.setSpan(image, start_index - word.length()+1,start_index+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            start_index -= word.length()+1;
        }
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, (bmp1.getWidth() - bmp2.getWidth()) / 2, (bmp1.getHeight() - bmp2.getHeight()) / 2, null);
        return bmOverlay;

    }

}
