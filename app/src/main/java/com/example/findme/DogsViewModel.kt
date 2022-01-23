package com.example.findme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findme.models.Dog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class DogsViewModel: ViewModel() {

    private val dbdogs = FirebaseDatabase.getInstance().getReference("Dogs")
    val orderByDateQuery = dbdogs.orderByChild("date")
    //val orderByDateQueryLimit = dbdogs.orderByChild("date").limitToLast(3)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog> get() = _dog

    private val childEventListener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val dog = snapshot.getValue(Dog::class.java)
            dog?.id = snapshot.key
            _dog.value = dog!!
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

    fun addDog(dog: Dog){
        dog.id = dbdogs.push().key
        dbdogs.child(dog.id!!).setValue(dog).addOnCompleteListener {
            if(it.isSuccessful){
                _result.value = null
            }else{
                _result.value = it.exception
            }
        }
    }

    fun getRealtimeUpdate(){
        //dbdogs.addChildEventListener(childEventListener)
        orderByDateQuery.addChildEventListener(childEventListener)
    }

    fun getRealtimeUpdateLimited(int: Int){
        val orderByDateQueryLimit = dbdogs.orderByChild("date").limitToLast(int)
        orderByDateQueryLimit.addChildEventListener(childEventListener)
    }

    fun getRealTimeUpdateHomePet(string: String){
        val orderByDateQueryHomePet = dbdogs.orderByChild("pet_home_link").startAt("schroniskonapaluchu", "pet_home_link")
        orderByDateQueryHomePet.addChildEventListener(childEventListener)
    }

    fun getRealTimeUpdateType(string: String){
        val orderByDateQueryType = dbdogs.orderByChild("type").equalTo("Beagle", "type")
        orderByDateQueryType.addChildEventListener(childEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        dbdogs.removeEventListener(childEventListener)
    }
}