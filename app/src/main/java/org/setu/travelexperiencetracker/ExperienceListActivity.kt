package org.setu.travelexperiencetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.travelexperiencetracker.models.TravelExperienceModel
import android.widget.EditText  // Access the search text field.
import androidx.core.widget.doAfterTextChanged // Detect when the user changes the search text

class ExperienceListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TravelExperienceAdapter

    // Store a reference to the search box.
    private lateinit var searchInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_experience_list)

        recyclerView =
            findViewById(R.id.experiencesRecyclerView)

        adapter = TravelExperienceAdapter(
            experiences = AppData.experiences.findAll(),

            onEdit = { experience ->
                editExperience(experience)
            },

            onDelete = { experience ->
                deleteExperience(experience)
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        // Connect searchInput to the search box in the XML layout.
        searchInput = findViewById(R.id.searchInput)

        // Update the list whenever the user types or removes text.
        searchInput.doAfterTextChanged {
            filterExperiences()
        }

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        // Refresh the list when returning from the edit screen.
        // Keep the current search text applied.
        if (::adapter.isInitialized && ::searchInput.isInitialized) {
            filterExperiences()
        }
    }



    // Filter the list using the text entered in the search box.
    private fun filterExperiences() {
        // Read the search text and remove spaces from either end.
        val searchText = searchInput.text.toString().trim()

        // Find titles containing the search text, ignoring letter case.
        // An empty search matches all experiences.
        val matches = AppData.experiences.findAll().filter {
            it.title.contains(searchText, ignoreCase = true)
        }

        // Display the matching experiences.
        adapter.updateExperiences(matches)
    }




    private fun editExperience(experience: TravelExperienceModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", experience.id)

        startActivity(intent)
    }

    private fun deleteExperience(experience: TravelExperienceModel) {
        // Delete the selected experience from the store.
        AppData.experiences.delete(experience.id)

        // Refresh the results while keeping the current search applied.
        filterExperiences()
    }
}