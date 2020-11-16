package com.alwin.youtubemobileplayer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alwin.youtubemobileplayer.databinding.VideoEntryDialogBinding
import com.alwin.youtubemobileplayer.storage.VideoDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Lets user add a new video, if one already exsist, it edits it
 */
class VideoEntryDialogFragment : BottomSheetDialogFragment() {
    private var KEY = "com.alwin.youtubemobileplayer"

    private lateinit var videoEntryViewModel: VideoEntryViewModel

    private enum class EditingState {
        NEW_VIDEO,
        EXISTING_VIDEO
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val videoDao = VideoDatabase.getDatabase(requireContext()).videoDao()

        videoEntryViewModel = ViewModelProvider(this, ViewModelFactory(videoDao))
                .get(VideoEntryViewModel::class.java)

        val binding = VideoEntryDialogBinding.bind(view)
        val ytArgs = arguments
        val ytURL = ytArgs?.getString(KEY)


        // Check if video exist
        var video: Video? = null
        val args: VideoEntryDialogFragmentArgs by navArgs()
        val editingState =
                if (args.itemId > 0) EditingState.EXISTING_VIDEO
                else EditingState.NEW_VIDEO

        if (editingState == EditingState.EXISTING_VIDEO) {
            videoEntryViewModel.get(args.itemId).observe(viewLifecycleOwner) { videoItem ->
                binding.name.setText(videoItem.name)
                binding.description.setText(videoItem.description)
                video = videoItem
            }
        }

        if (ytURL!= null) {
            val context = requireContext().applicationContext
            val navController = findNavController()

            videoEntryViewModel.addData(
                    video?.id ?: 0,
                    "Waiting Name extraction",
                    ytURL,
            ) { actualId ->
                val arg = VideoEntryDialogFragmentArgs(actualId).toBundle()
                val pendingIntent = navController
                        .createDeepLink()
                        .setDestination(R.id.videoEntryDialogFragment)
                        .setArguments(arg)
                        .createPendingIntent()
                Notifier.postNotification(actualId, context, pendingIntent)
            }
            dismiss()
        }

        // Update once the done button is clicked, sends
        binding.doneButton.setOnClickListener {
            val context = requireContext().applicationContext
            val navController = findNavController()

            videoEntryViewModel.addData(
                    video?.id ?: 0,
                    binding.name.text.toString(),
                    binding.description.text.toString(),
            ) { actualId ->
                val arg = VideoEntryDialogFragmentArgs(actualId).toBundle()
                val pendingIntent = navController
                        .createDeepLink()
                        .setDestination(R.id.videoEntryDialogFragment)
                        .setArguments(arg)
                        .createPendingIntent()
                Notifier.postNotification(actualId, context, pendingIntent)
            }
            dismiss()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
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