// Keeps a temporary list of travel experiences and lets the app add, view, edit and delete them.
package org.setu.travelexperiencetracker.models

import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber
class TravelExperienceMemStore {
    private val experiences = ArrayList<TravelExperienceModel>() // List that holds the travel experiences
    private val lastId = AtomicLong(0L) // Gives each new travel experience its own ID number (starts w 0)



    fun findAll(): List<TravelExperienceModel> {
        return experiences
    }



    fun create(experience: TravelExperienceModel) { // Gives the experience a new ID and adds it to the list
        experience.id = lastId.incrementAndGet()
        experiences.add(experience)
    }




    fun update(experience: TravelExperienceModel): Boolean {  // Finds the existing experience by ID and changes its details
        val foundExperience = findOne(experience.id)
        return if (foundExperience != null) {
            foundExperience.title = experience.title
            foundExperience.description = experience.description
            foundExperience.country = experience.country
            foundExperience.city = experience.city
            foundExperience.type = experience.type
            foundExperience.dateVisited = experience.dateVisited
            foundExperience.rating = experience.rating
            true
        } else {
            false
        }
    }



    fun delete(id: Long): Boolean {
        Timber.d("Store received delete request, id=%s", id)
        val foundExperience = findOne(id)
        return if (foundExperience != null) {
            experiences.remove(foundExperience)
            true
        } else {
            false
        }
    }





    fun findOne(id: Long): TravelExperienceModel? {  // Function can return an experience or null if there’s no matching ID
        return experiences.find { experience -> experience.id == id } // Searches the list and returns the first experience whose ID matches
    }
}