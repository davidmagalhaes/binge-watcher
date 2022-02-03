package br.com.davidmag.bingewatcher.domain.util

import java.util.concurrent.atomic.AtomicBoolean

class NamedBooleanSwitches(private val onSwitch: (String, Boolean)->Unit) {
    private val namedBooleans = mutableMapOf<String, AtomicBoolean>()

    fun switch(name: String, value: Boolean) {
        val switch = namedBooleans.getOrPut(name) { AtomicBoolean(false) }

        if(switch.compareAndSet(!value, value)) onSwitch(name, value)
    }

    fun get(name: String) = namedBooleans.getOrElse(name) { AtomicBoolean(false) }.get()
}