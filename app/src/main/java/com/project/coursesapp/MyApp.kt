package com.project.coursesapp

import android.app.Application
import com.project.coursesapp.models.Address
import com.project.coursesapp.models.Course
import com.project.coursesapp.models.Student
import com.project.coursesapp.models.Teacher
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Address::class,
                    Teacher::class,
                    Course::class,
                    Student::class
                )
            )
        )
    }
}