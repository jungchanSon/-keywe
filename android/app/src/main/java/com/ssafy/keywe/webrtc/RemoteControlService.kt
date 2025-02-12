package com.ssafy.keywe.webrtc

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.ssafy.keywe.webrtc.data.MessageType


class RemoteControlService : AccessibilityService() {
    override fun onCreate() {
        super.onCreate()
//        serviceInfo.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
        Log.d("RemoteControlService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val x = intent.getFloatExtra("x", -1F)
            val y = intent.getFloatExtra("y", -1F)
            if (validGesture(x, y)) return super.onStartCommand(intent, flags, startId)
            when (intent.action) {
                MessageType.Touch.name -> {
                    click(x, y)
                }

                MessageType.Drag.name -> {
//        Log.d("RemoteControlService", "onStartCommand")
//        val action = intent?.getStringExtra("Drag")
//        if (action == "Drag") doRightThenDownDrag()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun validGesture(x: Float, y: Float): Boolean = x == -1F || y == -1F


    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = AccessibilityServiceInfo()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            info.eventTypes =
                AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.CONTENT_CHANGE_TYPE_DRAG_STARTED or AccessibilityEvent.CONTENT_CHANGE_TYPE_DRAG_DROPPED or AccessibilityEvent.CONTENT_CHANGE_TYPE_DRAG_CANCELLED
        }

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        info.notificationTimeout = 1000
        this.serviceInfo = info
        Log.d("WebrtcAccessibilityService", "onServiceConnected : ")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        if (event?.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//
//        }
        val nodeInfo = event.source ?: return
        val eventType = event.eventType
        // Semantics 정보 확인
        val contentDescription = nodeInfo.contentDescription?.toString() ?: "없음"
//        Log.d("AccessibilityEvent", "------------------------")
//        Log.d("AccessibilityEvent", "이벤트 발생: $eventType")
//        Log.d("AccessibilityEvent", "설명: $contentDescription")
//        Log.d("AccessibilityEvent", "------------------------")

//        val nodeInfo = event.source ?: return

//        // 뒤는 원하는 대로...
//        val id = nodeInfo.viewIdResourceName // View의 ID를 취득
//        val packageName = event.packageName.toString() // View의 ID를 취득
//        val resourceName = nodeInfo.viewIdResourceName
//        val className = nodeInfo.className.toString() // Class명을 취득
//        val eventType = AccessibilityEvent.eventTypeToString(event.eventType)
//
//        Log.d("onAccessibilityEvent", "id : $id")
//        Log.d("onAccessibilityEvent", "packageName : $packageName")
//        Log.d("onAccessibilityEvent", "resourceName : $resourceName")
//        Log.d("onAccessibilityEvent", "className : $className")
//        Log.d("onAccessibilityEvent", "eventType : $eventType")
//        Log.d("RemoteControlService", "onAccessibilityEvent $event")
    }

    override fun onInterrupt() {
        Log.d("RemoteControlService", "onInterrupt")
    }

    fun stopAccessibilityService() {
        disableSelf()
        Log.d("AccessibilityService", "서비스 종료됨")
    }

    override fun onDestroy() {
        Log.d("RemoteControlService", "onInterrupt")
        super.onDestroy()
    }

    private fun click(x: Float, y: Float, startTime: Long = 100, duration: Long = 100) {
        Log.d("RemoteControlService", "CLICK")
        val gestureBuilder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, startTime, duration))
        val gesture = gestureBuilder.build()
        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d("RemoteControlService", "Gesture completed")
                super.onCompleted(gestureDescription)
                // 화면 터치 제스처 실행 완료
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.d("RemoteControlService", "Gesture onCancelled")
                super.onCancelled(gestureDescription)
                // 화면 터치 제스처 실행 취소
            }
        }, null)
    }


//    private fun createGesture(path: Path): GestureDescription {
//        val stroke = GestureDescription.StrokeDescription(path, 0, 50) // 빠른 응답
//        return GestureDescription.Builder().addStroke(stroke).build()
//    }


//    fun handleReceivedGesture(action: Int, x: Float, y: Float) {
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                path.reset()
//                path.moveTo(x, y)
//                isDragging = true
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                if (isDragging) {
//                    path.lineTo(x, y)
//                    handler.post { dispatchGesture(createGesture(path), null, null) }
//                }
//            }
//
//            MotionEvent.ACTION_UP -> {
//                isDragging = false
//                path.reset()
//            }
//        }
//    }
}