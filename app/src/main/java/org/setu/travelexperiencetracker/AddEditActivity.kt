package org.setu.travelexperiencetracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.travelexperiencetracker.models.TravelExperienceModel
// Import the dropdown field used to select the experience type
import com.google.android.material.textfield.MaterialAutoCompleteTextView
// For calendar picker
import android.app.DatePickerDialog
import java.util.Calendar

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var countryInput: EditText
    private lateinit var cityInput: EditText

    // The Type field now uses a dropdown.
    private lateinit var typeInput: MaterialAutoCompleteTextView
    private lateinit var dateVisitedInput: EditText
    private lateinit var ratingInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        countryInput =
            findViewById(R.id.countryInput)

        cityInput =
            findViewById(R.id.cityInput)

        typeInput =
            findViewById(R.id.typeInput)

        // Provide the three choices for the Type dropdown.
        val experienceTypes = arrayOf("Landmark", "Restaurant", "Activity")
        typeInput.setSimpleItems(experienceTypes)

        // Show the choices when the field is tapped.
        typeInput.setOnClickListener {
            typeInput.showDropDown()
        }

        dateVisitedInput =
            findViewById(R.id.dateVisitedInput)

        // Open a calendar instead of letting the user type a date.
        dateVisitedInput.isFocusable = false

        dateVisitedInput.setOnClickListener {
            val calendar = Calendar.getInstance()  // Use today's date as the starting date.

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    // Format the selected date as DD/MM/YYYY.
                    // Add 1 because Android counts months from 0.
                    val selectedDate =
                        "%02d/%02d/%04d".format(day, month + 1, year) //%02d gives the day and month two digits, %04d gives the year four digits.

                    // Display the selected date in the form.
                    dateVisitedInput.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()  // Display the date picker.
        }


        ratingInput =
            findViewById(R.id.ratingInput)

        val saveButton =
            findViewById<Button>(R.id.saveButton)

        val cancelButton =
            findViewById<Button>(R.id.cancelButton)

        editingId =
            intent.getLongExtra("id", -1L)
                .takeIf { it != -1L }

        if (editingId != null) {

            findViewById<TextView>(R.id.formTitle)
                .text = "Edit Travel Experience"

            loadExistingExperience(editingId!!)
        }

        saveButton.setOnClickListener {
            saveExperience()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingExperience(id: Long) {

        val experience =
            AppData.experiences.findOne(id)

        if (experience == null) {

            Toast.makeText(
                this,
                "Travel experience not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        titleInput.setText(experience.title)
        descriptionInput.setText(experience.description)
        countryInput.setText(experience.country)
        cityInput.setText(experience.city)
        // Show the saved type without filtering the dropdown choices.
        typeInput.setText(experience.type, false)
        dateVisitedInput.setText(experience.dateVisited)
        ratingInput.setText(experience.rating.toString())
    }

    private fun saveExperience() {

        val title =
            titleInput.text.toString().trim()

        val description =
            descriptionInput.text.toString().trim()

        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val country =
            countryInput.text.toString().trim()

        val city =
            cityInput.text.toString().trim()

        val type =
            typeInput.text.toString().trim()

        val dateVisited =
            dateVisitedInput.text.toString().trim()

        val rating =
            ratingInput.text.toString().toIntOrNull()

        if (rating == null) {
            ratingInput.error = "Enter a valid number"
            return
        }

        if (editingId == null) {

            val experience = TravelExperienceModel(
                title = title,
                description = description,
                country = country,
                city = city,
                type = type,
                dateVisited = dateVisited,
                rating = rating
            )

            AppData.experiences.create(experience)

            Toast.makeText(
                this,
                "Travel experience created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val experience = TravelExperienceModel(
                id = editingId!!,
                title = title,
                description = description,
                country = country,
                city = city,
                type = type,
                dateVisited = dateVisited,
                rating = rating
            )

            AppData.experiences.update(experience)

            Toast.makeText(
                this,
                "Travel experience updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}