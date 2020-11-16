package com.alwin.youtubemobileplayer

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alwin.youtubemobileplayer.storage.VideoDatabase
import com.alwin.youtubemobileplayer.databinding.VideoListBinding
import kotlinx.android.synthetic.main.video_list.*

// Sets up the video list, contains onclickListener for onEdit, onDelete.

class VideoList : Fragment() {
    private val TAG : String = "com.alwin.youtubemobileplayer"
    private lateinit var videoListViewModel: VideoListViewModel

    private val adapter = VideoListAdapter(
            onEdit = { video ->
                findNavController().navigate(
                        VideoListDirections.actionVideoListToVideoEntryDataFragment(video.id)
                )
            },
            onDelete = { video ->
                NotificationManagerCompat.from(requireContext()).cancel(video.id.toInt())
                videoListViewModel.delete(video)
            },

            // todo: Make this send intent, should only contain the URL
            onVideoClick = { video ->
                findNavController().navigate(
                        VideoListDirections.actionVideoListToPlayVideoFragment()
                )
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