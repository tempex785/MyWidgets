package com.example.wadigetlike

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("settings", Context.MODE_PRIVATE) }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL
            setPadding(48, 48, 48, 48)
        }
        root.addView(TextView(this).apply {
            text = "ودجتي"; textSize = 26f; setPadding(0, 0, 0, 24)
        })
        fun add(label: String, cls: Class<*>) = root.addView(Button(this).apply {
            text = label; setOnClickListener {
                val m = AppWidgetManager.getInstance(this@MainActivity)
                if (m.isRequestPinAppWidgetSupported) m.requestPinAppWidget(ComponentName(this@MainActivity, cls), null, null)
                else Toast.makeText(this@MainActivity, "اسحب الويدجت من قائمة الويدجتات في اللانشر", Toast.LENGTH_LONG).show()
            }
        })
        add("⏰ إضافة ويدجت الساعة والتاريخ", ClockWidget::class.java)
        add("🕌 إضافة ويدجت مواقيت الصلاة", PrayerWidget::class.java)
        add("💬 إضافة ويدجت الاقتباسات والأذكار", QuoteWidget::class.java)
        add("📿 إضافة ويدجت التسبيح", TasbihWidget::class.java)

        root.addView(TextView(this).apply { text = "موقع حساب المواقيت (خط العرض / الطول)"; textSize = 14f; setPadding(0, 32, 0, 8) })
        val lat = EditText(this).apply { hint = "خط العرض (مثال: 30.0444)"; setText(prefs.getFloat("lat", 30.0444f).toString()) }
        val lng = EditText(this).apply { hint = "خط الطول (مثال: 31.2357)"; setText(prefs.getFloat("lng", 31.2357f).toString()) }
        root.addView(lat); root.addView(lng)
        root.addView(Button(this).apply {
            text = "حفظ الموقع وتحديث ويدجت الصلاة"
            setOnClickListener {
                val la = lat.text.toString().toFloatOrNull(); val ln = lng.text.toString().toFloatOrNull()
                if (la == null || ln == null || la !in -90f..90f || ln !in -180f..180f) {
                    Toast.makeText(this@MainActivity, "إحداثيات غير صحيحة", Toast.LENGTH_SHORT).show(); return@setOnClickListener
                }
                prefs.edit().putFloat("lat", la).putFloat("lng", ln).apply()
                PrayerWidget.refreshAll(this@MainActivity)
                Toast.makeText(this@MainActivity, "تم الحفظ ✅", Toast.LENGTH_SHORT).show()
            }
        })
        setContentView(root)
    }
}
