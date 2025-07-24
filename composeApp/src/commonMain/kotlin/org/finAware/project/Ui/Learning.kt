package org.finAware.project.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.HttpClient
import org.finAware.project.Ui.BottomNavBar
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.model.LearningEntry
import org.finAware.project.model.QuizPayload
import org.finAware.project.model.QuizQuestion
import org.finAware.project.util.LoadImage



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LearningCenterScreen(
    navController: NavController,
    client: HttpClient,
    selectedLanguage: String

) {
    var selectedLanguage by remember { mutableStateOf(selectedLanguage) }
    var searchQuery by remember { mutableStateOf("") }

    val contentList = remember { mutableStateListOf<LearningEntry>() }
    var isLoading by remember { mutableStateOf(true) }

    // ✅ Fetch data (fallback to dummy courses)
    LaunchedEffect(Unit) {
        try {
            val fetched = fetchLearningEntries(client)
            if (fetched.isEmpty()) {
                contentList.addAll(getDummyCourses())
            } else {
                contentList.addAll(fetched)
            }
        } catch (e: Exception) {
            contentList.addAll(getDummyCourses())
        } finally {
            isLoading = false
        }
    }

    val filteredEntries = contentList.filter { entry ->
        entry.language.contains(selectedLanguage, ignoreCase = true) &&
                entry.title.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "learning")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Learning Center", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("English", "हिन्दी", "ਪੰਜਾਬੀ","অসমীয়া").forEach { lang ->
                        FilterChip(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang },
                            label = { Text(lang) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Courses") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (!isLoading && filteredEntries.isEmpty()) {
                item {
                    Text("No matching courses found.", modifier = Modifier.padding(16.dp))
                }
            }

            items(filteredEntries) { entry ->
                CourseCard(entry = entry) {
                    navController.navigate("course_detail/${entry.courseId}/${selectedLanguage}")
                }
            }
        }
    }
}

@Composable
fun CourseCard(entry: LearningEntry, onStartCourse: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            if (!entry.imageUrl.isNullOrBlank()) {
                LoadImage(
                    url = entry.imageUrl,
                    modifier = Modifier
                        .width(120.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "No Image", tint = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.intro,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.language,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Button(onClick = onStartCourse) {
                        Text("Start")
                    }
                }
            }
        }
    }
}
// ✅ Dummy fallback data
fun getDummyCourses(): List<LearningEntry> {
    return listOf(
        LearningEntry(
            courseId = "course1",
            title = "UPI Scam Awareness",
            imageUrl = "https://via.placeholder.com/300x200",
            intro = "Learn how scammers trick users with fake UPI requests.",
            example = "You receive a payment request from an unknown number claiming to send you money.",
            prevention = "Never approve unknown UPI requests.",
            quiz = "What should you do if a stranger sends a UPI collect request?",
            language = "English"
        ),
        LearningEntry(
            courseId = "course2",
            title = "निवेश घोटाले की मूल बातें",
            imageUrl = "https://via.placeholder.com/300x200",
            intro = "धोखाधड़ी वाले निवेश योजनाओं की पहचान करें और उनसे बचें।",
            example = "एक सोशल मीडिया विज्ञापन जो 7 दिनों में आपके पैसे को दोगुना करने का वादा करता है।",
            prevention = "कंपनी की जांच करें और 'जल्दी अमीर बनने' वाली योजनाओं से बचें।",
            quiz = "आप किसी निवेश कंपनी की वैधता की पुष्टि कैसे कर सकते हैं?",
            language = "हिन्दी"
        ),
        LearningEntry(
            courseId = "course2",
            title = "ਨਿਵੇਸ਼ ਠੱਗੀ ਦੇ ਮੁਢਲੇ ਪੱਖ",
            imageUrl = "https://via.placeholder.com/300x200",
            intro = "ਧੋਖਾਧੜੀ ਵਾਲੀਆਂ ਨਿਵੇਸ਼ ਯੋਜਨਾਵਾਂ ਦੀ ਪਛਾਣ ਕਰੋ ਅਤੇ ਉਨ੍ਹਾਂ ਤੋਂ ਬਚੋ।",
            example = "ਇੱਕ ਸੋਸ਼ਲ ਮੀਡੀਆ ਵਿਗਿਆਪਨ ਜੋ ਤੁਹਾਡੇ ਪੈਸੇ 7 ਦਿਨਾਂ ਵਿੱਚ ਦੁੱਗਣੇ ਕਰਨ ਦਾ ਵਾਅਦਾ ਕਰਦਾ ਹੈ।",
            prevention = "ਕੰਪਨੀ ਦੀ ਜਾਂਚ ਕਰੋ ਅਤੇ 'ਝੱਟ ਪੈਸਾ ਕਮਾਉ' ਵਾਲੀਆਂ ਯੋਜਨਾਵਾਂ ਤੋਂ ਬਚੋ।",
            quiz = "ਤੁਸੀਂ ਕਿਸ ਤਰ੍ਹਾਂ ਜਾਂਚ ਸਕਦੇ ਹੋ ਕਿ ਕੋਈ ਨਿਵੇਸ਼ ਕੰਪਨੀ ਕਾਨੂੰਨੀ ਹੈ ਜਾਂ ਨਹੀਂ?",
            language = "ਪੰਜਾਬੀ"
        ),
        LearningEntry(
            courseId = "course2",
            title = "বিনিয়োগ ঠগৰ প্ৰাথমিক জ্ঞান",
            imageUrl = "https://via.placeholder.com/300x200",
            intro = "ঠগবাজ বিনিয়োগ পৰিকল্পনাৰ চিনাক্তকৰণ কৰক আৰু ইয়াৰ পৰা বাচি থাকক।",
            example = "এটা ছচিয়েল মিডিয়া বিজ্ঞাপন যি কয় ৭ দিনৰ ভিতৰত আপোনাৰ ধন ডাবল হ’ব।",
            prevention = "কোম্পানীটোৰ বিষয়ে অনুসন্ধান কৰক আৰু 'তৎক্ষণাত ধনী হোৱা' ধৰণৰ প্ৰলোভনৰ পৰা বাচক।",
            quiz = "আপুনি কেনেদৰে এটা বিনিয়োগ কোম্পানী বৈধ নে নহয় চাব পাৰিব?",
            language = "অসমীয়া"
        ),

                LearningEntry(
            courseId = "course3",
            title = "OTP Fraud Prevention",
            imageUrl = "https://via.placeholder.com/300x200",
            intro = "OTP scams are common. Learn how to stay safe.",
            example = "You get a call pretending to be your bank, asking for your OTP.",
            prevention = "Never share OTPs with anyone.",
            quiz = "Is it safe to share your OTP with a bank employee?",
            language = "English"
        )
    )
}
fun getDummyQuizzes(courseId: String): List<QuizQuestion> {
    return when (courseId) {
        "investmentBasics" -> listOf(
            QuizQuestion(
                question = "What is SIP?",
                options = listOf(
                    "Systematic Investment Plan",
                    "Secure Insurance Plan",
                    "Simple Interest Policy",
                    "Stock Investment Program"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "What is compounding?",
                options = listOf(
                    "Loss over time",
                    "Earning interest on interest",
                    "One-time deposit",
                    "Short-term profit"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "True/False: Stocks are a form of debt.",
                options = listOf("True", "False", "Not Sure", "Sometimes"),
                correctAnswerIndex = 1
            )
        )

        "fraudAwareness" -> listOf(
            QuizQuestion(
                question = "What is phishing?",
                options = listOf(
                    "Fishing with a rod",
                    "Fraudulent messages to steal data",
                    "Safe online banking",
                    "Account recovery method"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Which is a type of OTP scam?",
                options = listOf(
                    "Fake customer care call",
                    "Police verification",
                    "ATM machine issue",
                    "Mobile app update"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "True/False: Banks will ask for your PIN over phone.",
                options = listOf("True", "False", "If urgent", "Only for reset"),
                correctAnswerIndex = 1
            )
        )

        else -> emptyList()
    }
}
