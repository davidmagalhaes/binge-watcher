package br.com.davidmag.bingewatcher.data.scheduler

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    fun network() : CoroutineDispatcher
    fun database() : CoroutineDispatcher
    fun main() : CoroutineDispatcher
}