package com.alwin.youtubemobileplayer.videoListUI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alwin.youtubemobileplayer.videoPlayerUI.PlayVideoActivity
import com.alwin.youtubemobileplayer.YT_VIDEO_NAME
import com.alwin.youtubemobileplayer.YT_VIDEO_URL
import com.alwin.youtubemobileplayer.videoModel.Video
import com.alwin.youtubemobileplayer.videoRecordUI.AddVideoActivity
import com.alwin.youtubemobileplayer.videoRecordUI.VIDEO_NAME
import com.alwin.youtubemobileplayer.videoRecordUI.VIDEO_URL
import com.alwin.youtubemobileplayer.videoModel.VideoDatabase
import com.alwin.youtubemobileplayer.databinding.VideoListBinding
import com.alwin.youtubemobileplayer.videoModel.VideoDeleteViewModel
import kotlinx.android.synthetic.main.video_list.*

/**
 * List fragment that maps functions from [VideoListAdapter] to button on [VideoList]
 * Listens for incoming intent from [IntentReceiver] and adds them to list
 */

class VideoList : Fragment() {
    private val TAG: String = "com.alwin.youtubemobileplayer.videoList.VideoList"
    private lateinit var videoDeleteViewModel: VideoDeleteViewModel
    private lateinit var videoAdditionViewModel: VideoAdditionViewModel
    private val newVideoActivityRequestCode = 1
    var video: Video? = null

    private val adapter = VideoListAdapter(

            onEdit = { video ->
                findNavController().navigate(
                        VideoListDirections.actionVideoListToVideoEntryDataFragment(video.id)
                )
                Log.i(TAG, "User editing entry with id = $id")
            },

            onDelete = { video ->
                NotificationManagerCompat.from(requireContext()).cancel(video.id.toInt())
                videoDeleteViewModel.delete(video)
            },

            onVideoClick = { video ->
                val sendIntent: Intent = Intent(context, PlayVideoActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, video.videoUrl)
                    type = "text/plain"
                }
                Log.i(TAG, "url sent " + video.videoUrl)
                startActivity(sendIntent)
            }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = VideoListBinding.bind(view)
        val videoDao = VideoDatabase.getDatabase(requireContext()).videoDao()
        Log.i(TAG, "Video list created, listening for incoming intent")
        videoDeleteViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoDeleteViewModel::class.java)

        videoDeleteViewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.submitList(videos)
        }
        videoAdditionViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoAdditionViewModel::class.java)

        if (activity?.intent?.hasExtra(YT_VIDEO_URL) == true) {
            var intent = activity?.intent
                var ytUrl = intent!!.getStringExtra(YT_VIDEO_URL)
                var ytVideoName = intent!!.getStringExtra(YT_VIDEO_NAME)
                Log.i(TAG,"Received $intent, adding to list")
                addVideo(ytVideoName.toString(), ytUrl.toString())
        }

        recyclerView.adapter = adapter
        binding.addVideoButton.setOnClickListener {
            val intent = Intent(context, AddVideoActivity::class.java)
            startActivityForResult(intent,newVideoActivityRequestCode)
            Log.i(TAG, "addVideoButton clicked, starting AddVideoActivity with intent: $intent")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return VideoListBinding.inflate(inflater, container, false).root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        /* Inserts video into viewModel. */
        if (requestCode == newVideoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val videoName = data.getStringExtra(VIDEO_NAME)
                val videoUrl = data.getStringExtra(VIDEO_URL)
                addVideo(videoName.toString(), videoUrl.toString())
            }
        } else if (resultCode == 0) {
            Log.i(TAG, "Return key was pressed / Video name and url are empty")
        }
    }

    private fun addVideo(videoName: String, videoUrl: String){
        videoAdditionViewModel.addVideo(
                video?.id ?: 0,
                videoName,
                videoUrl,
        ) {
        }
    }
}
