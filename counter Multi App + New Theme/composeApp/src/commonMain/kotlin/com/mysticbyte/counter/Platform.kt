package com.mysticbyte.counter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getDataStoreFilePath(): String
