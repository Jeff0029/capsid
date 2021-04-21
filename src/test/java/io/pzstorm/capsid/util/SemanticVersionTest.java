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

import java.util.*;

import org.gradle.api.InvalidUserDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import io.pzstorm.capsid.PluginUnitTest;

class SemanticVersionTest extends PluginUnitTest {

	@Test
	void whenConstructingMalformedSemanticVersionShouldThrowException() {

		String[] malformedVersions = new String[]{
				"0", "1.0", "0.1.0.0",
				"0-1.2", "0.1-2", "2.1.0$"
		};
		for (String version : malformedVersions) {
			Assertions.assertThrows(InvalidUserDataException.class, () -> new SemanticVersion(version));
		}
	}

	@Test
	void shouldConstructSemanticVersionsWithValidNumbers() {

		Map<String, Integer[]> validVersions = ImmutableMap.of(
				"1.4.2", new Integer[]{ 1, 4, 2 },
				"0.1.3", new Integer[]{ 0, 1, 3 },
				"3.5.1-beta", new Integer[]{ 3, 5, 1 },
				"0.1.0-alpha$", new Integer[]{ 0, 1, 0 }
		);
		for (Map.Entry<String, Integer[]> entry : validVersions.entrySet())
		{
			SemanticVersion semVer = new SemanticVersion(entry.getKey());
			Integer[] versionData = entry.getValue();

			Assertions.assertEquals(versionData[0], semVer.major);
			Assertions.assertEquals(versionData[1], semVer.minor);
			Assertions.assertEquals(versionData[2], semVer.patch);
		}
	}

	@Test
	void shouldConstructSemanticVersionsWithValidClassifiers() {

		Map<String, String> validVersions = ImmutableMap.of(
				"0.1.0-alpha", "alpha",
				"0.4.6-0rc", "0rc",
				"1.0.0", ""
		);
		for (Map.Entry<String, String> entry : validVersions.entrySet()) {
			Assertions.assertEquals(entry.getValue(), new SemanticVersion(entry.getKey()).classifier);
		}
	}

	@Test
	void shouldCorrectlyCompareSemanticVersions() {

		List<SemanticVersion> semanticVersionsList = ImmutableList.of(
				new SemanticVersion("0.1.0"),
				new SemanticVersion("0.2.0"),
				new SemanticVersion("0.3.0"),
				new SemanticVersion("0.3.1"),
				new SemanticVersion("1.5.3")
		);
		SortedSet<SemanticVersion> semanticVersions = new TreeSet<>(new SemanticVersion.Comparator());
		semanticVersions.addAll(semanticVersionsList);

		List<SemanticVersion> semVerList = new ArrayList<>(semanticVersions);
		for (int i = 0; i < semVerList.size(); i++) {
			Assertions.assertEquals(semVerList.get(i), semanticVersionsList.get(i));
		}
	}

	@Test
	void shouldCorrectlyCompareUsingEquals() {

		SemanticVersion A = new SemanticVersion("0.1.0");
		SemanticVersion B = new SemanticVersion("0.2.0");
		SemanticVersion C = new SemanticVersion("0.3.0");
		SemanticVersion D = new SemanticVersion("0.2.0");

		Assertions.assertNotEquals(A, B);
		Assertions.assertNotEquals(B, C);
		Assertions.assertNotEquals(C, D);

		Assertions.assertEquals(B, D);
	}

	@Test
	void shouldCorrectlyCompareUsingHashCode() {

		Set<SemanticVersion> semanticVersions = Sets.newHashSet(
				new SemanticVersion("0.1.0"),
				new SemanticVersion("0.2.0"),
				new SemanticVersion("0.3.0")
		);
		Assertions.assertTrue(semanticVersions.contains(new SemanticVersion("0.1.0")));
		Assertions.assertTrue(semanticVersions.contains(new SemanticVersion("0.2.0")));
		Assertions.assertTrue(semanticVersions.contains(new SemanticVersion("0.3.0")));

		Assertions.assertFalse(semanticVersions.contains(new SemanticVersion("0.4.0")));
	}

	@Test
	void shouldConvertSemanticVersionToString() {

		Assertions.assertEquals("3.1.0", new SemanticVersion("3.1.0").toString());
		Assertions.assertEquals("2.0.0-beta", new SemanticVersion("2.0.0-beta").toString());
	}
}
