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

    // Used to access the search box and read what the user types
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

        // Find the search box in activity_experience_list.xml
        searchInput = findViewById(R.id.searchInput)

        // Update the list whenever the user types or removes text
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

        // Only search after the adapter and search box are ready

        if (::adapter.isInitialized && ::searchInput.isInitialized) {
            // Use the current search text when refreshing the list
            filterExperiences()
        }
    }




    // Filter the list using the text entered in the search box
    private fun filterExperiences() {
        // Get what the user typed
        // toString() converts it to text
        // trim() removes spaces from the beginning and end
        val searchText = searchInput.text.toString().trim()

        // Start with all experiences so each search checks the full list.
        val matches = AppData.experiences.findAll().filter {
            // Keep an experience if its title contains the search text
            // For example, "uff" matches "Uffizi Gallery"
            // ignoreCase allows "UFF" and "uff" to give the same results
            // If the search is empty, every title matches
            it.title.contains(searchText, ignoreCase = true)
        }

        // Shows the matching experiences.
        adapter.updateExperiences(matches)
    }




    private fun editExperience(experience: TravelExperienceModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", experience.id)

        startActivity(intent)
    }




    private fun deleteExperience(experience: TravelExperienceModel) {
        // Delete the selected experience using its ID
        AppData.experiences.delete(experience.id)

        // Update the search results so the deleted entry disappears
        // Keep the user's current search applied
        filterExperiences()
    }
}