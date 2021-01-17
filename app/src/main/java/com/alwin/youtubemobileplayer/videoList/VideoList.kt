package com.alwin.youtubemobileplayer.videoList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alwin.youtubemobileplayer.YT_VIDEO_NAME
import com.alwin.youtubemobileplayer.YT_VIDEO_URL
import com.alwin.youtubemobileplayer.data.Video
import com.alwin.youtubemobileplayer.addVideo.AddVideoActivity
import com.alwin.youtubemobileplayer.addVideo.VIDEO_NAME
import com.alwin.youtubemobileplayer.addVideo.VIDEO_URL
import com.alwin.youtubemobileplayer.storage.VideoDatabase
import com.alwin.youtubemobileplayer.databinding.VideoListBinding
import kotlinx.android.synthetic.main.video_item.*
import kotlinx.android.synthetic.main.video_list.*


class VideoList : Fragment() {
    private val TAG: String = "com.alwin.youtubemobileplayer.videoList.VideoList"
    private lateinit var videoListViewModel: VideoListViewModel
    private lateinit var videoEditViewModel: VideoEditViewModel
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
                videoListViewModel.delete(video)
            },

            // todo: Make this send intent, should only contain the URL
            onVideoClick = { video ->
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, video.videoUrl)
                    type = "text/plain"
                }
                Log.i(TAG, "VideoList: url sent " + video.videoUrl)
                startActivity(sendIntent)
            }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = VideoListBinding.bind(view)
        val videoDao = VideoDatabase.getDatabase(requireContext()).videoDao()
        videoListViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoListViewModel::class.java)

        videoListViewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.submitList(videos)
        }
        videoEditViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoEditViewModel::class.java)

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

    // Todo: Check for duplicates
    override fun onResume() {
        super.onResume()
        var activityIntent = activity?.intent
        if (activityIntent != null && activityIntent!!.hasExtra(YT_VIDEO_URL)){
            Log.i(TAG, "Received intent $activityIntent")
            onNewIntent(activityIntent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        /* Inserts video into viewModel. */
        if (requestCode == newVideoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val videoName = data.getStringExtra(VIDEO_NAME)
                val videoUrl = data.getStringExtra(VIDEO_URL)
                addVideo(videoName.toString(), videoUrl.toString())
                Log.i(TAG, "Added video, name: $videoName, url: $videoUrl")
            }
        } else {
            Log.i(TAG, "Request code or result code mismatch. \nExpected $newVideoActivityRequestCode ${Activity.RESULT_OK}, got $requestCode $resultCode. \n" +
                    "Exiting to video list")
        }
    }

    private fun onNewIntent(intent: Intent){
        var ytUrl: String = intent.getStringExtra(YT_VIDEO_URL).toString()
        var ytName: String = intent.getStringExtra(YT_VIDEO_NAME).toString()
        addVideo(ytName, ytUrl)
        Log.i(TAG, "Received video. Name $ytName, url: $ytUrl")
    }

    private fun addVideo(videoName: String, videoUrl: String){
        videoEditViewModel.addVideo(
                video?.id ?: 0,
                videoName,
                videoUrl,
        ) {
        }
    }
}
