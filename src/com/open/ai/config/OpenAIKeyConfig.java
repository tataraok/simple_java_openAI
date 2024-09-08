package com.open.ai.config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OpenAIKeyConfig {
    public static Properties getProperties() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("src/config.properties")) {
            // Load properties from file
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Encountered an issue while loading properties. Please check the file availability and format");
        }
		return properties;
    }
}