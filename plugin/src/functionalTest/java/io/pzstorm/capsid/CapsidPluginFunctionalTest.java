package io.pzstorm.capsid;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.nio.file.Files;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A simple functional test for the 'io.pzstorm.capsid.greeting' plugin.
 */
class CapsidPluginFunctionalTest {

    @Test
    void canRunTask() throws IOException {
        // Setup the test build
        File projectDir = new File("build/functionalTest");
        Files.createDirectories(projectDir.toPath());
        writeString(new File(projectDir, "settings.gradle"), "");
        writeString(new File(projectDir, "build.gradle"),
            "plugins {" +
            "  id('io.pzstorm.capsid.greeting')" +
            "}");

        // Run the build
        GradleRunner runner = GradleRunner.create();
        runner.forwardOutput();
        runner.withPluginClasspath();
        runner.withArguments("greeting");
        runner.withProjectDir(projectDir);
        BuildResult result = runner.build();

        // Verify the result
        Assertions.assertTrue(result.getOutput().contains("Hello from plugin 'io.pzstorm.capsid.greeting'"));
    }

    private void writeString(File file, String string) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
