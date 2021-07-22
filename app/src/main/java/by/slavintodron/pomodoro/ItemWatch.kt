package by.slavintodron.pomodoro

data class ItemWatch (
        val id: Int,
        var currentMs: Long,
        var isStarted: Boolean
)