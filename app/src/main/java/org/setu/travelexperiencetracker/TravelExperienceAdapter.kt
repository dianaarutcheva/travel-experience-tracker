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

        val locationText: TextView =
            itemView.findViewById(R.id.locationText)

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

        //Each \n starts a new line
        holder.locationText.text =
            "Country: ${experience.country}\nCity: ${experience.city}\nType: ${experience.type}\nDate: ${experience.dateVisited}\nRating: ${experience.rating}/5"

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