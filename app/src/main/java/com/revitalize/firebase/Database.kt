package com.revitalize.firebase


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class Database {

    private val db = FirebaseFirestore.getInstance()

    fun addDoc(collection: String, document : String, data: Any) :Task<*>{

        val task = db.collection(collection).document(document).set(data)
        return task
    }

    fun addDoc(collection: String,  data: Any) : Task<*>
    {
        val task = db.collection(collection).document()
        val result  = task.set(data)
        return result
    }

    fun getDocument(collection: String, document : String) : Task<DocumentSnapshot>?
    {
        val returnDocument = db.collection(collection).document(document).get()
        return returnDocument
    }


    fun getCollection(collection: String) : Task<QuerySnapshot>?
    {
        val task = db.collection(collection).get()
        return task
    }

    fun delete(collection: String, document:String) : Task<*>?
    {
        val task = db.collection(collection).document(document).delete()

        return task;
    }
}

