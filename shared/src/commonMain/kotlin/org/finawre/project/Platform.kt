package org.finawre.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform