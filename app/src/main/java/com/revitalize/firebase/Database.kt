package com.revitalize.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class Database
{
    val db = FirebaseFirestore.getInstance()
    val sr = FirebaseStorage.getInstance()

    fun addDocument(collection: String, documentID: String, data: Any) : Task<*>
    {
        val task = db.collection(collection).document(documentID).set(data)
        return task
    }

    fun addDocument(collection: String, data: Any) :Task<*>
    {
        val task = db.collection(collection).document()
        val result  = task.set(data)
        return result
    }

    fun addImage(uri: Uri, id: String): Task<*>
    {
        val imgRef = sr.reference.child("Desafios").child("$id.jpg")
        return  imgRef.putFile(uri)
    }

    fun deleteImage(id: String): Task<*>
    {
        val imgRef = sr.reference.child("Desafios").child("$id.jpg")
        return imgRef.delete()
    }

    fun getDownloadULR(imgPath: StorageReference): Task<*>
    {
        return imgPath.downloadUrl
    }

    fun getImagePath(id: String): StorageReference
    {
        return sr.reference.child("Desafios").child("$id.jpg")
    }

    fun getDocument(collection: String, document : String): Task<DocumentSnapshot>?
    {
        val task = db.collection(collection).document(document).get()
        return task
    }

    fun getCollection(collection: String): Task<QuerySnapshot>?
    {
        val task = db.collection(collection).get()
        return task
    }

    fun delete(collection: String, document:String): Task<*>?
    {
        val task = db.collection(collection).document(document).delete()
        return task
    }

}

