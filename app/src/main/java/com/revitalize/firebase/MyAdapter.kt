package com.revitalize.firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_view.view.*
import kotlinx.android.synthetic.main.list_view.view.*
import java.util.ArrayList

class MyAdapter(private val myDataset: ArrayList<Desafio>, private val callback:(Int)->Unit ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.title.text = myDataset[position].title
        holder.desc.text = myDataset[position].description
        holder.imageURL.text = myDataset[position].imageURL
        holder.image.setImageResource(R.drawable.ic_android_black_24dp)
        holder.itemView.setOnClickListener {
            callback(position)
        }
    }

    override fun getItemCount() : Int
    {
        return myDataset.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var title = itemView.txtTitle
        var desc = itemView.txtDesc
        var imageURL = itemView.txtURL
        var image = itemView.imageView
    }

}