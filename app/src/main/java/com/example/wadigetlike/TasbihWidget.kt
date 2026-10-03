package com.example.wadigetlike

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TasbihWidget : AppWidgetProvider() {
    private val adhkar = listOf("سبحان الله", "الحمد لله", "الله أكبر", "لا إله إلا الله")

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val count = prefs.getInt("tasbih_count", 0)
        val dhikr = adhkar[(count / 33) % adhkar.size]
        val rv = RemoteViews(ctx.packageName, R.layout.widget_tasbih)
        rv.setTextViewText(R.id.dhikr, dhikr)
        rv.setTextViewText(R.id.count, "$count")
        rv.setTextViewText(R.id.cycle, "الجولة ${(count / 33) % adhkar.size + 1}")
        val tap = PendingIntent.getBroadcast(ctx, 0,
            Intent(ctx, TasbihWidget::class.java).setAction(ACTION_TAP).setComponent(ComponentName(ctx, TasbihWidget::class.java)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val reset = PendingIntent.getBroadcast(ctx, 1,
            Intent(ctx, TasbihWidget::class.java).setAction(ACTION_RESET).setComponent(ComponentName(ctx, TasbihWidget::class.java)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        rv.setOnClickPendingIntent(R.id.count, tap)
        rv.setOnClickPendingIntent(R.id.reset, reset)
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
        when (intent.action) {
            ACTION_TAP -> prefs.edit().putInt("tasbih_count", prefs.getInt("tasbih_count", 0) + 1).apply()
            ACTION_RESET -> prefs.edit().putInt("tasbih_count", 0).apply()
            else -> return
        }
        val mgr = AppWidgetManager.getInstance(ctx)
        onUpdate(ctx, mgr, mgr.getAppWidgetIds(ComponentName(ctx, TasbihWidget::class.java)))
    }

    companion object {
        const val ACTION_TAP = "com.example.wadigetlike.TASBIH_TAP"
        const val ACTION_RESET = "com.example.wadigetlike.TASBIH_RESET"
    }
}
