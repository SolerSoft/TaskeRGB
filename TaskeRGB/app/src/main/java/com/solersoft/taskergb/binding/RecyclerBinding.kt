package com.solersoft.taskergb.binding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.solersoft.taskergb.R

/*

/**
 * A dynamic {@link RecyclerView.ViewHolder} that performs data binding.
 * @param binding The {link ViewDataBinding} that the view is bound to.
 * @param variableId The binding variable that should be set when the view is updated to a new entity. These values come from the {@link BR} class.
 */
class RecyclerBindingHolder(private val binding : ViewDataBinding, private val variableId : Int) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the View Holder to the specific entity.
     * @param entity The entity to bind to.
     */
    fun bind(entity: Any?) {
        binding.setVariable(variableId, entity)
        binding.executePendingBindings()
    }
}

/**
 * A dynamic {@link RecyclerView.Adapter} that performs data binding.
 * @param layoutId - The resource ID of the view to inflate for items in the view.
 * @param variableId The binding variable that should be set when the view is updated to a new entity. These values come from the {@link BR} class.
 */
class RecyclerBindingAdapter(@IdRes private val layoutId : Int, private val variableId : Int) : RecyclerView.Adapter<RecyclerBindingHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerBindingHolder {

        // Get the inflater for the parents context
        val inflater = LayoutInflater.from(parent.context)

        // Use DataBindingUtil to inflate the resource into a view and associated binding
        val binding : ViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false)

        // Return a binding holder that can be used to update the view when the entity changes
        return RecyclerBindingHolder(binding, variableId)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerBindingHolder, position: Int) {
        // Update binding to new entity
        holder.bind(getItemForPosition(position))
    }

    fun setData(data: Any?) {

        // Notify that the data set has changed
        notifyDataSetChanged()
    }
}

/**
 * Data binding adapters for {@link RecyclerView}
 */
object RecyclerViewAdapters {

    /**
     * Binds a {@link RecyclerView} to new data.
     * @param data The data to bind to.
     */
    @BindingAdapter("android:data")
    @JvmStatic
    fun RecyclerView.setData(data: Any?) {
        val adapter = this.adapter
        if (adapter is RecyclerBindingAdapter) {
            adapter.setData(data)
        }
    }
}

 */