package dz.jilconnect.dipannini.webservices

class NetworkLayer {

    private val webService: DepaniniWorkerService = DepaniniWorkerService.retrofit
        .create(DepaniniWorkerService::class.java)

    suspend fun register(name: String, email: String, password: String) =
        webService.signUpAsync(name, email, password).await()

    suspend fun getUserData(id: String) = webService.getUserDataAsync(id).await()

    suspend fun updateUserData(
        id: String, phone: String, location: String
    ) = webService.updateAsync(id, phone, location).await()
}