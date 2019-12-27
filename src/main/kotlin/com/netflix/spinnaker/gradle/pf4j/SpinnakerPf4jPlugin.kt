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
import org.gradle.api.distribution.DistributionContainer

/**
 * Gradle plugin to support spinnaker plugin development life cycle.
 */
class SpinnakerPf4jPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Apply distribution plugin.
        project.plugins.apply("distribution")

        // Register checksum task and make build task as a dependency.
        val buildTask: Task = project.tasks.getByName("build")
        project.tasks.register("computeChecksum", ChecksumTask::class.java) {
            it.dependsOn(buildTask)
        }

        // Also add 'registerPlugin' task
        project.tasks.register("registerPlugin", RegistrationTask::class.java)

        // Configure distribution plugin.
        project.extensions.getByType(DistributionContainer::class.java).all {
            project.subprojects.forEach { sub ->
                it.contents.from(
                        "${sub.buildDir}/libs/${sub.name}.jar",
                        "${sub.buildDir}/libs/${sub.name}.jar.MD5")
                it.contents.into("distributions")
            }
        }
    }
}


