package com.example.pokemon_game

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var player : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StartGameButton.setOnClickListener {
            player?.release()
            startActivity(Intent(this,Main2Activity::class.java))
        }
        player = MediaPlayer.create(this,R.raw.title)
        player?.setLooping(true)
        player?.start()
    }
}
