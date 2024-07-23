pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven ( url =  ("https://jitpack.io") )
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            mavenCentral()
            google()
            maven ( url =  ("https://jitpack.io") )

        }
    }

    rootProject.name = "app_controle_financeiro"
    include(":app")
}
