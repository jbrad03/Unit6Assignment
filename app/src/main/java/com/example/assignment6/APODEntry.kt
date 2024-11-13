// APODEntry.kt
package com.example.unit5apiassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class APODEntry(
    val title: String,
    val description: String,
    val imageUrl: String
)

class APODAdapter(private val entries: List<APODEntry>) :
    RecyclerView.Adapter<APODAdapter.APODViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APODViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_apod, parent, false)
        return APODViewHolder(view)
    }

    override fun onBindViewHolder(holder: APODViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int = entries.size

    class APODViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)

        fun bind(entry: APODEntry) {
            titleTextView.text = entry.title
            descriptionTextView.text = entry.description

            Glide.with(itemView.context)
                .load(entry.imageUrl)
                .into(imageView)
        }
    }
}