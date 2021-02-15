package com.alwin.youtubemobileplayer.videoListUI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alwin.youtubemobileplayer.R
import com.alwin.youtubemobileplayer.videoModel.Video
import com.alwin.youtubemobileplayer.databinding.VideoItemBinding


/**
 * The adapter used by the RecyclerView to display the current list of videos
 * Defines control for [VideoList]
 */
class VideoListAdapter(private var onEdit: (Video) -> Unit, private var onDelete: (Video) -> Unit, private var onVideoClick: (Video) -> Unit) :
        androidx.recyclerview.widget.ListAdapter<Video, VideoListAdapter.VideoListViewHolder>(VideoDiffCallback()) {

    class VideoListViewHolder(
            private val binding: VideoItemBinding,
            private var onEdit: (Video) -> Unit,
            private var onDelete: (Video) -> Unit,
            private var onVideoClick: (Video) -> Unit,
            ) : RecyclerView.ViewHolder(binding.root) {
        private var videoId: Long = -1
        private var nameView = binding.name
        private var url = binding.description
        private var thumbnail = binding.thumbnail
        private var video: Video? = null

        fun bind(video: Video) {
            videoId = video.id
            nameView.text = video.videoName
            url.text = video.videoUrl
            thumbnail.setImageResource(R.drawable.add_video)
            this.video = video
            binding.deleteButton.setOnClickListener {
                onDelete(video)
            }
            // This controls what happen when the item in list is clicked
            binding.root.setOnClickListener {
                onVideoClick(video)
            }
            binding.root.setOnLongClickListener {
                onEdit(video)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {

        return VideoListViewHolder(
                VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onEdit,
                onDelete,
                onVideoClick
        )
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }
}
