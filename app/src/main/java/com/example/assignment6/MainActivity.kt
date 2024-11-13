package com.example.unit5apiassignment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val apiKey = ""
    private val currentDate = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.imageView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val refreshButton: Button = findViewById(R.id.refreshButton)

        fun fetchData(date: Calendar) {
            // Clear previous data
            titleTextView.text = "Loading..."
            descriptionTextView.text = ""
            imageView.setImageDrawable(null)

            val formattedDate = dateFormatter.format(date.time)
            val nasaApiUrl = "https://api.nasa.gov/planetary/apod?api_key=$apiKey&date=$formattedDate"

            val client = AsyncHttpClient()
            client.get(nasaApiUrl, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    response?.let {
                        val title = it.getString("title")
                        val description = it.getString("explanation")
                        val mediaType = it.getString("media_type") // Check if it's an image or video

                        titleTextView.text = title
                        descriptionTextView.text = description

                        if (mediaType == "image") {
                            val imageUrl = it.getString("url")

                            Glide.with(this@MainActivity)
                                .load(imageUrl)
                                //.error(R.drawable.placeholder_image) // Placeholder image in case of error
                                .into(imageView)
                        } else {

                            //imageView.setImageResource(R.drawable.video_placeholder)
                        }
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    titleTextView.text = "Failed to load data"
                    descriptionTextView.text = "Please check your connection or API key"
                }
            })
        }

        // Initial data load for today’s APOD
        fetchData(currentDate)

        // Button click listener to load the previous day's APOD
        refreshButton.setOnClickListener {
            currentDate.add(Calendar.DAY_OF_YEAR, -1) // Move to the previous day
            fetchData(currentDate) // Fetch data for the new date
        }
    }
}