package com.example.mywidgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(48, 48, 48, 48) }
        fun add(label: String, cls: Class<*>) = root.addView(Button(this).apply {
            text = label
            setOnClickListener {
                val m = AppWidgetManager.getInstance(this@MainActivity)
                if (m.isRequestPinAppWidgetSupported) m.requestPinAppWidget(ComponentName(this@MainActivity, cls), null, null)
            }
        })
        add("إضافة ويدجت الساعة والتاريخ", ClockWidget::class.java)
        add("إضافة ويدجت مواقيت الصلاة", PrayerWidget::class.java)
        setContentView(root)
    }
}
