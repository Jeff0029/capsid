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
package io.pzstorm.capsid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.pzstorm.capsid.PluginIntegrationTest;

class UtilsIntegrationTest extends PluginIntegrationTest {

	@Test
	void shouldGetFileFromResources() throws FileNotFoundException {

		File expected = new File("build/resources/integrationTest/dummy.zip");
		Assertions.assertEquals(expected.getAbsoluteFile(), Utils.getFileFromResources("dummy.zip"));
	}

	@Test
	void shouldUnzipArchive() throws IOException {

		File projectDir = getProject(false).getProjectDir();
		File archive = Utils.getFileFromResources("dummy.zip");

		File[] expected = new File[]{
				new File(projectDir, "dummy.txt"),
				new File(projectDir, "dummy.png")
		};
		for (File expectedFile : expected) {
			Assertions.assertFalse(expectedFile.exists());
		}
		Utils.unzipArchive(archive, projectDir);

		for (File expectedFile : expected) {
			Assertions.assertTrue(expectedFile.exists());
		}
	}
}
