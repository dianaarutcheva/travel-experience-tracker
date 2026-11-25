package org.setu.travelexperiencetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.travelexperiencetracker.models.TravelExperienceModel
import androidx.appcompat.app.AlertDialog
import timber.log.Timber


// SEARCH imports
import android.widget.EditText  // Access the search text field
import androidx.core.widget.doAfterTextChanged // Detect when the user changes the search text


// Controls the screen that displays, searches, edits and deletes experiences.
class ExperienceListActivity : AppCompatActivity(), OnExperienceListener {

    // Displays the scrolling list
    private lateinit var recyclerView: RecyclerView

    // Puts each experience's details into the scrolling list
    private lateinit var adapter: TravelExperienceAdapter

    // Used to access the search box and read what the user types
    private lateinit var searchInput: EditText




    // SEARCH
    // Set up the screen's layout, list, search box and buttons
    override fun onCreate(savedInstanceState: Bundle?) { // Bundle is a container for holding information
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_experience_list) // Show XML design

        recyclerView =
            findViewById(R.id.experiencesRecyclerView) // Find the scrolling list

        adapter = TravelExperienceAdapter(
            experiences = AppData.experiences.findAll(),
            listener = this
        )

        recyclerView.layoutManager =             // Show experiences one below another
            LinearLayoutManager(this)

        recyclerView.adapter = adapter    // Connect the adapter to the scrolling list

        // Find the search box in activity_experience_list.xml
        searchInput = findViewById(R.id.searchInput)

        // Update the list whenever the user types or removes text
        searchInput.doAfterTextChanged {
            filterExperiences()
        }


        val returnButton =
            findViewById<Button>(R.id.returnButton) // Back to menu

        returnButton.setOnClickListener {  // Open the main menu when the button is clicked

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }



    override fun onResume() {  // Refresh the list when screen becomes active
        super.onResume()

        // Check that the adapter and search box are both ready to use
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

        // Start with all experiences so each search checks the full list
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


    override fun onExperienceClick(experience: TravelExperienceModel) {
        Timber.d("Opening experience for edit, id=%s", experience.id)
        editExperience(experience)
    }

    override fun onExperienceLongClick(experience: TravelExperienceModel): Boolean {
        Timber.d("Showing delete confirmation, id=%s", experience.id)

        AlertDialog.Builder(this)
            .setTitle(R.string.delete_experience_title)
            .setMessage(getString(R.string.delete_experience_message, experience.title))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.delete_experience_action) { _, _ ->
                Timber.d("Delete confirmed, id=%s", experience.id)
                deleteExperience(experience)
            }
            .show()

        return true
    }






    private fun editExperience(experience: TravelExperienceModel) {

        val intent =  // Prepare to open the add/edit screen
            Intent(this, AddEditActivity::class.java)

        // Send the ID of the experience to edit.
        intent.putExtra("id", experience.id)

        startActivity(intent)
    }




    private fun deleteExperience(experience: TravelExperienceModel) {
        // Delete the selected experience using its ID
        AppData.experiences.delete(experience.id)

        // Refresh the list so it no longer shows the deleted experience
        filterExperiences()
    }
}