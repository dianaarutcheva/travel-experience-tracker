package org.setu.travelexperiencetracker

import org.setu.travelexperiencetracker.models.TravelExperienceMemStore

// Lets all screens use the same list of travel experiences (connects)
object AppData {
    val experiences = TravelExperienceMemStore()
}