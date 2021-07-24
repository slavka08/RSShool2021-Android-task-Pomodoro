package by.slavintodron.pomodoro.timer

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.pomodoro.R
import by.slavintodron.pomodoro.databinding.ItemTimerBinding

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val listener: ITimerListener
): RecyclerView.ViewHolder(binding.root) {

        private var currentTimer: Timer? = null

        fun bind(timer: Timer) {

            if (currentTimer != timer) {
                currentTimer?.tickCallback = null
                currentTimer?.stateCallback = null
                currentTimer = timer

                timer.tickCallback = {
                    setTimerTime(timer)
                    setTimerProgress(timer)
                }
                timer.stateCallback = {
                    setTimerState(timer)
                }
            }

            binding.customView.setPeriod(timer.initTime)
            binding.customView.setCurrent(timer.progress)

            setTimerState(timer)

            initButtonsListeners(timer)
        }

        private fun initButtonsListeners(timer: Timer) {

            binding.buttonSP.setOnClickListener {
                if (timer.isRunning) {
                    timer.stop()
                } else {
                    timer.start()
                }
                listener.replace(timer)
            }

            binding.deleteButton.setOnClickListener {
                listener.delete(timer)
            }

            binding.restartButton.setOnClickListener {
                timer.reset()
                listener.replace(timer)
            }
        }

        private fun setTimerState(timer: Timer) {
            binding.blinkingIndicator.isInvisible = (timer.isRunning)
            if (timer.isRunning) {
                binding.buttonSP.text = itemView.context.getString(R.string.stop)
                (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
            } else {
                binding.buttonSP.text = itemView.context.getString(R.string.start)
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
            }

            setTimerTime(timer)
            setTimerProgress(timer)

            if (timer.progress == 0L) {
                itemView.setBackgroundColor(itemView.resources.getColor(R.color.purple_200, null))
            } else {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }

        private fun setTimerTime(timer: Timer) {
            binding.stopwatchTimer.text = timer.text
        }

        private fun setTimerProgress(timer: Timer) {
            binding.customView.setCurrent(timer.progress)
       }

}