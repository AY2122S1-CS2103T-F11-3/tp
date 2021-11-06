---
layout: page
title: Wei Kiat's Project Portfolio Page
---
### Project: HeRon 

HeRon is a desktop application for HR managers to assist in managing HR administrative tasks such as tracking leaves and offs, calculating pay and updating payroll information.

HeRon is a project adapted from AddressBook - Level 3.

Given below are my contributions to the project.

* **New Feature**: Added a display panel to display information of employee,
together with a view command to select what content to display.
    * What it does: Allows user to view more detailed information about a certain employee in a separate panel.
    The employee information to be displayed on the panel can be controlled with the View command. 
  
    * Justification: As we have added a lot more fields added to the employee, displaying all this information within the
    employee list panel cards' itself would be very cramped and create clutter on the screen. By only keeping the key information on the card and
    separating out more detailed information out to the display panel, it makes the interface cleaner and neater.

    * Highlights: The implementation of updating the display panel depending on whatever command is called uses the Observer pattern. The Observer and
    Observable classes are created by myself.  Many of the existing commands were also updated to display the relevant information to that command itself,
    as different commands require the display panel to reflect information differently.

    * Credits: The Observer pattern that is in the Software engineering textbook.

* **New Feature**: Added a pay command that allows the user to mark employees as being paid
    * What it does: After starting the payroll, all employees will be marked with an unpaid label, with the salary amount left unpaid
    next to it. User can then pay these employees and marked them as being paid afterwards when they finished paying them.
    * Justification: Enables user to take note of which employees have been paid and which have been left unpaid when going through
    the payroll to ensure that all employees are paid on time.

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2021-09-17&tabOpen=true&tabType=authorship&tabAuthor=1waykiat&tabRepo=AY2122S1-CS2103T-F11-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=&authorshipIsBinaryFileTypeChecked=false)

* **Enhancements**:
    * Updated edit command to take into account the new attributes being added. (Pull Request [#43](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/43))
    * Added calculatedPay attribute (Pull Request [#62](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/62))
    * Update GUI color scheme (Pull Request [#185](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/185))
    * Changed app icon and title (Pull Request [#116](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/62))
    * Fix UI related bugs, and functionality (Pull Requests [#100](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/100),
    [#191](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/191) and [#190](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/190))

* **Documentation**:
    * User Guide:
        * Added documentation for the feature `edit`, `view` and `pay`, as well as the UI layout - [#93](https://github.com/AY2122S1-CS2103T-F11-3/tp/pull/93)
    * Developer Guide:
        * Added documentation for the feature `edit` 
        * Updated UML class diagrams of `Model` and `UI` - 
        * Added implementation of InfoPanel, together with its UML class and sequence diagram - 

* **Community**:
    * Reviewed PRs
    * Helped to post up issues for other teammates

