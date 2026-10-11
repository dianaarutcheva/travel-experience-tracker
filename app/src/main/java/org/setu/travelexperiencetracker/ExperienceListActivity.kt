package org.setu.travelexperiencetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.travelexperiencetracker.models.TravelExperienceModel

class ExperienceListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TravelExperienceAdapter

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

        if (::adapter.isInitialized) {

            adapter.updateExperiences(
                AppData.experiences.findAll()
            )
        }
    }

    private fun editExperience(experience: TravelExperienceModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", experience.id)

        startActivity(intent)
    }

    private fun deleteExperience(experience: TravelExperienceModel) {

        AppData.experiences.delete(experience.id)

        adapter.updateExperiences(
            AppData.experiences.findAll()
        )
    }
}