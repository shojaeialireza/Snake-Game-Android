package com.example.snake

import android.graphics.Bitmap
import com.example.snake.Consts.Companion.GAME_VIEW_H
import com.example.snake.Consts.Companion.GAME_VIEW_W
import com.example.snake.Consts.Companion.GRASS_SIZE

class Snake(
    var bmp: Bitmap,
    var x: Int,
    var y: Int
) {
    private val partNames = listOf(
        "bottom_left", "bottom_right", "horizontal", "top_left", "top_right",
        "vertical", "head_down", "head_left", "head_right", "head_up", "tail_up", "tail_right",
        "tail_left", "tail_down"
    )
    private val parts = mutableMapOf<String, Bitmap>()
    private val currentParts = mutableListOf<SnakePart>()

    var moveR = true
    var moveL = false
    var moveT = false
    var moveB = false

    init {
        partNames.forEachIndexed { index, s ->
            parts[s] = Bitmap.createBitmap(bmp, index * GRASS_SIZE, 0, GRASS_SIZE, GRASS_SIZE)
        }

        currentParts.add(
            SnakePart(
                parts["head_right"]!!,
                x,
                y
            )
        )
        currentParts.add(
            SnakePart(
                parts["horizontal"]!!,
                currentParts[0].x - GRASS_SIZE,
                y
            )
        )
        currentParts.add(
            SnakePart(
                parts["tail_right"]!!,
                currentParts[1].x - GRASS_SIZE,
                y
            )
        )
    }

    fun getSnake() = currentParts

    fun updateSnake() {
        for (i in currentParts.size - 1 downTo 1) {
            currentParts[i].x = currentParts[i - 1].x
            currentParts[i].y = currentParts[i - 1].y
        }

        when {
            moveR -> {
                if (currentParts[0].x>= GAME_VIEW_W)
                    currentParts[0].x=-GRASS_SIZE
                else
                    currentParts[0].x += GRASS_SIZE
                currentParts[0].bmp = parts["head_right"]!!
            }
            moveL -> {
                if (currentParts[0].x<0)
                    currentParts[0].x= GAME_VIEW_W
                else
                    currentParts[0].x -= GRASS_SIZE
                currentParts[0].bmp = parts["head_left"]!!
            }
            moveT -> {
                if (currentParts[0].y<0)
                    currentParts[0].y= GAME_VIEW_H
                else
                    currentParts[0].y -= GRASS_SIZE
                currentParts[0].bmp = parts["head_up"]!!
            }
            moveB -> {
                if (currentParts[0].y>= GAME_VIEW_H)
                    currentParts[0].y=-GRASS_SIZE
                else
                    currentParts[0].y += GRASS_SIZE
                currentParts[0].bmp = parts["head_down"]!!
            }
        }

        for (i in 1 until currentParts.size - 1) {
            if (currentParts[i].rLeft.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rLeft.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["bottom_left"]!!
            else if (currentParts[i].rRight.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rRight.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["bottom_right"]!!
            else if (currentParts[i].rLeft.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rTop.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rLeft.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rTop.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["top_left"]!!
            else if (currentParts[i].rRight.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rTop.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rRight.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rTop.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["top_right"]!!
            else if (currentParts[i].rLeft.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rRight.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rLeft.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rRight.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["horizontal"]!!
            else if (currentParts[i].rTop.intersect(currentParts[i + 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i - 1].rBody) ||
                currentParts[i].rTop.intersect(currentParts[i - 1].rBody) &&
                currentParts[i].rBottom.intersect(currentParts[i + 1].rBody)
            )
                currentParts[i].bmp = parts["vertical"]!!
        }
        val s = currentParts.size
        when {
            currentParts[s - 1].rRight.intersect(currentParts[s - 2].rBody) ->
                currentParts[s - 1].bmp = parts["tail_right"]!!
            currentParts[s - 1].rLeft.intersect(currentParts[s - 2].rBody) ->
                currentParts[s - 1].bmp = parts["tail_left"]!!
            currentParts[s - 1].rBottom.intersect(currentParts[s - 2].rBody) ->
                currentParts[s - 1].bmp = parts["tail_down"]!!
            currentParts[s - 1].rTop.intersect(currentParts[s - 2].rBody) ->
                currentParts[s - 1].bmp = parts["tail_up"]!!
        }
    }

    fun addPart(){
        val lastPart=currentParts.last()
        when(lastPart.bmp){
            parts["tail_left"]->{
                currentParts.add(
                    SnakePart(parts["tail_left"]!!, lastPart.x- GRASS_SIZE, lastPart.y)
                )
            }
            parts["tail_right"]->{
                currentParts.add(
                    SnakePart(parts["tail_right"]!!, lastPart.x+ GRASS_SIZE, lastPart.y)
                )
            }
            parts["tail_down"]->{
                currentParts.add(
                    SnakePart(parts["tail_down"]!!, lastPart.x, lastPart.y+ GRASS_SIZE)
                )
            }
            parts["tail_up"]->{
                currentParts.add(
                    SnakePart(parts["tail_up"]!!, lastPart.x, lastPart.y- GRASS_SIZE)
                )
            }
        }
    }

    fun checkCollision():Boolean{
        val head=currentParts.first()
        for(i in 1 until currentParts.size){
            if (head.x==currentParts[i].x&&head.y==currentParts[i].y)
                return true
        }
        return false
    }

    fun doFalse() {
        moveR = false
        moveL = false
        moveB = false
        moveT = false
    }
}