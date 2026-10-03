package com.example.wadiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val p = WidgetTheme.prefs(ctx)
        val coords = Coordinates(p.getFloat("lat", 30.0444f).toDouble(), p.getFloat("lng", 31.2357f).toDouble())
        val params = CalculationMethod.EGYPTIAN.parameters.apply {
            if (p.getBoolean("hanafi", false)) madhab = Madhab.HANAFI
        }
        val pt = PrayerTimes(coords, DateComponents.from(Date()), params)
        val fmt = SimpleDateFormat("hh:mm a", Locale("ar"))
        val names = linkedMapOf(Prayer.FAJR to "الفجر", Prayer.SUNRISE to "الشروق", Prayer.DHUHR to "الظهر",
            Prayer.ASR to "العصر", Prayer.MAGHRIB to "المغرب", Prayer.ISHA to "العشاء")
        val next = pt.nextPrayer().let { if (it == Prayer.NONE) Prayer.FAJR else it }
        val rv = RemoteViews(ctx.packageName, R.layout.widget_prayer)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextViewText(R.id.next, "الصلاة القادمة: ${names[next]}  ${fmt.format(pt.timeForPrayer(next))}")
        rv.setTextViewText(R.id.all, names.entries.joinToString("  •  ") { "${it.value} ${fmt.format(pt.timeForPrayer(it.key))}" })
        rv.setTextColor(R.id.next, accent)
        rv.setTextColor(R.id.all, WidgetTheme.white())
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }
}
