#  📚 Android University Library Management App (Java + PHP/MySQL)

**Project Title (French):** Conception et réalisation d’une application mobile pour la gestion d’une bibliothèque universitaire
**Project Title (English):** Design and implementation of a mobile application for managing a university library

This repository contains the source code for both the front-end Android application and the back-end PHP server for a University Library Management system. This project was developed as a **final year project for a Bachelor's degree in Computer Science at Mohamed Khider University of Biskra (Algeria)** by Sofiane Alloui.

## Table of Contents

*   [Project Overview](#project-overview)
*   [Problem Statement](#problem-statement)
*   [Objectives](#objectives)
*   [Key Features](#key-features)
*   [Technology Stack](#technology-stack)
*   [Architecture](#architecture)
*   [Diagrams](#diagrams)
*   [Setup and Installation](#setup-and-installation)
    *   [Backend Server](#backend-server)
    *   [Frontend Android App](#frontend-android-app)
*   [Usage](#usage)
*   [Future Work](#future-work)
*   [Author](#author)
*   [License](#license)

## Project Overview

The university library is a crucial resource for students and professors. Traditional library management systems can often be inefficient, leading to delays and difficulties in accessing resources. This project, developed as a final requirement for a Bachelor's in Computer Science at Mohamed Khider University of Biskra, aims to modernize the university library system by providing a dedicated **Android mobile application (developed in Java)**. The app facilitates easier book searching, reservations, and management for both library users (students, professors) and administrators, communicating with a **PHP & MySQL backend** using the **Volley library** for network requests.

## Problem Statement

Based on observations of traditional library workflows, several challenges were identified:

> L’un des problèmes qui surviennent à la bibliothèque et qui constituent un obstacle, que ce soit pour les étudiants ou pour les administrateurs de la bibliothèque, est l’accumulation de demandes, la perturbation du système de gestion et l’attente prolongée de l’étudiant pour recevoir le livre.

**Translation & Summary:**
One of the significant problems faced in libraries, hindering both students and administrators, includes:
*   **Accumulation of Requests:** Backlogs in processing book requests.
*   **Management System Disruptions:** Potential inefficiencies or interruptions in the existing management process.
*   **Prolonged Waiting Times:** Students experiencing significant delays in receiving requested books.

## Objectives

The primary goal of this project is to develop a mobile application that addresses the identified problems by:

*   **Enhancing Efficiency:** Streamlining the process of borrowing, returning, and managing library resources.
*   **Improving Accessibility:** Allowing students and staff to interact with the library system remotely via the mobile app.
*   **Reducing Wait Times:** Providing real-time information and potentially faster processing of requests.
*   **Simplifying Management:** Offering administrators tools for easier management of inventory, users, and requests.
*   **Providing Real-time Updates:** Keeping users informed about book availability, due dates, and request statuses.

## Key Features

### For Users (Students/Professors)

*   **Authentication:** Secure login/signup process.
*   **Book Browsing & Searching:** Search for books by title, author, ID, tags, etc.
*   **Book Details:** View detailed information about books (description, availability, location).
*   **Book Reservation:** Request/reserve available books.
*   **Borrowing Management:** View currently borrowed books and their due dates (potentially renewal functionality).
*   **Notifications:** Receive alerts for due dates, available reservations, etc. (Implementation dependent).

### For Library Administrators

*   **Authentication:** Secure login with admin privileges.
*   **Resource Management:** Add, update, or remove books and their details in the catalogue.
*   **User Management:** View and manage user accounts (potentially).
*   **Request Handling:** Manage book reservation requests from users.
*   **Inventory Tracking:** Monitor book availability and borrowing status.

## Technology Stack

### Frontend (Android Application)

*   **IDE:** Android Studio
*   **Language:** Java
*   **SDK:** Android SDK
*   **Libraries:**
    *   AndroidX Libraries
    *   **Glide:** For efficient image loading and display.
    *   **Volley:** For handling network requests (communication with the PHP backend).
    *   **SQLite:** (Potentially used for local caching or offline data storage within the app).

### Backend (Server)

*   **Language:** PHP
*   **Web Server:** Apache (via XAMPP)
*   **Database:** MySQL / MariaDB (Managed via phpMyAdmin in XAMPP)
*   **Environment:** XAMPP

## Architecture

The system follows a typical **Client-Server architecture**:

1.  **Client:** The native Android application (Java) serves as the front-end, providing the user interface and handling user interactions.
2.  **Server:** A PHP-based backend hosted on an Apache server handles business logic, processes requests from the Android app, and interacts with the database.
3.  **Database:** A MySQL/MariaDB database stores all persistent data, including user information, book catalogue, borrowing records, etc.
4.  **Communication:** The Android app communicates with the PHP backend via RESTful APIs (likely), using HTTP requests handled by the **Volley library**, possibly exchanging data in JSON format.

## Diagrams

This project includes several design diagrams to illustrate its structure and functionality:

1.  **Use Case Diagram:** Shows interactions between users (Utilisateur, Etudiant, Prof) and the system functionalities (Connecter, Chercher un livre, Demander un nouveau livre, Authentification).
2.  **Class Diagrams:** Detail the structure of the Android application classes (e.g., `LoginActivity`, `SignUpAdditionalPersonalInfoActivity`, `UserMainAppBooksSearchActivity`, `AdminBooksBookItem`, `User`) and their relationships.
3.  **Database Schema Diagram:** Outlines the structure of the database tables (`users`, `admins`, `books`, `bookshelves`, `bookplace`, `demands`, `borrowings`, `invitation`) and their columns and relationships.

<!-- Suggestion: Create a '/diagrams' folder in your repo and add the images -->
*You can find the visual diagrams used during development in the `/diagrams` folder (or link to your presentation if available).*
*   `![Use Case Diagram](diagrams/use_case_diagram.png)`
*   `![Class Diagram - Auth](diagrams/class_diagram_auth.png)`
*   `![Class Diagram - User](diagrams/class_diagram_user.png)`
*   `![Database Schema](diagrams/database_schema.png)`

## Setup and Installation

Follow these steps to set up and run the project locally.

### Backend Server

1.  **Prerequisites:** Install [XAMPP](https://www.apachefriends.org/index.html) (includes Apache, MySQL, PHP, phpMyAdmin).
2.  **Clone Repository:** `git clone https://github.com/your-username/your-repo-name.git`
3.  **Place Backend Code:** Copy the contents of the `backend-php-server` directory from the cloned repo into your XAMPP `htdocs` folder (e.g., `C:/xampp/htdocs/library_server`).
4.  **Start XAMPP:** Launch the XAMPP Control Panel and start the **Apache** and **MySQL** modules.
5.  **Database Setup:**
    *   Open phpMyAdmin (usually `http://localhost/phpmyadmin`).
    *   Create a new database (e.g., `unibibappdatabase`).
    *   Import the provided SQL schema file (`database_schema.sql` - **Note:** You need to create and include this file!) into the newly created database.
6.  **Configuration (if necessary):** Check the PHP scripts within the backend code for any database connection configuration files (e.g., `db_config.php`, `connect.php`) and ensure the database name, username (usually 'root' for local XAMPP), and password (usually empty for local XAMPP) are correct.

### Frontend Android App

1.  **Prerequisites:** Install [Android Studio](https://developer.android.com/studio).
2.  **Clone Repository:** (If not already done) `git clone https://github.com/your-username/your-repo-name.git`
3.  **Open Project:** In Android Studio, select `File > Open` and navigate to the `frontend-android-app` directory within the cloned repository.
4.  **Gradle Sync:** Allow Android Studio to sync the project and download any necessary dependencies (check Gradle version compatibility if needed).
5.  **Configuration:**
    *   **API Endpoint:** Locate the part of the Android code where the backend server URL is defined (often in a constants file, e.g., `Constants.java` or `ApiConfig.java`).
    *   **Update URL:** Change the base URL to point to your local XAMPP server's IP address and the backend directory (e.g., `http://192.168.1.100/library_server/` - replace `192.168.1.100` with your computer's local IP address, **not** `localhost` or `127.0.0.1` if testing on a physical device).
6.  **Build & Run:** Build the project and run it on an Android emulator or a physical device connected via USB. Ensure the device/emulator can access your computer over the local network if using a physical device.

## Usage

1.  Launch the Android application.
2.  **Sign Up / Login:** Create a new user account or log in using existing credentials.
3.  **Browse/Search:** Navigate the app to search for books using various criteria.
4.  **Reserve:** Select a book and use the reservation feature if available.
5.  **Admin Functions (if logged in as Admin):** Access administrative panels to manage books, users, or requests.

## Future Work

Potential improvements and future features for this application could include:

*   **Advanced Recommendation System:** Suggest books based on user history.
*   **Integration:** Connect with other campus services or e-learning platforms.
*   **Enhanced Notifications:** More granular controls and types of notifications.
*   **Book Renewal Feature:** Allow users to renew borrowed items directly through the app.
*   **Offline Support:** Limited functionality when the user is offline.

## Author

*   **Sofiane Alloui** - sofianealloui13@gmail.com
    *   *Developed as the final year project for the Bachelor's Degree in Computer Science at Mohamed Khider University of Biskra, Algeria.*

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
