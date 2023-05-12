package com.dan.musicapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dan.musicapp.R
import com.dan.musicapp.entites.Song

class SongAdapter(private val context: Context,var listSong: ArrayList<Song>): RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rv,parent,false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.itemView.apply {
            val image = findViewById<ImageView>(R.id.imgNote)
            val title = findViewById<TextView>(R.id.txtTitle)
            val duration = findViewById<TextView>(R.id.txtNote)
            val size = findViewById<TextView>(R.id.txtDate)
            val  artworkUri =  listSong[position].artworkUri

            image.setImageURI(artworkUri)
            title.text = listSong[position].title
            duration.text = listSong[position].duration.toString()
            size.text = listSong[position].size.toString()
        }
        holder.itemView.setOnClickListener {
            Toast.makeText(context,listSong[position].title,Toast.LENGTH_LONG).show()
        }
    }


    override fun getItemCount(): Int {
        return listSong.size
    }

//    fun searchSong(filter)
}