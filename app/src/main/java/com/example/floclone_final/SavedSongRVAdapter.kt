package com.example.floclone_final

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone_final.databinding.ItemSongBinding

class SavedSongRVAdapter() :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) { // 좋아요한노래들을 모두 추가하는 식으로 사용 -> isLike = True만 가져와야지? -> 쿼리문 필요
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더 필수
    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song){
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
            binding.itemSongTitleTv.text=song.title
            binding.itemSongSingerTv.text=song.singer
        }
    }

    // 기본 리사이클러뷰 어댑터 함수
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = songs.size


    interface MyItemClickListener{
        fun onRemoveSong(songId: Int) // id값을 입력받아서 송의 id값을 false로 바꿔줄것임
    }
    private lateinit var mItemClickListener: MyItemClickListener // 클릭리스너 변수

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }// 이 클릭리스너를 구체화 할 셋함수

    // 이 아이템이 눌렸을 때 이 리스너가 동작하게하기 위해서 클릭리스너에 호출해줌
    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position]) // 데이터바인딩할때마다 불리는 함수이므로 바인드함수에 포지션넣어서 주면됨

        //클릭리스너는 포지션이 있는 여기서 구현
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position) // 뷰에서 삭제
        }
    }
}