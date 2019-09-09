package com.revitalize.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.menu_create.*
import kotlinx.android.synthetic.main.menu_create.apply
import kotlinx.android.synthetic.main.menu_create.back
import kotlinx.android.synthetic.main.menu_create.progressBar
import kotlinx.android.synthetic.main.menu_create.txtEditDesc
import kotlinx.android.synthetic.main.menu_create.txtEditTitle
import kotlinx.android.synthetic.main.menu_create.txtInputDesc
import kotlinx.android.synthetic.main.menu_create.txtInputTitle
import kotlinx.android.synthetic.main.menu_edit.*

class CreateDesafio : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_create)

        apply.setOnClickListener {
            applyChanges()
        }

        back.setOnClickListener {
            finish()
        }
    }

    fun applyChanges()
    {
        val isValid: Boolean = verifyInputs()

        if(isValid)
        {
            val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            val connected = netInfo != null && netInfo.isConnected
            val data = Intent()

            if (connected)
            {
                val desafio = Desafio(
                    txtEditID.text.toString(),
                    txtEditTitle.text.toString(),
                    txtEditDesc.text.toString(),
                    "")
                val task = Database().addDocument("Desafios",  desafio.id, desafio.getHashMap())
                progressBar.visibility = View.VISIBLE

                task.addOnCompleteListener { result ->
                    progressBar.visibility = View.GONE
                    if (result.isSuccessful)
                    {
                        data.putExtra("main_activity", "Desafio criado com sucesso!")
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                    else
                    {
                        data.putExtra("main_activity", "Houve um erro ao criar o desafio!")
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

    private fun verifyInputs(): Boolean {

        var isValidInput = true

        if (txtEditID.text.toString().trim().isEmpty()){
            txtInputID.error = "O campo não pode ser vazio!"
            isValidInput = false
        }

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