package empire.digiprem.shared.util


inline fun <T,E: ResultError,R> Result<T,E>.map(map:(T)->R): Result<R,E> {
    return when(this){
        is Result.Failure -> this
        is Result.Success -> Result.Success(map(data))
    }
}



inline fun <T,E: ResultError> Result<T,E>.onSuccess(
    onAction:(T)-> Unit
): Result<T,E> {
    return when(this){
        is Result.Failure -> this
        is Result.Success -> {
            onAction(data)
            this
        }
    }
}


fun <T,E: ResultError> Result<T,E>.onSuccess(): T?{
    return when(this){
        is Result.Failure -> null
        is Result.Success -> data
    }
}



inline fun <T,E: ResultError> Result<T,E>.onFailure(
    onError:(E)-> Unit
): Result<T,E> {
    return when(this){
        is Result.Failure -> {
            onError(error)
            this
        }
        is Result.Success -> this

    }
}