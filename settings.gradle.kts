pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        mavenCentral()
        gradlePluginPortal()
        maven {
            url=uri("https://mvnrepository.com/artifact/com.revosleap.mpesapush/mpesapush")
            jcenter()
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()

        mavenCentral()
        maven {
            url=uri("https://mvnrepository.com/artifact/com.revosleap.mpesapush/mpesapush")
            jcenter()
        }
    }
}

rootProject.name = "Smart Parking"
include(":app")
