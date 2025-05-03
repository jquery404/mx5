package com.jquery404.mx5

import android.content.Intent
import android.provider.Settings
import android.app.NotificationManager
import android.view.View
import com.jquery404.mx5.ui.theme.Mx5Theme
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setLowBrightness()
        enterImmersiveMode()

        requestDNDPermission()
        enableDNDMode()

        setContent {
            //val latestMessage by NotificationState.latestNotification.collectAsState()
            val latestMessages by NotificationState.latestNotifications.collectAsState()

            Mx5Theme {
                var notificationCount by remember { mutableStateOf(0) }

                LaunchedEffect(Unit) {
                    while (true) {
                        notificationCount = NotificationCounter.totalNotifications
                        delay(5000)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                        .background(ComposeColor.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You have $notificationCount messages",
                            fontSize = 24.sp,
                            color = ComposeColor.LightGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
//                        Text(
//                            text = latestMessage,
//                            fontSize = 18.sp,
//                            color = ComposeColor.LightGray
//                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            items(latestMessages) { message ->
                                Text(
                                    text = message,
                                    fontSize = 16.sp,
                                    color = ComposeColor.LightGray,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    maxLines = Int.MAX_VALUE,
                                    overflow = TextOverflow.Visible
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 1.dp,
                                    color = ComposeColor.Gray.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun enterImmersiveMode() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun setLowBrightness() {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = 0.1f  // 0.0 (dark) to 1.0 (full brightness)
        window.attributes = layoutParams
    }

    fun requestDNDPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        startActivity(intent)
    }

    private fun enableDNDMode() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) enterImmersiveMode()
    }
}
