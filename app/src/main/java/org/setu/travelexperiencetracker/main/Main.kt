package org.setu.travelexperiencetracker.main

import org.setu.travelexperiencetracker.models.TravelExperienceMemStore
import org.setu.travelexperiencetracker.models.TravelExperienceModel

val store = TravelExperienceMemStore()

fun main() {
    println("=== Travel Experience Tracker Console App ===")
    var input: Int
    do {
        input = menu()
        when (input) {
            1 -> addExperience()
            2 -> listExperiences()
            3 -> updateExperience()
            4 -> deleteExperience()
            5 -> searchExperience()
            0 -> println("\nExiting Travel Experience Tracker. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Travel Experience")
    println(" 2. List All Travel Experiences")
    println(" 3. Update a Travel Experience")
    println(" 4. Delete a Travel Experience")
    println(" 5. Search Travel Experience by ID")
    println(" 0. Exit")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun addExperience() {
    println("\n--- Add Travel Experience ---")
    print("Enter Title: ")
    val title = readlnOrNull()?.trim().orEmpty()
    print("Enter Description: ")
    val description = readlnOrNull()?.trim().orEmpty()
    print("Enter Country: ")
    val country = readlnOrNull()?.trim().orEmpty()
    print("Enter City: ")
    val city = readlnOrNull()?.trim().orEmpty()
    print("Enter Type (Landmark, Restaurant or Activity): ")
    val type = readlnOrNull()?.trim().orEmpty()
    print("Enter Date Visited (DD/MM/YYYY): ")
    val dateVisited = readlnOrNull()?.trim().orEmpty()
    print("Enter Rating (1–5): ")
    val rating = readlnOrNull()?.trim()?.toIntOrNull()

    if (title.isNotEmpty()) {
        val experience = TravelExperienceModel(
            title = title,
            description = description,
            country = country,
            city = city,
            type = type,
            dateVisited = dateVisited,
            rating = rating ?: 0
        )
        store.create(experience)
        println("Travel experience added successfully with ID: ${experience.id}")
    } else {
        println("Title cannot be empty. Creation cancelled.")
    }
}

fun listExperiences() {
    println("\n--- All Travel Experiences ---")
    val experiences = store.findAll()
    if (experiences.isEmpty()) {
        println("No travel experiences stored yet.")
    } else {
        experiences.forEach {
            println(
                "ID: ${it.id} | Title: ${it.title} | Description: ${it.description} | Country: ${it.country} | City: ${it.city} | Type: ${it.type} | Date Visited: ${it.dateVisited} | Rating: ${it.rating}/5"
            )
        }
    }
}

fun updateExperience() {
    println("\n--- Update Travel Experience ---")
    listExperiences()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Travel Experience to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter New Title: ")
        val title = readlnOrNull()?.trim().orEmpty()
        print("Enter New Description: ")
        val description = readlnOrNull()?.trim().orEmpty()
        print("Enter New Country: ")
        val country = readlnOrNull()?.trim().orEmpty()
        print("Enter New City: ")
        val city = readlnOrNull()?.trim().orEmpty()
        print("Enter New Type (Landmark, Restaurant or Activity): ")
        val type = readlnOrNull()?.trim().orEmpty()
        print("Enter New Date Visited (DD/MM/YYYY): ")
        val dateVisited = readlnOrNull()?.trim().orEmpty()
        print("Enter New Rating (1–5): ")
        val rating = readlnOrNull()?.trim()?.toIntOrNull()

        if (title.isNotEmpty()) {
            val updated = store.update(
                TravelExperienceModel(
                    id = id,
                    title = title,
                    description = description,
                    country = country,
                    city = city,
                    type = type,
                    dateVisited = dateVisited,
                    rating = rating ?: 0
                )
            )
            if (updated) println("Travel experience updated successfully.")
        } else {
            println("Title cannot be empty. Update cancelled.")
        }
    } else {
        println("Travel experience with ID $id not found.")
    }
}

fun deleteExperience() {
    println("\n--- Delete Travel Experience ---")
    listExperiences()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Travel Experience to delete: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val deleted = store.delete(id)
        if (deleted) {
            println("Travel experience with ID $id deleted successfully.")
        } else {
            println("Travel experience with ID $id not found.")
        }
    } else {
        println("Invalid ID entered.")
    }
}

fun searchExperience() {
    println("\n--- Search Travel Experience ---")
    print("Enter ID: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val experience = store.findOne(id)
        if (experience != null) {
            println(
                "Found: ID: ${experience.id} | Title: ${experience.title}" +
                        " | Description: ${experience.description}" +
                        " | Country: ${experience.country} | City: ${experience.city}" +
                        " | Type: ${experience.type} | Date Visited: ${experience.dateVisited}" +
                        " | Rating: ${experience.rating}/5"
            )
        } else {
            println("No travel experience found with ID $id.")
        }
    } else {
        println("Invalid ID entered.")
    }
}