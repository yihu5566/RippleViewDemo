package com.example.rippleviewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.roundToInt


class RippleView : View {
    lateinit var mPaint: Paint
    lateinit var pointList: ArrayList<Point>
    var isMeasured: Boolean = false
    var viewWidth: Int = 0
    var viewHeight: Int = 0
    //矩形区域
    lateinit var mRipplePath: Path
    //波峰高度，view的1/2
    var mCrestHeight: Float = 80f
    //波峰长度
    var mCrestWidth: Int = 200
    //波起始点
    var mStartPoint: Float = 0f
    //移动速度
    val SPEED: Float = 5.5f
    //博移动的距离
    var mMoveLen: Float = 0f
    //隐藏一个波长
    var mLeftHide: Float = 0f

    constructor(ctx: Context) : this(ctx, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    val mHandler: Handler = Handler {
        when (it.what) {
            1 -> {
                mMoveLen += SPEED
                for (i in pointList.indices) {
                    pointList.get(i).setX(pointList.get(i).getX() + SPEED)
                    when (i % 4) {
                        0, 2 -> {
                            pointList.get(i).setY(mStartPoint)
                        }
                        1 -> pointList.get(i).setY(mStartPoint + mCrestHeight)
                        3 -> pointList.get(i).setY(mStartPoint - mCrestHeight)
                    }
                }

                if (mMoveLen >= mCrestWidth) {
                    // 波形平移超过一个完整波形后复位
                    mMoveLen = 0f
                    resetPoints()
                }
                invalidate()
            }

        }
        false
    }

    private fun init() {
        pointList = ArrayList()
        mPaint = Paint()
        mPaint!!.color = Color.parseColor("#00ffff")
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL

        mRipplePath = Path()
    }

    fun resetPoints() {
        mLeftHide = -mCrestWidth.toFloat()
        for (i in pointList.indices) {
            pointList.get(i).setX((i * mCrestWidth / 4).toFloat()- mCrestWidth)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isMeasured) {
            isMeasured = true
            viewWidth = measuredWidth
            viewHeight = measuredHeight
            //底部开始
            mStartPoint = measuredHeight.toFloat() / 2
            //波峰高度
            mCrestHeight = viewHeight / 15f
            //波峰长度
            mCrestWidth = measuredWidth / 2
            //隐藏一个波长
            mLeftHide = -mCrestWidth.toFloat()
            //几个波峰
            val n = (viewWidth / mCrestWidth + 0.5).roundToInt()

            for (i in 0..n * 4 + 4) {
                var x = (i * mCrestWidth / 4).toFloat() - mCrestWidth
                var y = 0f
                when (i % 4) {
                    0, 2 -> y = mStartPoint
                    1 -> y = mStartPoint + mCrestHeight
                    3 -> y = mStartPoint - mCrestHeight
                }
                pointList.add(Point(x, y))
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        mRipplePath.reset()
        mRipplePath.moveTo(pointList[0].x, pointList[0].y)
        Log.d("RippleView", pointList.size.toString())

        for (i in 0..(pointList.size - 3) step 2) {
            Log.d("RippleView", i.toString())
            mRipplePath.quadTo(pointList[i + 1].x, pointList[i + 1].y, pointList[i + 2].x, pointList[i + 2].y)
        }
        mRipplePath.lineTo(pointList.get(pointList.size - 1).getX(), viewHeight.toFloat())
        mRipplePath.lineTo(mLeftHide, viewHeight.toFloat())
        mRipplePath.close()
        canvas!!.drawPath(mRipplePath, mPaint)
        mHandler.sendEmptyMessageDelayed(1, 10)
    }

}