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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import io.pzstorm.capsid.CapsidPlugin;
import io.pzstorm.capsid.CapsidPluginExtension;
import io.pzstorm.capsid.CapsidTask;
import io.pzstorm.capsid.ProjectProperty;
import io.pzstorm.capsid.mod.ModProperties;
import io.pzstorm.capsid.property.CapsidProperty;
import io.pzstorm.capsid.property.validator.PropertyValidators;

/**
 * This task loads mod metadata information from {@code mod.info} file.
 */
public class LoadModMetadataTask extends DefaultTask implements CapsidTask {

	@Override
	public void configure(String group, String description, Project project) {
		CapsidTask.super.configure(group, description, project);

		ExtraPropertiesExtension ext = project.getExtensions().getExtraProperties();
		CapsidPluginExtension capsidExt = CapsidPlugin.getCapsidPluginExtension();

		String repoOwner = capsidExt.getProjectRepositoryOwner();
		String repoName = capsidExt.getProjectRepositoryName();

		// first check if repository data has been defined by user
		if (!Strings.isNullOrEmpty(repoOwner)) {
			ext.set("repo.owner", repoOwner);
		}
		if (!Strings.isNullOrEmpty(repoName)) {
			ext.set("repo.name", repoName);
		}
		File modInfoFile = ProjectProperty.MOD_INFO_FILE.get(project);
		if (modInfoFile.exists())
		{
			Properties properties = new Properties();
			try (InputStream inputStream = new FileInputStream(modInfoFile))
			{
				// load properties from properties file
				properties.load(inputStream);

				// load mod properties as project extra properties
				for (Map.Entry<Object, Object> entry : properties.entrySet())
				{
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();

					CapsidPlugin.LOGGER.info("Loading property " + key + ':' + value);
					CapsidProperty<?> metadataMapping = ModProperties.METADATA_MAPPING.get(key);
					if (metadataMapping != null) {
						ext.set(metadataMapping.name, entry.getValue());
					}
					else CapsidPlugin.LOGGER.warn("Found unknown mod metadata entry '" + key + '\'');
				}
				// read repository information from url property
				String sUrl = properties.getProperty("url");
				if (!Strings.isNullOrEmpty(sUrl))
				{
					// only read if link is a valid github url
					if (PropertyValidators.GITHUB_URL_VALIDATOR.isValid(new URL(sUrl)))
					{
						StringBuilder sb = new StringBuilder();
						char[] charArray = new URL(sUrl).getPath().toCharArray();

						int a = charArray.length - 1;
						int startIndex = charArray[0] != '/' ? 0 : 1;
						int endIndex = charArray[a] != '/' ? charArray.length : a;

						// remove slashes from first and last string index
						for (int i = startIndex; i < endIndex; i++) {
							sb.append(charArray[i]);
						}
						String urlPath = sb.toString();

						List<String> pathElements = Splitter.on("/").splitToList(urlPath);
						if (pathElements.size() != 2) {
							throw new InvalidUserDataException("Unexpected mod url format " + urlPath);
						}
						// these properties are used to generate changelog
						if (!ext.has("repo.owner")) {
							ext.set("repo.owner", pathElements.get(0));
						}
						if (!ext.has("repo.name")) {
							ext.set("repo.name", pathElements.get(1));
						}
					}
				}
			}
			catch (IOException e) {
				throw new GradleException("I/O exception occurred while loading mod info.", e);
			}
		}
		else CapsidPlugin.LOGGER.warn("WARN: Unable to find mod.info file");

		// these properties can be loaded without file
		if (!ext.has(ModProperties.MOD_NAME.name)) {
			ext.set(ModProperties.MOD_NAME.name, project.getName());
		}
		if (!ext.has(ModProperties.MOD_ID.name)) {
			ext.set(ModProperties.MOD_ID.name, project.getRootProject().getName());
		}
	}
}
