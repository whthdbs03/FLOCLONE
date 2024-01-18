package com.example.floclone_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.util.Log
import android.widget.Toast
import com.example.floclone_final.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener{
            signUp()
            finish() // 함수되면 피니쉬 되면서 로그인화면으로 ㄱㄱ되겠지
        }
    }

    private fun getUser(): User { // 사용자가 입력한 값을 가져오는 함수
        val email: String =
            binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString() // editTxt 사용자가 입력가능한 텍스트뷰
        val pwd: String = binding.signUpPasswordEt.text.toString()

        return User(email, pwd)
    }

    private fun signUp() {
        // 입력안하거나 비번 != 비번확인 이거나
        // : 엘리데이션 처리?
        if (binding.signUpIdEt.text.toString()
                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return //함수 끝
        }
        if (binding.signUpPasswordCheckEt.text.toString() != binding.signUpPasswordEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return //함수 끝
        }

        // 정보를 DB에 저장
        val userDB= SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("user",user.toString())
    }
}