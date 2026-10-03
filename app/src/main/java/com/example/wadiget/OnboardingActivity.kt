package com.example.wadiget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class OnboardingActivity : Activity() {
    private val pages = listOf(
        Triple("أهلًا بك في ودجتي 🎉", "ويدجتات الساعة ومواقيت الصلاة والأذكار والمهام على شاشتك الرئيسية — حدّثها كما تحب من الاستوديو.", null),
        Triple("📍 الموقع", "نستخدم موقعك لحساب مواقيت الصلاة لمدينتك بدقة. يمكنك تغييره يدويًا في أي وقت من الإعدادات.", null),
        Triple("🔋 البطارية", "اسمح لودجتي بالعمل في الخلفية حتى تبقى الويدجتات محدثة دائمًا.", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
        Triple("🔔 الإشعارات", "ستصلك التذكيرات والأذان في وقتها (تُفعّل الإشعارات في تحديث قادم).", null),
        Triple("✅ كل شيء جاهز!", "اضغط مطولًا على الشاشة الرئيسية ← «الويدجتس» ← اختر «ودجتي».", null)
    )
    private var idx = 0

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(48, 48, 48, 48) }
        val title = TextView(this).apply { textSize = 24f; setTypeface(null, android.graphics.Typeface.BOLD) }
        val body = TextView(this).apply { textSize = 16f; setPadding(0, 24, 0, 24) }
        val extra = Button(this)
        val next = Button(this)
        root.addView(title); root.addView(body); root.addView(extra); root.addView(next)

        fun show() {
            val (t, s, action) = pages[idx]
            title.text = t; body.text = s
            extra.visibility = if (action == null) android.view.View.GONE else android.view.View.VISIBLE
            extra.text = "فتح الإعدادات"
            extra.setOnClickListener { startActivity(Intent(action)) }
            next.text = if (idx == pages.lastIndex) "ابدأ 🚀" else "التالي"
            next.setOnClickListener {
                if (idx < pages.lastIndex) { idx++; show() }
                else { WidgetTheme.prefs(this).edit().putBoolean("seen_onboarding", true).apply(); finish() }
            }
        }
        show()
        setContentView(root)
    }
}
