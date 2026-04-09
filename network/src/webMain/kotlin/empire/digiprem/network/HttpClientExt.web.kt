package empire.digiprem.network

import empire.digiprem.shared.util.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse

 actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handlerResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handlerResponse(response)
    }  catch (e: ServerResponseException) {
        println(message = e.message)
        Result.Failure(DataError.Remote.ServerError)
    } catch (e: HttpRequestTimeoutException) {
        println(message = e.message)
        Result.Failure(DataError.Remote.RequestTimeout)
    } catch (e: Throwable) {
        println(message = e.message)
        Result.Failure(DataError.Remote.NoInternet)
    }
}

