package com.example.floclone_final.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floclone_final.LockerVPAdapter
import com.example.floclone_final.LoginActivity
import com.example.floclone_final.R
import com.example.floclone_final.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일") // 탭레이아웃에 띄울
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater,container,false)

        // 뷰페이저와 어댑터 연결
        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        // 탭레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        // 액티비티 전환
        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(context,LoginActivity::class.java))
        }

        return binding.root
    }


}