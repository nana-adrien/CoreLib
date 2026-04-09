package empire.digiprem.network

import empire.digiprem.shared.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

 actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handlerResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response=execute()
        handlerResponse(response)
    }catch (e: UnknownHostException){
        println(message = e.message)
        Result.Failure(DataError.Remote.NoInternet)
    }catch (e: UnresolvedAddressException){
        println(message = e.message)
        Result.Failure(DataError.Remote.NoInternet)
    }catch (e: ConnectException){
        println(message = e.message)
        Result.Failure(DataError.Remote.NoInternet)
    }catch (e: SocketTimeoutException){
        println(message = e.message)
        Result.Failure(DataError.Remote.RequestTimeout)
    }catch (e: HttpRequestTimeoutException){
        println(message = e.message)
        Result.Failure(DataError.Remote.RequestTimeout)
    }catch (e: SerializationException){
        println(message = e.message)
        Result.Failure(DataError.Remote.Serialization)
    }catch (e: Exception){
        println(message = e.message)
        coroutineContext.ensureActive()
        Result.Failure(DataError.Remote.Unknown)
    }
}