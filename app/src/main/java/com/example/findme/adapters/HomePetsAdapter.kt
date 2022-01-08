package com.example.findme.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.findme.databinding.DogLayoutBinding
import com.example.findme.databinding.PethomeLayoutBinding
import com.example.findme.models.Dog
import com.example.findme.models.HomePet


class HomePetsAdapter() : RecyclerView.Adapter<HomePetsAdapter.ViewHolder>() {

    var homepets = mutableListOf<HomePet>()
    private lateinit var mListener : HomePetsAdapter.onHomePetItemClickListener

    fun setOnItemClickListener(listener: HomePetsAdapter.onHomePetItemClickListener){
        mListener = listener
    }

    class ViewHolder(val binding: PethomeLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PethomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mListener.onHomePetItemClicked(position, homepets[position])
        }
        holder.binding.address.text = homepets[position].address
        holder.binding.city.text = homepets[position].city
    }

    override fun getItemCount(): Int {
        return homepets.size
    }

    fun addHomePet(homePet: HomePet){
        if(!homepets.contains(homePet)){
            homepets.add(homePet)
        }
        notifyDataSetChanged()
    }

    interface onHomePetItemClickListener{
        fun onHomePetItemClicked(position : Int, homePet: HomePet)
    }
}