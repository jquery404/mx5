package com.jquery404.mx5

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationState {
//    private val _latestNotification = MutableStateFlow("No messages yet")
//    val latestNotification = _latestNotification.asStateFlow()
//
//    fun update(text: String) {
//        _latestNotification.value = text
//    }

    private val _latestNotifications = MutableStateFlow<List<String>>(emptyList())
    val latestNotifications = _latestNotifications.asStateFlow()
    private val seenMessages = mutableSetOf<String>()

    // Update the list with the new message
    fun update(message: String) {
        if (seenMessages.contains(message)) return
        seenMessages.add(message)

        if (seenMessages.size > 10) {
            seenMessages.remove(seenMessages.first())
        }

        val newList = _latestNotifications.value.toMutableList()
        newList.add(message) // Append new message
        if (newList.size > 5) {
            newList.removeAt(0)
        }
        _latestNotifications.value = newList
    }

    fun isDuplicate(message: String): Boolean {
        return seenMessages.contains(message)
    }
}
