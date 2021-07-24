package by.slavintodron.pomodoro.timer

import by.slavintodron.pomodoro.timer.Timer

interface ITimerListener {
    fun replace(timer: Timer)
    fun delete(timer: Timer)
}