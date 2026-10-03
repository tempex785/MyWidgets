package com.example.wadiget

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
        "«فَاذْكُرُونِي أَذْكُرْكُمْ»",
        "«رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي»",
        "حسبي الله لا إله إلا هو، عليه توكلت",
        "لا حول ولا قوة إلا بالله العلي العظيم",
        "رضيت بالله ربًا وبالإسلام دينًا وبمحمد ﷺ نبيًا",
        "اللهم إني أسألك العفو والعافية في الدنيا والآخرة",
        "«وَقُل رَّبِّ زِدْنِي عِلْمًا»",
        "سبحان الله، والحمد لله، ولا إله إلا الله، والله أكبر",
        "اللهم آتِنا في الدنيا حسنة وفي الآخرة حسنة"
    )

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val idx = WidgetTheme.prefs(ctx).getInt("quote_idx", 0)
        val rv = RemoteViews(ctx.packageName, R.layout.widget_quote)
        val accent = WidgetTheme.apply(ctx, rv, R.id.root)
        rv.setTextViewText(R.id.quote, quotes[idx % quotes.size])
        rv.setTextViewText(R.id.counter, "${(idx % quotes.size) + 1} / ${quotes.size}")
        rv.setTextColor(R.id.quote, WidgetTheme.white())
        rv.setTextColor(R.id.counter, accent)
        val pi = PendingIntent.getBroadcast(ctx, 0,
            Intent(ctx, QuoteWidget::class.java).setAction(ACTION_NEXT).setComponent(ComponentName(ctx, QuoteWidget::class.java)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        rv.setOnClickPendingIntent(R.id.quote, pi)
        ids.forEach { mgr.updateAppWidget(it, rv) }
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        if (intent.action == ACTION_NEXT) {
            val p = WidgetTheme.prefs(ctx)
            p.edit().putInt("quote_idx", p.getInt("quote_idx", 0) + 1).apply()
            val mgr = AppWidgetManager.getInstance(ctx)
            onUpdate(ctx, mgr, mgr.getAppWidgetIds(ComponentName(ctx, QuoteWidget::class.java)))
        }
    }

    companion object { const val ACTION_NEXT = "com.example.wadiget.NEXT_QUOTE" }
}
