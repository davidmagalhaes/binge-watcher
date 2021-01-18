package br.com.davidmag.bingewatcher.data.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object AppSchedulersImpl : AppSchedulers {
    override fun network(): Scheduler {
        return Schedulers.io()
    }

    override fun database(): Scheduler {
        return Schedulers.single()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}