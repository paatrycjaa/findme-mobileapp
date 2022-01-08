package com.example.findme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findme.R
import com.example.findme.databinding.DogLayoutBinding
import com.example.findme.models.Dog
import com.bumptech.glide.RequestManager;

class DogsAdapter() : RecyclerView.Adapter<DogsAdapter.ViewHolder>(), Filterable{

    var dogs = mutableListOf<Dog>()
    //var filtered_dogs = mutableListOf<Dog>()
    private lateinit var mListener : onDogItemClickListener

    fun setOnItemClickListener(listener: onDogItemClickListener){
        mListener = listener
    }

    class ViewHolder(val binding: DogLayoutBinding, listener: onDogItemClickListener) : RecyclerView.ViewHolder(binding.root){
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DogLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
                mListener.onDogItemClicked(position, dogs[position])
            }
        holder.binding.petHome.text = dogs[position].pet_home
        holder.binding.date.text = dogs[position].date
        val url = dogs[position].image_url
        val imagePath = holder.binding.dogImage
        Glide.with(holder.itemView.context).load(url).into(imagePath)
    }

    fun addDog(dog: Dog){
        if(!dogs.contains(dog)){
            dogs.add(dog)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dogs.size
    }

    interface onDogItemClickListener{
        fun onDogItemClicked(position : Int, dog : Dog)
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}