package com.revitalize.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Desafio (var id: String = "", var title: String = "", var desc : String = "", var imageURL : String = "", var image : Int = 0) : Parcelable