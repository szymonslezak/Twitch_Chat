package pl.edu.uwr.pum.footballapp.kotlin

import android.app.Application
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.ImageRequest
import pl.droidsonroids.gif.GifDrawable
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps

class GifDownloader(val context:Context):Application( ) {

    val imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }
            .build()
    fun get_gif(url: String, name: String, context: Context)
    {
        val request = ImageRequest.Builder(context)
                .data(url)
                .target { drawable ->
                    Log.e("COIL IS HERE","NOT YET")
                    drawable as GifDrawable
                    Log.e("COIL IS HERE","DRAWABLE")
                  //  EmoteMaps.Gifs.put(name, drawable)
                    drawable.start()
                    // Handle the result.
                }
                .build()
        val disposable = imageLoader.enqueue(request)
    }

    fun get_gifs(url: String)
    {
    }


}