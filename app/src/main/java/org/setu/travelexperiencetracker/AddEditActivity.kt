package org.setu.travelexperiencetracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.travelexperiencetracker.models.TravelExperienceModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var countryInput: EditText
    private lateinit var cityInput: EditText
    private lateinit var typeInput: EditText
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

        dateVisitedInput =
            findViewById(R.id.dateVisitedInput)

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
        typeInput.setText(experience.type)
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