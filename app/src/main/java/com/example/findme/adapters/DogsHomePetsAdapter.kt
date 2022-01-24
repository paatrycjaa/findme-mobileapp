package com.example.findme.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findme.databinding.DogLayout2Binding
import com.example.findme.databinding.DogLayoutBinding
import com.example.findme.models.Dog

class DogsHomePetsAdapter() : RecyclerView.Adapter<DogsHomePetsAdapter.ViewHolder>(){
    var dogs = mutableListOf<Dog>()
//    var filtered_dogs = mutableListOf<Dog>()
    private lateinit var mListener : onDogItemClickListener

    fun setOnItemClickListener(listener: onDogItemClickListener){
        mListener = listener
    }

    class ViewHolder(val binding: DogLayout2Binding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsHomePetsAdapter.ViewHolder {
        return ViewHolder(DogLayout2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun addHomeDog(dog: Dog, string: String){
        if(!dogs.contains(dog) and (dog.pet_home_link?.contains(string) == true) and (dogs.size < 6)){
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

    override fun onBindViewHolder(holder: DogsHomePetsAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mListener.onDogItemClicked(position, dogs[position])
        }
        holder.binding.date.text = dogs[position].date
        val url = dogs[position].image_url
        val imagePath = holder.binding.dogImage
        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(imagePath)
    }
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(p0: CharSequence?): FilterResults {
//                val charSearch = p0.toString()
//                Log.i("DogsHomePetsAdapter", "before")
//                Log.i("DogsHomePetsAdapter", dogs.size.toString())
//                filtered_dogs = dogs
//                if (charSearch.isEmpty()) {
//                    filtered_dogs = dogs
//                }
//                else {
//                    Log.i("DogsHomePetsAdapter", "after")
//                    var z = 0
//                    Log.i("DogsHomePetsAdapter", "after")
//                    Log.i("DogsHomePetsAdapter", dogs.size.toString())
//                    while(z < dogs.size){
//                        z += 1
//                        Log.i("DogsHomePetsAdapter", "size")
//                    }
//                    for (dog in dogs){
//                        Log.i("DogsHomePetsAdapter", "size2")
//                        if (dogs.size == 0) {
//                            filtered_dogs = dogs
//                        }
//                        if (dog?.pet_home_link?.contains("schronisko") == true){
//                            filtered_dogs.add(dog)
//                        }
//                        filtered_dogs.add(dog)
////                        if (charSearch?.let { dog.pet_home_link?.contains(it) } == true){
////                            filtered_dogs.add(dog)
////                        }
//                    }
//                }
//                val filterResult = FilterResults()
//                filterResult.values = filtered_dogs
//                return filterResult
//            }
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//                filtered_dogs = p1?.values as MutableList<Dog>
//                notifyDataSetChanged()
//            }
//        }
//    }
}


