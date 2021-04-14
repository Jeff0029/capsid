/*
 * Storm Capsid - Project Zomboid mod development framework for Gradle.
 * Copyright (C) 2021 Matthew Cain
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.pzstorm.capsid.dist.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.pzstorm.capsid.PluginFunctionalTest;
import io.pzstorm.capsid.dist.DistributionTasks;

class ProcessResourcesTaskFunctionalTest extends PluginFunctionalTest {

	@BeforeEach
	void createSourceAndResourceFiles() throws IOException {

		String[] filesToCreate = new String[]{
				"lua/client/mainClient.lua",
				"lua/server/mainServer.lua",
				"models/testModel.obj",
				"maps/testMap.map"
		};
		for (String path : filesToCreate)
		{
			File file = getProject().file("media/" + path);
			File parentFile = file.getParentFile();
			Assertions.assertTrue(parentFile.exists() || parentFile.mkdirs());
			Assertions.assertTrue(file.createNewFile());
		}
	}

	@Test
	void shouldProcessModResourcesWithCorrectDirectoryStructure() throws IOException {

		GradleRunner runner = getRunner();
		BuildResult result = runner.withArguments(DistributionTasks.PROCESS_RESOURCES.name).build();
		assertTaskOutcomeSuccess(result, DistributionTasks.PROCESS_RESOURCES.name);

		File resourcesDir = new File(runner.getProjectDir(), "build/resources/media");
		String[] expectedFiles = new String[]{
				"models/testModel.obj",
				"maps/testMap.map"
		};
		try (Stream<Path> stream = Files.walk(resourcesDir.toPath()).filter(Files::isRegularFile)) {
			Assertions.assertEquals(expectedFiles.length, stream.count());
		}
		for (String expectedFile : expectedFiles) {
			Assertions.assertTrue(new File(resourcesDir, expectedFile).exists());
		}
	}

	@Test
	void whenRunningProcessResourcesShouldDependOnThisTask() {

		BuildResult result = getRunner().withArguments("processResources").build();
		assertTaskOutcome(result, "processResources", TaskOutcome.NO_SOURCE);
		assertTaskOutcomeSuccess(result, DistributionTasks.PROCESS_RESOURCES.name);
	}
}
