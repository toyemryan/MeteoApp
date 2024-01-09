/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp

import com.google.android.libraries.places.api.model.Place
import java.io.Serializable

class SerializablePlace(place: Place) : Serializable {
    var id: String? = null
    var name: String? = null
    var address: String? = null
    var latLng: String? = null

    init {
        this.id = place.id
        this.name = place.name
        this.address = place.address
        this.latLng = place.latLng?.toString()
    }

}