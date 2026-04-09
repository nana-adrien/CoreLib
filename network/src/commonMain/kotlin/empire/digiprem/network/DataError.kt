package empire.digiprem.network

import corelib.network.generated.resources.Res
import corelib.network.generated.resources.error_disk_full
import empire.digiprem.shared.util.ResultError
import empire.digiprem.shared.util.UiText

sealed interface DataError : ResultError {

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


fun DataError.toUiText(): UiText {
    if (this is DataError.Remote.ServerProcessError) {
        return UiText.DynamicString(this.message)
    }
    val ressource = when (this) {
        DataError.Local.DiskFull -> Res.string.error_disk_full
        DataError.Local.NotFound -> Res.string.error_disk_full
        DataError.Local.Unknown -> Res.string.error_disk_full
        DataError.Remote.BadRequest -> Res.string.error_disk_full
        DataError.Remote.Conflict -> Res.string.error_disk_full
        DataError.Remote.Forbidden -> Res.string.error_disk_full
        DataError.Remote.NoInternet -> Res.string.error_disk_full
        DataError.Remote.NotFound -> Res.string.error_disk_full
        DataError.Remote.PayloadTooLarge -> Res.string.error_disk_full
        DataError.Remote.RequestTimeout -> Res.string.error_disk_full
        DataError.Remote.Serialization -> Res.string.error_disk_full
        DataError.Remote.ServerError -> Res.string.error_disk_full
        DataError.Remote.ServerUnavailable -> Res.string.error_disk_full
        DataError.Remote.TooManyRequests -> Res.string.error_disk_full
        DataError.Remote.Unauthorized -> Res.string.error_disk_full
        DataError.Remote.Unknown -> Res.string.error_disk_full
        is DataError.Remote.ServerProcessError -> Res.string.error_disk_full
    }
    return UiText.Resource(ressource)
}