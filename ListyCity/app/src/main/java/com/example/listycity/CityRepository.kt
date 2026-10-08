package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db= Firebase.firestore
    private val citiesRef = db.collection("cities")

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            _cities.clear()

            snapshot?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null){
                    _cities.add(city)
                }
            }
        }
    }
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        /*
        Changes update city from starter code because
        there was document name cannot be modified, and there is no way
        to recover the old city name if it isn't in the City list.
         */
        if (oldCity.name == updatedCity.name){
            citiesRef.document(oldCity.name).set(updatedCity)
        }
        else{
            deleteCity(oldCity)
            addCity(updatedCity)
        }
    }

    fun deleteCity(city: City){
        citiesRef.document(city.name).delete()
    }
}