package com.example.snake

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView=findViewById(R.id.game_view)
        val appleTv:TextView=findViewById(R.id.apple_tv)
        val maxTv:TextView=findViewById(R.id.max_tv)
        gameView.onEat={
            MediaPlayer.create(this, R.raw.eat).start()
            appleTv.text="x$it"
            if (maxTv.text.toString().replace("x","").toInt()<it)
                maxTv.text="x$it"
        }
        gameView.onFail={
            MediaPlayer.create(this, R.raw.fail).start()
            appleTv.text="x0"
            showAlert()
        }
        showAlert()
    }

    private fun showAlert(){
        val alertView=LayoutInflater.from(this).inflate(R.layout.alert_layout, null)
        val alert=AlertDialog.Builder(this)
            .setView(alertView)
            .create()
        alert.show()
        alertView.findViewById<Button>(R.id.btn_start).setOnClickListener {
            alert.dismiss()
            gameView.startGame()
        }
    }
}