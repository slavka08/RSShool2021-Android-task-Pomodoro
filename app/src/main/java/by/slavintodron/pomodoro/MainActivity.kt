package by.slavintodron.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import by.slavintodron.pomodoro.databinding.ActivityMainBinding
import by.slavintodron.pomodoro.fservice.ForegroundService
import by.slavintodron.pomodoro.fservice.ForegroundService.Companion.COMMAND_ID
import by.slavintodron.pomodoro.fservice.ForegroundService.Companion.COMMAND_START
import by.slavintodron.pomodoro.fservice.ForegroundService.Companion.COMMAND_STOP
import by.slavintodron.pomodoro.fservice.ForegroundService.Companion.STARTED_TIMER_TIME_MS
import by.slavintodron.pomodoro.timer.ITimerListener
import by.slavintodron.pomodoro.timer.Timer
import by.slavintodron.pomodoro.timer.TimerAdapter

class MainActivity : AppCompatActivity(), ITimerListener, LifecycleObserver {
    private lateinit var binding: ActivityMainBinding

    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }
        binding.apply {
            addNewStopwatchButton.setOnClickListener {
                val minutes = inpuTime.text.toString().toLongOrNull()
                if (minutes != null) {
                    timers.add(Timer(minutes * 60 * 1000))
                    timerAdapter.submitList(timers.toList())
                }
            }
        }

    }


    override fun replace(timer: Timer) {
        if (timer.isRunning) {
            timers.forEach {
                if (it.id != timer.id && it.isRunning) {
                    it.stop()
                }
            }
        }
        timerAdapter.notifyDataSetChanged()
        timerAdapter.submitList(timers.toList())
    }

    override fun delete(timer: Timer) {
        timer.stop()
        timers.remove(timer)
        timerAdapter.submitList(timers.toList())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        val currentTime = getCurrentTimerTime()
        if (currentTime != -1L) {
            val startIntent = Intent(this, ForegroundService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(STARTED_TIMER_TIME_MS, currentTime)
            startService(startIntent)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun onDestroy() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)

        super.onDestroy()
    }

    private fun getCurrentTimerTime(): Long {
        timers.forEach {
            if (it.isRunning) return it.progress
        }
        return -1
    }
}


