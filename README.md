## Authors
Charlie Sun, Bella Bregazzi, Manyu Wang, Carissa Chen, Younwoo Choi, InSeok Oh, Chris Dryden


## Running the program
To run the program, please run Main.java

## Accounts 
There are four types of accounts, to create an admin account, you will need a code that is located in "settings/Adminkey.txt"
The default admin code is 1234, to be used if the code cannot be loaded
Temporary user: Account is only available for 30 days.
Trial user: Account is only available this time.

## Templates
Template has different configurations, including timing, scoring, show unfamiliar flashcards again, etc.
Only admins may edit and create templates.

## Study tools
All users may create, edit and delete their own study tools.
Users may study with an available study tool (public, owned or invited), to search for the ID of a study tool, you may
    choose corresponding options from the main menu.
Trial users can do all that a regular user can, except they can't save their study tools upon exiting (they are still
    able to create and play them as long as they don't exit)

## Types of study tools and acceptable answer forms
- Correct answers must be non-empty and entered in acceptable form, as below:
- Exact answer
- Multiple choice: a, b, d
- Flashcard
- Sorting: (1, a), (1, b), (2, c)

## Collaboration
As a non-admin user, you can invite other users to edit your study tool, you can also revoke such invitation.

## Admin powers
Admins may create and edit templates, as well as suspending users and freeze study tools.

## Default templates and study tools
There are some default templates and study tools, if normal saves are not provided under savefiles, the default ones will be loaded.
