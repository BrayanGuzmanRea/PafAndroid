// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories{
        google()
    }
    dependencies{
        classpath(libs.navigation.safe.args.gradle.plugin)
        classpath (libs.play.services.basement.v1800)
        classpath (libs.google.services.v4313)
        classpath (libs.gradle)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
}