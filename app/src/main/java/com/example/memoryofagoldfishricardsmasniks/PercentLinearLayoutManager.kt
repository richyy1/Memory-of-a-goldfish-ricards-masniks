package com.example.memoryofagoldfishricardsmasniks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

open class PercentLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    // fragments to be measured based on custom layout params
    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        val pp = child.layoutParams as PercentParams
        if (pp.maxPercentHeight <= 0.0f)
            super.measureChildWithMargins(child, widthUsed, heightUsed)
        else {
            val widthSpec = getChildMeasureSpec(width, widthMode,
                paddingLeft + paddingRight + pp.leftMargin + pp.rightMargin + widthUsed, pp.width,
                canScrollHorizontally())
            val maxHeight = (height * pp.maxPercentHeight).toInt()
            val heightSpec = when (pp.height) {
                ViewGroup.LayoutParams.MATCH_PARENT -> View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.EXACTLY)
                ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
                else -> View.MeasureSpec.makeMeasureSpec(min(pp.height, maxHeight), View.MeasureSpec.AT_MOST)
            }
            child.measure(widthSpec, heightSpec)
        }
    }

    // everything below is needed to generate custom params
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams) = lp is PercentParams

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return PercentParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return PercentParams(lp)
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return PercentParams(c, attrs)
    }

    class PercentParams : RecyclerView.LayoutParams {
        /** Max percent height of recyclerview this item can have. If height is `match_parent` this size is enforced. */
        var maxPercentHeight = 0.0f

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs){
            val t = c.obtainStyledAttributes(attrs, R.styleable.PercentLinearLayoutManager_Layout)
            maxPercentHeight = t.getFloat(R.styleable.PercentLinearLayoutManager_Layout_maxPercentHeight, 0f)
            t.recycle()
        }
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
        constructor(source: RecyclerView.LayoutParams?) : super(source)
    }
}