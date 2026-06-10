package com.flowingcode.fixture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The entry point of the Spring Boot application. Also acts as the Vaadin
 * application shell: in Vaadin 25 all app-shell annotations ({@code @Push},
 * {@code @Viewport}) and stylesheet loading must live on the single
 * {@link AppShellConfigurator}. Theming is done with plain CSS loaded via
 * {@code @StyleSheet} (the {@code @Theme} annotation is deprecated in 25).
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@Push
@Viewport("width=device-width, initial-scale=1.0")
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles.css")
public class FixtureApp implements AppShellConfigurator {

    public static void main(final String[] args) {
        SpringApplication.run(FixtureApp.class, args);
    }

    @Override
    public void configurePage(final AppShellSettings settings) {
        settings.addFavIcon("icon", "/frontend/images/favicons/favicon-96x96.png", "96x96");
        settings.addLink("shortcut icon", "/frontend/images/favicons/favicon-96x96.png");
    }

    @Bean
    Executor executor() {
        return Executors.newFixedThreadPool(4);
    }

}
