package com.example.wadiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews

object Prefs { const val NAME = "settings" }

object WidgetTheme {
    fun prefs(ctx: Context) = ctx.getSharedPreferences(Prefs.NAME, Context.MODE_PRIVATE)
    fun accent(ctx: Context): Int = prefs(ctx).getInt("accent", 0xFFFF6B57.toInt())
    fun bg(ctx: Context): Int {
        val alpha = prefs(ctx).getInt("alpha", 0xCC).coerceIn(0x10, 0xFF)
        return (alpha shl 24) or 0x00111318
    }
    fun apply(ctx: Context, rv: RemoteViews, rootId: Int): Int {
        rv.setInt(rootId, "setBackgroundColor", bg(ctx))
        return accent(ctx)
    }
    fun white() = 0xFFFFFFFF.toInt()
    fun grey() = 0xFFB0B4BC.toInt()
    fun isPro(ctx: Context) = prefs(ctx).getBoolean("pro", false)
}

object Widgets {
    private val providers = listOf(
        ClockWidget::class.java, PrayerWidget::class.java, QuoteWidget::class.java,
        TasbihWidget::class.java, TasksWidget::class.java, SportsWidget::class.java
    )
    fun refreshAll(ctx: Context) {
        val mgr = AppWidgetManager.getInstance(ctx)
        providers.forEach { cls ->
            val ids = mgr.getAppWidgetIds(ComponentName(ctx, cls))
            if (ids.isNotEmpty()) cls.getDeclaredConstructor().newInstance().onUpdate(ctx, mgr, ids)
        }
    }
}

data class Task(val name: String, val done: Boolean)

object TasksStore {
    fun tasks(ctx: Context): List<Task> {
        val s = WidgetTheme.prefs(ctx).getString("tasks", "") ?: ""
        if (s.isBlank()) return emptyList()
        return s.split("|").filter { it.isNotBlank() }.map {
            val p = it.split(":", limit = 2)
            Task(p[0], p.getOrElse(1) { "0" } == "1")
        }
    }
    fun save(ctx: Context, list: List<Task>) {
        WidgetTheme.prefs(ctx).edit()
            .putString("tasks", list.joinToString("|") { "${it.name}:${if (it.done) 1 else 0}" })
            .apply()
    }
}
