package com.example.floclone_final

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone_final.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged() // 어댑터에게 꼭 아이템추가됐ㄹ다고 알려야함!!!
    }
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
    // d이것응ㄴ 소윤이 브랜치
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        //뷰홀더를 생성해줄때 호출되는 함수 1. 사용하고자하는 아이템뷰 객체 생성
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        //여기서 아이템뷰객체를 만든 뒤, 재활용하기위해 뷰홀더에 던져줌
        return ViewHolder(binding) //2. 아이템뷰 객체를 뷰홀더에 던져줌
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) { //position = 인덱스 아이디
        // 뷰홀더에 데이터를 바인딩해줄때마다 호출되는 함수
        // 따라서 사용자가 화면을 위아래로 스크롤할때마다 엄청많이 호출됨
        // 받아온 뷰홀더에 바인딩해주기위해 앨범리스트에서 해당포지션인 데이터를 뷰홀더에 바인드 함수에 던져주면 됨
        holder.bind(albumList[position])

        // 클릭이벤트는 position이 있는 여기에서
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
//        holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    // 데이터셋 크기 알려주는 함수
    // 리사이클러뷰가 마지막이 언제인지를 알려주는 함수
    // 받아온 앨범리스트의 크기를 넣어주면 됨
    override fun getItemCount(): Int = albumList.size

    // 아이템뷰 객체들을 생성하고 날아가지않도록 잡고있는 애 -> 매개변수로 아이템뷰 객체 받기
    // 뷰홀더클래스 상속받기 ~ 이건 이렇게 하는구나 까지만 ...
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}