package br.com.davidmag.bingewatcher.testcommons

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class CustomCountingIdlingResource(resourceName: String) : IdlingResource {

    private val counting = CountingIdlingResource(resourceName)
    private var waitingIncrement = AtomicBoolean(false)

    fun increment() {
        counting.increment()
        waitingIncrement.set(false)
    }

    fun decrement(){
        if(!counting.isIdleNow) counting.decrement()
    }

    fun waitIncrement() {
        waitingIncrement.set(true)
    }

    override fun getName(): String {
        return counting.name
    }

    override fun isIdleNow(): Boolean {
        return counting.isIdleNow && !waitingIncrement.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        return counting.registerIdleTransitionCallback(callback)
    }
}