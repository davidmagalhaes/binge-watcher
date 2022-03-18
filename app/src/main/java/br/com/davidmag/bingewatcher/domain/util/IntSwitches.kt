package br.com.davidmag.bingewatcher.domain.util

import java.util.concurrent.atomic.AtomicInteger

class IntSwitches(private val onSwitch: (String, Int)->Unit = { _, _ -> }) {
    private val namedInts = mutableMapOf<String, AtomicInteger>()

    fun switch(name: String, value: Int) {
        val switch = namedInts.getOrPut(name) { AtomicInteger(0) }

        synchronized(namedInts) {
            if(switch.get() != value) {
                switch.set(value)
                onSwitch(name, value)
            }
        }
    }

    fun get(name: String) = namedInts.getOrElse(name) { AtomicInteger(0) }.get()
}