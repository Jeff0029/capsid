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
package io.pzstorm.capsid.zomboid.task;

import java.io.File;

import org.gradle.api.Project;
import org.jetbrains.annotations.Nullable;

import io.pzstorm.capsid.CapsidTask;
import io.pzstorm.capsid.ProjectProperty;
import io.pzstorm.capsid.zomboid.ZomboidJar;
import io.pzstorm.capsid.zomboid.ZomboidTasks;

/**
 * This task assembles a jar archive containing game classes.
 */
public class ZomboidJarTask extends ZomboidJar implements CapsidTask {

	@Override
	public void configure(String group, String description, Project project) {
		CapsidTask.super.configure(group, description, project);

		File zomboidClassesDir = ProjectProperty.ZOMBOID_CLASSES_DIR.get(project);
		onlyIf(t -> {
			@Nullable File[] zomboidClasses = zomboidClassesDir.listFiles();
			return zomboidClassesDir.exists() && zomboidClasses != null && zomboidClasses.length > 0;
		});
		getDestinationDirectory().set(new File(project.getProjectDir(), "lib"));
		from(zomboidClassesDir);

		setIncludeEmptyDirs(false);
		getArchiveBaseName().set("zomboid");

		dependsOn(project.getTasks().getByName(ZomboidTasks.ZOMBOID_CLASSES.name));
	}
}
