package org.setu.travelexperiencetracker

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displayExperiences()
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Travel Experiences"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Experience"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            addButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            listLayout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)

        displayExperiences()
    }

    private fun displayExperiences() {

        listLayout.removeAllViews()

        val experiences = AppData.experiences.findAll()

        if (experiences.isEmpty()) {

            val emptyText = TextView(this).apply {
                text = "No travel experiences yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }

            listLayout.addView(emptyText)

            return
        }

        for (experience in experiences) {

            val experienceLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            val experienceTitle = TextView(this).apply {
                text = "${experience.id}: ${experience.title}"
                textSize = 20f
            }

            val experienceDescription = TextView(this).apply {
                text = experience.description
                textSize = 16f
            }

            val location = TextView(this).apply {
                text = "Country: ${experience.country}, City: ${experience.city}"
                textSize = 14f
            }

            val experienceType = TextView(this).apply {
                text = "Type: ${experience.type}"
                textSize = 14f
            }

            val dateVisited = TextView(this).apply {
                text = "Date Visited: ${experience.dateVisited}"
                textSize = 14f
            }

            val rating = TextView(this).apply {
                text = "Rating: ${experience.rating}/5"
                textSize = 14f
            }

            val editButton = Button(this).apply {
                text = "Edit"

                setOnClickListener {
                    val intent = Intent(
                        this@MainActivity,
                        AddEditActivity::class.java
                    )

                    intent.putExtra("id", experience.id)

                    startActivity(intent)
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"

                setOnClickListener {
                    AppData.experiences.delete(experience.id)
                    displayExperiences()
                }
            }

            experienceLayout.addView(experienceTitle)
            experienceLayout.addView(experienceDescription)
            experienceLayout.addView(location)
            experienceLayout.addView(experienceType)
            experienceLayout.addView(dateVisited)
            experienceLayout.addView(rating)
            experienceLayout.addView(editButton)
            experienceLayout.addView(deleteButton)

            listLayout.addView(experienceLayout)
        }
    }
}