package com.example.headup_game


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headup_game.databinding.RowRecyclerviewBinding


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    class RecyclerViewHolder(val binding: RowRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    var celebritiesList: ArrayList<Celebrity.CelebrityItem> = ArrayList()

    fun setCelebrityList(celebritiesArrayList: ArrayList<Celebrity.CelebrityItem>) {
        this.celebritiesList = celebritiesArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(RowRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        var currentCelebrity = celebritiesList[position]
        holder.binding.apply {
            nameTV.text = currentCelebrity.name +"\n"
            tabooTv.text =  currentCelebrity.taboo1 +"\n" + currentCelebrity.taboo2 +"\n" + currentCelebrity.taboo3
        }
    }
    override fun getItemCount() = celebritiesList.size

}
