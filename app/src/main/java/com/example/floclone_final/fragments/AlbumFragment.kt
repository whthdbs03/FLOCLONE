package com.example.floclone_final.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone_final.Album
import com.example.floclone_final.AlbumVPAdapter
import com.example.floclone_final.MainActivity
import com.example.floclone_final.R
import com.example.floclone_final.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {
   lateinit var binding: FragmentAlbumBinding
   private val gson:Gson = Gson()

   private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentAlbumBinding.inflate(layoutInflater)
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album") //알규먼트에서 json꺼내서
        val album = gson.fromJson(albumJson, Album::class.java) // 다시 앨범객체로 변환
        setInit(album) // 바인딩 세팅 ㄱㄱ

        binding.albumTurnBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_fl,HomeFragment()).commitAllowingStateLoss()
        }
        binding.albumCoverIv.setOnClickListener {
            Toast.makeText(activity,"새벽 4시",Toast.LENGTH_SHORT).show()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        binding.albumContentVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 탭레이아웃미디에이터 : 탭레이아웃과 뷰페이저2를 연결하는 중재자.
        TabLayoutMediator(binding.tabTb, binding.albumContentVp){
            // 탭레이아웃에 어떤 텍스트가 들어갈지
            tab, position ->
            tab.text = information[position]
        }.attach() // 탭레이아웃과 뷰페이저를 붙이겠다 ㅇㅇ 어태치
        return binding.root
    }

    private fun setInit(album: Album?) {
        binding.albumTitleTv.text = album!!.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
        binding.albumCoverIv.setImageResource(album.coverImg!!)
    }

}