package dz.jilconnect.dipannini.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(@Expose @SerializedName("id") val id: String)