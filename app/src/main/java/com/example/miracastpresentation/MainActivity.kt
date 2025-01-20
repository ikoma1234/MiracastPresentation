package com.example.miracastpresentation

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.Display
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var displayManager: DisplayManager
    private var presentation: CustomPresentation? = null

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
        setContentView(R.layout.activity_main)

        // 下画面作成
        tvInputText = findViewById(R.id.tvInputText)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val keyboardLayout: GridLayout = findViewById(R.id.keyboardLayout)

        hiraganaList.forEach { char ->
            val buttonSize = 120  // ボタンのサイズ（ピクセル単位）

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

        // 上画面作成
        displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.displays

        if (displays.size > 1) {
            // 外部ディスプレイが接続されている場合
            showPresentation(displays[1], displayNumber = 1)
        } else {
            Toast.makeText(
                this, "ディスプレイが見つかりません！スタッフにご連絡ください。", Toast.LENGTH_SHORT
            ).show()
        }

        sendButton.setOnClickListener {
            if (tvInputText.text.toString() == "ばなな") {
                showPresentation(displays[1], displayNumber = 2)
            } else {
                showPresentation(displays[1], displayNumber = 3)
            }
            tvInputText.text = ""
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

    private fun showPresentation(display: Display, displayNumber: Int) {
        // 新しいプレゼンテーションを作成して表示
        // 更新遅延防止のため、二回実施
        presentation = CustomPresentation(this, display, displayNumber)
        presentation?.show()
        presentation = CustomPresentation(this, display, displayNumber)
        presentation?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // プレゼンテーションをクリーンアップ
        presentation?.dismiss()
    }

    // カスタムプレゼンテーションクラス
    class CustomPresentation(context: Context, display: Display, private val displayNumber: Int) :
        Presentation(context, display) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // レイアウトを設定
            when (displayNumber) {
                1 -> setContentView(R.layout.presentation_layout_init)
                2 -> setContentView(R.layout.presentation_layout_correct)
                3 -> setContentView(R.layout.presentation_layout_incorrect)
            }
        }
    }
}
