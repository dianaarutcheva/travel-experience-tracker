// Controls the screen where users add or edit a travel experience.
package org.setu.travelexperiencetracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.travelexperiencetracker.models.TravelExperienceModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView  //  Type dropdown
import android.app.DatePickerDialog  // Used to display the calendar picker
import java.util.Calendar  // Used to get today's date
import timber.log.Timber
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent

class AddEditActivity : AppCompatActivity() {

    //  private lateinit var = its value is set later and can change
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var countryInput: EditText
    private lateinit var cityInput: EditText

    // Reference to the Type dropdown
    private lateinit var typeInput: MaterialAutoCompleteTextView
    private lateinit var dateVisitedInput: EditText
    private lateinit var ratingInput: EditText

    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null
    private var editingId: Long? = null


    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                    // Some providers don't allow persistent access.
                }

                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {  // Sets up the screen when Android creates it
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("id")) {
            Timber.d("Opened experience form in edit mode")
        } else {
            Timber.d("Opened experience form in add mode")
        }
        setContentView(R.layout.activity_add_edit) // Chooses the XML layout to display


        imagePreview = findViewById(R.id.experienceImagePreview)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        val mapPicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    selectedLatitude = data.getDoubleExtra("latitude", 0.0)
                    selectedLongitude = data.getDoubleExtra("longitude", 0.0)

                    selectedLatitude?.let {
                        latitudeInput.setText(it.toString())
                    }

                    selectedLongitude?.let {
                        longitudeInput.setText(it.toString())
                    }
                }
            }

        findViewById<Button>(R.id.buttonLoc).setOnClickListener {
            val mapIntent = Intent(this, MapPickerActivity::class.java)
            mapPicker.launch(mapIntent)
        }
        findViewById<Button>(R.id.pickExperienceImageButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }


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


        val experienceTypes = arrayOf(
            "Landmark",
            "Restaurant",
            "Activity"
        ) // Lets the user choose Landmark, Restaurant or Activity
        typeInput.setSimpleItems(experienceTypes)  //puts those choices into the Type dropdown

        //Show the choices when the user taps Type
        typeInput.setOnClickListener {
            typeInput.showDropDown()  //opens the list of choices
        }


        // CALENDAR - opens a date picker and puts the chosen date into the date field
        dateVisitedInput =
            findViewById(R.id.dateVisitedInput)

        dateVisitedInput.isFocusable = false  // Stops the user typing into the date box

        dateVisitedInput.setOnClickListener {
            val calendar = Calendar.getInstance()  // Use today's date as the starting date

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    // Format the date as DD/MM/YYYY
                    // Add 1 because Android counts months from 0
                    //%02d gives the day and month two digits, %04d gives the year four digits
                    val selectedDate =
                        "%02d/%02d/%04d".format(day, month + 1, year)

                    // Display the selected date in the form.
                    dateVisitedInput.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),                 // Start on today's year
                calendar.get(Calendar.MONTH),              // Start on today's month
                calendar.get(Calendar.DAY_OF_MONTH)   // Start on today's day
            ).show()  // Display the date picker.
        }


// find rating box, save button and cancel button in the layout
        ratingInput =
            findViewById(R.id.ratingInput)


        val saveButton =
            findViewById<Button>(R.id.saveButton)


        val cancelButton =
            findViewById<Button>(R.id.cancelButton)


        // Remembers which experience you are editing. If it is null, you are adding a new experience
        editingId =
            intent.getLongExtra("id", -1L) // Use -1 if no ID was sent (starts at 1)
                .takeIf { it != -1L }

        if (editingId != null) { // != null means an ID is present (editing an existing experience)

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


    // Finds the experience by ID and fills the form with its saved details
    private fun loadExistingExperience(id: Long) {

        val experience =
            AppData.experiences.findOne(id) // Find the experience using its ID

        if (experience == null) {    // == null means no experience was found

            Toast.makeText(   // Show a short message saying it was not found
                this,
                "Travel experience not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()  // Close this screen
            return    // Stop running this function
        }

        // Put the saved details into the form's boxes
        titleInput.setText(experience.title)
        descriptionInput.setText(experience.description)
        countryInput.setText(experience.country)
        cityInput.setText(experience.city)
        // Put the experience's saved type (Restaurant) into the Type box, still let the user choose from all three types
        typeInput.setText(experience.type, false)
        dateVisitedInput.setText(experience.dateVisited)
        ratingInput.setText(experience.rating.toString())
        latitudeInput.setText(experience.latitude.toString())
        longitudeInput.setText(experience.longitude.toString())

        selectedImageUri = experience.imageUri?.let { Uri.parse(it) }

        val savedImageUri = selectedImageUri
        if (savedImageUri != null) {
            imagePreview.setImageURI(savedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }


    // Read the form, check the title and rating, then save the experience
    private fun saveExperience() {

        // trim() removes spaces from the beginning and end of the text
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
            ratingInput.text.toString()
                .toIntOrNull()   // Change the rating to a whole number so it can be stored

        if (rating == null) {
            ratingInput.error = "Enter a valid number"
            return
        }


        try {
            val experience = TravelExperienceModel(
                id = editingId ?: 0L,
                title = title,
                description = description,
                country = country,
                city = city,
                type = type,
                dateVisited = dateVisited,
                rating = rating,
                latitude = latitudeInput.text.toString().toDouble(),
                longitude = longitudeInput.text.toString().toDouble(),
                imageUri = selectedImageUri?.toString()
            )

            experience.validate()

            if (editingId == null) {
                AppData.experiences.create(experience)
                Toast.makeText(this, "Travel experience created", Toast.LENGTH_SHORT).show()
            } else {
                AppData.experiences.update(experience)
                Toast.makeText(this, "Travel experience updated", Toast.LENGTH_SHORT).show()
            }

            finish()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                e.message ?: "Please check the entered values.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}