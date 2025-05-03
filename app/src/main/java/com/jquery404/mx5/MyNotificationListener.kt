package com.jquery404.mx5

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

object NotificationCounter {
    var totalNotifications: Int = 0
}

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val extras = sbn?.notification?.extras ?: return
//        val title = extras?.getString("android.title") ?: ""
//        val text = extras?.getCharSequence("android.text")?.toString() ?: ""
        val packageName = sbn.packageName ?: "unknown"
        val title = extras.getString("android.title") ?: return
        val text = extras.getCharSequence("android.text")?.toString() ?: return
        if (packageName != "com.whatsapp.w4b") return

        // Match phone number
//        val isPhoneNumber = title.matches(Regex("""^\+?\d[\d\s-]{6,}$"""))
//        val hasLetters = title.matches(Regex(".*[a-zA-Z].*"))
//        val hasFBabu = title.startsWith("F Babu")
        val hasFBabu = title.startsWith("Brother Faisal")
        if (!hasFBabu) return

        val message = "$title: $text"
        if (NotificationState.isDuplicate(message)) return
        NotificationCounter.totalNotifications += 1
        NotificationState.update(message)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        NotificationCounter.totalNotifications = maxOf(0, NotificationCounter.totalNotifications - 1)
    }

}