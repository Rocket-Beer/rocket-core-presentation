allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
        maven("https://maven.pkg.github.com/Rocket-Beer/*") {
            println("**** Rocket Beer maven ****")

            credentials {
                val userName = publish.CommonMethods.getPublisherUserName(rootProject)
                val userPass = publish.CommonMethods.getPublisherPassword(rootProject)

                username = userName
                password = userPass
            }
        }
    }
}

    println("\n********** Configuration for == $project == **********")
    apply(plugin = "rocket-plugin")

    println("\n********** Sonar configuration for all projects **********")
    apply(plugin = "sonar.plugin")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
