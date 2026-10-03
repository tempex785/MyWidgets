package com.example.wadiget

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class TasksActivity : Activity() {
    private lateinit var list: LinearLayout

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 32, 32, 32) }
        root.addView(TextView(this).apply { text = "المهام والعادات"; textSize = 22f; setTypeface(null, android.graphics.Typeface.BOLD) })
        val input = EditText(this).apply { hint = "أضف مهمة أو عادة جديدة…" }
        list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, 16, 0, 0) }
        root.addView(input)
        root.addView(Button(this).apply {
            text = "إضافة"
            setOnClickListener {
                val name = input.text.toString().replace(Regex("[:|]"), " ").trim()
                if (name.isNotBlank()) {
                    TasksStore.save(this@TasksActivity, TasksStore.tasks(this@TasksActivity) + Task(name, false))
                    input.setText(""); render()
                }
            }
        })
        root.addView(list)
        render()
        setContentView(root)
    }

    private fun render() {
        list.removeAllViews()
        val tasks = TasksStore.tasks(this)
        if (tasks.isEmpty()) list.addView(TextView(this).apply { text = "لا توجد مهام بعد — أضف أول عادة 💪"; setPadding(0, 24, 0, 0) })
        tasks.forEachIndexed { i, t ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            row.addView(CheckBox(this).apply {
                text = t.name; isChecked = t.done; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setOnCheckedChangeListener { _, c ->
                    val l = TasksStore.tasks(this@TasksActivity).toMutableList(); l[i] = l[i].copy(done = c); TasksStore.save(this@TasksActivity, l)
                }
            })
            row.addView(Button(this).apply {
                text = "حذف"; setOnClickListener {
                    val l = TasksStore.tasks(this@TasksActivity).toMutableList(); l.removeAt(i)
                    TasksStore.save(this@TasksActivity, l); render()
                }
            })
            list.addView(row)
        }
    }

    override fun onPause() { super.onPause(); Widgets.refreshAll(this) }
}
