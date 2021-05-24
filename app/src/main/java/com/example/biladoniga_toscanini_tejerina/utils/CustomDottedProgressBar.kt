package com.example.biladoniga_toscanini_tejerina.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import com.example.biladoniga_toscanini_tejerina.R
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class CustomDottedProgressBar : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    //Normal dot radius
    private val dotRadius = 10F

    //Expanded Dot Radius
    private val bounceDotRadius = 15F

    //to get identified in which position dot has to expand its radius
    private var dotPosition = 0

    //specify the circle radius
    private val circleRadius = 50

    // specify the sizes of dots
    private val dotRadiusList = arrayListOf(2F, 3F, 4F, 5F, 6F, 7F, 8F, 9F, 10F)

    //specify how many dots you need in a progressbar
    private val dotAmount = dotRadiusList.size

    private val progressPaint = Paint()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //Animation called when attaching to the window, i.e to your screen
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //take the point to the center of the screen
        canvas.translate((this.width / 2).toFloat(), (this.height / 2).toFloat())
        progressPaint.color = context.getColor(R.color.black)

        //call create dot method
        createDotInCircle(canvas, progressPaint)
    }

    private fun createDotInCircle(canvas: Canvas, progressPaint: Paint) {
        //angle for each dot angle = (360/number of dots) i.e  (360/10)
        val angle = 360 / dotAmount
        for (i in 0 until dotAmount) {
            // angle should be in radians  i.e formula (angle *(Math.PI / 180))
            val x = (circleRadius * cos(angle * i * (Math.PI / 180))).toFloat()
            val y = (circleRadius * sin(angle * i * (Math.PI / 180))).toFloat()
            if (i == dotPosition) {
                canvas.drawCircle(x, y, bounceDotRadius, progressPaint)
            } else {
                canvas.drawCircle(x, y, dotRadiusList[i], progressPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //Dynamically setting width and height to progressbar 100 is circle radius, dotRadius * 3 to cover the width and height of Progressbar
        val width = 100 + dotRadius * 3
        val height = 100 + dotRadius * 3

        //MUST CALL THIS
        setMeasuredDimension(width.roundToInt(), height.roundToInt())
    }

    private fun startAnimation() {
        val bounceAnimation = BounceAnimation()
        bounceAnimation.duration = 120
        bounceAnimation.repeatCount = Animation.INFINITE
        bounceAnimation.interpolator = LinearInterpolator()
        bounceAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {
                dotPosition++
                //when dotPosition == dotAmount , then start again applying animation from 0th position , i.e  dotPosition = 0;
                if (dotPosition > dotAmount) {
                    dotPosition = 0
                }
            }
        })
        startAnimation(bounceAnimation)
    }

    private inner class BounceAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            //call invalidate to redraw your view again.
            invalidate()
        }
    }}