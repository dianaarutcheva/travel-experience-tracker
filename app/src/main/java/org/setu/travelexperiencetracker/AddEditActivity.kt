package org.setu.travelexperiencetracker

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            loadExistingExperience(editingId!!)
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        titleInput = EditText(this).apply {
            hint = "Title"
        }

        descriptionInput = EditText(this).apply {
            hint = "Description"
        }

        countryInput = EditText(this).apply {
            hint = "Country"
        }

        cityInput = EditText(this).apply {
            hint = "City"
        }

        typeInput = EditText(this).apply {
            hint = "Type (Landmark, Restaurant or Activity)"
        }

        dateVisitedInput = EditText(this).apply {
            hint = "Date Visited (DD/MM/YYYY)"
        }

        ratingInput = EditText(this).apply {
            hint = "Rating (1-5)"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        val saveButton = Button(this).apply {
            text = "Save"

            setOnClickListener {
                saveExperience()
            }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }

        root.addView(titleInput)
        root.addView(descriptionInput)
        root.addView(countryInput)
        root.addView(cityInput)
        root.addView(typeInput)
        root.addView(dateVisitedInput)
        root.addView(ratingInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }

    private fun loadExistingExperience(id: Long) {

        val experience = AppData.experiences.findOne(id)

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

        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val country = countryInput.text.toString().trim()
        val city = cityInput.text.toString().trim()
        val type = typeInput.text.toString().trim()
        val dateVisited = dateVisitedInput.text.toString().trim()

        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val rating = ratingInput.text.toString().toIntOrNull()

        if (rating == null) {
            ratingInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {

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