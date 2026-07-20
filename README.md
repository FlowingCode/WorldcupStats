# Demo Application: Global Football 2026 Stats

A demo application that displays the fixture and results of the **2026 international football
tournament** (Canada / USA / Mexico): match schedule, live scores, group standings and
per-country fixtures.

It is the modernized version of the original 2018 demo, migrated from Vaadin 10 to **Vaadin 25**.
See [MIGRATION_PLAN.md](MIGRATION_PLAN.md) for the full upgrade story.

## Tech stack

- **Vaadin 25.1.7** (Lit / npm frontend, plain-CSS theming via `@StyleSheet`)
- **Spring Boot 4.0.6**, **Java 21**, executable-jar packaging
- **Flowing Code AppLayout add-on** for the navigation shell
- **Caffeine** for short-lived response caching

## Data source

Data comes from the free, no-API-key **[worldcup26.ir](https://worldcup26.ir/)** REST API
(teams, groups/standings, games, stadiums). Because it is a free source, rich per-match
statistics (lineups, possession, cards) are not available — the app shows fixtures, scores,
goal scorers and group standings. Live scores update during matches.

## Running

Development mode (hot reload):

```
mvn spring-boot:run
```

Then open <http://localhost:8080>.

Production build (compiles the optimized frontend bundle and a runnable jar):

```
mvn -Pproduction package
java -jar target/worldcup-fixture-2.0.0-SNAPSHOT.jar
```

## More information

For more information on Vaadin Flow, visit <https://vaadin.com/flow>.

For more information regarding this demo application, refer to [this blog post](https://www.flowingcode.com/en/vaadin-10-to-25-migrating-a-8-year-old-app-with-claude-ai/).

## Disclaimer

Unofficial demo application. Not affiliated with, endorsed by, or sponsored by FIFA or any
football governing body. All team and tournament data comes from the public
[worldcup26.ir](https://worldcup26.ir/) API.

## Author

Developed by [Flowing Code](https://www.flowingcode.com).
