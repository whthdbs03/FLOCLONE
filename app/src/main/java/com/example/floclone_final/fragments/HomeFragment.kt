package com.example.floclone_final.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone_final.Album
import com.example.floclone_final.AlbumRVAdapter
import com.example.floclone_final.BannerVPAdapter
import com.example.floclone_final.MainActivity
import com.example.floclone_final.R
import com.example.floclone_final.SongActivity
import com.example.floclone_final.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas= ArrayList<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


//        binding.todayAlbumExpLl.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_fl,AlbumFragment()).commitAllowingStateLoss()
//        }
        // ArrayList에 데이터 담기
        // 데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("제목","가수",R.drawable.img_album_exp))
            add(Album("제목","가수",R.drawable.img_album_exp2))
            add(Album("제목","가수",R.drawable.img_album_exp3))
            add(Album("제목","가수",R.drawable.img_album_exp4))
            add(Album("제목","가수",R.drawable.img_album_exp5))
            add(Album("제목","가수",R.drawable.img_album_exp6))
        } //원래는 데이터 서버에서 받아와야하는데 걍 일단 더미데이터로 하나하나 ㄱㄱ

        val albumRVAdapter= AlbumRVAdapter(albumDatas) // 어댑터와 데이터 연결
        binding.todayReleaseMusicRv.adapter = albumRVAdapter // 너가 사용해야할어댑터는 이거야

        // 수평배치할것임
        binding.todayReleaseMusicRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

        })

        val bannerAdapter = BannerVPAdapter(this)
        // 우리가 만든 배너프래그먼트 넣어줌
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        //뷰페이저와 어댑터 연결
        binding.viewpaperFirstVp.adapter = bannerAdapter
        // 뷰페이저가 좌우로 스크롤
        binding.viewpaperFirstVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_fl, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                    // 번들에 Gson을 활용해서 객체를 Json형태로 변환해서 한번에 담아줌
                } // 이 번들을 arguments에 담아줌
            }).commitAllowingStateLoss()
    }


}