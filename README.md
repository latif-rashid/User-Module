# User-Module
This is User module which is completely based on Spring Security and along with JWT tokens including with Authentication and Authorization

This module provides basic user management functionality needed by a typical website. This includes:

User log in/out as per their role and they can add something

Security
All passwords are stored as in HMAC SHA256 algorithm. Each password is salted with a securely generated random salt. The same salt is never used twice (a new salt is generated every time a user changes their password).
Usage
The user module library contains two sub-projects:

Project usermodule provides the core functionality. This project can be used with any database model via the UserDatabaseModel interface.

Project usermodule-jpa provides a default JPA implementation of the database model. You can use this implementation with the core project, or you can create your custom implementation.
