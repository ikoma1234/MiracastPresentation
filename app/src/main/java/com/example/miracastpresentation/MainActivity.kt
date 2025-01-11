package com.example.miracastpresentation

import android.app.Presentation
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.Display
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var displayManager: DisplayManager
    private var presentation: CustomPresentation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // テキストボックスとボタンの参照
        val inputText = findViewById<EditText>(R.id.inputText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        // DisplayManager のインスタンスを取得
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
            val text = inputText.text.toString()
            if (text == "バナナ") {
                showPresentation(displays[1], displayNumber = 2)
            } else {
                showPresentation(displays[1], displayNumber = 3)
            }
            inputText.text.clear() // テキストボックスをクリア
        }
    }

    private fun showPresentation(display: Display, displayNumber: Int) {
        // 新しいプレゼンテーションを作成して表示
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
