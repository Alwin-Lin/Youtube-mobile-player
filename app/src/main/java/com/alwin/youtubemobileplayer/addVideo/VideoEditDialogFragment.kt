package com.alwin.youtubemobileplayer.addVideo

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.alwin.youtubemobileplayer.data.Video
import com.alwin.youtubemobileplayer.databinding.VideoEntryDialogBinding
import com.alwin.youtubemobileplayer.storage.VideoDatabase
import com.alwin.youtubemobileplayer.videoList.VideoEditViewModel
import com.alwin.youtubemobileplayer.videoList.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Lets user add a new video, if one already exsist, it edits it
 */

class VideoEditDialogFragment : BottomSheetDialogFragment() {
private lateinit var videoEditViewModel: VideoEditViewModel

    private enum class EditingState {
        NEW_VIDEO,
        EXISTING_VIDEO
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val videoDao = VideoDatabase.getDatabase(requireContext()).videoDao()

        videoEditViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoEditViewModel::class.java)

        val binding = VideoEntryDialogBinding.bind(view)

        // Check if video already exist
        var video: Video? = null
        val args: VideoEditDialogFragmentArgs by navArgs()
        val editingState =
                if (args.itemId > 0) EditingState.EXISTING_VIDEO
                else EditingState.NEW_VIDEO

        if (editingState == EditingState.EXISTING_VIDEO) {
            videoEditViewModel.get(args.itemId).observe(viewLifecycleOwner) { videoItem ->
                binding.name.setText(videoItem.videoName)
                binding.description.setText(videoItem.videoUrl)
                video = videoItem
            }
        }

        // Exit out fragment
        binding.cancelButton.setOnClickListener {
            dismiss()
            Log.i(TAG, "VideoEntryDialogFragment: Video Addition canceled")
        }

        // Update once the done button is clicked
        binding.doneButton.setOnClickListener {
            videoEditViewModel.addVideo(
                    video?.id ?: 0,
                    binding.name.text.toString(),
                    binding.description.text.toString(),
            ){}
            Log.i(TAG, "VideoEntryDialogFragment: Adding user input video")
            dismiss()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return VideoEntryDialogBinding.inflate(inflater, container, false).root
    }
}