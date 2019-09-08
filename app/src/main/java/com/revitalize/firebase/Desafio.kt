package com.revitalize.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Desafio(var id: String, var title: String, var description: String, var imageURL: String) : Parcelable
{
    fun getHashMap() : HashMap<String, String>
    {
        val result = hashMapOf<String,String>(
            "titulo" to title,
            "descricao" to description,
            "imageURL" to imageURL
        )

        return  result
    }
}