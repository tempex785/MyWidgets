package com.example.wadiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ClockWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val ar = Locale("ar")
        val rv = RemoteViews(ctx.packageName, R.layout.widget_clock)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextColor(R.id.clock, WidgetTheme.white())
        rv.setTextViewText(R.id.date, LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d MMMM", ar)))
        rv.setTextViewText(R.id.hijri, DateTimeFormatter.ofPattern("d MMMM uuuu", ar).format(HijrahDate.now()))
        rv.setTextColor(R.id.date, accent)
        rv.setTextColor(R.id.hijri, WidgetTheme.grey())
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }
}
