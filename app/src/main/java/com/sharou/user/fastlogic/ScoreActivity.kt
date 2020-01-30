package com.sharou.user.fastlogic

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {
    private var etMax: EditText? = null
    private var etMin: EditText? = null
    private var etTime: EditText? = null
    var max = 0
    var score: String? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.bestResult -> {
                val intentBestResult = Intent(this, ScoreActivity::class.java)
                startActivity(intentBestResult)
            }
            R.id.settings -> {
                val intentSettings = Intent(this, MainActivity::class.java)
                startActivity(intentSettings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        etMin = findViewById(R.id.editTextMin)
        etMax = findViewById(R.id.editTextMax)
        etTime = findViewById(R.id.editTextTime)
        //val intent = intent

        if (intent != null && intent.hasExtra("result")) {
            val result = intent.getIntExtra("result", 0)
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            max = preferences.getInt("max", 0)
            score = String.format("Ваш результат: %s\nМаксимальный результат: %s", result, max)
            textViewResult.text = score
        }
    }


    fun onClickStartNewGame(view: View) {

        val minValue = etMin!!.text.toString().trim { it <= ' ' }.toInt()
        val maxValue = etMax!!.text.toString().trim { it <= ' ' }.toInt()
        val timeValue = etTime!!.text.toString().trim { it <= ' ' }.toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("minValue", minValue)
        intent.putExtra("maxValue", maxValue)
        intent.putExtra("timeValue", timeValue)
        startActivity(intent)
    }

    fun ClearResult (view: View) {
        //val intent = intent
        val result = intent.getIntExtra("result", 0)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit().remove("max").apply()
        max = 0
        score = String.format("Ваш результат: %s\nМаксимальный результат: %s", result, max)
        textViewResult.text = score
        Toast.makeText(this, "Максимальный результат сброшен", Toast.LENGTH_SHORT).show()
    }
}