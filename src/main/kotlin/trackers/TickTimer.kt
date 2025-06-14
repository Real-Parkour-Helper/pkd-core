package trackers

class TickTimer {

    private var running = false
    private var elapsedTicks: Long = 0

    /**
     * Starts the tick timer.
     */
    fun start() {
        running = true
    }

    /**
     * Stops the tick timer.
     */
    fun stop() {
        running = false
    }

    /**
     * Increments the tick counter by one.
     */
    fun tick() {
        elapsedTicks++
    }

    /**
     * Resets the tick counter to zero.
     */
    fun reset() {
        elapsedTicks = 0
    }

    /**
     * Returns the number of elapsed ticks since the last reset.
     */
    fun getElapsedTicks(): Long {
        return elapsedTicks
    }

    /**
     * Returns the elapsed time formatted as mm:ss.SSS
     */
    fun getElapsedTimeString(): String {
        val totalSeconds = elapsedTicks / 20
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val milliseconds = (elapsedTicks % 20) * 50

        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds)
    }

}