// MainActivity.kt
package com.example.unit5apiassignment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val apiKey = "C3OfnDSTJDz1XMd3dWJJKY3UVCmRWwUcpz9fnbfx" // Replace with your actual API key
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var recyclerView: RecyclerView
    private lateinit var apodAdapter: APODAdapter
    private val entries = mutableListOf<APODEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        apodAdapter = APODAdapter(entries)
        recyclerView.adapter = apodAdapter

        // Fetch data from NASA API
        fetchAPODData()
    }

    private fun fetchAPODData() {
        val endDate = Calendar.getInstance() // Today
        val startDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) } // 10 days ago
        val formattedEndDate = dateFormatter.format(endDate.time)
        val formattedStartDate = dateFormatter.format(startDate.time)
        val nasaApiUrl = "https://api.nasa.gov/planetary/apod?api_key=$apiKey&start_date=$formattedStartDate&end_date=$formattedEndDate"

        val client = AsyncHttpClient()
        client.get(nasaApiUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray) {
                parseAPODResponse(response)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                Log.e("API Error", "Failed to load data: $statusCode", throwable)
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseAPODResponse(response: JSONArray) {
        entries.clear() // Clear previous data

        for (i in 0 until response.length()) {
            val item = response.getJSONObject(i)
            val title = item.optString("title", "No Title")
            val description = item.optString("explanation", "No Description")
            val mediaType = item.optString("media_type", "unknown")
            val imageUrl = if (mediaType == "image") item.optString("url") else ""

            if (mediaType == "image") {
                entries.add(APODEntry(title, description, imageUrl))
            }
        }

        apodAdapter.notifyDataSetChanged()
    }
}