package com.alwin.youtubemobileplayer

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alwin.youtubemobileplayer.storage.VideoDatabase
import com.alwin.youtubemobileplayer.databinding.VideoListBinding
import kotlinx.android.synthetic.main.video_list.*
import java.util.logging.Level.INFO

// Sets up the video list, contains onclickListener for onEdit, onDelete.

class VideoList : Fragment() {
    private val TAG : String = "com.alwin.youtubemobileplayer"
    private lateinit var videoListViewModel: VideoListViewModel

    private val adapter = VideoListAdapter(
            onEdit = { video ->
                findNavController().navigate(
                        VideoListDirections.actionVideoListToVideoEntryDataFragment(video.id)
                )
                Log.i(TAG, "VideoList: User editing entry")
            },
            onDelete = { video ->
                NotificationManagerCompat.from(requireContext()).cancel(video.id.toInt())
                videoListViewModel.delete(video)
                Log.i(TAG,"VideoList: User deleted entry:" + video.description)
            },

            // todo: Make this send intent, should only contain the URL
            onVideoClick = { video ->
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, video.description)
                    type = "text/plain"
                }
                Log.i(TAG, "VideoList: URL sent " + video.description)
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

        recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                    VideoListDirections.actionVideoListToVideoEntryDataFragment()
            )
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return VideoListBinding.inflate(inflater, container, false).root
    }
}