package com.wbb.bottomslide

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.include_main_content.*
import kotlinx.android.synthetic.main.include_main_top.*

/**
 * @author vico
 * @date 2019/1/23
 * email: 1005078384@qq.com
 */
class MainActivity : AppCompatActivity() {

    private var heightPixels: Int = 0
    private var peekHeight: Int = 0
    private var marginTop: Int = 0
    private var offsetDistance: Int = 0
    private lateinit var mHandler: Handler

    companion object {
        const val TAG = "MainActivity.class"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始屏幕相关的参数
        initSystem()
        initView()
        initBehavior()
    }

    private fun initBehavior() {
        val behavior = BottomSheetBehavior.from(nestedScrollView)
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val layoutParams = bottomSheet.layoutParams
                //如果控件本身的Height值就小于返回按钮的高度，就不用做处理
                if (bottomSheet.height > heightPixels - marginTop) {
                    //屏幕高度减去marinTop作为控件的Height
                    layoutParams.height = heightPixels - marginTop
                    bottomSheet.layoutParams = layoutParams
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var distance: Float = 0F;
                /**
                 * slideOffset为底部的新偏移量，值在[-1,1]范围内。当BottomSheetBehavior处于折叠(STATE_COLLAPSED)和
                 * 展开(STATE_EXPANDED)状态之间时,它的值始终在[0,1]范围内，向上移动趋近于1，向下区间于0。[-1,0]处于
                 * 隐藏状态(STATE_HIDDEN)和折叠状态(STATE_COLLAPSED)之间。
                 */

                //这里的BottomSheetBehavior初始化完成后，界面设置始终可见，所以不用考虑[-1,0]区间
                //色差值变化->其实是遮罩的透明度变化，拖拽至最高，顶部成半透明色
                maskView.alpha = slideOffset
                //offsetDistance是initSystem()中获得的，是返回按钮至根布局的距离
                distance = offsetDistance * slideOffset
                //当BottomSheetBehavior由隐藏状态变为折叠状态(即gif图开始的由底部滑出至设置的最小高度)
                //slide在[-1,0]的区间内，不加判断会出现顶部布局向下偏移的情况。
                if (distance > 0) {
                    constraint.translationY = -distance
                }

                Log.i(
                    TAG,
                    String.format(
                        "slideOffset -->>> %s bottomSheet.getHeight() -->>> %s heightPixels -->>> %s",
                        slideOffset,
                        bottomSheet.height,
                        heightPixels
                    )
                )
                Log.i(TAG, String.format("distance -->>> %s", distance))
            }

        })
        mHandler.postDelayed({
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.peekHeight = peekHeight
            ObjectAnimator.ofFloat(nestedScrollView, "alpha", 0f, 1f).setDuration(500).start()
        }, 200)
    }

    private fun initView() {
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.addTab(tabLayout.newTab().setText("费用说明"))
        tabLayout.addTab(tabLayout.newTab().setText("预定须知"))
        tabLayout.addTab(tabLayout.newTab().setText("退款政策"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> frameLayout.setBackgroundColor(Color.parseColor("#ff0000"))
                    1 -> frameLayout.setBackgroundColor(Color.parseColor("#0000ff"))
                    2 -> frameLayout.setBackgroundColor(Color.parseColor("#00ff00"))
                }
            }
        })
        imageView.setOnClickListener { finish() }
        imageView2.setOnClickListener { Toast.makeText(this, "转发", Toast.LENGTH_SHORT).show() }
        imageView3.setOnClickListener { Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show() }
        mHandler = Handler()
    }

    private fun initSystem() {
        //获取屏幕高度
        heightPixels = resources.displayMetrics.heightPixels
        Log.i(TAG, "heightPixels: $heightPixels")

        val behaviorHeight = DensityUtils.px2dp(this, (heightPixels / 2).toFloat())
        peekHeight =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, behaviorHeight, resources.displayMetrics).toInt()
        Log.i(TAG, "peekHeight: $peekHeight")

        imageView.post {
            val lp = imageView.layoutParams as ConstraintLayout.LayoutParams

            //获取状态栏高度
            var statusBarHeight = 0
            val resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            //返回按钮至屏幕顶部的高度
            marginTop = imageView.height + lp.topMargin + lp.bottomMargin / 2 + statusBarHeight
            //返回按钮至根布局的距离
            offsetDistance = lp.topMargin
        }
    }
}
