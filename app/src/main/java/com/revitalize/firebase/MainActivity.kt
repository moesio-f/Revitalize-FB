package com.revitalize.firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity()
{

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var listDocuments: ArrayList<Desafio>
    private var database = Database()
    private var positionDesafio = -1
    private val ACTIVITY_IDENTIFICATOR_EDIT = 1
    private val ACTIVITY_IDENTIFICATOR_CREATE = 0
    private val ACTIVITY_NAME = "main_activity"


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setDocumentList(null)

        fab.setOnClickListener {
            val intent = Intent(this, CreateDesafio::class.java)
            startActivityForResult(intent, ACTIVITY_IDENTIFICATOR_CREATE)
        }

    }

    fun setDocumentList(toast: Toast?)
    {
        progressBarMain.visibility = View.VISIBLE
        val task = database.getCollection("Desafios")
        task?.addOnCompleteListener { result ->
            progressBarMain.visibility = View.GONE
            if (result.isSuccessful) {
                listDocuments = arrayListOf()
                result.result?.forEach { document ->
                    var desafio = Desafio(
                        document.id,
                        document.data["titulo"].toString(),
                        document.data["descricao"].toString(),
                        document.data["imageURL"].toString()
                    )
                    listDocuments.add(desafio)
                }
                    viewAdapter = MyAdapter(listDocuments, ::editDesafio)
                    viewManager = LinearLayoutManager(this)
                    recyclerView = rvDesafios.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    }
                    toast?.show()
            }
        }
    }

    fun editDesafio(position: Int)
    {
        val intent = Intent(this, EditDesafio::class.java)
        intent.putExtra("desafio", listDocuments[position])
        positionDesafio = position
        startActivityForResult(intent, ACTIVITY_IDENTIFICATOR_EDIT)
    }

    override fun onActivityResult(requestCode :Int, resultCode :Int, data :Intent? )
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_IDENTIFICATOR_EDIT)
        {

            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(baseContext, data?.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
                listDocuments[positionDesafio] = data!!.getParcelableExtra("desafio")
                updateDesafios(null)
            }
            else if (resultCode == Activity.RESULT_CANCELED && data != null)
            {
                Toast.makeText(baseContext, data.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
            }
        }
        else if(requestCode == ACTIVITY_IDENTIFICATOR_CREATE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(baseContext, data?.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
                updateDesafios(null)
            }
            else if (resultCode == Activity.RESULT_CANCELED && data != null)
            {
                Toast.makeText(baseContext, data.getStringExtra(ACTIVITY_NAME), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateDesafios(toast: Toast?)
    {
        listDocuments.clear()
        viewAdapter.notifyDataSetChanged()
        setDocumentList(toast)
        viewAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == R.id.action_sync)
        {
            updateDesafios(Toast.makeText(baseContext, "Desafios atualizados com sucesso!", Toast.LENGTH_LONG))
            return true
        }

        return  false
    }

}
