package com.example.findme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findme.models.HomePet
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class HomePetsViewModel: ViewModel() {

    private val dbhomepets = FirebaseDatabase.getInstance().getReference("HomePets")
    val orderByNameQuery = dbhomepets.orderByChild("name")

    private val _homepet = MutableLiveData<HomePet>()
    val homepet: LiveData<HomePet> get() = _homepet

    private val childEventListener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val homepet = snapshot.getValue(HomePet::class.java)
            homepet?.id = snapshot.key
            _homepet.value = homepet!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    fun getRealtimeUpdate(){
        orderByNameQuery.addChildEventListener(childEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        dbhomepets.removeEventListener(childEventListener)
    }

}