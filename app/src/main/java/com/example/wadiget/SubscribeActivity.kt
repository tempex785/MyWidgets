package com.example.wadiget

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SubscribeActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val prefs = WidgetTheme.prefs(this)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 48, 48, 48) }
        root.addView(TextView(this).apply { text = "ودجتي Pro"; textSize = 26f; setTypeface(null, android.graphics.Typeface.BOLD) })
        root.addView(TextView(this).apply {
            text = "✦ الاستوديو الكامل: تدرجات وخطوط وتخطيطات
✦ ويدجتات غير محدودة على الشاشة
✦ الأذان والتذكيرات بصوت تلقائي
✦ بدون إعلانات"; textSize = 15f; setPadding(0, 16, 0, 24)
        })
        fun plan(label: String, planId: String) = root.addView(Button(this).apply {
            text = label; setOnClickListener {
                prefs.edit().putBoolean("pro", true)
                    .putString("pro_plan", planId)
                    .putString("pro_since", SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()))
                    .apply()
                Toast.makeText(this@SubscribeActivity, "تم تفعيل الاشتراك ✅ (وضع تجريبي)", Toast.LENGTH_LONG).show()
                finish()
            }
        })
        plan("اشتراك شهري — ١٩ ج", "monthly")
        plan("اشتراك سنوي — ١٤٩ ج", "yearly")
        plan("مدى الحياة — ٣٩٩ ج", "lifetime")
        root.addView(Button(this).apply {
            text = "استرجاع المشتريات"
            setOnClickListener {
                Toast.makeText(this@SubscribeActivity,
                    if (WidgetTheme.isPro(this@SubscribeActivity)) "تم استرجاع اشتراكك ✅" else "لا توجد مشتريات سابقة",
                    Toast.LENGTH_SHORT).show()
            }
        })
        root.addView(Button(this).apply {
            text = "إلغاء الشراء"
            setOnClickListener { prefs.edit().putBoolean("pro", false).apply(); Toast.makeText(this@SubscribeActivity, "تم إلغاء الاشتراك", Toast.LENGTH_SHORT).show(); finish() }
        })
        root.addView(TextView(this).apply {
            text = "وضع تجريبي — لا توجد عمليات دفع حقيقية هنا. اربط Google Play Billing للنشر."; textSize = 11f
            setTextColor(0xFFB0B4BC.toInt()); setPadding(0, 24, 0, 0)
        })
        setContentView(root)
    }
}
