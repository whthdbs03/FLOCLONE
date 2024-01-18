package com.example.floclone_final

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title: String = "",
    val singer: String = "",

    var second: Int = 0, //몇 분 재생되었는지
    var playTime: Int = 0, // 몇 분짜리 노래인지
    var isPlaying: Boolean = false, //재생되는 중인지

    var music: String = "", // 어떤 음악이 재생되고 있었는지

    var coverImg: Int? = null,
    var isLike: Boolean = false
){
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}
