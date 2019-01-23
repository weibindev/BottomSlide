# BottomSlide
使用BottomSheetBehavior实现仿美团拖拽效果
> 前几天看到一片文章，文章的标题是[Android 仿美团拖拽效果](https://www.jianshu.com/p/92180b45aaf7)，抱着好奇心去看了下，效果确实不错，但实现过程较为复杂。用原生的CoordinatorLayout+BottomSheetBehavior可以快速的实现这一效果，所以心血来潮打算水一篇文章。<br>demo地址：[https://github.com/weibindev/BottomSlide](https://github.com/weibindev/BottomSlide)

`注：`本文的所有图文素材均来自[https://www.jianshu.com/p/92180b45aaf7](https://www.jianshu.com/p/92180b45aaf7)

先来看下成品效果：

![效果图.gif](https://upload-images.jianshu.io/upload_images/8518082-48e41f9026b4db59.gif?imageMogr2/auto-orient/strip)

#### 实现思路分析：
1. 界面上可以分为两个部分：顶部部分包括一系列的图片和按钮控件；底部部分是用NestedScrollView包裹实现的界面,并且指定 `app:layout_behavior="@string/bottom_sheet_behavior"`。界面根布局采用CoordinatorLayout，与BottomSheetBehavior包装底部部分的布局实现拖拽。
2. 当界面初始化时，BottomSheetBehavior以淡入的方式平滑至设定的最小高度。在BottomSheetBehavior拖拽过程中，通过代码改变View的layoutParams属性使其达到所能拖拽的最大高度。
3. 除去底部部分初始化淡入的过程，其余时间顶部部分都会发生色差值和视图偏移的变化。

#### 界面布局：
`activity_main.xml`
```XML
<android.support.design.widget.CoordinatorLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

    <include layout="@layout/include_main_top"/>

    <include layout="@layout/include_main_content"/>

</android.support.design.widget.CoordinatorLayout>
```
`include_main_top.xml`

```XML
<android.support.constraint.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/constraint"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

    <ImageView
            android:id="@+id/imageView1"
            android:layout_width="match_parent"
            android:layout_height="350dp"
            android:scaleType="centerCrop"
            android:src="@mipmap/icon_tour_background"
            android:transitionName="cardView"/>

    <ImageView
            android:id="@+id/imageView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="25dp"
            android:padding="10dp"
            android:src="@mipmap/icon_back_white"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>

    <ImageView
            android:id="@+id/imageView2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="25dp"
            android:padding="10dp"
            android:src="@mipmap/icon_tour_forward"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>

    <ImageView
            android:id="@+id/imageView3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="25dp"
            android:padding="10dp"
            android:src="@mipmap/icon_tour_loved"
            app:layout_constraintEnd_toStartOf="@+id/imageView2"
            app:layout_constraintTop_toTopOf="parent"/>
    
     <!--这里为了方便，使用半透明色遮罩的方式来代替色差值的实现-->
    <View
            android:id="@+id/maskView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:alpha="0"
            android:background="#80000000"/>

</android.support.constraint.ConstraintLayout>
```
`include_main_content.xml`

```XML
<android.support.v4.widget.NestedScrollView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/nestedScrollView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/bg_cardview_white_top"
        app:layout_behavior="@string/bottom_sheet_behavior"
        android:overScrollMode="never">

    <android.support.constraint.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingStart="16dp"
            android:paddingEnd="16dp">

        <android.support.v7.widget.CardView
                android:id="@+id/cardView"
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:layout_margin="20dp"
                android:layout_marginTop="8dp"
                android:layout_marginBottom="8dp"
                android:foreground="?attr/selectableItemBackground"
                app:cardCornerRadius="25dp"
                app:cardElevation="1dp"
                app:layout_constraintBottom_toBottomOf="@+id/view"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="@+id/textView">

            <ImageView
                    android:id="@+id/avatar"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:src="#59cebe"
                    android:transitionName="avatar"/>

        </android.support.v7.widget.CardView>

        <TextView
                android:id="@+id/textView"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginTop="15dp"
                android:layout_marginEnd="15dp"
                android:text="一种色彩一座城,多彩摩洛哥自由行、慢行、心灵之旅"
                android:textColor="@color/black"
                android:textSize="16sp"
                android:textStyle="bold"
                app:layout_constrainedWidth="true"
                app:layout_constraintEnd_toStartOf="@id/cardView"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"/>

        <TextView
                android:id="@+id/textView1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:alpha="0.8"
                android:drawableStart="@mipmap/icon_tour_location"
                android:drawablePadding="4dp"
                android:text="摩洛哥"
                android:textColor="@color/black"
                android:textSize="12sp"
                app:layout_constraintTop_toBottomOf="@id/textView"/>

        <TextView
                android:id="@+id/textView2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="15dp"
                android:alpha="0.8"
                android:drawableStart="@mipmap/icon_tour_map"
                android:drawablePadding="4dp"
                android:text="线路玩法"
                android:textColor="@color/black"
                android:textSize="12sp"
                app:layout_constraintBottom_toBottomOf="@+id/textView1"
                app:layout_constraintStart_toEndOf="@+id/textView1"
                app:layout_constraintTop_toTopOf="@+id/textView1"/>

        <TextView
                android:id="@+id/textView3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:drawableStart="@mipmap/icon_tour_label"
                android:drawablePadding="4dp"
                android:text="心灵之旅"
                android:textColor="@color/gray"
                android:textSize="10sp"
                app:layout_constraintTop_toBottomOf="@+id/textView1"/>


        <TextView
                android:id="@+id/textView4"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="15dp"
                android:alpha="0.8"
                android:drawableStart="@mipmap/icon_tour_label"
                android:drawablePadding="4dp"
                android:text="古城"
                android:textColor="@color/gray"
                android:textSize="10sp"
                app:layout_constraintBottom_toBottomOf="@+id/textView3"
                app:layout_constraintStart_toEndOf="@+id/textView3"
                app:layout_constraintTop_toTopOf="@+id/textView3"/>


        <TextView
                android:id="@+id/textView5"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="15dp"
                android:alpha="0.8"
                android:drawableStart="@mipmap/icon_tour_label"
                android:drawablePadding="4dp"
                android:text="清真寺"
                android:textColor="@color/gray"
                android:textSize="10sp"
                app:layout_constraintBottom_toBottomOf="@+id/textView3"
                app:layout_constraintStart_toEndOf="@+id/textView4"
                app:layout_constraintTop_toTopOf="@+id/textView3"/>

        <View
                android:id="@+id/view"
                android:layout_width="match_parent"
                android:layout_height="0.5dp"
                android:layout_marginTop="10dp"
                android:background="@color/background"
                app:layout_constraintTop_toBottomOf="@+id/textView3"/>

        <TextView
                android:id="@+id/textView6"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:text="行程详情"
                android:textColor="@color/black"
                android:textSize="12sp"
                app:layout_constraintTop_toBottomOf="@+id/view"/>

        <ImageView
                android:id="@+id/imageView1"
                android:layout_width="match_parent"
                android:layout_height="200dp"
                android:layout_marginTop="10dp"
                android:scaleType="centerCrop"
                android:src="@mipmap/icon_tour_background"
                app:layout_constraintTop_toBottomOf="@+id/textView6"/>

        <TextView
                android:id="@+id/textView7"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:text="It was a humorously peritous business for both of us.For, before we proceed further,it must be said that the mokey-rope was fast at both ends.fast to Queequeg's broad canvas belt,and fast to my narrow leather one,So tat for better or for worse.所以说这是一场净化心灵之旅"
                android:textColor="@color/black"
                android:textSize="12sp"
                app:layout_constrainedWidth="true"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/imageView1"/>

        <View
                android:id="@+id/view2"
                android:layout_width="match_parent"
                android:layout_height="0.5dp"
                android:layout_marginTop="8dp"
                android:background="@color/background"
                app:layout_constraintTop_toBottomOf="@+id/textView7"/>

        <android.support.design.widget.TabLayout
                android:id="@+id/tabLayout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_constraintTop_toBottomOf="@+id/view2"
                app:tabIndicatorColor="@color/yellow"
                app:tabIndicatorHeight="2dp"
                app:tabMaxWidth="80dp"
                app:tabMinWidth="50dp"
                app:tabPadding="-1dp"
                app:tabSelectedTextColor="@color/black"
                app:tabTextColor="@color/gray"/>

        <FrameLayout
                android:id="@+id/frameLayout"
                android:layout_width="match_parent"
                android:layout_height="420dp"
                android:layout_marginTop="5dp"
                android:background="#aabbcc"
                app:layout_constraintTop_toBottomOf="@+id/tabLayout"/>

    </android.support.constraint.ConstraintLayout>

</android.support.v4.widget.NestedScrollView>
```
在`<android.support.v4.widget.NestedScrollView`中加入了`app:layout_behavior="@string/bottom_sheet_behavior"`。除此之外，还有三个关于BottomSheetBehavior相关的xml属性：

```
app:behavior_hideable 设置此底部工作表在向下滑动时是否可以隐藏。

app:behavior_peekHeight 设置折叠时底部工作表的高度

app:behavior_skipCollapsed  设置此底部工作表在展开一次后是否应在隐藏时跳过折叠状态。除非工作表可隐藏，否则将此设置为true无效。
```
顺便附上BottomSheetBehavior的5种状态：

```
STATE_COLLAPSED：底部页面折叠。
STATE_EXPANDED：底部页面扩展。
STATE_DRAGGING：底部是拖动。
STATE_SETTLING：底层正在沉淀。
STATE_HIDDEN：底部页面是隐藏的。
```
更多关于BottomSheetBehavior的介绍，可以前往官方文档查看：[https://developer.android.com/reference/android/support/design/widget/BottomSheetBehavior](https://developer.android.com/reference/android/support/design/widget/BottomSheetBehavior)

#### 代码实现：
假设为了适配不同的机型，我们需要把底部布局的最小高度设为屏高的一半，所以初始化控件之前，先初始化相关高度的参数
```Kotlin
    //获取屏幕高度
    heightPixels = resources.displayMetrics.heightPixels
    Log.i(TAG, "heightPixels: $heightPixels")
    
    val behaviorHeight = DensityUtils.px2dp(this, (heightPixels / 2).toFloat())
    peekHeight =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, behaviorHeight, resources.displayMetrics).toInt()
    Log.i(TAG, "peekHeight: $peekHeight")
```
最小高度已经搞定了，使用`setPeekHeight`生效。那么最大高度该如何实现呢，我查看文档，发现api并没有提供相关的函数来实现这一功能。这样的话，我们可以改变布局控件自身的高度来实现，也就是layoutParams.heigth。

说是这么说，我们还是得取一个参照物的高度值套进layoutParams.heigth。这里我就采用返回按钮做为参照物，即底部的最大高度不遮挡返回按钮。
```Kotlin
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
```
获取到`marginTop`后在`BottomSheetBehavior.BottomSheetCallback()`回调监听有底部工作的事件。

`BottomSheetBehavior.BottomSheetCallback()`有两个事件
```Java
//在拖动时调用
void onSlide (View bottomSheet, float slideOffset) 
//在改变状态时调用
void onStateChanged (View bottomSheet, int newState)
```
只要底部进行拖拽，其状态就会发生变化，所以在`onStateChanged (View bottomSheet, int newState)`做处理
```Kotlin
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
       
    }

})
```
相应的顶部色差值和偏移值的变化在另一个回调事件`void onSlide (View bottomSheet, float slideOffset) `中处理。
```Kotlin
behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

     override fun onStateChanged(bottomSheet: View, newState: Int) {
       
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
     }
})
```
最后还有`BottomSheetBehavior`的滑出效果：先设置`BottomSheetBehavior`的状态为隐藏，然后调用`Handler`的`postDelayed()`方法设置状态为折叠以及最小高度，当然再加一个属性动画，起到锦上添花的作用。

附上`MainActivity.kt`的全部代码
```Kotlin
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
```
#### 最后：
文章demo地址：[https://github.com/weibindev/BottomSlide](https://github.com/weibindev/BottomSlide)

demo引用素材：
[Android 仿美团拖拽效果](https://www.jianshu.com/p/92180b45aaf7)

参考文章：
[Material Design系列-严振杰](https://blog.csdn.net/yanzhenjie1003/article/details/51946749)
