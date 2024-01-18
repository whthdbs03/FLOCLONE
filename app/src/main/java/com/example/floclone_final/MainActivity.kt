package com.example.floclone_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.floclone_final.databinding.ActivityMainBinding
import com.example.floclone_final.fragments.HomeFragment
import com.example.floclone_final.fragments.LockerFragment
import com.example.floclone_final.fragments.LookFragment
import com.example.floclone_final.fragments.MyFragment
import com.example.floclone_final.fragments.SavedSongFragment
import com.example.floclone_final.fragments.SearchFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private var firstId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_FLOCLONE_final)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
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
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("title",song.title) //putExtra이용하여 제목을 "title"이라는 값으로 전달
//            intent.putExtra("singer",song.singer)
//            intent.putExtra("second",song.second)
//            intent.putExtra("playTime",song.playTime)
//            intent.putExtra("isPlaying",song.isPlaying)
//            intent.putExtra("music",song.music)
//            startActivity(intent)
            //startActivity(Intent(this, SongActivity::class.java))

            // 이제 인텐트로 넘기지 말고 걍 id 값 넘겨주면 되겠지?
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setMiniPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //셰어드프리퍼런스자체의이름
//        val songJson = sharedPreferences.getString("songData",null) //송이라는 셰어드프리퍼런스 내부의 어떤 스트링의 키값
//
//        song = if(songJson == null){ // 데이터가 없을땐 걍 지정
//            Song("라일락", "아이유(IU)", 0,60, false, "aprilshower")
//        } else {
//            gson.fromJson(songJson, Song::class.java) //jgon으로 저장되어있으니까 song자바객체로 변환
//        }

        // 송데이터 초기화 하기
        val spf = getSharedPreferences("song", MODE_PRIVATE) //셰어드프리퍼런스자체의이름
        var songId = spf.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!


        song = if (songId == 0) { // 만약 songID값이 0이면 걍 리스트 처음 거 넣어줘
            songDB.songDao().getSong(firstId)//13 1
        } else{ // 그게 아니면 해당 id값을 찾아 넣어줘
            songDB.songDao().getSong(songId)
        }
        Log.d("song ID", song.id.toString())

        setMiniPlayer(song)
    }

    private fun initBottomNav() {
        switchFragment(HomeFragment()) // 맨 첫 화면

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> switchFragment(HomeFragment())
                R.id.my -> switchFragment(LockerFragment())
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

    private fun allDelete(){
        val songDB = SongDatabase.getInstance(this)!! //db의인스턴스 받아와주고
        songDB.songDao().deleteAll()
    }
    // db가 비었으면 데이터 넣어주기
    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!! //db의인스턴스 받아와주고
        // 지금 db에 송데이터가 들어있는지 확인하기위해선
        // 송db의 데이터들을 전부 받아와줘야함 -> songDao에 메서드 하나 추가 getSongs
        val songs = songDB.songDao().getSongs() // 현재 테이블에 있는 데이터들 몽땅 가져온 것
        // ㄴ얘가 비어있다면 더미데이터 넣어주고 아니면 걍... 냅둬야지
        //allDelete()


        if (songs.isNotEmpty()) {
            firstId = songs[0].id
            Log.d("ID", firstId.toString() + songs[0].id.toString())
            return
        }

        // 걍 더미데이터 넣기
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "aprilshower",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "dust",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "fire",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "fuck_my_life",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "i_dont_understand_but_i_luv_u",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "sonogong",
                R.drawable.img_album_exp5,
                false,
            )
        )

        // 데이터 잘 들어왔는지 확인용 한번더 데ㅣ니터 받아와주기
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
}