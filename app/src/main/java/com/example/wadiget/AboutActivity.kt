package com.example.wadiget

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class AboutActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 48, 48, 48) }
        val ver = try { packageManager.getPackageInfo(packageName, 0).versionName } catch (e: Exception) { "2.0" }
        root.addView(TextView(this).apply { text = "ودجتي"; textSize = 26f; setTypeface(null, android.graphics.Typeface.BOLD) })
        root.addView(TextView(this).apply { text = "الإصدار $ver

ويدجتات للشاشة الرئيسية: مواقيت الصلاة والتاريخ الهجري والأذكار والتسبيح والمهام.

صُنع بـ Kotlin ❤"; textSize = 14f; setPadding(0, 16, 0, 24) })
        root.addView(Button(this).apply {
            text = "قيّمنا ⭐ على Google Play"
            setOnClickListener {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))) }
                catch (e: ActivityNotFoundException) { Toast.makeText(this@AboutActivity, "Google Play غير متاح هنا", Toast.LENGTH_SHORT).show() }
            }
        })
        root.addView(Button(this).apply {
            text = "مشاركة التطبيق"
            setOnClickListener {
                val i = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "جرّب تطبيق ودجتي 🕌⏰") }
                startActivity(Intent.createChooser(i, "مشاركة عبر"))
            }
        })
        setContentView(root)
    }
}
