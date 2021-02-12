# Clean Code Exercise 1

## Context 

The first exercise is about cleaning up code in a web service.
You have just joined a start-up on pet health care and have
become the maintainer of a web service for a veterinary service.
It provides pet clinics with a database for storing pets, owners
and their appointments at the clinic.

This repository contains the application back-end implementing 
a REST API, which is what you will be working with. Fortunately, 
the app has plenty of [documentation](readme_petclinic.md). 

The previous maintainer told you that you should be able to clone
this repository and build and start the project with Maven or an
IDE like IntelliJ. Once started, the back-end provides a Swagger-UI
at http://localhost:9966/petclinic for testing its functions.

## CSV Import

You have heard of complaints that one of the features of the app
is buggy: The CSV import allows administrator users to quickly
add or delete many pets to or from the database.

Under the REST endpoint `/api/import`, the app allows users
to upload CSV files with content such as the following. 

```csv
fifi;2015-02-03;dog;Franklin;add
lassie;2010-02-03;dog;Franklin;delete
felix;2020-02-03;cat;Franklin;add
```

The columns are the name of a pet, its birth date, its type, its
owner's last name and an action (add or delete).
Uploading the above example file would add the dog fifi and the
cat felix to the data base, and delete the dog lassie. In all
three cases the owner is Benjamin Franklin. The CSV-file only
contains the owner's last name. 

Now, you have heard reports that it can sometimes crash 
with manually edited data. You decide to look at the code to 
see how it can be improved. You find the code in 
[ImportCSV.java](src/main/java/org/springframework/samples/petclinic/rest/importcsv/ImportCSV.java)
and decide to clean it up before you fix any errors.

## Task 1: Code Cleanup

Create a fork of this repository and clean up the code in 
[ImportCSV.java](src/main/java/org/springframework/samples/petclinic/rest/importcsv/ImportCSV.java)
to the best of your ability.
Unfortunately, company policy disallows the use of any additional
dependencies, so you cannot use any external libraries.

## Task 2: Improving Robustness

Improve the code further to make it more robust. If the input csv date is bad,
the app should return a proper error message rather than just crashing.
You may have already noticed and fixed some problems already in task 1.
Task 2 is to resolve them all.

## Submission

After completing tasks 1 and 2, submit the resulting code by submitting a
pull request with your changes in github.

## Task 3

After all groups have submitted their cleaned up code, each group will be
given another group's submission for code review. Please make comments to
provide feedback, e.g., where you still see problems in the code or where
you think that a good solution was found.

# Instructions

- Create a fork of this repository.
- Clone the forked project and look at the 
  [documentation](readme_petclinic.md) for working with it.
- Finish task 1 and 2 by tomorrow noon and submit a pull request with your 
  changes.
- After submission, you will receive another group's submission
  to review. Please then finish task 3 until the next session by 
  making review comments on the pull request in github.
