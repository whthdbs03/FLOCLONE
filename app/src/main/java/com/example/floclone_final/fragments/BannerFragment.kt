package com.example.floclone_final.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floclone_final.R
import com.example.floclone_final.databinding.FragmentAlbumBinding
import com.example.floclone_final.databinding.FragmentBannerBinding


class BannerFragment(val imgRes: Int) : Fragment() {
    lateinit var binding: FragmentBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater,container,false)

        binding.bannerImageIv.setImageResource(imgRes) //인자값으로 받은 이미지로. SRC가 변경됨
        return binding.root
    }
}