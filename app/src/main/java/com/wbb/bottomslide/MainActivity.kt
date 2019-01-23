package com.wbb.bottomslide

import android.animation.ObjectAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
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
        behavior.peekHeight = peekHeight
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val layoutParams = bottomSheet.layoutParams
                if (bottomSheet.height > heightPixels - marginTop) {
                    layoutParams.height = heightPixels - marginTop
                    bottomSheet.layoutParams = layoutParams
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var distance: Float = 0F;
                //slideOffset的值在0-1区间 向上拖拽趋近1，向下拖拽趋近0
                maskView.alpha = slideOffset
                distance = offsetDistance * slideOffset
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
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.peekHeight = peekHeight
            behavior.isHideable = false
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
