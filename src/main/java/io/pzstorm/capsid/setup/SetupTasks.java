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
package io.pzstorm.capsid.setup;

import org.gradle.api.Project;

import io.pzstorm.capsid.CapsidTask;
import io.pzstorm.capsid.setup.task.CreateDiscordIntegrationTask;
import io.pzstorm.capsid.setup.task.CreateRunConfigurationsTask;
import io.pzstorm.capsid.setup.task.CreateSearchScopesTask;
import io.pzstorm.capsid.setup.task.setGameDirectoryTask;

/**
 * Tasks that help setup modding work environment.
 */
public enum SetupTasks {

	SET_GAME_DIRECTORY(setGameDirectoryTask.class, "setGameDirectory",
			"Set game directory via user input."
	),
	CREATE_RUN_CONFIGS(CreateRunConfigurationsTask.class, "createRunConfigurations",
			"Create useful IDEA run configurations."
	),
	CREATE_SEARCH_SCOPES(CreateSearchScopesTask.class, "createSearchScopes",
			"Create IDEA search scopes for project files."
	),
	CREATE_DISCORD_INTEGRATION(CreateDiscordIntegrationTask.class, "createDiscordIntegration",
			"Show IDEA project in Discord via rich presence."
	);
	public final String name, description;
	private final Class<? extends CapsidTask> type;

	SetupTasks(Class<? extends CapsidTask> type, String name, String description) {

		this.type = type;
		this.name = name;
		this.description = description;
	}

	/**
	 * Configure and register this task for the given {@code Project}.
	 *
	 * @param project {@code Project} register this task.
	 */
	public void register(Project project) {
		project.getTasks().register(name, type, t -> t.configure("build setup", description, project));
	}
}
