package com.project.coursesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.coursesapp.models.Address
import com.project.coursesapp.models.Course
import com.project.coursesapp.models.Student
import com.project.coursesapp.models.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val realm = MyApp.realm

    val courses = realm
        .query<Course>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    var courseDetails: Course? by mutableStateOf(null)
    private set

    var isAddCourseDialogVisible by mutableStateOf(false)
        private set

    fun showAddCourseDialog() {
        isAddCourseDialogVisible = true
    }

    fun hideAddCourseDialog() {
        isAddCourseDialogVisible = false
    }

    fun showCourseDetails(course: Course) {
        courseDetails = course
    }

    fun hideCourseDetails() {
        courseDetails = null
    }

    fun addCourse(courseName: String, teacher: Teacher?, address: Address?, students: List<Student>) {
        viewModelScope.launch {
            realm.write {
                val newCourse = Course().apply {
                    name = courseName
                    this.teacher = teacher
                    this.address = address
                    enrolledStudents.addAll(students)
                }
                copyToRealm(newCourse)
            }
            hideAddCourseDialog()
        }
    }

    fun deleteCourse() {
        viewModelScope.launch {
            realm.write {
                val course = courseDetails?: return@write
                val latestCourse = findLatest(course)?: return@write
                delete(latestCourse)

                courseDetails = null
            }
        }
    }
}