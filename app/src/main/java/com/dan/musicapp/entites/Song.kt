package com.dan.musicapp.entites

import android.net.Uri

class Song (
    var title: String,
    var uri: Uri,
    var artworkUri: Uri,
    var size: Int,
    var duration: Int
)