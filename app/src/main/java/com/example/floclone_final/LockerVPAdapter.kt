package com.example.floclone_final

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.floclone_final.fragments.MusicFileFragment
import com.example.floclone_final.fragments.SavedSongFragment

class LockerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            else -> MusicFileFragment()
        }
    }
}