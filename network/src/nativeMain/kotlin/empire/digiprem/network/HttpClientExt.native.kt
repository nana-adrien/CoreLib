package empire.digiprem.network

import empire.digiprem.shared.util.Result
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut

 actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handlerResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handlerResponse(response)

    } catch (e: DarwinHttpRequestException) {
        handleDarwinException(e)

    } catch (e: ServerResponseException) {
        print(e.message)
        Result.Failure(DataError.Remote.ServerError)

    } catch (e: HttpRequestTimeoutException) {
        print(e.message)
        Result.Failure(DataError.Remote.RequestTimeout)

    } catch (e: UnresolvedAddressException) {
        print(e.message)
        Result.Failure(DataError.Remote.NoInternet)

    } catch (e: SerializationException) {
        print(e.message)
        Result.Failure(DataError.Remote.Serialization)

    } catch (e: Exception) {
        print(e.message)
        Result.Failure(DataError.Remote.Unknown)
    }
}


private fun handleDarwinException(e: DarwinHttpRequestException): Result<Nothing, DataError.Remote>{
    val nsError=e.origin
    return if (nsError.domain== NSURLErrorDomain){
        when(nsError.code){
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed->Result.Failure(DataError.Remote.NoInternet)
            NSURLErrorTimedOut-> Result.Failure(DataError.Remote.RequestTimeout)
            else->Result.Failure(DataError.Remote.Unknown)
        }
    } else Result.Failure(DataError.Remote.Unknown)
}