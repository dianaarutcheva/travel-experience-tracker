// Keeps a temporary list of travel experiences and lets the app add, view, edit and delete them.
package org.setu.travelexperiencetracker.models

import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
class TravelExperienceMemStore(private val file: File) {
    private val experiences = ArrayList<TravelExperienceModel>() // List that holds the travel experiences
    private val lastId = AtomicLong(0L) // Gives each new travel experience its own ID number (starts w 0)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }


    private fun load() {
        if (!file.exists()) return

        val type = object : TypeToken<ArrayList<TravelExperienceModel>>() {}.type

        val loaded: ArrayList<TravelExperienceModel> =
            gson.fromJson(file.readText(), type) ?: arrayListOf()

        experiences.clear()
        experiences.addAll(loaded)

        lastId.set(experiences.maxOfOrNull { it.id } ?: 0L)
    }

    private fun save() {
        file.parentFile?.mkdirs()
        file.writeText(gson.toJson(experiences))
    }





    fun findAll(): List<TravelExperienceModel> {
        return experiences
    }



    fun create(experience: TravelExperienceModel) { // Gives the experience a new ID and adds it to the list
        experience.validate()
        experience.id = lastId.incrementAndGet()
        experiences.add(experience)
        save()
    }




    fun update(experience: TravelExperienceModel): Boolean {  // Finds the existing experience by ID and changes its details
        experience.validate()
        val foundExperience = findOne(experience.id)
        return if (foundExperience != null) {
            foundExperience.title = experience.title
            foundExperience.description = experience.description
            foundExperience.country = experience.country
            foundExperience.city = experience.city
            foundExperience.type = experience.type
            foundExperience.dateVisited = experience.dateVisited
            foundExperience.rating = experience.rating
            foundExperience.latitude = experience.latitude
            foundExperience.longitude = experience.longitude
            foundExperience.imageUri = experience.imageUri
            save()
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
            save()
            true
        } else {
            false
        }
    }





    fun findOne(id: Long): TravelExperienceModel? {  // Function can return an experience or null if there’s no matching ID
        return experiences.find { experience -> experience.id == id } // Searches the list and returns the first experience whose ID matches
    }
}