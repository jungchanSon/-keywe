package com.ssafy.keywe.webrtc

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
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
        val info = AccessibilityServiceInfo()
        info.eventTypes =
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        info.notificationTimeout = 100
        info.flags =
            AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
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

            "MenuCart" -> {
                Log.d(TAG, "MenuCart: ")
                findAndClickNodeByDescription("menu_cart")
            }

            "CartOpenDialog" -> {
                Log.d(TAG, "CartOpenDialog: ")
                findAndClickNodeByDescription("cart_open_dialog")
            }

            "CartCloseDialog" -> {
                Log.d(TAG, "CartCloseDialog: ")
                findAndClickNodeByDescription("cart_close_dialog")
            }

            "CartAcceptDialog" -> {
                Log.d(TAG, "CartAcceptDialog: ")
                findAndClickNodeByDescription("cart_accept_dialog")
            }

            "MenuDetailSelectCommonOption" -> {
                val optionValue = intent.getStringExtra("optionValue") ?: return
                Log.d(TAG, "optionValue: $optionValue")
                findAndClickNodeByDescription("select_common_option_$optionValue")
            }

            "MenuDetailPlusExtraOption" -> {
                val optionName = intent.getStringExtra("optionName") ?: return
                Log.d(TAG, "optionName: $optionName")
                findAndClickNodeByDescription("plus_extra_option_$optionName")
            }

            "MenuDetailMinusExtraOption" -> {
                val optionName = intent.getStringExtra("optionName") ?: return
                Log.d(TAG, "optionName: $optionName")
                findAndClickNodeByDescription("minus_extra_option_$optionName")
            }

            "MenuCartPlusAmount" -> {
                val cartItemName = intent.getStringExtra("cartItemName") ?: return
                Log.d(TAG, "cartItemName: $cartItemName")
                findAndClickNodeByDescription("plus_amount_$cartItemName")
            }

            "MenuCartMinusAmount" -> {
                val cartItemName = intent.getStringExtra("cartItemName") ?: return
                Log.d(TAG, "cartItemName: $cartItemName")
                findAndClickNodeByDescription("minus_amount_$cartItemName")
            }
        }
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

            if (node.text?.toString()?.equals(text, ignoreCase = true) == true) {
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
                    ?.equals(description, ignoreCase = true) == true
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
                Log.d(" ", "화면 변경 감지됨: ${event.className}")

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