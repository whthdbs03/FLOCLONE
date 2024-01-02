package com.example.floclone_final

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    // 이 클래스 내에서만(=외부에선 데이터변경x) 사용할 변수 (private) ArrayList()로 초기화
    private val fragmentList : ArrayList<Fragment> = ArrayList()

    //이 클래스와 연결된 뷰페이저에게 데이터 전달할 때 데이터 몇 개 전달할 것이냐
    // = fragmentList에 담긴 데이터의 개수
    override fun getItemCount(): Int {
        return fragmentList.size
    }
    // override fun getItemCount(): Int = fragmentList.size
    // 위와 동일한 뜻의 코드

    //fragmentList 내부 아이템들 = fragment들을 생성하는 함수
    override fun createFragment(position: Int): Fragment = fragmentList[position]
    // *position : 0부터 getItemCount 만큼까지 배출 4면  0 1 2 3

    // 맨 처음 함수가 실행될 때 프래그먼트리스트엔 아무것도 없을 것.
    // 홈프래그먼트에서 추가해준 프래그먼트를 사용하기위해 이 함수를 쓴다.
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment) // 인자값으로 받은 프래그먼트를 이 리스트에 추가해줄것이야.
        notifyItemInserted(fragmentList.size -1)
        // 리스트 내 새로운 값이 추가되었을 때, 뷰페이저에게 새값이 추가되었다고 알려줘야 함
        // fragmentList.size -1 = 새값이 추가된 위치.인덱스.
    }
}