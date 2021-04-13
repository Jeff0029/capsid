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
package io.pzstorm.capsid.setup.xml;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.w3c.dom.Element;

import io.pzstorm.capsid.setup.LocalProperties;
import io.pzstorm.capsid.setup.VmParameter;
import io.pzstorm.capsid.util.UnixPath;

public class LaunchRunConfig extends XMLDocument {

	public static final LaunchRunConfig RUN_ZOMBOID = new LaunchRunConfig(
			"Run Zomboid",
			new VmParameter.Builder().build()
	);
	public static final LaunchRunConfig RUN_ZOMBOID_LOCAL = new LaunchRunConfig(
			"Run Zomboid (local)",
			new VmParameter.Builder().withSteamIntegration(false).build()
	);
	public static final LaunchRunConfig DEBUG_ZOMBOID = new LaunchRunConfig(
			"Debug Zomboid",
			new VmParameter.Builder().withDebug(true).build()
	);
	public static final LaunchRunConfig DEBUG_ZOMBOID_LOCAL = new LaunchRunConfig(
			"Debug Zomboid (local)",
			new VmParameter.Builder().withDebug(true).withSteamIntegration(false).build()
	);

	private final VmParameter vmParameters;

	public LaunchRunConfig(String name, VmParameter vmParameters) {
		super(name, ".idea/runConfigurations/");
		this.vmParameters = vmParameters;
	}

	/**
	 * Configure this instance of {@code ZomboidLaunchRunConfig} for given {@code Project}.
	 *
	 * @return an instance of this {@code ZomboidLaunchRunConfig}.
	 *
	 * @throws InvalidUserDataException if {@code gameDir} local property is not initialized.
	 */
	@Override
	public LaunchRunConfig configure(Project project) {

		// <component name="ProjectRunConfigurationManager">
		Element component = document.createElement("component");
		component.setAttribute("name", "ProjectRunConfigurationManager");
		appendOrReplaceRootElement(component);

		// <configuration default="false" name="<name>" type="Application" factoryName="Application">
		Element configuration = document.createElement("configuration");

		configuration.setAttribute("default", "false");
		configuration.setAttribute("name", name);
		configuration.setAttribute("type", "Application");
		configuration.setAttribute("factoryName", "Application");
		component.appendChild(configuration);

		// <option name="MAIN_CLASS_NAME" value="zombie.gameStates.MainScreenState" />
		Element optionClass = document.createElement("option");

		optionClass.setAttribute("name", "MAIN_CLASS_NAME");
		optionClass.setAttribute("value", "zombie.gameStates.MainScreenState");
		configuration.appendChild(optionClass);

		// <module name="<rootProjectName>.main" />",
		Element module = document.createElement("module");

		String rootProjectName = project.getRootProject().getName();
		module.setAttribute("name", rootProjectName + ".main");
		configuration.appendChild(module);

		// <option name="VM_PARAMETERS" value="<launchParameters> />
		Element optionParams = document.createElement("option");

		optionParams.setAttribute("name", "VM_PARAMETERS");
		optionParams.setAttribute("value", vmParameters.toString());
		configuration.appendChild(optionParams);

		// <option name="WORKING_DIRECTORY" value="<game_dir>" />
		Element optionWorkDir = document.createElement("option");

		UnixPath gameDir = LocalProperties.GAME_DIR.findProperty(project);
		if (gameDir == null)
		{
			throw new InvalidUserDataException("Unable to find gameDir local property");
		}
		optionWorkDir.setAttribute("name", "WORKING_DIRECTORY");
		optionWorkDir.setAttribute("value", gameDir.toString().replace('\\', '/'));
		configuration.appendChild(optionWorkDir);

		// <method v="2">
		Element method = document.createElement("method");
		method.setAttribute("v", "2");
		configuration.appendChild(method);

		// <option name="Gradle.BeforeRunTask" enabled="true" tasks="zomboidClasses"
		// externalProjectPath="$PROJECT_DIR$" vmOptions="" scriptParameters="" />
		Element optionBeforeRunTask = document.createElement("option");

		optionBeforeRunTask.setAttribute("name", "Gradle.BeforeRunTask");
		optionBeforeRunTask.setAttribute("enabled", "true");
		optionBeforeRunTask.setAttribute("tasks", "zomboidClasses");
		optionBeforeRunTask.setAttribute("externalProjectPath", "$PROJECT_DIR$");
		optionBeforeRunTask.setAttribute("vmOptions", "");
		optionBeforeRunTask.setAttribute("scriptParameters", "");

		method.appendChild(optionBeforeRunTask);

		return (LaunchRunConfig) super.configure(project);
	}
}
