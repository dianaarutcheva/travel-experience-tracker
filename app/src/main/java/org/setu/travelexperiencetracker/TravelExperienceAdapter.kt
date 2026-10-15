package org.setu.travelexperiencetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.travelexperiencetracker.models.TravelExperienceModel

class TravelExperienceAdapter(
    private var experiences: List<TravelExperienceModel>,
    private val onEdit: (TravelExperienceModel) -> Unit,
    private val onDelete: (TravelExperienceModel) -> Unit
) : RecyclerView.Adapter<TravelExperienceAdapter.ExperienceViewHolder>() {

    class ExperienceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val titleText: TextView =
            itemView.findViewById(R.id.titleText)

        val descriptionText: TextView =
            itemView.findViewById(R.id.descriptionText)

        // Connect each detail to its TextView.
        val countryText: TextView = itemView.findViewById(R.id.countryText)
        val cityText: TextView = itemView.findViewById(R.id.cityText)
        val typeText: TextView = itemView.findViewById(R.id.typeText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val ratingText: TextView = itemView.findViewById(R.id.ratingText)

        val editButton: Button =
            itemView.findViewById(R.id.editButton)

        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperienceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_travel_experience,
                parent,
                false
            )

        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ExperienceViewHolder,
        position: Int
    ) {

        val experience = experiences[position]

        holder.titleText.text =
            "${experience.id}: ${experience.title}"

        holder.descriptionText.text =
            experience.description

        // Display each detail beside its icon.
        holder.countryText.text = "Country: ${experience.country}"
        holder.cityText.text = "City: ${experience.city}"
        holder.typeText.text = "Type: ${experience.type}"
        holder.dateText.text = "Date: ${experience.dateVisited}"
        holder.ratingText.text = "Rating: ${experience.rating}/5"

        holder.editButton.setOnClickListener {
            onEdit(experience)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(experience)
        }
    }

    override fun getItemCount(): Int {
        return experiences.size
    }

    fun updateExperiences(newExperiences: List<TravelExperienceModel>) {
        experiences = newExperiences
        notifyDataSetChanged()
    }
}