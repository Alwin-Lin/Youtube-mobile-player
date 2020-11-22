package com.alwin.youtubemobileplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alwin.youtubemobileplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG : String = "com.alwin.youtubemobileplayer"

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        Notifier.init(this)
        if (intent?.action == Intent.ACTION_SEND && intent.type != null && "text/plain" == intent.type){
            handleSharedVideo(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun handleSharedVideo(intent: Intent) {
        var bundle = Bundle()
        var ytUrl: String = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        Log.i(TAG, "MainActivity: Recived intent from Youtube, url: $ytUrl")
        bundle.putString(TAG, ytUrl)
        var frag = VideoEntryDialogFragment()
        frag.arguments = bundle
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, frag)
        fragmentTransaction.commit()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, ytUrl)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
        Log.i(TAG, "MainActivity: Starting video with URL: $ytUrl")
    }
}