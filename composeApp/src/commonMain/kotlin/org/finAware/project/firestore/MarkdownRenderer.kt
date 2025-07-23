package org.finAware.project.firestore

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon

class MarkdownRenderer(private val context: Context) {

    private val markwon: Markwon = Markwon.create(context)

    /**
     * Renders the given markdown into the provided TextView.
     */
    fun renderToView(markdownText: String, textView: TextView) {
        markwon.setMarkdown(textView, markdownText)
    }

    /**
     * Returns a spanned content that you can use in other text-based views.
     */
    fun getRenderedMarkdown(markdownText: String) = markwon.toMarkdown(markdownText)
}

/*
example
    val textView = findViewById<TextView>(R.id.markdownText)
    val renderer = MarkdownRenderer(this) // specific to called one
    val markdownContent = """
            # Kotlin Markdown Class
            This is an **example** of a reusable class.

            - Works with any TextView
            - Can also return Spanned text
        """.trimIndent()
     renderer.renderToView(markdownContent, textView)
     or
     val spanned = renderer.getRenderedMarkdown("# Hello *again*")
     myTextView.text = spanned // RecyclerView
 */