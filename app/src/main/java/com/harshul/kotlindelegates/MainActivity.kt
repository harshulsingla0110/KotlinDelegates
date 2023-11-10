package com.harshul.kotlindelegates

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity(),
    AnalyticsLogger by AnalyticsLoggerImpl(),
    DeeplinkHandler by DeeplinkHandlerImpl() {

    private val result by MyLazy {
        println("MyLazy working")
        "RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerLifecycleOwner(this)
        println(result)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(this, intent)
    }
}

interface AnalyticsLogger {
    fun registerLifecycleOwner(owner: LifecycleOwner)
}

class AnalyticsLoggerImpl : AnalyticsLogger, LifecycleEventObserver {
    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> println("User has entered the screen")
            Lifecycle.Event.ON_PAUSE -> println("User has left the screen")
            else -> Unit
        }
    }
}

interface DeeplinkHandler {
    fun handleDeepLink(activity: AppCompatActivity, intent: Intent?)
}

class DeeplinkHandlerImpl : DeeplinkHandler {
    override fun handleDeepLink(activity: AppCompatActivity, intent: Intent?) {
        //handle intent here
    }
}

class MyLazy<out T : Any>(
    private val initialize: () -> T
) {
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (value == null) {
            value = initialize()
            value!!
        } else value!!
    }

}
