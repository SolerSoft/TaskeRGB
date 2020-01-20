package com.solersoft.taskergb.binding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.solersoft.taskergb.R
import com.solersoft.taskergb.binding.GlideBindingAdapters.setImage

/**
 * Data binding adapters that use Glide to load images.
 */
object GlideBindingAdapters {
/*
    /**
     * Returns a Glide RequestManager with app default options set.
     */
    private fun glideWithDefaultOptions(context: Context) : RequestManager {

        // Create request options with default images
        val options = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background) // TODO: What's the placeholder image?
                .error(R.drawable.ic_launcher_background) // TODO: What's the error image?

        return Glide.with(context)
                .setDefaultRequestOptions(options)
    }

    /**
     * Loads the source or a default if the source can't be loaded.
     * @param source The source to load.
     */
    private fun ImageView.glideOrDefault(source : Any?) {

        // Placeholder for options
        var builder : RequestBuilder<Drawable>? = null

        // Try to create options by source type
        if (source != null)
        {
            // Get default options
            val options = glideWithDefaultOptions(this.context)

            // By source using Kotlin smart casting
            builder = when (source) {
                is Bitmap -> options.load(source)
                is Int -> options.load(source)
                is String -> options.load(source)
                else -> null
            }
        }

        // Proceed based on builder
        if (builder != null) {
            // We have a builder. Build into image.
            builder.into(this)
        } else {
            // Load default image
            this.setImageResource(android.R.color.transparent);
        }
    }
*/
    /**
     * Binds an ImageView to a Bitmap.
     * @param imageUrl The image Bitmap.
     */
    @BindingAdapter("android:image")
    @JvmStatic
    fun ImageView.setImage(bitmap : Bitmap?) {
        this.load(bitmap)
    }

    /**
     * Binds an ImageView to an image resource.
     * @param imageUrl The ID of the resource.
     */
    @BindingAdapter("android:image")
    @JvmStatic
    fun ImageView.setImage(drawableRes : Int?) {
        if (drawableRes != null) {
            this.load(drawableRes)
        }
    }

    /**
     * Binds an ImageView to a string URL.
     * @param imageUrl The URL of the image.
     */
    @BindingAdapter("android:image")
    @JvmStatic
    fun ImageView.setImage(url : String?) {
        this.load(url)
    }
}