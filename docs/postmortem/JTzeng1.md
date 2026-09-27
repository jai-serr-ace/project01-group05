# Justin Tzeng - Project 01 Retrospective

## My work

- **Merged PRs:** [My closed pull requests](https://github.com/jai-serr-ace/project01-group05/pulls?q=is%3Apr+state%3Aclosed+author%3AJTzeng1)
- **My issues:** [My closed issues](https://github.com/jai-serr-ace/project01-group05/issues?q=is%3Aissue+state%3Aclosed+author%3AJTzeng1+assignee%3AJTzeng1)

### What I built

I worked mainly on the user database and admin. I created the UserEntity to store user information, including the username, password, 
and whether the user is an admin. I also worked on the UserDAO to insert, delete, and retrieve users, and to verify login credentials. 
I connected this to the Room database so the app could store user information locally. I also worked on the Admin functionality, including 
the screens and logic needed for an admin user to manage users. I also created the Favorite feature, which lets you save or unsave your 
manga and display them at the top of the main manga page (where you see all the manga) so you can easily see your favorites. Unfortunately, 
it never merged into main because it was submitted on the last day, and nobody wanted to deal with the merge conflict, so it's sitting in Pull Request Approval.


## Biggest challenge

My biggest challenge was getting the Admin functionality working with the database and the rest of the app. It was not just about creating an 
Admin screen; the Admin feature depended on the UserEntity, UserDAO, database, login, and navigation all working together. I had to understand 
how the user information was stored and how the app could tell whether someone was an admin. I also ran into Git merge conflicts and changes from 
other branches while working on the project. I handled this by carefully reviewing the code changes, resolving conflicts, and testing the app after 
each change, rather than assuming the merge worked correctly.

## Most valuable thing I learned

The most valuable things I learned were how to make a pull request, communicate better with my partners, resolve merge conflicts, and ask for help when I
needed it. One of my first merges was a good learning experience because I created a branch called first merge, and when I tried to merge it, I ran into a 
merge conflict. I tried to get out of the merge and looked up the errors to figure out how to fix them, but nothing I tried worked. I eventually asked the 
professor for help. At first, he explained that I had pressed too many buttons while trying to handle the merge without fully understanding what each option 
did. As we looked further into the problem, though, we found several other issues and discovered that the root cause was that my project files were stored in 
OneDrive. This experience taught me that when I am stuck, I should not keep trying random commands without understanding what they do. I should stop, ask for 
help, and figure out the actual cause of the problem. It also helped me become more comfortable with Git and communicating with my team when I run into problems

## What I carry into Project 02

### 1. I will ask for help sooner when I am stuck.

During Project 01, I spent time trying to fix my merge problem on my own before asking the professor for help. Asking for help eventually helped me 
realize there were multiple issues, including that my project was stored in OneDrive. In Project 02, I will still try to troubleshoot problems myself 
first, but I will ask a teammate, TA, or professor when I am stuck, rather than repeatedly trying things without understanding the cause. Of course, now 
I understand how to do a merge conflict because of this mistake.

### 2. Come up with a plan before starting our indvidual features
During our Project 01 presentations, I noticed that some teams had planned more of their app ahead of time. Some teams showed layout sketches and talked 
about how they wanted the different parts of their app to work together. Our team mostly worked on our own individual features without always having a 
clear picture of the complete app. That worked, but I think having a shared goal and planning the app earlier could have helped us think of more features 
and make the app feel more complete. In Project 02, I want to be more involved in planning the overall app before we start coding. I will know it worked if 
our team has a shared plan or layout for the main parts of the app before we start implementing individual features.


