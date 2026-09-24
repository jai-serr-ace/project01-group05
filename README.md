# Project 01 Retrospective and overview

[Video Walkthrough](https://www.youtube.com/watch?v=6-GO5czMB4c)

[Github Repo](https://github.com/jai-serr-ace/project01-group05)

## Overview

MangaReader is an Android application that allows users to search, browse, save, and read manga. The application uses the MangaDex API to retrieve manga information and uses a local database to store information about users, manga, and chapters.

The application includes features such as user login and sign-up, manga search, manga details, manga covers, genre sorting, favorites, chapter storage and display, an 18+ censorship toggle, and admin user management.

## Introduction

* How was communication managed

  * Communication was managed through Google Meet and GitHub. We used Google Meet to discuss our progress, divide up work, coordinate features, and solve problems. GitHub was used to manage issues, branches, and pull requests.
* How many stories/issues were initially considered

  * We initially considered 8 main issues: Create Login, Create SignUp, Integrate MangaDex API, Create MangaDAO & MangaEntity, Create Admin, Save Manga to Favorite, Create Favorite Library, and Create Search Feature.
* How many stories/issues were completed

  * All 8 of the initially planned issues were completed. We also added additional functionality as development continued.

## Team Retrospective

### Noemhi Marquez

* [Noemhi's pull requests](https://github.com/jai-serr-ace/project01-group05/pulls?q=is%3Apr+author%3Anoemhi-marquez)
* [Noemhi's issues](https://github.com/jai-serr-ace/project01-group05/issues?q=is%3Aissue+author%3Anoemhi-marquez)

#### What was your role / which stories did you work on

Noemhi mainly worked on integrating the MangaDex API and features that used information retrieved from the API. She integrated the API and worked on the Search and MangaDetail features.

* What was the biggest challenge?

  * Figuring out how to integrate our own features without breaking other teammates' features.
* Why was it a challenge?

  * Everyone was working on different parts of the same application, so changes made by one person could affect another person's work.
  * How was the challenge addressed?
  * We communicated through Google Meet and GitHub to figure out conflicts and worked together to make sure our features worked together.
* Favorite / most interesting part of this project

  * Working with the MangaDex API and seeing real manga information appear in the application.
* If you could do it over, what would you change?

  * I would communicate more about changes before merging them so we could avoid some integration conflicts.
* What is the most valuable thing you learned?

  * Communication is very important when multiple people are working on different features in the same project.

### Anthony Duenez Ramirez

* [Anthony's pull requests](https://github.com/jai-serr-ace/project01-group05/pulls?q=is%3Apr+author%3Aanthonyduenez)
* [Anthony's issues](https://github.com/jai-serr-ace/project01-group05/issues?q=is%3Aissue+author%3Aanthonyduenez)

#### What was your role / which stories did you work on

Anthony worked on integrating the login page, manga covers, sorting manga by genre, and the 18+ censorship toggle.

* What was the biggest challenge?

  * Pushing code that ran correctly on my computer, but later rebuilding the project and having it no longer compile.
* Why was it a challenge?

  * Something could appear to be working correctly and then stop working after other changes were made or after rebuilding the project. This made it difficult to figure out exactly what caused the problem.
  * How was the challenge addressed?
  * I worked through the errors, tested the project after making changes, and communicated with my teammates when the problem involved other parts of the application.
* Favorite / most interesting part of this project

  * Seeing all of our separate features come together and eventually having an application that was usable.
* If you could do it over, what would you change?

  * I would rebuild and test the entire project more often after making changes so problems could be found earlier.
* What is the most valuable thing you learned?

  * Testing changes throughout development is very important, especially when multiple people are working on the same project.

### Jaime Serrano Acevedo

* [Jaime's pull requests](https://github.com/jai-serr-ace/project01-group05/pulls?q=is%3Apr+author%3Ajai-serr-ace)
* [Jaime's issues](https://github.com/jai-serr-ace/project01-group05/issues?q=is%3Aissue+author%3Ajai-serr-ace)

#### What was your role / which stories did you work on

Jaime mainly worked on the manga and chapter database functionality. He created the MangaDB package, worked on MangaDAO and MangaEntity, created the chapter database, and worked on retrieving, storing, and displaying manga chapters.

* What was the biggest challenge?

  * Getting the chapter data from the MangaDex API to work correctly with the local database and chapter display.
* Why was it a challenge?

  * The chapter data had to be retrieved from the API, stored correctly in the database, and then displayed properly in the application. A problem in one part could prevent the chapter information from loading or displaying correctly.
  * How was the challenge addressed?
  * I broke the feature into smaller parts and worked on the database, chapter retrieval, and chapter display separately. Testing each part helped me find where problems were happening before connecting everything together.
* Favorite / most interesting part of this project

  * Working on the chapter functionality because it connected the API, database, and user interface together.
* If you could do it over, what would you change?

  * I would test the chapter and database functionality earlier so integration problems could be found sooner.
* What is the most valuable thing you learned?

  * How to work with API data and store it locally using a database in an Android application.

### Justin Tzeng

* [Justin's pull requests](https://github.com/jai-serr-ace/project01-group05/pulls?q=is%3Apr+author%3AJTzeng1)
* [Justin's issues](https://github.com/jai-serr-ace/project01-group05/issues?q=is%3Aissue+author%3AJTzeng1)

#### What was your role / which stories did you work on

Justin mainly worked on the user database and Admin functionality. He created UserEntity and UserDAO, added user information to MangaDAO, created the Admin page and User Management screen, and added the ability for an Admin to delete user accounts.

* What was the biggest challenge?

  * Figuring out what my teammates had done and making sure my code did not conflict with their code. This became more difficult as the program became larger.
* Why was it a challenge?

  * As the application grew, more parts of the code became connected. I needed to understand how another teammate's code worked before changing something that could affect it.
  * How was the challenge addressed?
  * I reviewed the existing code before making changes and communicated with my teammates when I needed to understand how another part of the application worked.
* Favorite / most interesting part of this project

  * Working on the Admin and user management functionality because it added another type of user and different functionality to the application.
* If you could do it over, what would you change?

  * I would spend more time understanding how everyone's features connect earlier in development so it would be easier to avoid conflicts later.
* What is the most valuable thing you learned?

  * Understanding the overall structure of the project is important when working with a team instead of only focusing on your own code.

## Conclusion

* How successful was the project?

  * The project was successful because we completed all 8 of the main issues that we initially planned. We also added additional functionality such as manga covers, genre sorting, the 18+ censorship toggle, chapter storage and display, and user management.
* What was the largest victory?

  * The largest victory was getting everyone's individual features integrated into one usable application. There were challenges with code conflicts and compilation problems, but communication through Google Meet and collaboration through GitHub helped us work through them.
* Final assessment of the project

  * Overall, MangaReader gave us experience developing an Android application as a team. We learned how to work with an external API, local databases, GitHub, and code written by multiple developers. The project also showed us how important communication, testing, and coordination are when working together on a shared codebase.
