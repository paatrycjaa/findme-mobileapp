package com.example.findme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findme.databinding.DogLayout3Binding
import com.example.findme.models.Dog

class DogsHomePetsAdapter2 : RecyclerView.Adapter<DogsHomePetsAdapter2.ViewHolder>(){
    var dogs = mutableListOf<Dog>()
    private lateinit var mListener : onDogItemClickListener

    fun setOnItemClickListener(listener: onDogItemClickListener){
        mListener = listener
    }

    class ViewHolder(val binding: DogLayout3Binding, listener: onDogItemClickListener) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DogLayout3Binding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mListener.onDogItemClicked(position, dogs[position])
        }
        holder.binding.date.text = dogs[position].date
        holder.binding.gender.text = dogs[position].gender
        val url = dogs[position].image_url
        val imagePath = holder.binding.dogImage
        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(imagePath)
    }


    fun addHomeDog(dog: Dog, string: String){
        if(!dogs.contains(dog) and (dog.pet_home_link?.contains(string) == true)){
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
}