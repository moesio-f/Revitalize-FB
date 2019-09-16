package com.revitalize.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.menu_create.*


class CreateDesafio : AppCompatActivity()
{
    private lateinit var imgURI : Uri
    private val GALLERY_REQUEST_CODE = 10

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_create)

        desafioIcon.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
        }

        apply.setOnClickListener {
            applyChanges()
        }

        back.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode :Int, resultCode :Int, data :Intent? ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(data != null)
                {
                    imgURI = data.data
                    GlideApp.with(desafioIcon.context).load(imgURI).into(desafioIcon)
                }
            }
        }
    }

    fun applyChanges()
    {
        val isValid: Boolean = verifyInputs()
        val data = Intent()

        if (isValid)
        {
            val nDesafio = Desafio(
                txtEditID.text.toString(),
                txtEditTitle.text.toString(),
                txtEditDesc.text.toString(),
                "")

            val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            val connected = netInfo != null && netInfo.isConnected

            if (connected)
            {
                val task = Database().addDocument("Desafios",  nDesafio.id, nDesafio.getHashMap())
                progressBar.visibility = View.VISIBLE

                task.addOnCompleteListener { result ->
                    if (result.isSuccessful)
                    {
                        Database().addImage(imgURI, nDesafio.id).addOnCompleteListener {uploadImage ->
                            if (uploadImage.isSuccessful)
                            {
                                Database().getDownloadULR(Database().getImagePath(nDesafio.id)).addOnCompleteListener { downloadUrl ->
                                    progressBar.visibility = View.GONE
                                    if(downloadUrl.isSuccessful)
                                    {
                                        nDesafio.imageURL = downloadUrl.result.toString()
                                        Database().addDocument("Desafios", nDesafio.id, nDesafio.getHashMap()).addOnCompleteListener { refresh ->
                                            if(refresh.isSuccessful)
                                            {
                                                progressBar.visibility = View.GONE
                                                data.putExtra("main_activity", "Desafio criado com sucesso!")
                                                data.putExtra("desafio", nDesafio)
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
                                        data.putExtra("main_activity", "Houve um erro ao criar o desafio!")
                                        setResult(Activity.RESULT_CANCELED, data)
                                        finish()
                                    }
                                }
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