Random User Application

Random User Application is an Android app built using modern development practices. It fetches random user data from an API and displays it in a user-friendly interface. The app leverages Jetpack Compose for UI and follows Clean Architecture principles for maintainability and scalability.

Features

Fetch random user data from the Random User API.

Enter a count to specify the number of users to fetch.

View user details including name, email, and profile picture.

Search users dynamically by name.

Error handling for network and input issues.

Tech Stack

Language: Kotlin

Architecture: Clean Architecture

UI: Jetpack Compose

Networking: Retrofit

Dependency Injection: Hilt

Testing: JUnit, Mockito

Layers

1. Domain Layer

Contains use cases that define the business logic.

Example: GetUsersUseCase to fetch users from the repository.

2. Data Layer

Handles data retrieval and mapping between API responses and domain models.

Includes the UserRepository and RandomUserApi service.

3. Presentation Layer

Manages the UI components and ViewModels.

Example: UserListViewModel connects the domain layer to the UI.

4. Utils Layer

Provides utility functions like NetworkUtil for network connectivity checks.

How It Works

Enter User Count:

Input the number of users to fetch in the text field and click "Fetch Users."

View User List:

A list of users is displayed with their names, emails, and profile pictures.

Search Users:

Use the search bar to dynamically filter the user list by name.

Error Handling:

If the network is unavailable or the input is invalid, an error message is shown.