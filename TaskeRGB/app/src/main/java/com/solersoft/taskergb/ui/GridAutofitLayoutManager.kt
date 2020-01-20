package com.solersoft.taskergb.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.solersoft.taskergb.R
import java.lang.Exception


/**
 * A {@link GridLayoutManager} that automatically calculates the number of columns.
 * @param context The parent context.
 * @param columnWidth The width of each column in DP.
 * @see https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
 */
class GridAutofitLayoutManager : GridLayoutManager {
    companion object {
        private val TAG = GridAutofitLayoutManager::class.simpleName
        private const val DEFAULT_WIDTH = 48
    }

    private val context: Context
    private var columnWidth: Int
    private var isColumnWidthChanged = true
    private var lastWidth = 0
    private var lastHeight = 0


    constructor(context: Context, columnWidth: Int) : super(context, 1) { /* Initially set spanCount to 1, will be changed automatically later. */
        this.context = context
        this.columnWidth = columnWidth
        checkedColumnWidth()
    }

    /*
    constructor(
            context: Context,
            columnWidth: Int,
            orientation: Int,
            reverseLayout: Boolean) : super(context, 1, orientation, reverseLayout) { /* Initially set spanCount to 1, will be changed automatically later. */
        this.context = context
        this.columnWidth = columnWidth
        checkedColumnWidth()
    }*/

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If columnWidth is not specified in the XML, it defaults to
     * 48dp.
     *
     * [androidx.recyclerview.R.attr.spanCount]
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        // androidx.recyclerview.R.attr.spanCount

        // Store the context
        this.context = context

        // Try to get columnWidth
        this.columnWidth = 0
        try {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.GridAutofitLayoutManager_LayoutParams)
            this.columnWidth = ta.getDimensionPixelSize(R.styleable.GridAutofitLayoutManager_LayoutParams_columnWidth, DEFAULT_WIDTH).toInt()
        }
        catch (e : Exception)
        {
            Log.e(TAG, "Could not load attributes", e)
        }

        // Check
        checkedColumnWidth()
    }

    private fun checkedColumnWidth() {
        if (columnWidth <= 0) {
            columnWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_WIDTH.toFloat(),
                    context.resources.displayMetrics).toInt()
        }
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val width = width
        val height = height
        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {
            val totalSpace = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }
        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}

/*

/**
 * Adapters for auto width.
 */
object GridAutofitLayoutAdapters {
    /**
     * Sets the column width on a RecyclerView.
     * @param newColumnWidth The new column width.
     */
    @BindingAdapter("ss:columnWidth")
    @JvmStatic
    fun RecyclerView.setColumnWidth(newColumnWidth: Int) {
        val layoutManager = this.layoutManager
        if (layoutManager is GridAutofitLayoutManager)
        {
            // Already auto fit. Just update.
            layoutManager.setColumnWidth(newColumnWidth)
        }
        else
        {
            // Make auto fit
            this.layoutManager = GridAutofitLayoutManager(this.context, newColumnWidth)
        }
    }
}

 */