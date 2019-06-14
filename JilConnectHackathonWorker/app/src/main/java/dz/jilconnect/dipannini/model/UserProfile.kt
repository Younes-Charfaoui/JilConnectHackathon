package dz.jilconnect.dipannini.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfile(
    @Expose @SerializedName("id") val id: String,
    @Expose @SerializedName("name") val name: String = "N/A",
    @Expose @SerializedName("phone") val phone: String = "N/A",
    @Expose @SerializedName("location") val location: String = "N/A",
    @Expose @SerializedName("email") val email: String = "N/A",
    @Expose @SerializedName("activated") val activated: Boolean
)