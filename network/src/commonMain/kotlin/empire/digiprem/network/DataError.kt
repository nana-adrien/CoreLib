package empire.digiprem.network

import empire.digiprem.shared.util.ResultError

sealed interface DataError : ResultError {

    enum class test: DataError{

    }
    sealed interface Remote : DataError {
        object BadRequest : Remote
        object RequestTimeout : Remote
        object Unauthorized : Remote
        object Forbidden : Remote
        object NotFound : Remote
        object Conflict : Remote
        object TooManyRequests : Remote
        object NoInternet : Remote
        object PayloadTooLarge : Remote
        object ServerError : Remote
        object ServerUnavailable : Remote
        object Unknown : Remote
        object Serialization : Remote
        data class ServerProcessError(val message: String) : Remote

    }

    sealed interface Local : DataError {
        object DiskFull : Local
        object NotFound : Local
        object Unknown : Local
    }
}