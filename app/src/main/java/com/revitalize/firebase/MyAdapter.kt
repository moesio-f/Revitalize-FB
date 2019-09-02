package com.revitalize.firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_view.view.*
import kotlinx.android.synthetic.main.list_view.view.*
import java.util.ArrayList

//Classe para montar o RecyclerViewAdapter
class MyAdapter(private val myDataset: ArrayList<Desafio>, private val callback:(Int, String)->Unit) :RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    //Função para inflar o layout da lista criada no RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false)
        return MyViewHolder(view)
    }

    //Função para fazer o bind dos elementos da lista com os itens da RecyclerView (lista visual)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = myDataset[position].title
        holder.desc.text = myDataset[position].desc
        holder.imageURL.text = myDataset[position].imageURL
        holder.image.setImageResource( myDataset[position].image)

    }

    //Função para retornar o tamanho da lista
    override fun getItemCount() : Int{
        return myDataset.size
    }

    //Classe interna para criação do ViewHolder
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.txtTitle
        var desc = itemView.txtDesc
        var imageURL = itemView.txtURL
        var image = itemView.imageView
    }

}