package com.revitalize.firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.menu_edit.*

class MainActivity : AppCompatActivity()
{

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var listDocuments: ArrayList<Desafio>
    private var database = Database()
    private var positionDesafio = -1
    private val ACTIVITY_IDENTIFICATOR = 1
    private val ACTIVITY_NAME = "main_activity"


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setDocumentList()

        fab.setOnClickListener {
        }

    }

    fun setDocumentList()
    {
        val task = database.getCollection("Desafios")
        task?.addOnCompleteListener {result ->
            if(result.isSuccessful)
            {
                listDocuments = arrayListOf()
                result.result?.forEach {document ->
                    var desafio = Desafio(document.id, document.data["titulo"].toString(), document.data["descricao"].toString(), document.data["imageURL"].toString())
                    listDocuments.add(desafio)
                }
                viewAdapter = MyAdapter(listDocuments, ::editDesafio)
                viewManager = LinearLayoutManager(this)
                recyclerView = rvDesafios.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }
    }

    fun editDesafio(position: Int)
    {
        val intent = Intent(this, EditDesafio::class.java)
        intent.putExtra("desafio", listDocuments[position])
        positionDesafio = position
        startActivityForResult(intent, ACTIVITY_IDENTIFICATOR)
    }

    override fun onActivityResult(requestCode :Int, resultCode :Int, data :Intent? )
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_IDENTIFICATOR)
        {

            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(baseContext, data?.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
                listDocuments[positionDesafio] = data!!.getParcelableExtra("desafio")
                viewAdapter.notifyItemChanged(positionDesafio)
            }
            else if (resultCode == Activity.RESULT_CANCELED && data != null)
            {
                Toast.makeText(baseContext, data?.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId)
        {
            R.id.action_sync -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
