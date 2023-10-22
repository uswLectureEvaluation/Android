@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("suwiki.android.library")
    id("suwiki.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.suwiki.local.timetable.editor"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.data.timetable.editor)
    implementation(projects.core.database)

    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
}