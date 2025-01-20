package com.example.miracastpresentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KeyboardActivity : AppCompatActivity() {
    private lateinit var tvInputText: TextView
    private val hiraganaList = listOf(
        "わ", "ら", "や", "ま", "は", "な", "た", "さ", "か", "あ",
        "　", "り", "　", "み", "ひ", "に", "ち", "し", "き", "い",
        "を", "る", "ゆ", "む", "ふ", "ぬ", "つ", "す", "く", "う",
        "　", "れ", "　", "め", "へ", "ね", "て", "せ", "け", "え",
        "ん", "ろ", "よ", "も", "ほ", "の", "と", "そ", "こ", "お",
        "゛", "゜", "削除"
    )
    private val alphabetList = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z", " ", " ", " ","削除"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)

        tvInputText = findViewById(R.id.tvInputText)
        val keyboardLayout: GridLayout = findViewById(R.id.keyboardLayout)

        hiraganaList.forEach { char ->
            val buttonSize = 140  // ボタンのサイズ（ピクセル単位）

            val button = Button(this).apply {
                text = char
                textSize = 40f  // 文字サイズ（単位: sp）
                setTextColor(Color.WHITE)  // 文字の色
                setTypeface(typeface, Typeface.BOLD)  // 太字設定
                layoutParams = GridLayout.LayoutParams().apply {
                    width = buttonSize
                    height = buttonSize
                    setMargins(2, 2, 2, 2)  // 余白の設定
                }
                setBackgroundColor(Color.parseColor("#6200EE"))  // ボタンの背景色
                setOnClickListener { onKeyPressed(char) }
            }
            keyboardLayout.addView(button)
        }
    }

    private fun onKeyPressed(char: String) {
        when (char) {
            "削除" -> {
                val currentText = tvInputText.text.toString()
                if (currentText.isNotEmpty()) {
                    tvInputText.text = currentText.dropLast(1)
                }
            }
            "゛" -> addDakuten()
            "゜" -> addHandakuten()
            "　" -> Unit
            " " -> Unit
            else -> tvInputText.append(char)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addDakuten() {
        val text = tvInputText.text.toString()
        if (text.isNotEmpty()) {
            val lastChar = text.last()
            val converted = mapOf(
                'か' to 'が', 'き' to 'ぎ', 'く' to 'ぐ', 'け' to 'げ', 'こ' to 'ご',
                'さ' to 'ざ', 'し' to 'じ', 'す' to 'ず', 'せ' to 'ぜ', 'そ' to 'ぞ',
                'た' to 'だ', 'ち' to 'ぢ', 'つ' to 'づ', 'て' to 'で', 'と' to 'ど',
                'は' to 'ば', 'ひ' to 'び', 'ふ' to 'ぶ', 'へ' to 'べ', 'ほ' to 'ぼ',
                'ぱ' to 'ば', 'ぴ' to 'び', 'ぷ' to 'ぶ', 'ぺ' to 'べ', 'ぽ' to 'ぼ'
            )
            tvInputText.text = text.dropLast(1) + (converted[lastChar] ?: lastChar)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addHandakuten() {
        val text = tvInputText.text.toString()
        if (text.isNotEmpty()) {
            val lastChar = text.last()
            val converted = mapOf(
                'は' to 'ぱ', 'ひ' to 'ぴ', 'ふ' to 'ぷ', 'へ' to 'ぺ', 'ほ' to 'ぽ',
                'ば' to 'ぱ', 'び' to 'ぴ', 'ぶ' to 'ぷ', 'べ' to 'ぺ', 'ぼ' to 'ぽ'
            )
            tvInputText.text = text.dropLast(1) + (converted[lastChar] ?: lastChar)
        }
    }
}
