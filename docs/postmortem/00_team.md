# Project 01 Post Mortem - MangaReader / Group 05

## Context

For Project 01, our team set out to build MangaReader, an Android application written in Kotlin and Jetpack Compose that uses the MangaDex API to retrieve manga information. Our original plan included 14 issues covering features such as login, MangaDex API integration, manga search and details, chapter display, navigation, favorites, reading progress, error handling, and genre filtering. What ultimately shipped included the core MangaDex integration, login, Home screen, manga search, manga details, chapter functionality, Room database support, and navigation between several of the application's main features. We completed many of the core features needed for the application, but several of the features from our original plan, particularly favorites, reading progress, API error handling, and genre filtering, remained incomplete by the end of Project 01.

## By the numbers

- Issues opened: **22** ([GitHub Issues](https://github.com/jai-serr-ace/project01-group05/issues)) | closed: **15**
- Pull requests opened: **27** ([GitHub Pull Requests](https://github.com/jai-serr-ace/project01-group05/pulls)) | merged: **22**
- Planned at kickoff: **14 stories/issues** | done: **7**

## What went well

1. **We established the MangaDex API and data layer early enough for multiple features to build on it.** Once the project could retrieve and represent manga information from MangaDex, that work could be reused for features such as Search, Manga Details, the Home screen, and chapter-related functionality. This gave different team members a common source of manga data instead of requiring each feature to implement its own API access.

2. **Issues and pull requests gave us a useful structure for dividing and reviewing work.** We created issues for the major features and used separate branches and pull requests to integrate changes. Team members reviewed PRs and provided feedback before merging them. This made individual contributions visible and gave us a process for discussing changes before they reached the main branch.

3. **The team was able to recover when integration problems appeared.** Several features stopped working correctly after changes from different branches were combined, including problems involving navigation and access to Search and Manga Details. Instead of leaving the application in that state, we investigated the combined code, created integration fixes, resolved merge conflicts, and retested the application. This allowed several independently developed features to work together in the final project.

## What went wrong

1. **Features that worked independently sometimes stopped working after they were merged together.**
    - **Cause:** Multiple features modified shared parts of the application, particularly navigation and application-level code, while much of the testing happened on individual feature branches. We did not consistently perform a complete integration test after every major merge. As a result, a feature could pass its own tests but still interfere with another feature once both were on the main branch.

2. **Our original scope was larger than what we were able to complete and validate during the project.**
    - **Cause:** At kickoff we planned 14 stories/issues, but we did not sufficiently account for the additional time required for integration, debugging, merge conflicts, testing, tooling changes, and dependencies between features. Some development time therefore shifted from implementing planned features to making already-developed features work together reliably.

3. **Work was not always distributed or communicated as clearly as it could have been.**
    - **Cause:** Team members often took initiative when they noticed something that needed to be implemented or fixed, but we did not always discuss ownership before that work began. This sometimes resulted in one person taking on several issues or integration tasks while other work remained unfinished. More frequent coordination around issue ownership and current workload would have made the distribution of work clearer.

## Advice to our next teams

1. **Test the integrated application after major merges, not only individual features before merging.** A feature working on its own branch does not guarantee that it will continue working when navigation, database, API, and UI changes from other branches are combined. We would recommend regularly pulling the latest main branch and testing the major user flows together.

2. **Plan fewer features and leave explicit time for integration and debugging.** Initial estimates should include the time needed for PR review, merge conflicts, testing, and unexpected dependencies between features. Completing a smaller set of features reliably is more useful than planning many features that cannot all be integrated by the deadline.

3. **Make issue ownership and workload visible before coding begins.** Team members should communicate which issue they intend to take and check how work is distributed before starting additional tasks. This can reduce duplicated effort, prevent one person from unintentionally carrying too much of the project, and make it easier to identify features that are not receiving enough attention.
