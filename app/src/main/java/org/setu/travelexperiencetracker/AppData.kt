package org.setu.travelexperiencetracker

import org.setu.travelexperiencetracker.models.TravelExperienceMemStore
import android.content.Context
import java.io.File

// Lets all screens use the same list of travel experiences (connects)
object AppData {
    lateinit var experiences: TravelExperienceMemStore
        private set

    fun initialize(context: Context) {
        val file = File(context.filesDir, "place_list.json")
        experiences = TravelExperienceMemStore(file)
    }
}