package com.ssafy.keywe.webrtc

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityWindowInfo
import com.ssafy.keywe.webrtc.data.MessageType


class RemoteControlService : AccessibilityService() {
    override fun onCreate() {
        super.onCreate()
//        serviceInfo.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
        Log.d("RemoteControlService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand 실행됨, intent: $intent")

        intent?.let {
            Log.d(TAG, "intent.action: ${intent.action}")

            when (intent.action) {
                MessageType.BUTTON_EVENT.name -> {
                    Log.d(TAG, "버튼 이벤트 처리 시작")
                    handleButtonEvent(intent)
                }

                else -> {
                    Log.e(TAG, "알 수 없는 intent.action: ${intent.action}")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected")

        val info = AccessibilityServiceInfo()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
//            info.eventTypes =
//                AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED
        }

        info.apply {
            feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
            notificationTimeout = 100  // 반응성 향상을 위해 타임아웃 감소
            flags =
                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }

        this.serviceInfo = info
    }


    override fun onInterrupt() {
        Log.d("RemoteControlService", "onInterrupt")
    }

    fun stopAccessibilityService() {
        disableSelf()
        Log.d("AccessibilityService", "서비스 종료됨")
    }

    override fun onDestroy() {
        Log.d("RemoteControlService", "onDestroy")
        stopAccessibilityService()
        super.onDestroy()
    }

    private fun handleButtonEvent(intent: Intent) {
        when (intent.getStringExtra("eventType")) {
            "CategorySelect" -> {
                val categoryName = intent.getStringExtra("categoryName") ?: return
                Log.d(TAG, "CategorySelect: $categoryName")
                findAndClickNodeByText(categoryName)
            }

            "MenuSelect" -> {
                val menuId = intent.getLongExtra("menuId", -1L)
                Log.d(TAG, "MenuSelect: $menuId")
                findAndClickNodeByDescription("menu_item_$menuId")
            }

            "MenuAddToCart" -> {
                val menuId = intent.getLongExtra("menuId", -1L)
                Log.d(TAG, "MenuAddToCart: $menuId")
                findAndClickNodeByDescription("add_to_cart_$menuId")
            }
        }
    }

    // 현재 활성화된 창의 루트 노드를 반환하는 함수
    private fun getActiveRootNode(): AccessibilityNodeInfo? {
        // serviceInfo에 FLAG_RETRIEVE_INTERACTIVE_WINDOWS 플래그가 설정되어 있어야 함
        for (window in windows) {
            // 애플리케이션 창이며 루트가 존재하는 창을 찾음
            if (window.isActive && window.root != null && window.type == AccessibilityWindowInfo.TYPE_APPLICATION) {
                return window.root
            }
        }
        return null
    }

    private fun findAndClickNodeByText(text: String) {
        // 매번 최신의 루트 노드를 새로 가져옴
//        val rootNode = getActiveRootNode()
        val rootNode = rootInActiveWindow  // 항상 최신 노드 가져오기
        if (rootNode == null) {
            Log.e(TAG, "rootInActiveWindow is null")
            return
        }
        val nodes = mutableListOf<AccessibilityNodeInfo>()
        nodes.add(rootNode)

        while (nodes.isNotEmpty()) {
            val node = nodes.removeAt(0)

            if (node.text?.toString()?.contains(text, ignoreCase = true) == true) {
                Log.d(TAG, "Found node with text: $text")

                // 1. 포커스 주기
                if (node.isFocusable) {
                    node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                    Log.d(TAG, "Node focused before clicking")
                }

                // 2. UI 갱신 (Android 11 이상에서 지원)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    node.refresh()
                    Log.d(TAG, "Node refreshed before clicking")
                }

                // 3. 클릭 가능한 경우 클릭
                if (node.isEnabled && node.isClickable) {
                    Log.d(TAG, "Node is enabled and clickable. Performing click action.")
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                } else {
                    Log.w(TAG, "Node is not clickable. Trying to find clickable parent.")
                    var parent = node.parent
                    while (parent != null) {
                        // 갱신 후 클릭 가능 여부 확인
                        if (parent.isClickable && parent.refresh()) {
                            Log.d(TAG, "Found clickable parent. Performing click action.")
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            return
                        }
                        parent = parent.parent
                    }
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { nodes.add(it) }
            }
        }
        Log.w(TAG, "Node with text '$text' not found")
    }

    private fun findAndClickNodeByDescription(description: String) {
        // 매번 최신의 루트 노드를 새로 가져옴
        val rootNode = rootInActiveWindow  // 항상 최신 노드 가져오기

        if (rootNode == null) {
            Log.e(TAG, "rootInActiveWindow is null")
            return
        }
        val nodes = mutableListOf<AccessibilityNodeInfo>()
        nodes.add(rootNode)

        while (nodes.isNotEmpty()) {
            val node = nodes.removeAt(0)

            if (node.contentDescription?.toString()
                    ?.contains(description, ignoreCase = true) == true
            ) {
                Log.d(TAG, "Found node with description: $description")

                if (node.isFocusable) {
                    node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                    Log.d(TAG, "Node focused before clicking")
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    node.refresh()
                    Log.d(TAG, "Node refreshed before clicking")
                }

                if (node.isEnabled && node.isClickable) {
                    Log.d(TAG, "Node is enabled and clickable. Performing click action.")
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                } else {
                    Log.w(TAG, "Node is not clickable. Trying to find clickable parent.")
                    var parent = node.parent
                    while (parent != null) {
                        if (parent.isClickable && parent.refresh()) {
                            Log.d(TAG, "Found clickable parent. Performing click action.")
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            return
                        }
                        parent = parent.parent
                    }
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { nodes.add(it) }
            }
        }
        Log.w(TAG, "Node with description '$description' not found")
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // 디버깅을 위한 이벤트 로깅
        val nodeInfo = event.source ?: return
        val eventType = event.eventType
        val contentDescription = nodeInfo.contentDescription?.toString() ?: "없음"
        val text = nodeInfo.text?.toString() ?: "없음"

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                -> {
                Log.d("Accessibility", "화면 변경 감지됨: ${event.className}")

                // 새로운 화면의 루트 노드 업데이트
                updateCurrentRootNode()
            }
        }

        Log.d(TAG, "Event: type=$eventType, text=$text, description=$contentDescription")
    }

    /**
     * 현재 화면의 최신 노드를 가져와 갱신하는 함수
     */
    private fun updateCurrentRootNode() {
        val rootNode = rootInActiveWindow ?: return
        Log.d("Accessibility", "현재 화면의 루트 노드 갱신됨")

        // 특정 요소를 찾고 싶다면 여기에 추가
        val targetNodes = rootNode.findAccessibilityNodeInfosByText("네비게이션 버튼")
        targetNodes?.forEach {
            Log.d("Accessibility", "찾은 요소: ${it.text}")
        }
    }

    companion object {
        private const val TAG = "RemoteControlService"
    }


}