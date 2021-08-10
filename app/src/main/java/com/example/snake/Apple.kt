package com.example.snake

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.snake.Consts.Companion.GRASS_SIZE

class Apple(
    val bmp:Bitmap,
    val x:Int,
    val y:Int
) {
    val rect:Rect
        get() {
            return Rect(x,y, x+GRASS_SIZE, y+ GRASS_SIZE)
        }
}