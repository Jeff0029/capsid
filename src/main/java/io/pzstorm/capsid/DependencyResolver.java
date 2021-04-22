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
package io.pzstorm.capsid;

import java.util.Set;

import org.gradle.api.Project;

import com.google.errorprone.annotations.Immutable;

@Immutable
interface DependencyResolver {

	/**
	 * Resolve dependency notations, files or paths for given project.
	 *
	 * @param project {@code Project} resolving dependencies.
	 * @return resolved dependency objects.
	 */
	Set<Object> resolveDependencies(Project project);
}
