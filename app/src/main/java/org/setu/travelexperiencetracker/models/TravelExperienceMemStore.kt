package org.setu.travelexperiencetracker.models

import java.util.concurrent.atomic.AtomicLong

class TravelExperienceMemStore {
    private val experiences = ArrayList<TravelExperienceModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<TravelExperienceModel> {
        return experiences
    }

    fun create(experience: TravelExperienceModel) {
        experience.id = lastId.incrementAndGet()
        experiences.add(experience)
    }

    fun update(experience: TravelExperienceModel): Boolean {
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
        val foundExperience = findOne(id)
        return if (foundExperience != null) {
            experiences.remove(foundExperience)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): TravelExperienceModel? {
        return experiences.find { experience -> experience.id == id }
    }
}