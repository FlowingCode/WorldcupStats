# WorldCup Stats — Vaadin 25 Upgrade & 2026 World Cup Data: Findings & Plan

**Prepared:** 2026-06-08
**Goal:** (1) Upgrade this app to **Vaadin 25**, and (2) make it show data from the **2026 World Cup** (kicks off **June 11, 2026**).

> Note: this is an unofficial demo. "FIFA" and "FIFA World Cup" are trademarks of FIFA; the app and its public-facing name avoid them and carry a non-affiliation disclaimer. Internal identifiers from the data API (e.g. `fifa_code`) are kept as-is.

> TL;DR: Both are achievable, but neither is a simple version bump. The app is from 2018 (Vaadin 10, Polymer/Bower, Spring Boot 2.0) and its data source is permanently offline. The frontend needs a rebuild, and the entire data layer must be rewritten against a new API. The two biggest decisions are **which data source** and **how much of the original feature set** we rebuild — both are blocked on input and explained below.

---

## 1. Current state (what we have today)

| Area | Current | Notes |
|---|---|---|
| Vaadin | **10.0.1** (2018) | Polymer 2 era |
| Spring Boot | **2.0.2** | `javax.*` namespace |
| Java | **1.8** (source/target) | Build machine has JDK 21 ✓ |
| Packaging | **WAR** | + Heroku Maven plugin |
| Frontend | **Polymer templates** (`.html`), Bower webjars, `@HtmlImport`, `shared-styles.html`, `marked-element` | All removed in modern Vaadin |
| App layout | `com.flowingcode.addons.applayout:app-layout-addon:**1.0.1**` | Old FC add-on version |
| Data source | **`worldcup.sfg.io`** REST API | ❌ **Confirmed dead** (2018-only project; no longer responds) |
| Architecture | `repository → service → presenter → screen` (MVP) | Domain models map the old API's rich schema |

**Source layout (for reference):**
- `repository/` — REST calls to `worldcup.sfg.io` + domain models (`Match`, `Team`, `GroupDetail`, …)
- `service/` — `GroupService`, `MatchService`, `FlagUtils`
- `view/presenter/` — `MatchesPresenter`, `MatchDetailPresenter`, `GroupsPresenter`, `CountryPresenter`, `WelcomePresenter`
- `view/screen/` — `MainLayout`, `MatchesScreen`, `MatchDetailScreen`, `GroupsScreen`, `CountryScreen`, `AboutScreen`, `WelcomeScreen`, `DateFilterDialog`
- `view/component/` — `MatchResultComponent`, `GroupView`, `TitleComponent`, `MarkedElement` (HtmlImport), error handlers
- `view/util/` — `MatchUpdater` (live polling/push), `DateTimeUtil`, `CssStyles`

---

## 2. Two independent workstreams

### Workstream A — Vaadin 10 → 25 (a frontend rebuild, not a bump)

Everything the original frontend is built on has been removed from modern Vaadin. This is the larger of the two efforts.

- **Polymer / Bower / `@HtmlImport` → Lit + npm + CSS themes.** Affects `shared-styles.html`, the `marked-element` webjar, and `MarkedElement.java`. Styles get reworked into a Vaadin theme (`frontend/themes/...` / `@CssImport`).
- **Spring Boot 2.0 → 4.0** → `javax.*` → `jakarta.*` migration across the whole app.
- **Java 8 → 17/21**, **WAR → Spring Boot executable jar** (recommended; the Heroku WAR setup goes away).
- **Removed/changed component APIs:** `Label` (removed → `Span`/`NativeLabel`), `Grid` API changes, routing/`@Route`/`@PageTitle` tweaks, layout APIs.
- **App layout:** bump `app-layout-addon` `1.0.1` → **`6.2.0-SNAPSHOT`** (Vaadin 25 compatible) — see **Decision 2**. API changed substantially across 1.x→6.x; the menu/drawer setup in `MainLayout` will need rewriting.
- **Live updates:** `MatchUpdater` (server push / polling) needs revalidation against the Vaadin 25 push API.

> Node: the machine has **Node 18**; Vaadin 25 prefers **Node 20+**. Vaadin can provision its own Node automatically, so this is low-risk, but installing Node 20+ avoids surprises.

### Workstream B — New 2026 World Cup data source

`worldcup.sfg.io` is gone, and **no free source matches its old schema**, so the `repository / service / presenter / DTO` layer is rewritten **regardless of which source we pick**. The original app's richest features (live player events, lineups, possession/shot stats, weather) come **only** from a keyed/paid API — no free, no-key source provides them.

**Options researched (June 2026):**

| Source | Live in-match scores | API key | Real 2026 data | Rich stats (lineups / events / possession) | Reliability |
|---|---|---|---|---|---|
| **worldcup26.ir** | ✅ Yes | ❌ None | ✅ Yes (matches official draw) | ❌ Basic scores/scorers only | ⚠️ Third-party project; uptime/longevity not guaranteed |
| **openfootball/worldcup.json** | ❌ No (static, git-updated) | ❌ None | ✅ Yes (public domain) | ❌ No | ✅ Very reliable, but not real-time |
| **API-Football** (api-football.com) | ✅ Yes | ✅ Required | ✅ Yes | ✅ Yes | ✅ Commercial; free tier rate-limited (~100 req/day) |
| **football-data.org** | ✅ Yes | ✅ Required | ✅ Yes | ⚠️ Partial | ✅ Commercial; free tier ~10 req/min |
| **Sportmonks / TheStatsAPI / others** | ✅ Yes | ✅ Required | ✅ Yes | ✅ Yes | ✅ Commercial, paid |

> Verified live during research: `worldcup26.ir/get/games` returns HTTP 200 with the real 2026 fixtures (e.g. Mexico v South Africa, June 11) — cross-checked against openfootball. Its schema is flat (team IDs, EN/FA names, group, matchday, scores) and very different from the old sfg.io model.

---

## 3. Decisions the team needs to make

These drive scope, effort, and feasibility. Nothing else can be finalized until **Decision 1 & 2** are made.

### Decision 1 — Data source ⭐ (highest impact)
> **Team direction (2026-06-08): keep it free / no external API key.** This rules out Option C below and, with it, the rich match stats (lineups, possession, player events) — those are only available from keyed/paid providers. The realistic choice is therefore between the two free, no-key options (A and B) or a hybrid of them.
- **Option A — `worldcup26.ir`** (free, live, no key). Closest to the original's "live during the tournament" spirit. **Trade-off:** third-party reliability risk; basic data only.
- **Option B — openfootball** (free, reliable, no key). Best if correctness/uptime matter more than a live-ticking score. **Trade-off:** no real-time in-match updates.
- ~~**Option C — Keyed API (e.g. API-Football)**~~ — **excluded** by the free/no-key constraint above.
- **Recommended: hybrid (A + B)** — openfootball for fixtures/groups/results (reliable), `worldcup26.ir` for live scores. Both free, no key. Keep behind a clean interface so a keyed source could be added later if the constraint ever changes.

### Decision 2 — App shell / navigation layout
> **Finding (verified 2026-06-08):** FC's own Vaadin 25 demo (`https://addonsv25.flowingcode.com/applayout/applayout-demo`) runs the AppLayout add-on at version **`6.2.0-SNAPSHOT`** on **Vaadin `25.1.5`** — confirmed by the team. So the add-on **works on Vaadin 25** and the target coordinates are known. *Caveat: `6.2.0-SNAPSHOT` is a snapshot, not a stable release; we'd consume the SNAPSHOT (and the FC snapshots repo) until a stable v25-compatible release is published.*
- **Option A — Flowing Code app-layout add-on. (Confirmed on Vaadin 25.)** Use **`6.2.0-SNAPSHOT`** with Vaadin **`25.1.7`** (latest; demo verified on the 25.1.x line). Keeps our look-and-feel and dogfoods our add-on. Only caveat is the SNAPSHOT dependency noted above.
- **Option B — Vaadin built-in `AppLayout`.** Zero external dependency, supported by the platform. A fallback if we'd rather avoid the add-on entirely.

### Decision 3 — Feature scope
> With the free/no-key constraint (Decision 1), **Option A is effectively the only viable scope** — full parity is excluded because the rich stats require a keyed API.
- **Option A — Core rebuild (selected by the free constraint):** match list/fixtures, live scores, group standings, country pages, modernized Vaadin 25 UI. Drops rich match-detail stats that free sources don't provide. Faster.
- ~~**Option B — Full parity**~~ — needs a keyed/paid API; **excluded** by the free/no-key constraint.

### Decision 4 — Vaadin target & migration style
- **Target confirmed: Vaadin `25.1.7` (latest) + app-layout add-on `6.2.0-SNAPSHOT`** (FC's v25 demo verified on the 25.1.x line). The earlier concern about the add-on blocking a Vaadin 25 target is **resolved**. Only open item is the SNAPSHOT caveat (Decision 2); a stable add-on release would be preferable before a production deploy.
- **In-place migration** of existing code vs. **clean rebuild** on the same domain. Given the volume of removed tech, a clean rebuild of the view layer (reusing business logic) is often faster and lower-risk than incremental migration.

### Decision 5 — Deployment & timeline
- WAR/Heroku → Spring Boot jar; pick a hosting target.
- The tournament starts **June 11, 2026** — clarify whether there's a hard "live for kickoff" deadline (affects scope choices above).

### Decision 6 — Assets
- Flag SVGs in `frontend/images/flags/` cover the **2018** teams. The 2026 lineup (48 teams) differs — we'll need to **add/replace flags** for the new participants.

---

## 4. Recommended approach (proposal — open to discussion)

For a demo/showcase that feels live for the tournament with the least external risk:

1. **Data:** Use **worldcup26.ir** as the single free source — it covers teams, group standings, games and stadiums, which map cleanly onto the existing view DTOs. Keep the data access behind a thin client so a reliable fallback (openfootball) or a keyed API could be added later. *(As-built: worldcup26.ir only; the openfootball fallback was left as an optional, unimplemented item.)*
2. **Scope:** **Core rebuild** (Decision 3A) — drop the rich per-match stats that no free source offers.
3. **Layout:** **Flowing Code app-layout add-on `6.2.0-SNAPSHOT`** (Decision 2A) — confirmed running on Vaadin 25 via FC's own v25 demo.
4. **Platform:** Spring Boot 4.0.x jar, Java 21, **Vaadin 25.1.7**, Lit/CSS theme.

This keeps it free/no-key (per the team's direction), reliable for fixtures, and live-ish for scores — at the cost of the detailed match statistics. If the free constraint is ever relaxed, the clean data interface lets us add a keyed API later for full-parity stats without reworking the views.

---

## 5. Execution checklist

> Locked targets: **Vaadin 25.1.7 · Java 21 · Spring Boot 4.0.6 · app-layout-addon 6.2.0-SNAPSHOT · free data (worldcup26.ir) · core-rebuild scope.** Suggested order — each phase should compile/run before moving on.

> **As-built note:** the checklist below is the original plan. A few things landed differently in practice — see [§7 As-built](#7-as-built-what-actually-shipped) for the authoritative summary (theming via `@StyleSheet` instead of `@Theme`, `vaadin-dev` dependency for dev mode, Vaadin `Card` instead of `PaperCard`, single data source, app-shell on `FixtureApp`).

### Phase 0 — Branch & baseline
- [ ] Create a migration branch off `master`.
- [ ] (Optional) Install Node 20+ to avoid Vaadin auto-provisioning surprises.
- [ ] Snapshot current behavior/screenshots for later comparison.

### Phase 1 — Build & platform (get it compiling on the new stack)
- [ ] Rewrite `pom.xml`: `vaadin.version` → **25.1.7**, `vaadin-bom`; Spring Boot **4.0.6** (`spring-boot-starter-parent`); `maven.compiler.source/target` → **21**.
- [ ] Packaging `war` → **`jar`**; drop `maven-war-plugin`, `failOnMissingWebXml`, the `heroku-maven-plugin`, and the `frontend-es5/es6` clean filesets.
- [ ] Remove obsolete deps/repos: `vaadin-prereleases`, Bower `marked-element` webjar, the old `app-layout-addon:1.0.1`. Add **`app-layout-addon:6.2.0-SNAPSHOT`** (+ FC snapshots repo).
- [ ] Re-evaluate the Vaadin `productionMode` profile — Vaadin 25 uses the `vaadin-maven-plugin` `prepare-frontend`/`build-frontend` goals (not `copy-production-files`/`package-for-production`).
- [ ] **`javax.* → jakarta.*`** across all imports (servlet, etc.).
- [ ] Decide on ehcache: Spring Boot 4 cache abstraction (chosen: Caffeine, 120s TTL). Re-check `ehcache.xml`.

### Phase 2 — Frontend foundation
- [ ] Delete Polymer/Bower artifacts: `shared-styles.html`, `src/main/webapp/frontend/*.html`, `MarkedElement.java`, any `@HtmlImport`.
- [ ] Create a Vaadin theme (`frontend/themes/worldcup/styles.css` + `@Theme`); port the CSS from `shared-styles.html` (drop the Polymer `app-*`/`paper-*` selectors).
- [ ] Move `frontend/images/**` (favicons, flags) under the theme / `src/main/resources/META-INF/resources` as appropriate.
- [ ] Replace the `marked-element` usage in `AboutScreen` with a Vaadin-native markdown approach (e.g. the FC Markdown add-on, or render to HTML server-side).

### Phase 3 — Data layer (Workstream B)
- [ ] Define a `WorldCupDataSource` interface (matches, groups, teams, by-country) so sources are swappable.
- [ ] New domain/DTO models for the chosen schemas (openfootball + worldcup26.ir) — replace the sfg.io `Match`/`Team`/etc.
- [ ] Implement: openfootball for fixtures/groups/results; worldcup26.ir for live scores. Map both into the app's view DTOs.
- [ ] Update `GroupService`/`MatchService`/`FlagUtils` and the presenters to the new models.
- [ ] Re-point or remove `SFGRestTemplate`; use `RestClient`/`WebClient` (Spring 6).

### Phase 4 — Views (Vaadin 25)
- [ ] `MainLayout` → rebuild the menu/drawer on app-layout-addon 6.x API.
- [ ] Fix removed APIs: `Label` → `Span`/`NativeLabel`; revalidate `Grid`, `@Route`, `@PageTitle`, dialogs.
- [ ] Rebuild `MatchesScreen`, `MatchDetailScreen`, `GroupsScreen`, `CountryScreen`, `WelcomeScreen`, `AboutScreen`, `DateFilterDialog`, `MatchResultComponent`, `GroupView`, `TitleComponent`.
- [ ] Revalidate live updates: `MatchUpdater` + `@Push` against the Vaadin 25 push API (and confirm the data source actually ticks).

### Phase 5 — Assets & content
- [ ] Add/replace flag SVGs for the **48** participating nations (current set is the 2018 lineup).
- [ ] Update `AboutScreen` copy, page titles, manifest/favicons as needed.

### Phase 6 — Test & deploy
- [ ] Run locally (`mvn spring-boot:run`) against live 2026 data; verify each screen.
- [ ] Production build (`-Pproduction` / `vaadin.productionMode`) sanity check.
- [ ] Pick a hosting target (Heroku setup is gone) and deploy.

> Effort note: multi-day effort dominated by Phase 4 (view rebuild) and Phase 3 (data rewrite). Phases 1–2 are mechanical but touch everything.

---

## 6. Key risks

- **Data-source longevity** — free no-key sources (esp. `worldcup26.ir`) may change or go down mid-tournament. The client degrades gracefully (empty lists, no crash); a reliable fallback (openfootball) or stale-cache is the suggested mitigation but is **not yet implemented**.
- **Feature-parity gap** — rich match stats are simply unavailable for free; setting expectations early avoids rework.
- **Migration surprises** — 7 years of removed Vaadin APIs; the Polymer→Lit and Spring Boot 4 (Jakarta) steps are where unknowns hide.
- **Deadline pressure** — tournament starts **June 11, 2026**; core-rebuild scope is chosen to fit.
- **SNAPSHOT dependency** — relying on `app-layout-addon 6.2.0-SNAPSHOT`; pin a build or move to a stable release before any production deploy.

---

## 7. As-built (what actually shipped)

The migration was completed on branch `migrate/vaadin25-worldcup2026`. Final state, where it differs from or refines the plan above:

**Platform**
- Vaadin **25.1.7**, Spring Boot **4.0.6**, Java **21**, executable-jar packaging.
- Added `com.vaadin:vaadin-dev` (optional) — required for the dev server (`spring-boot:run`); not needed by the production jar. Matches the official Vaadin 25 Spring Boot starter.
- Caching: **Caffeine** (`spring.cache.type=caffeine`, 120 s TTL); `ehcache.xml` removed. *Rationale:* the original Ehcache 3 integration relied on JSR-107 (JCache), which adds fragile wiring under Spring Boot 4 / Jakarta (Jakarta-classified provider, `cache-api`, `spring.cache.jcache.config` → XML). For this single-node, in-memory, short-TTL use case, Caffeine is Spring Boot's recommended default: native auto-config, same semantics (TTL + max-size in one `spec` line), `@Cacheable`/`@EnableCaching` unchanged, no JCache/XML. (A distributed cache like Redis/Hazelcast would only be warranted for a multi-node deployment.)
- `FixtureApp` no longer extends `SpringBootServletInitializer` (jar, not WAR).

**Frontend / theming**
- `@Theme` is **deprecated in Vaadin 25**, so theming is done with plain CSS loaded via `@StyleSheet(Lumo.STYLESHEET)` + `@StyleSheet("styles.css")` on the app shell. The stylesheet lives at `src/main/resources/META-INF/resources/styles.css` (the deprecated `frontend/themes/<name>` folder is not used).
- All app-shell annotations (`@Push`, `@Viewport`, `@StyleSheet`) live on **`FixtureApp implements AppShellConfigurator`** (Vaadin 25 requires a single shell class; no separate `AppShell`).
- `PaperCard` was removed from app-layout 6.2.0, so cards use the **built-in Vaadin `Card`** component. `Label` → `NativeLabel`.
- Markdown in `AboutScreen` is rendered via the `Html` component (no `marked-element`).

**Data**
- Single source: **worldcup26.ir** (`WorldCupClient` with Spring `RestClient`, plus `TeamCatalog` / `StadiumCatalog` lookups). Domain records in `repository/worldcup`.
- Flags come from worldcup26.ir's remote flag URLs (via `FlagUtils`); local flag SVGs were removed.
- `MatchServiceImpl` / `GroupServiceImpl` map worldcup26 responses into the **unchanged** view DTOs; presenters/screens kept. Stage labels are humanized data-driven (handles future knockout `type` values).
- `MatchDetailScreen` shows only available data (score, venue/city, kickoff, goal scorers); the empty events grid and `EventGridDto` were removed.

**Not implemented (optional)**
- openfootball fallback for fixtures (resilience if worldcup26.ir is down).
- Rich match stats (lineups/possession) — unavailable from a free, no-key source.

*Original decisions (2026-06-08): Vaadin 25.1.7 · free data · core-rebuild scope · app-layout-addon 6.2.0-SNAPSHOT.*
