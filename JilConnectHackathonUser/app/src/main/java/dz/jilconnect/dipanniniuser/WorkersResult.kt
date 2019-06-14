package dz.jilconnect.dipanniniuser

data class WorkersResult(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val location: String,
    val activated: Int,
    val api_token: String,
    val created_at: String,
    val updated_at: String,
    val distance: String
)