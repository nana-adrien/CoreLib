package empire.digiprem.network

import empire.digiprem.shared.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

 expect suspend fun <T>platformSafeCall(
    execute: suspend ()-> HttpResponse,
    handlerResponse: suspend (HttpResponse)-> Result<T, DataError.Remote>
):Result<T, DataError.Remote>


suspend inline  fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse,
): Result<T, DataError.Remote>{
    return  platformSafeCall(
        execute=execute,
        handlerResponse = {response ->responseToResult(response)}
    )
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote>{
    return  when (response.status.value){
        in 200 .. 299 -> {
            try {
                Result.Success(response.body<T>())
            }catch (e: NoTransformationFoundException){
                Result.Failure(DataError.Remote.Serialization)
            }
        }
        400-> Result.Failure(DataError.Remote.BadRequest)
        401-> Result.Failure(DataError.Remote.Unknown)
        403-> Result.Failure(DataError.Remote.Forbidden)
        404-> Result.Failure(DataError.Remote.NotFound)
        408-> Result.Failure(DataError.Remote.RequestTimeout)
        409-> Result.Failure(DataError.Remote.Conflict)
        413-> Result.Failure(DataError.Remote.PayloadTooLarge)
        429-> Result.Failure(DataError.Remote.TooManyRequests)
        500-> Result.Failure(DataError.Remote.ServerError)
        503-> Result.Failure(DataError.Remote.ServerUnavailable)
        else -> Result.Failure(DataError.Remote.Unknown)
    }

}



suspend inline fun <reified Request,reified Response: Any> HttpClient.post(
    route: String,
    body:Request,
    queryParams:Map<String, Any> =mapOf(),
    crossinline builder: HttpRequestBuilder.()-> Unit={}
): Result<Response, DataError.Remote>{
    return safeCall {
        post{
            url(route)
            queryParams.forEach {(key,valeu)->
                parameter(key,valeu)
            }
            setBody(body)
            builder()
        }
    }
}
suspend inline fun <reified Response: Any> HttpClient.get(
    route: String,
    queryParams:Map<String, Any> =mapOf(),
    crossinline builder: HttpRequestBuilder.()-> Unit={}
): Result<Response, DataError.Remote>{
    return safeCall {
        get{
            url(route)
            queryParams.forEach {(key,valeu)->
                parameter(key,valeu)
            }
            builder()
        }
    }
}
suspend inline fun <reified Response: Any> HttpClient.delete(
    route: String,
    queryParams:Map<String, Any> =mapOf(),
    crossinline builder: HttpRequestBuilder.()-> Unit={}
): Result<Response, DataError.Remote>{
    return safeCall {
        delete{
            url(route)
            queryParams.forEach {(key,valeu)->
                parameter(key,valeu)
            }
            builder()
        }
    }
}
suspend inline fun <reified Response: Any> HttpClient.put(
    route: String,
    queryParams:Map<String, Any> =mapOf(),
    crossinline builder: HttpRequestBuilder.()-> Unit={}
): Result<Response, DataError.Remote>{
    return safeCall {
        delete{
            url(route)
            queryParams.forEach {(key,valeu)->
                parameter(key,valeu)
            }
            builder()
        }
    }
}

