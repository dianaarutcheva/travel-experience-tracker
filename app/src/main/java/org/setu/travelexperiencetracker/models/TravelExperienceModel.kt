package org.setu.travelexperiencetracker.models

data class TravelExperienceModel(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var country: String = "",
    var city: String = "",
    var type: String = "",
    var dateVisited: String = "",
    var rating: Int = 0
)