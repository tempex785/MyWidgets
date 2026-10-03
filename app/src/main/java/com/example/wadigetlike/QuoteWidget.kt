package com.example.wadigetlike

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class QuoteWidget : AppWidgetProvider() {
    private val quotes = listOf(
        "سبحان الله وبحمده، سبحان الله العظيم",
        "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد",
        "أستغفر الله العظيم وأتوب إليه",
        "اللهم صلِّ وسلم على نبينا محمد",
        "«وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا»",
        "«إِنَّ مَعَ الْعُسْرِ يُسْرًا»",
        "«فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ»",
        "«رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي»",
        "حسبي الله لا إله إلا هو، عليه توكلت",
        "لا حول ولا قوة إلا بالله العلي العظيم",
        "رضيت بالله ربًا وبالإسلام دينًا وبمحمد ﷺ نبيًا",
        "اللهم إني أسألك العفو والعافية في الدنيا والآخرة",
        "«وَقُل رَّبِّ زِدْنِي عِلْمًا»",
        "سبحان الله، والحمد لله، ولا إله إلا الله، والله أكبر",
        "اللهم آتِنا في الدنيا حسنة وفي الآخرة حسنة وقنا عذاب النار"
    )

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val idx = prefs.getInt("quote_idx", 0)
        val rv = RemoteViews(ctx.packageName, R.layout.widget_quote)
        rv.setTextViewText(R.id.quote, quotes[idx % quotes.size])
        rv.setTextViewText(R.id.counter, "${(idx % quotes.size) + 1} / ${quotes.size}")
        val pi = PendingIntent.getBroadcast(ctx, 0,
            Intent(ctx, QuoteWidget::class.java).setAction(ACTION_NEXT).setComponent(ComponentName(ctx, QuoteWidget::class.java)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        rv.setOnClickPendingIntent(R.id.quote, pi)
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        if (intent.action == ACTION_NEXT) {
            val prefs = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putInt("quote_idx", prefs.getInt("quote_idx", 0) + 1).apply()
            val mgr = AppWidgetManager.getInstance(ctx)
            val ids = mgr.getAppWidgetIds(ComponentName(ctx, QuoteWidget::class.java))
            onUpdate(ctx, mgr, ids)
        }
    }

    companion object { const val ACTION_NEXT = "com.example.wadigetlike.NEXT_QUOTE" }
}
