package com.example.floclone_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.floclone_final.databinding.ActivityMainBinding
import com.example.floclone_final.fragments.HomeFragment
import com.example.floclone_final.fragments.LookFragment
import com.example.floclone_final.fragments.MyFragment
import com.example.floclone_final.fragments.SearchFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_FLOCLONE_final)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNav()

        // 얜 이제 필요없지 셰어드 프리퍼런스 값 가져올거니까...
//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(),
//            binding.mainMiniplayerSingerTv.text.toString(),0,60,false,
//            "aprilshower")
        //song 데이터 클래스에 맞는 값으로 위 텍스트뷰의 텍스트값을 저장함
        //잘 저장했는지 확인하기위해 로그를 사용 로그; 안드로이드 어플 개발하면서 데이터가 잘 오는지 잘 저장되었는지 확인하기위해 많이 사용
        // 로그캣에서 로그라는 메서드가 실행될 때 확인할 수 있음
        // 사용방법 ㄱ 로그캣에서 해당 태그를 검색하여 찾아볼 수 있음
        Log.d("Song", song.title + song.singer) // 타이틀과 싱어의 텍스트값이 Song에 잘 저장되었는가? 확인
        // 실행하고 로그캣 열어서 Song 찾아보면 라일락아이유 되어있음 ㅇㅇ
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title",song.title) //putExtra이용하여 제목을 "title"이라는 값으로 전달
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            startActivity(intent)
            //startActivity(Intent(this, SongActivity::class.java))
        }
    }
    private fun setMiniPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //셰어드프리퍼런스자체의이름
        val songJson = sharedPreferences.getString("songData",null) //송이라는 셰어드프리퍼런스 내부의 어떤 스트링의 키값

        song = if(songJson == null){ // 데이터가 없을땐 걍 지정
            Song("라일락", "아이유(IU)", 0,60, false, "aprilshower")
        } else {
            gson.fromJson(songJson, Song::class.java) //jgon으로 저장되어있으니까 song자바객체로 변환
        }
        setMiniPlayer(song)
    }

    private fun initBottomNav() {
        switchFragment(HomeFragment()) // 맨 첫 화면

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> switchFragment(HomeFragment())
                R.id.my -> switchFragment(MyFragment())
                R.id.look -> switchFragment(LookFragment())
                R.id.search -> switchFragment(SearchFragment())
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        //프래그먼트 교체해주는 메서드
        supportFragmentManager.beginTransaction().replace(R.id.main_fl, fragment)
            .commitAllowingStateLoss()
    }
}