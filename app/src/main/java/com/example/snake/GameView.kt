package com.example.snake

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.snake.Consts.Companion.GAME_VIEW_H
import com.example.snake.Consts.Companion.GAME_VIEW_W
import com.example.snake.Consts.Companion.GRASS_SIZE
import kotlinx.coroutines.*

class GameView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var w = 0
    private var h = 0

    private lateinit var grasses: ArrayList<Grass>

    private lateinit var snake: Snake
    private lateinit var snakeImg:Bitmap

    private var lastX = 0
    private var lastY = 0
    private val minDiff = 150

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var apple: Apple

    private var emtiaz=0
    lateinit var onEat:(emtiaz:Int)->Unit
    lateinit var onFail:()->Unit

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        w = MeasureSpec.getSize(widthMeasureSpec)
        h = MeasureSpec.getSize(heightMeasureSpec)
        w /= GRASS_SIZE
        h /= GRASS_SIZE
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(w * GRASS_SIZE, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(h * GRASS_SIZE, MeasureSpec.EXACTLY)
        )
    }

    override fun onSizeChanged(neww: Int, newh: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(neww, newh, oldw, oldh)
        GAME_VIEW_W=neww
        GAME_VIEW_H=newh
        val grass1 =
            ContextCompat.getDrawable(context, R.drawable.grass)?.toBitmap(GRASS_SIZE, GRASS_SIZE)!!
        val grass2 = ContextCompat.getDrawable(context, R.drawable.grass2)
            ?.toBitmap(GRASS_SIZE, GRASS_SIZE)!!
        grasses = ArrayList()
        for (i in 0 until w) {
            for (j in 0 until h) {
                grasses.add(
                    Grass(
                        if ((i + j) % 2 == 0) grass1 else grass2,
                        i * GRASS_SIZE,
                        j * GRASS_SIZE
                    )
                )
            }
        }

        snakeImg = ContextCompat.getDrawable(context, R.drawable.snakeparts)?.toBitmap(
            14 * GRASS_SIZE, GRASS_SIZE
        )!!
        snake = Snake(snakeImg, w / 2 * GRASS_SIZE, h / 2 * GRASS_SIZE)

        randomApple()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x.toInt()
                lastY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                var head = snake.getSnake()[0]
                if (head.x in 0 until width && head.y in 0 until height) {
                    if (lastX - event.x > minDiff && !snake.moveR) {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                        snake.doFalse()
                        snake.moveL = true
                    } else if (lastX - event.x < -minDiff && !snake.moveL) {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                        snake.doFalse()
                        snake.moveR = true
                    } else if (lastY - event.y > minDiff && !snake.moveB) {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                        snake.doFalse()
                        snake.moveT = true
                    } else if (lastY - event.y < -minDiff && !snake.moveT) {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                        snake.doFalse()
                        snake.moveB = true
                    }
                }
            }
        }
        return true
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        for (g in grasses) {
            canvas?.drawBitmap(g.bmp, g.x.toFloat(), g.y.toFloat(), null)
        }
        drawSnake(canvas)
        if (apple.rect.intersect(snake.getSnake().first().rBody)){
            onEat.invoke(++emtiaz)
            randomApple()
            snake.addPart()
        }
        if (snake.checkCollision()){
            coroutineScope.cancel()
            snake = Snake(snakeImg, w / 2 * GRASS_SIZE, h / 2 * GRASS_SIZE)
            onFail.invoke()
        }
        canvas?.drawBitmap(apple.bmp, apple.x.toFloat(), apple.y.toFloat(), null)
    }

    private fun drawSnake(canvas: Canvas?) {
        snake.updateSnake()
        canvas?.let { c ->
            snake.getSnake().forEach {
                c.drawBitmap(it.bmp, it.x.toFloat(), it.y.toFloat(), null)
            }
        }
    }

    private fun randomApple(){
        val randIdx=(0 until grasses.size-1).random()
        val appleBmp=ContextCompat.getDrawable(context, R.drawable.apple)?.toBitmap(GRASS_SIZE, GRASS_SIZE)!!
        apple= Apple(appleBmp, grasses[randIdx].x, grasses[randIdx].y)
    }

    fun startGame(){
        emtiaz=0
        coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            while (true) {
                invalidate()
                delay(500)
            }
        }
    }
}