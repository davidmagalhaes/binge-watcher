package br.com.davidmag.bingewatcher.data.scheduler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AppDispatchersImpl : AppDispatchers {
    override fun network(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun database(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}