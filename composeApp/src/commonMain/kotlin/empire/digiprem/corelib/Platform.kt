package empire.digiprem.corelib

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform