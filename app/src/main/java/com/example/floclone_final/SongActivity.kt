package com.example.floclone_final

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.floclone_final.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
//    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null // 나중에 소멸시켜줄거라 널 사용
    private var gSon: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) //xml에 있는 것들을 우리가 맘껏 쓰게 할 거야!



        initPlayList()
        initSong() // 데이터 받아와주기 + setPlayer()
        Log.d("SongAc", songs[nowPos].title + songs[nowPos].singer+songs[nowPos].playTime)
        initClickListener()
    }

    // 사용자가 포커스 잃으면 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        // 근데 어디까지 재생됐는지 데이터는 남겨야지?
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000 //분단위 ㅇㅇ
        // 내부저장소에 송 데이터 남겨야지? 앱이 종료됐다가 재실행되어도 기존 데이터 사용
        // 무겁고 중요한 데이터는 db, 서버, 파이어베이스에 저장하겟지만... 로그인/비번기억같은 간단한건 이거 사용
        // 셰얼드프리퍼런스 이름, 자기앱에서만 프라이빗하게 사용 (다른 모드쓰면 다른앱에서도 접근가능하게 가능
        // 물론 이런경우 거의 없음 걍 프라이빗하셈)
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // 여기에 데이터 저장해야겠지? 그러려면 에비터/ 가 필요해
        val editor = sharedPreferences.edit() // 에디터
        // 인텐트 넣듯 키밸루값으로 넣으면 됨
        //editor.putString("title",song.title) *6해주면되는데 이거 어느세월에 다해... ->json 쓰자
        //json? 에디터의 데이터표현 표준 ㅇㅇ
        //보통 자바 객체를 전송할 때 json포맷으로 보냄 다른 언어에서도 보통 json 포맷 이용

        // song객체를 json으로 어케 변환시키지? -> 쥐쓴...? 얘는 자바객체<->json 을 간단히 해주는 애
        // 라이브러리 추가해주기 module gradle
//        val songJson = gSon.toJson(songs[nowPos])
//        editor.putString("songData",songJson)

        editor.putInt("songId",songs[nowPos].id)
        editor.apply() // 이거 필수~ 이래야 실제 저장공간에 저장
        // 송액티비티에서 송데이터를 내부 저장소에 저장 완!
    }

    // 낭비 방지
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

    private fun initClickListener(){
        // 화면 전환
        binding.songArrowDownIb.setOnClickListener {
            finish()
        }
        // 버튼 모양 바꾸기
        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        // 노래 앞 뒤 재생
        binding.songSkipNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songSkipPrevIv.setOnClickListener {
            moveSong(-1)
        }

        binding.icLikeOffIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun setLike(isLike: Boolean){ // 눌렸으니 데이터 상태 바꿔야지
        songs[nowPos].isLike = !isLike // -> DB의 값을 업데이트 한 게 아님...
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id) // db값 업데이트

        if(!isLike){ // 모양 바꾸기
            binding.icLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.icLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    private fun moveSong(direct: Int){ // nowPos +-1
        if (nowPos + direct <0){ // 리스트 첫곡인 경우
            Toast.makeText(this,"first Song",Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >=  songs.size){ // 리스트 막곡인 경우
            Toast.makeText(this,"last Song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt() // 타이머 멈춰주고 재시작
        startTimer()
        mediaPlayer?.release() //미디어플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제

        setPlayer(songs[nowPos]) // 다시 세팅
    }
    private fun initPlayList(){ // 송 리스트 데이터 받아오기
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initSong(){ // song 값 받아오기
        //타이틀과 싱어라는 키값이 intent에 있는지 확인
        // 받는 쪽에선 해당 데이터가 올수도있고 안올수도있음
//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second",0)!!,
//                intent.getIntExtra("playTime",0)!!,
//                intent.getBooleanExtra("isPlaying",false)!!,
//                intent.getStringExtra("music")!!
//            )
//        }
        // spf를 이용해서 id 값을 받아와준 후 리스트의 id 값과 비교하여 song 초기화
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId) // 해당 인덱스 저장
        Log.d("now Song ID", songs[nowPos].id.toString())
        startTimer() // 값 받아오자마자 타이머 시작~!
        setPlayer(songs[nowPos]) // 데이터 렌더링
    }

    private fun getPlayingSongPosition(songId: Int): Int{ // 반복문을 이용하여 인덱스 찾기
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    // 받아온 값으로 뷰렌더링 해주기
    private fun setPlayer(song: Song){
        binding.songTitleTv.text = song.title
        binding.singerTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName) //리소스파일받기
        mediaPlayer=MediaPlayer.create(this,music) //미디어플레이어에게 이리소스를 사용할거라 알리기

        binding.albumIv.setImageResource(song.coverImg!!)

        if(song.isLike){
            binding.icLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.icLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    // 버튼 모양 바꾸기
    fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying // 버튼 눌리면 현재 재생상태가 바뀌는게 당연
        timer.isPlaying = isPlaying // 버튼 눌리면 타이머도 바뀌는 게 당연!

        if (isPlaying) {
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying) //객체 생성
        timer.start() // 스레드 시작~!
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){ // 노래재생되는 내내 반복

                    if (second >= playTime){ // 노래 끝나면 재생 끝
                        break
                    }

                    if (isPlaying){
                        sleep(50) //50m 에 한번씩 확인 ㅇㅇ
                        mills += 50

                        runOnUiThread { // 뷰렌더링은 main에 시켜야지?
                            // 해당 시간만큼 프로그래스도 움직여야지 50m
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread { // 뷰렌더링은 main에 시켜야지?
                                // 1000m =1초 지날때마다 타이머 바꿔줘
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
}