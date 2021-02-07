package com.seif.catfactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception

const val base_url = "https://cat-fact.herokuapp.com"
class MainActivity : AppCompatActivity() {
    private var Tag = "main" // for log key used in debugging
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get data for one time when user enter the app
        getCurrentData()

        btn_generateFact.setOnClickListener {
            // get data when user click the generate button to change the shown data
            getCurrentData()
        }
    }

    private fun getCurrentData() {
        // to show progress bar when we view new cat fact
        txt_catFact.visibility = View.INVISIBLE
        txt_timeStamp.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java) // pass the interface we had made

        // using coroutines to make it more simple
        // we use Dispatchers.IO bec: it is data and this is the dispatcher
        // that put it on a thread to managing data
        GlobalScope.launch(Dispatchers.IO) {

            try {
                val response = api.getCatFacts().awaitResponse()
                if (response.isSuccessful){
                    val data = response.body()!!
                    Log.d(Tag, data.text)
                    // we use Dispatchers.Main to update Ui
                    withContext(Dispatchers.Main){
                        txt_catFact.visibility = View.VISIBLE
                        txt_timeStamp.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        txt_catFact.text = data.text
                        txt_timeStamp.text = data.createdAt

                    }

                }
            }
            catch (e : Exception){
                Log.d(Tag, e.message.toString())
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity,"some thing went wrong!${e.message}",Toast.LENGTH_LONG).show()
                }
            }


        }

    }
}