package com.dan.musicapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.ContentUris
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dan.musicapp.adapter.SongAdapter
import com.dan.musicapp.databinding.ActivityHomeBinding
import com.dan.musicapp.entites.Song
import java.util.Objects

private lateinit var binding: ActivityHomeBinding
class HomeActivity : AppCompatActivity() {
    private lateinit var songAdapter: SongAdapter
    private lateinit var allSongs: ArrayList<Song>
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<String>
    private val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Objects.requireNonNull(getSup)

        binding.rvHome

        storagePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){granted->
            if(granted){
                FetchSongs();
            }else{
                uReponse()
            }
        }
        storagePermissionLauncher.launch(permission)
    }

    private fun uReponse() {
        if(ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED){
            //fetchSong
        }else
            if(shouldShowRequestPermissionRationale(permission)){
                // show an educational ui to user explaining why we need permisssion
                // user alert diolog
                val dialog = AlertDialog.Builder(this)
                dialog.apply {
                    setTitle("Confirm Requesting Permission")
                    setMessage("Allow us to fetch songs on your device")
                    setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->
                        Toast.makeText(applicationContext,"You denied us to show songs", Toast.LENGTH_LONG).show()
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("Allow"){ dialogInterface: DialogInterface, i: Int ->
                        storagePermissionLauncher.launch(permission)
                    }
                }.show()
            }
    }

    @SuppressLint("Recycle")
    private fun FetchSongs() {
        //define a list to cary songs
        var songs = ArrayList<Song>()
        val mediaStoreUri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mediaStoreUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            mediaStoreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

//        //define projection
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID
        )
        //order
//        var sortOrder = MediaStore.Audio.Media.DATE_ADDED + "DESC"
//        val cursor = contentResolver.query(mediaStoreUri, projection, null, null, sortOrder)
        //get Songs
        try {
            contentResolver.query(
                mediaStoreUri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                //clear the previous loaded before adding loading again
                while (cursor.moveToNext()) {
                    //get the values of a column for a given audio file
                    val id = cursor.getLong(idColumn)
                    var name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)
                    val albumId = cursor.getLong(albumIdColumn)

                    //song uri
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    //ablum artwork uri
                    val albumArtworkUri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), albumId
                    )
                    //remove .mp3 extension from the song's name
                    name = name.substring(0, name.lastIndexOf("."))

                    val song = Song(name, uri, albumArtworkUri, size, duration)
                    songs.add(song)
                }
                showsong(songs)
            }
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
    }

    private fun showsong(songs: ArrayList<Song>) {
        if(songs.size == 0){
            Toast.makeText(this,"No Song", Toast.LENGTH_LONG).show()
            return
        }

        allSongs.clear()
        allSongs.addAll(songs)

        binding.rvHome.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        songAdapter = SongAdapter(this,songs);
        binding.rvHome.adapter = songAdapter
    }
}
