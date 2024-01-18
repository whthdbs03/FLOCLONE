package com.example.floclone_final

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Delete
    fun delete(song: Song)

    @Update
    fun update(song: Song)
    // 여기까진 기본 구문
    // 더 필요하면 쿼리문 써서 추가
    @Query("SELECT * FROM SongTable") // 송테이블에서 모든 데이터를 받아와라
    fun getSongs(): List<Song> // 해당 데이터를 리스트에 저장

    @Query("SELECT * FROM SongTable Where id = :id") // where 문을 조건문처럼 사용하여 id 맞는 송데이터 불러오기
    fun getSong(id: Int): Song

    @Query("DELETE FROM SongTable")
    fun deleteAll()

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id= :id") // 매개변수id가 id인 애의 isLike를 업데이트해주겠다
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike= :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>
}