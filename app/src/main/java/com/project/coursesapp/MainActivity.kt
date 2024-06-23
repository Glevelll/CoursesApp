package com.project.coursesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.project.coursesapp.models.Address
import com.project.coursesapp.models.Course
import com.project.coursesapp.models.Student
import com.project.coursesapp.models.Teacher
import com.project.coursesapp.ui.theme.CoursesAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val courses by viewModel.courses.collectAsState()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(courses) { course ->
                            CourseItem(
                                course = course,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        viewModel.showCourseDetails(course)
                                    }
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            viewModel.showAddCourseDialog()
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(bottom = 16.dp, end = 16.dp)
                            .wrapContentSize(Alignment.BottomEnd)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Добавить")
                    }
                    if (viewModel.isAddCourseDialogVisible) {
                        AddCourseDialog(
                            onDismissRequest = viewModel::hideAddCourseDialog,
                            onAddCourse = { courseName, teacher, address, students ->
                                viewModel.addCourse(courseName, teacher, address, students)
                            }
                        )
                    }
                    if(viewModel.courseDetails != null) {
                        Dialog(onDismissRequest = viewModel::hideCourseDetails) {
                            Column(
                                modifier = Modifier
                                    .widthIn(200.dp, 300.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            ) {
                                viewModel.courseDetails?.teacher?.address?.let { address ->
                                    Text(text = address.fullName)
                                    Text(text = address.street + " " + address.houseNumber)
                                    Text(text = address.city)
                                }
                                Button(
                                    onClick = viewModel::deleteCourse,
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                ) {
                                    Text(text = "Удалить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddCourseDialog(
    onDismissRequest: () -> Unit,
    onAddCourse: (String, Teacher?, Address?, List<Student>) -> Unit
) {
    var courseName by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var students by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Добавить новый курс") },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = { Text("Название курса") }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text("Преподаватель") }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Улица") }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("Дом") }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Город") }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(vertical = 5.dp)
                        .border(
                            1.dp,
                            Color(android.graphics.Color.parseColor("#7d32a8")),
                            shape = RoundedCornerShape(50)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    value = students,
                    onValueChange = { students = it },
                    label = { Text("Студенты") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (courseName.isNotBlank()) {
                        val teacher = Teacher().apply {
                            address = Address().apply {
                                fullName = teacherName
                                this.street = street
                                this.houseNumber = houseNumber.toIntOrNull() ?: 0
                                this.city = city
                            }
                        }
                        val studentList = students.split(",").map { Student().apply { name = it.trim() } }
                        onAddCourse(courseName, teacher, teacher.address, studentList)
                        onDismissRequest()
                    }
                },
                shape = RoundedCornerShape(50)
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                shape = RoundedCornerShape(50)
            ) {
                Text("Закрыть")
            }
        }
    )
}




@Composable
fun CourseItem(
    course: Course,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = course.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Преподается: ${course.teacher?.address?.fullName}",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ученики: ${course.enrolledStudents.joinToString { it.name }}",
            fontSize = 10.sp,
        )
    }
}