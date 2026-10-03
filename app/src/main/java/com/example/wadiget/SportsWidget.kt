package com.example.wadiget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SportsWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val teams = WidgetTheme.prefs(ctx).getString("teams", "") ?: ""
        val rv = RemoteViews(ctx.packageName, R.layout.widget_sports)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextViewText(R.id.body, if (teams.isBlank())
            "اختر فرقك المفضلة من التطبيق لتظهر مبارياتها وترتيبها هنا"
        else
            "فرقك: $teams
ستُعرض النتائج المباشرة وجداول الترتيب هنا عند ربط خدمة بيانات رياضية.")
        rv.setTextColor(R.id.title, accent)
        rv.setTextColor(R.id.body, WidgetTheme.white())
        val pi = PendingIntent.getActivity(ctx, 0, Intent(ctx, TeamsActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        rv.setOnClickPendingIntent(R.id.root, pi)
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }
}
