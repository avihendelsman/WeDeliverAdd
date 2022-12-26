package app.wedeliveradd

import com.google.firebase.database.FirebaseDatabase

class Repository {

    // FireBase
    var databade = FirebaseDatabase.getInstance()
    var myRef = databade.getReference("packages")

    fun addPackage(pack: Package){
        pack.id = myRef.push().key.toString()
        myRef.child(pack.id).setValue(pack)
    }
}