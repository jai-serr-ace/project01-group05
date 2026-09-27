# Jaime Serrano Acevedo - Project 01 Retrospective

## My work
- Merged PRs: 
    + [Create mangaDB package with DAO, entities, and unit tests- #18](https://github.com/jai-serr-ace/project01-group05/pull/18)
    + [Chapter DB created and storing features added- #25](https://github.com/jai-serr-ace/project01-group05/pull/25)
    + [Manga Chapter Display- #34](https://github.com/jai-serr-ace/project01-group05/pull/34)
    + [Chapter List Display and Downloads Manager implementation- #44](https://github.com/jai-serr-ace/project01-group05/pull/44)


- My issues:
    + [Create MangaDAO and MangaEntity #3](https://github.com/jai-serr-ace/project01-group05/issues/3)
    + [Create MangaEntity #16](https://github.com/jai-serr-ace/project01-group05/issues/16)
    + [Configure Data storing / accessing Manga database and chapters #21](https://github.com/jai-serr-ace/project01-group05/issues/21)
    + [Retrieve Manga Chapters #7](https://github.com/jai-serr-ace/project01-group05/issues/7)
    + [Chapter List UI & Download Manager #39](https://github.com/jai-serr-ace/project01-group05/issues/39)

- What I built:

   In this project, my main work was on the database and chapters. I built the database for the Manga, including both the Entity and DAO methods. I included a POJO to make the database follow this one-to-many system because part of the idea was tags for the manga, which needed their own database. This was also useful because the POJO was also required for the database for chapters and linking everything: the manga, tags, and chapters.


   The next step was downloading the chapters. Noehmi worked around the API by requesting links and got this halfway done, but I wanted to save the files of chapters in the user storage and allow them to choose where to keep them. Although this functionality has some notorious bugs, it works; for the most, it does what it needs, allowing the downloading and deleting of chapters. I tried to implement a cache cleaner for the chapters that are not directly downloaded and are streamed instead, but it has some bugs.


   My final contribution was the chapter display. This included two main things: one, inside the manga detail screen, add the chapter list array, make it visible and scrollable. Add a feature to click on the chapter and be able to read it, be able to download the chapter with the download button, and delete it if the user long-clicks the chapter after it is downloaded.
  
   The second part was to make the actual manga reader. This was very straightforward, but complex. It was telling the program to show an image (in JPG) in the center of the screen and either by scrolling or swiping, make the next image appear. I actually implemented a system to ask the user to choose between scrolling or swiping. I also had to adjust the reading/page flipping speed so it is smooth and not laggy. I added a next chapter button for the end of the chapter, but I did not test all edge cases, so it tends to fail.


## Biggest challenge
The biggest challenge was the download system and the chapter display. The difficulty was that I did this in reverse order; I made the chapter display before the download system because I misread one of the issues. The issue is that when I developed the chapter display, I could only read one chapter, so I couldn't test the reading-through or other features like the next chapter button, which is why it carries that bug. In addition, since I tested the chapter display before the download manager (basically using the stream / API retrieval cache), the program would have issues detecting the images, which I managed to solve; however, it got very messy because when I tried to move files or folders, that would cause the problem again. As a matter of fact, even when you click on the download folder to change it, the current location of the already existing files will not change. At the end of the day, my only concern was to let the display work, regardless of this major existing bug.

## Most valuable thing I learned
The most valuable thing I learned in this project was the importance of communication. Just because I feel like, if I had better communication with my teammates we would not some of the problems we had such as: having different gradle versions, using different packages and sdk versions, having merge conflict, redo some work, being unable to complete some tasks and so on. For me was like, I felt like the group sometimes did not want to talk to each other, notbecause no one wanted to start a talk, I just feel awkward in general. But then, as soon as, we started getting a long better and getting further with project, communication and work got a lot better. Nevertheless, some of the problems we had could have been easily avoid if we just had communicate properly.

## What I carry into Project 02
## 1. I want to be more communicative. 
Basically I want to talk more with my teammates, asking them questions about how they are doing with the project. What things are their working on, if they needed to make changes to build, what stuff they either add, remove, or modified. Likewise, share my own progress very actively, letting them know what things I need, before just sending my PR and be like I did work 
### I will know it worked if we avoid having merge issues, duplicated code, build / independencie conflicts between us, and work load is more evenly spread. 

## 2. Read my group code / work
Summary, I feel like I need to look to what my teammates did and how they got things done rather than just making an assumption based on what their PRs said. Read their code comments, skim through their function, not only if their code is related to my code, but just in general. Because, it could happened that they add a feature that require a package, and I could attempt to re-add the package but with a different version and cause problems for both us.
### I will know it worked if the code is smoother, has no duplicates at all, it recycles and re-uses functions properly. In general, everything should be very organized, easy to read, and easy to track. 