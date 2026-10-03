package com.example.wadiget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val prefs by lazy { WidgetTheme.prefs(this) }
    private lateinit var container: LinearLayout

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        if (!prefs.getBoolean("seen_onboarding", false)) startActivity(Intent(this, OnboardingActivity::class.java))

        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val tabs = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        container = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 16, 32, 16) }

        fun tab(label: String, build: () -> View) = tabs.addView(Button(this).apply {
            text = label; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setOnClickListener { container.removeAllViews(); container.addView(build()) }
        })
        tab("استكشاف") { exploreView() }
        tab("الاستوديو") { studioView() }
        tab("الإعدادات") { settingsView() }

        root.addView(tabs); root.addView(container)
        container.addView(exploreView())
        setContentView(root)
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun pin(cls: Class<*>) {
        val m = AppWidgetManager.getInstance(this)
        if (m.isRequestPinAppWidgetSupported) m.requestPinAppWidget(ComponentName(this, cls), null, null)
        else toast("اسحب الويدجت من قائمة الويدجتات في اللانشر")
    }

    private fun card(title: String, desc: String, vararg actions: Pair<String, () -> Unit>): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(32, 24, 32, 24)
            setBackgroundColor(0xFF1B1E26.toInt()); layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 0, 0, 24) }
        }
        box.addView(TextView(this).apply { text = title; textSize = 18f; setTextColor(0xFFFF6B57.toInt()); setTypeface(null, android.graphics.Typeface.BOLD) })
        box.addView(TextView(this).apply { text = desc; textSize = 13f; setTextColor(0xFFB0B4BC.toInt()); setPadding(0, 6, 0, 12) })
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        actions.forEach { (label, fn) -> row.addView(Button(this).apply { text = label; setOnClickListener { fn() } }) }
        box.addView(row)
        return box
    }

    private fun exploreView(): View {
        val v = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        v.addView(TextView(this).apply { text = "اختار ويدجت وثبّته على شاشتك"; textSize = 15f; setPadding(0, 8, 0, 16) })
        v.addView(card("⏰ الساعة والتاريخ", "ساعة رقمية + التاريخ الميلادي والهجري بالعربي", "تثبيت الويدجت" to { pin(ClockWidget::class.java) }))
        v.addView(card("🕌 مواقيت الصلاة", "الصلاة القادمة وكل المواقيت — المدينة والمذهب من الإعدادات", "تثبيت الويدجت" to { pin(PrayerWidget::class.java) }))
        v.addView(card("💬 اقتباسات وأذكار", "اضغط على الويدجت للتنقل بين الاقتباسات والآيات", "تثبيت الويدجت" to { pin(QuoteWidget::class.java) }))
        v.addView(card("📿 التسبيح", "عدّاد ضغطات ينتقل بين الأذكار كل 33 مع زر تصفير", "تثبيت الويدجت" to { pin(TasbihWidget::class.java) }))
        v.addView(card("✅ المهام والعادات", "مهامك اليومية وما أنجزته — اضغط للإدارة", "تثبيت الويدجت" to { pin(TasksWidget::class.java) }, "إدارة المهام" to { startActivity(Intent(this, TasksActivity::class.java)) }))
        v.addView(card("⚽ مباريات فرقك", "نتائج مباشرة وترتيب لفرقك المفضلة", "تثبيت الويدجت" to { pin(SportsWidget::class.java) }, "اختيار الفرق" to { startActivity(Intent(this, TeamsActivity::class.java)) }))
        return v
    }

    private fun studioView(): View {
        val v = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        v.addView(TextView(this).apply { text = "استوديو التخصيص"; textSize = 20f; setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 8, 0, 8) })
        v.addView(TextView(this).apply { text = "لون التمييز"; textSize = 15f; setPadding(0, 8, 0, 8) })
        val colors = listOf(0xFFFF6B57, 0xFF4FC3F7, 0xFF81C784, 0xFFFFD54F, 0xFFBA68C8, 0xFFF06292)
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        colors.forEach { c ->
            row.addView(Button(this).apply {
                setBackgroundColor(c.toInt()); text = ""
                layoutParams = LinearLayout.LayoutParams(110, 110).apply { setMargins(12, 0, 12, 0) }
                setOnClickListener { prefs.edit().putInt("accent", c.toInt()).apply(); Widgets.refreshAll(this@MainActivity); toast("تم تغيير اللون ✅") }
            })
        }
        v.addView(row)
        v.addView(TextView(this).apply { text = "شفافية خلفية الويدجت"; textSize = 15f; setPadding(0, 24, 0, 8) })
        v.addView(SeekBar(this).apply {
            max = 255; progress = prefs.getInt("alpha", 0xCC)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                    if (fromUser) { prefs.edit().putInt("alpha", p).apply(); Widgets.refreshAll(this@MainActivity) }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        })
        v.addView(Button(this).apply {
            text = "تحديث كل الويدجتات الآن"; setPadding(0, 24, 0, 0)
            setOnClickListener { Widgets.refreshAll(this@MainActivity); toast("تم التحديث ✅") }
        })
        v.addView(TextView(this).apply {
            text = "الاستوديو الكامل (تدرجات، خطوط، تخطيطات) متاح مع الاشتراك Pro"; textSize = 12f
            setTextColor(0xFFB0B4BC.toInt()); setPadding(0, 24, 0, 8)
        })
        return v
    }

    private fun settingsView(): View {
        val v = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        v.addView(TextView(this).apply { text = "الإعدادات"; textSize = 20f; setTypeface(null, android.graphics.Typeface.BOLD) })

        v.addView(TextView(this).apply { text = "📍 الموقع (خط العرض / الطول)"; textSize = 15f; setPadding(0, 16, 0, 8) })
        val lat = EditText(this).apply { hint = "خط العرض (مثال: 30.0444)"; setText(prefs.getFloat("lat", 30.0444f).toString()) }
        val lng = EditText(this).apply { hint = "خط الطول (مثال: 31.2357)"; setText(prefs.getFloat("lng", 31.2357f).toString()) }
        v.addView(lat); v.addView(lng)
        v.addView(Button(this).apply {
            text = "حفظ الموقع"
            setOnClickListener {
                val la = lat.text.toString().toFloatOrNull(); val ln = lng.text.toString().toFloatOrNull()
                if (la == null || ln == null || la !in -90f..90f || ln !in -180f..180f) { toast("إحداثيات غير صحيحة"); return@setOnClickListener }
                prefs.edit().putFloat("lat", la).putFloat("lng", ln).apply(); Widgets.refreshAll(this@MainActivity); toast("تم الحفظ ✅")
            }
        })
        v.addView(Switch(this).apply {
            text = "حساب العصر بالمذهب الحنفي"
            isChecked = prefs.getBoolean("hanafi", false)
            setOnCheckedChangeListener { _, c -> prefs.edit().putBoolean("hanafi", c).apply(); Widgets.refreshAll(this@MainActivity) }
        })
        v.addView(Button(this).apply { text = "إدارة المهام والعادات"; setOnClickListener { startActivity(Intent(this@MainActivity, TasksActivity::class.java)) } })
        v.addView(Button(this).apply { text = "فرقي ودورياتي"; setOnClickListener { startActivity(Intent(this@MainActivity, TeamsActivity::class.java)) } })
        v.addView(Button(this).apply { text = if (WidgetTheme.isPro(this@MainActivity)) "الاشتراك ✅ Pro مفعّل" else "الترقية إلى Pro"; setOnClickListener { startActivity(Intent(this@MainActivity, SubscribeActivity::class.java)) } })
        v.addView(Button(this).apply { text = "تواصل معنا"; setOnClickListener { startActivity(Intent(this@MainActivity, ContactActivity::class.java)) } })
        v.addView(Button(this).apply { text = "عن التطبيق"; setOnClickListener { startActivity(Intent(this@MainActivity, AboutActivity::class.java)) } })
        v.addView(Button(this).apply {
            text = "مشاركة التطبيق"
            setOnClickListener {
                val i = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "جرّب تطبيق ودجتي — ويدجتات الصلاة والساعة والأذكار 🕌⏰") }
                startActivity(Intent.createChooser(i, "مشاركة عبر"))
            }
        })
        v.addView(Button(this).apply {
            text = "إعادة عرض جولة التعريف"
            setOnClickListener { prefs.edit().putBoolean("seen_onboarding", false).apply(); startActivity(Intent(this@MainActivity, OnboardingActivity::class.java)) }
        })
        return v
    }
}
