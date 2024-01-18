package com.example.floclone_final

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, User::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null){
                synchronized(SongDatabase::class){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" //다른 db랑 이름 겹치면 꼬임
                    ).allowMainThreadQueries().build() // 지금은 편의상 메인스레드에 넘겨주엇지만 잘 돌아가려면 이럼 안되겟지?
                } // 스레드에 이 데이터베이스와 통신하는 작업 해주어야함

            }
            return instance
        }
    }
}