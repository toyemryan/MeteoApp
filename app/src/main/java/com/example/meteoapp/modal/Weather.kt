/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.lionel.mameteo.modal

import com.google.gson.annotations.SerializedName


data class Weather (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("main"        ) var main        : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("icon"        ) var icon        : String? = null

)