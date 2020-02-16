package com.solersoft.taskergb.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.solersoft.taskergb.R
import kotlin.math.ceil
import kotlin.math.roundToInt


/**
 * A {@link GridLayoutManager} that automatically calculates the number of columns.
 * @param context The parent context.
 * @param columnWidth The width of each column in DP.
 * @param rowHeight The height of each row in DP.
 * @see https://stackoverflow.com/questions/26666143/recyclerview-gridlayoutmanager-how-to-auto-detect-span-count
 * @see https://gist.github.com/heinrichreimer/2fcb22f160eefee6f07b
 */
class GridAutofitLayoutManager : GridLayoutManager {
    companion object {
        private val TAG = GridAutofitLayoutManager::class.simpleName
        private const val DEFAULT_HEIGHT = 48
        private const val DEFAULT_WIDTH = 48
    }

    private val context: Context
    private var columnWidth: Int
    // private var itemDecoration: ItemDecoration? = null
    private var isDimensionChanged = true
    private var lastWidth = -1
    private var lastHeight = -1
    private var rowHeight: Int

    constructor(context: Context, columnWidth: Int, rowHeight: Int) : super(context, 1) { /* Initially set spanCount to 1, will be changed automatically later. */
        this.context = context
        this.columnWidth = columnWidth
        this.rowHeight = rowHeight
        checkedRowAndColumn()
    }

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
        this.rowHeight = 0
        try {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.GridAutofitLayoutManager_LayoutParams)
            this.columnWidth = ta.getDimensionPixelSize(R.styleable.GridAutofitLayoutManager_LayoutParams_columnWidth, 0).toInt()
            this.rowHeight = ta.getDimensionPixelSize(R.styleable.GridAutofitLayoutManager_LayoutParams_rowHeight, 0).toInt()
        }
        catch (e : Exception)
        {
            Log.e(TAG, "Could not load attributes", e)
        }

        // Check
        checkedRowAndColumn()
    }

    private fun checkedRowAndColumn() {
        if (columnWidth <= 0) {
            setColumnWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_WIDTH.toFloat(),
                    context.resources.displayMetrics).toInt())
        }

        if (rowHeight <= 0) {
            setRowHeight(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT.toFloat(),
                    context.resources.displayMetrics).toInt())
        }
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isDimensionChanged = true
        }
    }

    fun setRowHeight(newRowHeight: Int) {
        if (newRowHeight > 0 && newRowHeight != rowHeight) {
            rowHeight = newRowHeight
            isDimensionChanged = true
        }
    }

/*
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val width = this.width
        val height = this.height

        if (columnWidth > 0 && width > 0 && height > 0 && (isDimensionChanged || lastWidth != width || lastHeight != height)) {

            val totalSpace = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }

            // Calculate span count (columns that fit entirely in a row)
            val spanCount = if (orientation == LinearLayoutManager.VERTICAL) {
                1.coerceAtLeast(totalSpace / columnWidth) // Has to be 1 or higher
            } else {
                1.coerceAtLeast(totalSpace / rowHeight) // Has to be 1 or higher
            }

            setSpanCount(spanCount)
            isDimensionChanged = false
        }

        // Store values
        lastWidth = width
        lastHeight = height

        super.onLayoutChildren(recycler, state)
    }

 */

    override fun isAutoMeasureEnabled(): Boolean {
        return false
    }

    /*
    override fun onAttachedToWindow(view: RecyclerView?) {
        // Pass to super first
        super.onAttachedToWindow(view)

        // Only if we have a view
        if (view != null) {

            // Create decoration if necessary
            if (itemDecoration == null) {
                var dec = MiddleDividerItemDecoration(view.context, MiddleDividerItemDecoration.ALL)
                dec.setDrawable(view.context.getDrawable(R.drawable.grid_separator))
                itemDecoration = dec
            }

            // Add our decoration
            view.addItemDecoration(itemDecoration!!)
        }
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {

        // Remove item decorators
        if (view != null && itemDecoration != null) {
            view.removeItemDecoration(itemDecoration!!)
        }

        // Pass to super to finish
        super.onDetachedFromWindow(view, recycler)
    }
     */

    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {

        // val widthMode = View.MeasureSpec.getMode(widthSpec)
        // val heightMode = View.MeasureSpec.getMode(heightSpec)
        var widthSize = View.MeasureSpec.getSize(widthSpec)
        var heightSize = View.MeasureSpec.getSize(heightSpec)


        // Avoid unnecessary work
        if (isDimensionChanged || lastWidth != widthSize || lastHeight != heightSize) {

            // Calculate span count
            val newSpanCount = if (orientation == LinearLayoutManager.VERTICAL) {
                1.coerceAtLeast(widthSize / columnWidth) // Has to be 1 or higher
            } else {
                1.coerceAtLeast(heightSize / rowHeight) // Has to be 1 or higher
            }

            // Apply span count change?
            if (newSpanCount != spanCount) {
                spanCount = newSpanCount
            }

            // Store last computed values
            isDimensionChanged = false
            lastWidth = widthSize
            lastHeight = heightSize
        }


        // Calculate width or height based on column or row height
        if (orientation == LinearLayoutManager.HORIZONTAL) {

            // Calculate number of columns needed to render all items
            val cols = ceil( itemCount / spanCount.toDouble()).roundToInt()

            // Calculate total width needed to render all rows
            widthSize = (cols * columnWidth) + paddingLeft + paddingRight

        } else if (orientation == LinearLayoutManager.VERTICAL) {

            // Calculate number of rows needed to render all items
            val rows = ceil( itemCount / spanCount.toDouble()).roundToInt()

            // Calculate total height needed to render all rows
            heightSize = (rows * rowHeight) + paddingBottom + paddingTop
        }

        // Set new measure
        setMeasuredDimension(widthSize, heightSize)
    }

    /*
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        // Avoid unnecessary work
        if (columnWidth > 0 && width > 0 && height > 0 && (isDimensionChanged || lastWidth != width || lastHeight != height)) {

            // Calculate span count
            val newSpanCount = if (orientation == LinearLayoutManager.VERTICAL) {
                1.coerceAtLeast(getHorizontalSpace() / columnWidth) // Has to be 1 or higher
            } else {
                1.coerceAtLeast(getVerticalSpace() / rowHeight) // Has to be 1 or higher
            }

            // Apply span count change?
            if (newSpanCount != spanCount) {
                spanCount = newSpanCount
                isDimensionChanged = false
            }

            // Store last computed values
            lastWidth = width
            lastHeight = height
        }

        // Pass to super to complete
        super.onLayoutChildren(recycler, state)
    }

     */


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?): RecyclerView.LayoutParams? {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams? {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams? {

        /*
        // Calculate width or height based on column or row height
        if (orientation == LinearLayoutManager.HORIZONTAL) {

            // Calculate number of columns needed to render all items
            val cols = ceil( itemCount / spanCount.toDouble()).roundToInt()

            // Calculate total width needed to render all rows
            layoutParams.width = cols * columnWidth

        } else if (orientation == LinearLayoutManager.VERTICAL) {

            // Calculate number of rows needed to render all items
            val rows = ceil( itemCount / spanCount.toDouble()).roundToInt()

            // Calculate total height needed to render all rows
            layoutParams.height = rows * rowHeight
        }

         */

        // Set to column width and row height
        layoutParams.width = columnWidth
        layoutParams.height = rowHeight

        // Return new params
        return layoutParams
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingRight - paddingLeft
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }
}