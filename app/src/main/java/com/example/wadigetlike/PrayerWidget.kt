package com.example.wadigetlike

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val coords = Coordinates(prefs.getFloat("lat", 30.0444f).toDouble(), prefs.getFloat("lng", 31.2357f).toDouble())
        val pt = PrayerTimes(coords, DateComponents.from(Date()), CalculationMethod.EGYPTIAN.parameters)
        val fmt = SimpleDateFormat("hh:mm a", Locale("ar"))
        val names = linkedMapOf(Prayer.FAJR to "الفجر", Prayer.SUNRISE to "الشروق", Prayer.DHUHR to "الظهر",
            Prayer.ASR to "العصر", Prayer.MAGHRIB to "المغرب", Prayer.ISHA to "العشاء")
        val next = pt.nextPrayer().let { if (it == Prayer.NONE) Prayer.FAJR else it }
        val rv = RemoteViews(ctx.packageName, R.layout.widget_prayer)
        rv.setTextViewText(R.id.next, "الصلاة القادمة: ${names[next]}  ${fmt.format(pt.timeForPrayer(next))}")
        rv.setTextViewText(R.id.all, names.entries.joinToString("  •  ") { "${it.value} ${fmt.format(pt.timeForPrayer(it.key))}" })
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }

    companion object {
        fun refreshAll(ctx: Context) {
            val mgr = AppWidgetManager.getInstance(ctx)
            val ids = mgr.getAppWidgetIds(ComponentName(ctx, PrayerWidget::class.java))
            if (ids.isNotEmpty()) PrayerWidget().onUpdate(ctx, mgr, ids)
        }
    }
}
