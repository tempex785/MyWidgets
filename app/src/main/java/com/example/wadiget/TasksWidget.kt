package com.example.wadiget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TasksWidget : AppWidgetProvider() {
    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val tasks = TasksStore.tasks(ctx)
        val pending = tasks.count { !it.done }
        val rv = RemoteViews(ctx.packageName, R.layout.widget_tasks)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextViewText(R.id.summary,
            if (tasks.isEmpty()) "لا توجد مهام — أضفها من التطبيق"
            else "غير المنجزة: $pending من ${tasks.size}")
        rv.setTextViewText(R.id.list, tasks.take(3).joinToString("\n") { (if (it.done) "☑ " else "☐ ") + it.name })
        rv.setTextColor(R.id.title, accent)
        rv.setTextColor(R.id.summary, WidgetTheme.white())
        rv.setTextColor(R.id.list, WidgetTheme.grey())
        val pi = PendingIntent.getActivity(ctx, 0, Intent(ctx, TasksActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        rv.setOnClickPendingIntent(R.id.root, pi)
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }
}
