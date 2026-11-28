package org.setu.travelexperiencetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import timber.log.Timber

// Controls the main menu screen
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppData.initialize(this)
        if (Timber.forest().isEmpty()) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d("Main menu opened")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewExperiencesButton =  // Find the View Experiences button
            findViewById<Button>(R.id.viewExperiencesButton)

        val addExperienceButton =  // Find the Add Experience button
            findViewById<Button>(R.id.addExperienceButton)

        viewExperiencesButton.setOnClickListener {
            startActivity(
                Intent(this, ExperienceListActivity::class.java)
            )
        }

        // Open an empty form when Add Experience is clicked
        addExperienceButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }
    }
}