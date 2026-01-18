/*
 * Copyright (c) 2019-2020 Owain van Brakel <https://github.com/Owain94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://repo.runelite.net")
        maven(url = "https://rl211.jire.org") {
            content {
                includeModule("net.runelite.rs", "vanilla")
                includeModule("net.runelite.rs", "vanilla-211")
            }
        }
        maven(url = "https://raw.githubusercontent.com/runewiki/openosrs-hosting/master")
        maven(url = "https://raw.githubusercontent.com/jbx5/hosting/master")
        maven(url = "https://raw.githubusercontent.com/jbx5/devious-hosting/master")
    }
    dependencies {
        classpath("com.openosrs:script-assembler-plugin:1.0.1")
        classpath("com.openosrs:injector-plugin:2.0.8")
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

plugins {
    application
}

val localGitCommit: String = "n/a"

allprojects {
    group = "net.unethicalite"
    version = ProjectVersions.unethicaliteVersion
    apply<MavenPublishPlugin>()
}

subprojects {
    repositories {
        if (System.getenv("JITPACK") != null) {
            mavenLocal()
        }

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://jitpack.io")
                }
            }
            filter {
                includeGroup("com.github.petitparser.java-petitparser")
                includeModule("com.github.petitparser", "java-petitparser")
            }
        }

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://repo.runelite.net")
                }
            }
            filter {
                //includeGroup("net.runelite.rs")
                includeModule("net.runelite", "discord")
                includeModule("net.runelite", "orange-extensions")
            }
        }
        /*exclusiveContent {
            forRepository {
                maven(url = "https://rl211.jire.org") {
                    content {
                        includeModule("net.runelite.rs", "vanilla")
                        includeModule("net.runelite.rs", "vanilla-211")
                    }
                }
            }
            filter {
                includeGroup("net.runelite.rs")
            }
        }*/
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://raw.githubusercontent.com/runewiki/openosrs-hosting/master")
                }
            }
            filter {
                includeModule("net.runelite", "fernflower")
            }
        }

        mavenCentral()
    }

    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()

    project.extra["gitCommit"] = localGitCommit
    project.extra["rootPath"] = rootDir.toString().replace("\\", "/")

    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("$buildDir/repo")
            }
            if (System.getenv("REPO_URL") != null) {
                maven {
                    url = uri(System.getenv("REPO_URL"))
                    credentials {
                        username = System.getenv("REPO_USERNAME")
                        password = System.getenv("REPO_PASSWORD")
                    }
                }
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
            }
        }
    }

    tasks {
        test {
            exclude("**/*")
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Checkstyle> {
            group = "verification"

            exclude("**/ScriptVarType.java")
            exclude("**/LayoutSolver.java")
            exclude("**/RoomType.java")
        }

        withType<Jar> {
            exclude("**/devtoolsplugin/**")
            doLast {
                // sign jar
                if (System.getProperty("signKeyStore") != null) {
                    // ensure ant is initialized so we can copy the project variable later
                    ant.invokeMethod("echo", mapOf("message" to "initializing ant"))

                    for (file in outputs.files) {
                        org.apache.tools.ant.taskdefs.SignJar().apply {
                            // why is this required
                            project = ant.project

                            setKeystore(System.getProperty("signKeyStore"))
                            setStorepass(System.getProperty("signStorePass"))
                            setAlias(System.getProperty("signAlias"))
                            setJar(file)
                            setSignedjar(file)
                            execute()
                        }
                    }
                }
            }
        }
    }

    configurations.compileOnly.get().extendsFrom(configurations["annotationProcessor"])
}

application {
    mainClass.set("net.runelite.client.RuneLite")
}

tasks {
    named<JavaExec>("run") {
        group = "openosrs"

        classpath = project(":runelite-client").sourceSets.main.get().runtimeClasspath
        enableAssertions = true
        mainClass.set("net.unethicalite.client.Unethicalite")
    }
    execTask("runLocal") {
        args("--local", "--developer-mode")
    }
    execTask("runLiveDev") {
        args("--developer-mode")
    }
}

fun TaskContainerScope.execTask(name: String, block: JavaExec.() -> Unit = {}) {
    register(name, JavaExec::class.java) {
        group = "_nr"
        classpath = project(":runelite-client").sourceSets.main.get().runtimeClasspath
        enableAssertions = true
        mainClass.set("net.unethicalite.client.Unethicalite")
        block()
    }
}
