package com.example.floclone_final

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.floclone_final.fragments.DetailFragment
import com.example.floclone_final.fragments.SongFragment
import com.example.floclone_final.fragments.VideoFragment

class AlbumVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // 우린 세 개의 프래그먼트를 만들것이기 때문.

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}