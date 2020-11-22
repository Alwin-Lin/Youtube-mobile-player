package com.alwin.youtubemobileplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class IntentPlaceholderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_placeholder)

        if (intent?.action == Intent.ACTION_SEND && intent.type != null && "text/plain" == intent.type) {
            var ytUrl: String = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
            val textView: TextView = findViewById(R.id.textView) as TextView
            textView.setOnClickListener {
                textView.text =ytUrl
            }
        }
    }

}