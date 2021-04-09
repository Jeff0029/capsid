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
package io.pzstorm.capsid.mod.task;

import java.util.ArrayList;
import java.util.List;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import io.pzstorm.capsid.PluginFunctionalTest;
import io.pzstorm.capsid.mod.ModTasks;
import io.pzstorm.capsid.zomboid.ZomboidTasks;

class SaveModMetadataTaskFunctionalTest extends PluginFunctionalTest {

	@Test
	void shouldSuccessfullyExecuteSaveModInfoTask() {

		GradleRunner runner = getRunner();
		List<String> arguments = new ArrayList<>(runner.getArguments());
		arguments.add(ModTasks.SAVE_MOD_METADATA.name);
		arguments.add("-x" + ZomboidTasks.ZOMBOID_VERSION.name);

		BuildResult result = runner.withArguments(arguments).build();
		assertTaskOutcomeSuccess(result, ModTasks.SAVE_MOD_METADATA.name);
	}
}
