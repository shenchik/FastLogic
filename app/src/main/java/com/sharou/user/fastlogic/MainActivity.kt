package com.sharou.user.fastlogic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val options = ArrayList<TextView>()
    private var question: String? = null
    private var rightAnswer = 0
    private var rightAnswerPosition = 0
    private var isPositive = false
    private var min = 0
    private var max = 10
    private var countOfQuestions = 0
    private var countOfRightAnswers = 0
    private var gameOver = false

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
        setContentView(R.layout.activity_main)
        options.add(textViewOpinion0)
        options.add(textViewOpinion1)
        options.add(textViewOpinion2)
        options.add(textViewOpinion3)
        playNext()

        val time = intent.getIntExtra("timeValue", 20000)
        val timer: CountDownTimer = object : CountDownTimer(time.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textViewTimer.text = getTime(millisUntilFinished)
                if (millisUntilFinished < 10000) {
                    textViewTimer.setTextColor(Color.RED)
                    textViewTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP,48f)
                }
            }

            override fun onFinish() {
                gameOver = true
                val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val max = preferences.getInt("max", 0)
                if (countOfRightAnswers >= max) {
                    preferences.edit().putInt("max", countOfRightAnswers).apply()
                }
                val intent = Intent(applicationContext, ScoreActivity::class.java)
                intent.putExtra("result", countOfRightAnswers)
                startActivity(intent)
            }
        }
        timer.start()
    }

    private fun playNext() {
        generateQuestion()
        for (i in options.indices) {
            if (i == rightAnswerPosition) {
                options[i].text = rightAnswer.toString()
            } else {
                options[i].text = generateWrongAnswer().toString()
            }
        }
        val score = String.format("%s / %s", countOfRightAnswers, countOfQuestions)
        textViewScore.text = score
    }

    private fun generateQuestion() {
       // val intent = intent
        min = intent.getIntExtra("minValue", 0)
        max = intent.getIntExtra("maxValue", 10)
        Log.i("value", min.toString())
        Log.i("value", max.toString())
        val a = (Math.random() * (max - min + 1) + min).toInt()
        val b = (Math.random() * (max - min + 1) + min).toInt()
        val mark = (Math.random() * 2).toInt()
        isPositive = mark == 1
        if (isPositive) {
            rightAnswer = a + b
            question = String.format("%s + %s", a, b)
        } else {
            rightAnswer = a - b
            question = String.format("%s - %s", a, b)
        }
        textViewQuestion.text = question
        rightAnswerPosition = (Math.random() * 4).toInt()
    }

    private fun generateWrongAnswer(): Int {
        var result: Int
        do {
            result = (Math.random() * max * 2 + 1).toInt() - (max - min)
        } while (result == rightAnswer)
        return result
    }

    private fun getTime(millis: Long): String {
        var seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        seconds %= 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    fun onClickAnswer(view: View) {
        if (!gameOver) {
            val textView = view as TextView
            val answer = textView.text.toString()
            val chosenAnswer = answer.toInt()
            if (chosenAnswer == rightAnswer) {
                countOfRightAnswers++
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Неверно", Toast.LENGTH_SHORT).show()
            }
            countOfQuestions++
            playNext()
        }
    }
}