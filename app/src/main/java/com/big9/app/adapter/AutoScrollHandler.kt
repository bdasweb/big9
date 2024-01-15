package com.big9.app.adapter

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2

class AutoScrollHandler(private val viewPager: ViewPager2) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            viewPager.currentItem = currentPage
            currentPage++
            if (currentPage >= viewPager.adapter?.itemCount ?: 0) {
                currentPage = 0
            }
            handler.postDelayed(this, AUTO_SCROLL_DELAY) // Adjust the delay as needed
        }
    }

    fun startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
    }

    fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 3000L // Adjust the delay in milliseconds
    }
}
