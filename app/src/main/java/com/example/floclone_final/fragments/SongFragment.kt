package com.example.floclone_final.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floclone_final.R
import com.example.floclone_final.databinding.FragmentDetailBinding
import com.example.floclone_final.databinding.FragmentSongBinding


class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

}