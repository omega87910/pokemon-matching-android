package com.example.pokemon_game

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var titlePlayer : MediaPlayer? = null
    private var player = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var i = Intent(this,Main2Activity::class.java)
        var level = 4
        StartGameButton.setOnClickListener {
            titlePlayer?.release()
            i.putExtra("level",level)
            startActivity(i)
            playSound("press")
        }
        gameSettingButton.setOnClickListener {
            popLayoutBackground.alpha = 0.5f
            settingPopLayout.alpha = 1f
            settingCloseButton.visibility = View.VISIBLE
            playSound("press")
        }
        settingCloseButton.setOnClickListener {
            popLayoutBackground.alpha = 0f
            settingPopLayout.alpha = 0f
            settingCloseButton.visibility = View.INVISIBLE
            playSound("press")
        }
        gameExplainButton.setOnClickListener {
            popLayoutBackground.alpha = 0.5f
            gameExplainPopLayout.alpha = 1f
            gameExplainCloseButton.visibility = View.VISIBLE
            playSound("press")
        }
        gameExplainCloseButton.setOnClickListener {
            popLayoutBackground.alpha = 0f
            gameExplainPopLayout.alpha = 0f
            gameExplainCloseButton.visibility = View.INVISIBLE
            playSound("press")
        }
        radioGroup.setOnCheckedChangeListener {_,id ->radioGroup.checkedRadioButtonId
            when(id){
                R.id.radioButton1 -> {
                    settingPokemonText.text = "10種寶可夢"
                    settingTimeText.text = "180秒"
                    level = 1
                }
                R.id.radioButton2 -> {
                    settingPokemonText.text = "14種寶可夢"
                    settingTimeText.text = "180秒"
                    level = 2
                }
                R.id.radioButton3 -> {
                    settingPokemonText.text = "18種寶可夢"
                    settingTimeText.text = "180秒"
                    level = 3
                }
                R.id.radioButton4 -> {
                    settingPokemonText.text = "22種寶可夢"
                    settingTimeText.text = "180秒"
                    level = 4
                }
            }
        }
        titlePlayer = MediaPlayer.create(this,R.raw.title)
        titlePlayer?.setLooping(true)
        titlePlayer?.start()
    }
    private fun playSound(sound:String){
        player.reset()
        player = MediaPlayer.create(this,when(sound){
            "hit" -> R.raw.hit
            "cancel" -> R.raw.cancel
            "spell" -> R.raw.spell
            "select" -> R.raw.select
            "victory" -> R.raw.victory
            "open" -> R.raw.pokeball_opening
            "ohoh" -> R.raw.ohoh
            "miss" -> R.raw.miss
            "clear" -> R.raw.clear
            "gameover" -> R.raw.gameover
            "press" -> R.raw.press
            else -> R.raw.cancel
        })
        player.start()
    }
}
