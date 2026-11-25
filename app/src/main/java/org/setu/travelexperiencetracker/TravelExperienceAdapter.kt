package org.setu.travelexperiencetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.travelexperiencetracker.models.TravelExperienceModel
import timber.log.Timber
import android.net.Uri
import android.widget.ImageView

interface OnExperienceListener {
    fun onExperienceClick(experience: TravelExperienceModel)
    fun onExperienceLongClick(experience: TravelExperienceModel): Boolean
}
class TravelExperienceAdapter(
    private var experiences: List<TravelExperienceModel>,
    private val listener: OnExperienceListener
) : RecyclerView.Adapter<TravelExperienceAdapter.ExperienceViewHolder>() {



    // Finds and holds the text views and buttons for one experience in the list
    class ExperienceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val titleText: TextView =
            itemView.findViewById(R.id.titleText)

        val descriptionText: TextView =
            itemView.findViewById(R.id.descriptionText)

        // Finds the text boxes for country, city, type, date and rating in the layout
        val countryText: TextView = itemView.findViewById(R.id.countryText)
        val cityText: TextView = itemView.findViewById(R.id.cityText)
        val typeText: TextView = itemView.findViewById(R.id.typeText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val ratingText: TextView = itemView.findViewById(R.id.ratingText)

        val experienceImage: ImageView =
            itemView.findViewById(R.id.experienceImage)
    }





    // Creates the layout for one experience in the list and holds its text views and buttons
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




    // Put an experience's details into the list item
    override fun onBindViewHolder(
        holder: ExperienceViewHolder,
        position: Int  // experience’s place in the list
    ) {

        val experience = experiences[position]

        holder.titleText.text =
            "${experience.id}: ${experience.title}"

        holder.descriptionText.text =
            experience.description

        // Second section puts the details into the text boxes
        // Fill each text box with this experience's details.
        // Each box already has its icon set in the XML layout.
        holder.countryText.text = "Country: ${experience.country}"
        holder.cityText.text = "City: ${experience.city}"
        holder.typeText.text = "Type: ${experience.type}"
        holder.dateText.text = "Date: ${experience.dateVisited}"
        holder.ratingText.text = "Rating: ${experience.rating}/5"
        val imageUri = experience.imageUri?.let { Uri.parse(it) }

        if (imageUri != null) {
            holder.experienceImage.setImageURI(imageUri)
        } else {
            holder.experienceImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener {
            val selected = experiences.getOrNull(holder.bindingAdapterPosition)
                ?: return@setOnClickListener
            Timber.d("Experience row tapped, id=%s", selected.id)
            listener.onExperienceClick(selected)
        }

        holder.itemView.setOnLongClickListener {
            val selected = experiences.getOrNull(holder.bindingAdapterPosition)
                ?: return@setOnLongClickListener false
            Timber.d("Experience row long-pressed, id=%s", selected.id)
            listener.onExperienceLongClick(selected)
        }
    }


    // SEARCH functions
    // Return how many experiences are in the list
    override fun getItemCount(): Int {
        return experiences.size
    }


    // Change the experiences shown and refresh the list
    fun updateExperiences(newExperiences: List<TravelExperienceModel>) {
        experiences = newExperiences  // Use the new list of experiences
        notifyDataSetChanged()        // Tell the RecyclerView to update what it shows
    }
}