package org.finAware.project.firestore

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun MarkdownText(markdown: String) {
    val context = LocalContext.current
    val renderer = MarkdownRenderer(context)

    AndroidView(
        factory = {
            TextView(it).apply {
                // Optional: set styling
                textSize = 16f
                setPadding(24, 24, 24, 24)
            }
        },
        update = { textView ->
            renderer.renderToView(markdown,textView)
        }
    )
}

//@Composable
//@Preview(showSystemUi = true)
//fun MyScreen() {
//    val markdown = """
//        # Welcome
//        This is **Markdown** in _Jetpack Compose_.
//
//        - Easy to use
//        - Supports [links](https://openai.com)
//    """.trimIndent()
//
//    MarkdownText(markdown) // use this to directly view not
//}