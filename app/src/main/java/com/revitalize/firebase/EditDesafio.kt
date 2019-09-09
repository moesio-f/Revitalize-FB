package com.revitalize.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.menu_edit.*


class EditDesafio : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_edit)

        val desafio = intent.getParcelableExtra<Desafio>("desafio")
        txtEditTitle.text =  Editable.Factory.getInstance().newEditable(desafio.title)
        txtEditDesc.text =   Editable.Factory.getInstance().newEditable(desafio.description)

        apply.setOnClickListener{
            applyChanges(desafio)
        }

        delete.setOnClickListener{
            deleteDesafio(desafio)
        }

        back.setOnClickListener {
            finish()
        }
    }

    fun applyChanges(desafio: Desafio)
    {
        val isValid: Boolean = verifyInputs()
        val data = Intent()

        if (isValid)
        {
            val desafio = Desafio(
                desafio.id,
                txtEditTitle.text.toString(),
                txtEditDesc.text.toString(),
                desafio.imageURL)
            val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            val connected = netInfo != null && netInfo.isConnected

            if (connected)
            {
                val task = Database().addDocument("Desafios",  desafio.id, desafio.getHashMap())
                progressBar.visibility = View.VISIBLE

                task.addOnCompleteListener { result ->
                    progressBar.visibility = View.GONE
                    if (result.isSuccessful)
                    {
                        data.putExtra("main_activity", "Desafio atualizado com sucesso!")
                        data.putExtra("desafio", desafio)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                    else
                    {
                        data.putExtra("main_activity", "Houve um erro ao atualizar o desafio!")
                        setResult(Activity.RESULT_CANCELED, data)
                        finish()
                    }
                }
            }
            else
            {
                data.putExtra("main_activity", "Sem conexão com a internet!")
            }
        }
    }

    fun deleteDesafio(desafio: Desafio)
    {
        val data = Intent()
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        val connected = netInfo != null && netInfo.isConnected

        if (connected)
        {
            val task = Database().delete("Desafios",  desafio.id)
            progressBar.visibility = View.VISIBLE

            task?.addOnCompleteListener { result ->
                progressBar.visibility = View.GONE
                if (result.isSuccessful)
                {
                    data.putExtra("main_activity", "Desafio excluído com sucesso!")
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
                else
                {
                    data.putExtra("main_activity", "Houve um erro ao deletar o desafio!")
                    setResult(Activity.RESULT_CANCELED, data)
                    finish()
                }
            }
        }
        else
        {
            data.putExtra("main_activity", "Sem conexão coma internet!")
        }
    }

    private fun verifyInputs(): Boolean {

        var isValidInput = true

        if (txtEditTitle.text.toString().trim().isEmpty()) {
            txtInputTitle.error = "O campo não pode ser vazio!"
            isValidInput = false
        }

        if (txtEditDesc.text.toString().trim().isEmpty()){
            txtInputDesc.error = "O campo não pode ser vazio!"
            isValidInput = false
        }

        return isValidInput
    }
}