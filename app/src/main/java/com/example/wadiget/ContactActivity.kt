package com.example.wadiget

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ContactActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 48, 48, 48) }
        root.addView(TextView(this).apply { text = "تواصل معنا"; textSize = 22f; setTypeface(null, android.graphics.Typeface.BOLD) })
        val name = EditText(this).apply { hint = "الاسم" }
        val email = EditText(this).apply { hint = "البريد الإلكتروني" }
        val msg = EditText(this).apply { hint = "رسالتك أو اقتراحك…"; minLines = 4 }
        root.addView(name); root.addView(email); root.addView(msg)
        root.addView(Button(this).apply {
            text = "إرسال الرسالة"
            setOnClickListener {
                if (name.text.isBlank() || email.text.isBlank() || msg.text.isBlank()) {
                    Toast.makeText(this@ContactActivity, "الاسم والبريد والرسالة مطلوبة", Toast.LENGTH_SHORT).show(); return@setOnClickListener
                }
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@wadiget.app")).apply {
                    putExtra(Intent.EXTRA_SUBJECT, "رسالة من ${name.text}")
                    putExtra(Intent.EXTRA_TEXT, "${msg.text}

— ${name.text} (${email.text})")
                }
                try { startActivity(i) } catch (e: Exception) {
                    Toast.makeText(this@ContactActivity, "لا يوجد تطبيق بريد — احفظ رسالتك لاحقًا", Toast.LENGTH_LONG).show()
                }
            }
        })
        setContentView(root)
    }
}
