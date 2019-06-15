package dz.jilconnect.dipanniniuser

class NetworkLayer {

    private val webService: DepaniniService = DepaniniService.retrofit
        .create(DepaniniService::class.java)

    suspend fun getWorkerData(data: String) = webService.workerDataAsync(data).await()
}