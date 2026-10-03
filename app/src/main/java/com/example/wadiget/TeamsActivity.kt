package com.example.wadiget

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class TeamsActivity : Activity() {
    private val allTeams = listOf("الأهلي", "الزمالك", "برشلونة", "ريال مدريد", "مانشستر سيتي", "ليفربول",
        "آرسنال", "بايرن ميونخ", "باريس سان جيرمان", "الهلال", "النصر", "الوداد", "الترجي")

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val prefs = WidgetTheme.prefs(this)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 32, 32, 32) }
        root.addView(TextView(this).apply { text = "فرقي ودورياتي"; textSize = 22f; setTypeface(null, android.graphics.Typeface.BOLD) })
        root.addView(TextView(this).apply { text = "اختر فرقك المفضلة (حتى 5) لتظهر مبارياتها في الويدجت"; setPadding(0, 8, 0, 16) })
        val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        root.addView(list)

        fun selected(): MutableSet<String> = (prefs.getString("teams", "") ?: "").split(",").filter { it.isNotBlank() }.toMutableSet()

        allTeams.forEach { team ->
            list.addView(CheckBox(this).apply {
                text = team; isChecked = team in selected(); textSize = 16f
                setOnCheckedChangeListener { _, c ->
                    val s = selected()
                    if (c) {
                        if (s.size >= 5) {
                            isChecked = false
                            return@setOnCheckedChangeListener
                        }
                        s.add(team)
                    } else {
                        s.remove(team)
                    }
                    prefs.edit().putString("teams", s.joinToString(",")).apply()
                }
            })
        }
        root.addView(TextView(this).apply {
            text = "ملاحظة: النتائج المباشرة تحتاج ربطًا بخدمة بيانات رياضية قبل النشر."; textSize = 11f
            setTextColor(0xFFB0B4BC.toInt()); setPadding(0, 24, 0, 0)
        })
        setContentView(root)
    }

    override fun onPause() { super.onPause(); Widgets.refreshAll(this) }
}
