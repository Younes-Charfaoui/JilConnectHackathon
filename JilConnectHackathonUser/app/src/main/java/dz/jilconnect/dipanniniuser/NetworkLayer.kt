package dz.jilconnect.dipanniniuser

class NetworkLayer {

    private val webService: CashToCashService = CashToCashService.retrofit
        .create(CashToCashService::class.java)

    suspend fun getWorkerData(data: String) = webService.workerDataAsync(data).await()
}