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
        maven { url = uri("https://raw.githubusercontent.com/alexgreench/google-webrtc/master") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // ----- 추가 코드 -----
        maven { url = uri("https://raw.githubusercontent.com/alexgreench/google-webrtc/master") }
    }
}

rootProject.name = "KeyWe"
include(":app")
 