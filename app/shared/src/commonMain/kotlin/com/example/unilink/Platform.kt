package com.example.unilink

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform