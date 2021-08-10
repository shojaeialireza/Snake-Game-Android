package com.example.snake

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.snake.Consts.Companion.GRASS_SIZE

class SnakePart(
    var bmp:Bitmap,
    var x:Int,
    var y:Int
) {
    val rBody:Rect
        get() {
            return Rect(x, y, x+GRASS_SIZE, y+ GRASS_SIZE)
        }
    val rTop:Rect
        get() {
            return Rect(x, y-20, x+GRASS_SIZE, y)
        }
    val rBottom:Rect
        get() {
            return Rect(x, y+ GRASS_SIZE, x+GRASS_SIZE, y+ GRASS_SIZE+20)
        }
    val rLeft:Rect
        get() {
            return Rect(x-20, y, x, y+ GRASS_SIZE)
        }
    val rRight:Rect
        get() {
            return Rect(x+ GRASS_SIZE, y, x+GRASS_SIZE+20, y+ GRASS_SIZE)
        }
}