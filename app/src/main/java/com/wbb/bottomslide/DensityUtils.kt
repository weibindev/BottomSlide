package com.wbb.bottomslide

import android.content.Context
import android.util.TypedValue

/**
 * 屏幕密度像素工具类
 * @author vico
 * @date 2019/1/23
 * email: 1005078384@qq.com
 */
object DensityUtils {
    /**
     * dp转px
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * sp转px
     */
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * px转dp
     */
    fun px2dp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.density;
        return (pxVal / scale)
    }

    /**
     * px转sp
     */
    fun px2sp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return pxVal / scale
    }
}