package com.example.floclone_final

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable") // 사용자 이메일, 패스워드, 유저인덱스
data class User(
    var email: String,
    var password : String
){
    @PrimaryKey(autoGenerate = true) var id : Int=0
} // 사용자 추가될 때마다 자동으로 유저 아이디 생성
