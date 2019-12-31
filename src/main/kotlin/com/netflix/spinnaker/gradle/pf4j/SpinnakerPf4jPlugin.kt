/*
 * Copyright 2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.gradle.pf4j

import com.netflix.spinnaker.gradle.pf4j.tasks.ChecksumTask
import com.netflix.spinnaker.gradle.pf4j.tasks.RegistrationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Zip

/**
 * Gradle plugin to support spinnaker plugin development life cycle.
 */
class SpinnakerPf4jPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.tasks.register("computeChecksum", ChecksumTask::class.java)
        project.tasks.register("registerPlugin", RegistrationTask::class.java)

        val allBuildDirs: MutableList<String> = mutableListOf()
        project.subprojects.forEach {
            allBuildDirs.add("${it.name}/build/libs")
        }
        project.logger.debug(allBuildDirs.toString())
        project.tasks.register<Zip>("distPluginZip", Zip::class.java) {
            it.from(allBuildDirs).into("/")
            it.archiveFileName.set("${project.name}-${project.version}.zip")
            it.include("*")
            it.destinationDirectory.set(project.rootDir)
        }

        val computeChecksumTask: Task = project.tasks.getByName("computeChecksum")
        project.afterEvaluate {
            project.subprojects.forEach {
                it.afterEvaluate { subProject ->
                    computeChecksumTask.dependsOn.add(subProject.tasks.getByName("build"))
                }
            }
            project.tasks.getByName("distPluginZip").dependsOn(computeChecksumTask)
        }
    }
}


