package org.setu.travelexperiencetracker.models

// Holds the details of one travel experience
// var = the value can change (be edited)
data class TravelExperienceModel(
    var id: Long = 0L,                // Long means the ID is a whole number
    var title: String = "",
    var description: String = "",
    var country: String = "",
    var city: String = "",
    var type: String = "",
    var dateVisited: String = "",
    var rating: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var imageUri: String? = null
)
{
    fun validate() {
        require(title.isNotBlank()) {
            "Title must not be empty."
        }
        require(latitude.isFinite() && latitude in -90.0..90.0) {
            "Latitude must be between -90 and 90."
        }
        require(longitude.isFinite() && longitude in -180.0..180.0) {
            "Longitude must be between -180 and 180."
        }
    }
    }
