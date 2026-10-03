package com.example.wadiget

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
        val count = WidgetTheme.prefs(ctx).getInt("tasbih_count", 0)
        val rv = RemoteViews(ctx.packageName, R.layout.widget_tasbih)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextViewText(R.id.dhikr, adhkar[(count / 33) % adhkar.size])
        rv.setTextViewText(R.id.count, "$count")
        rv.setTextViewText(R.id.cycle, "الجولة ${(count / 33) % adhkar.size + 1}")
        rv.setTextColor(R.id.dhikr, accent)
        rv.setTextColor(R.id.count, WidgetTheme.white())
        rv.setTextColor(R.id.cycle, WidgetTheme.grey())
        rv.setTextColor(R.id.reset, WidgetTheme.grey())
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
        val p = WidgetTheme.prefs(ctx)
        when (intent.action) {
            ACTION_TAP -> p.edit().putInt("tasbih_count", p.getInt("tasbih_count", 0) + 1).apply()
            ACTION_RESET -> p.edit().putInt("tasbih_count", 0).apply()
            else -> return
        }
        val mgr = AppWidgetManager.getInstance(ctx)
        onUpdate(ctx, mgr, mgr.getAppWidgetIds(ComponentName(ctx, TasbihWidget::class.java)))
    }

    companion object {
        const val ACTION_TAP = "com.example.wadiget.TASBIH_TAP"
        const val ACTION_RESET = "com.example.wadiget.TASBIH_RESET"
    }
}
