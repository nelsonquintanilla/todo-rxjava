## RxJava Todo App
<img src="https://github.com/nelsonquintanilla/todo-rxjava/blob/main/todo_rxjava_demo.gif" width="300" alt="Todo RxJava Demo">

## Overview
This Todo app is designed to expose the inner structure of an Rx application. It's a complete RxJava app that illustrates Reactive Programming with Kotlin and RxJava in Android development.

## Motivation
While Kotlin Coroutines have gained popularity for asynchronous programming in Android, there's still a significant number of legacy projects that use RxJava. This project aims to provide developers with a comprehensive, up-to-date example of how to use RxJava with Kotlin in a real-world scenario.

Learning RxJava through this project can be beneficial for several reasons:

* Many existing projects still use RxJava, and understanding it is crucial for maintaining and improving these codebases.
* RxJava offers a different paradigm for handling asynchronous operations, which can broaden a developer's toolkit.
* There's a lack of resources showing complete, modern apps using RxJava with Kotlin, making this project a valuable learning tool.

## UI Toolkit Choice
This project intentionally uses the older UI toolkit based on Views rather than the newer Jetpack Compose. This decision was made for two main reasons:

1. Legacy Project Representation: Many legacy projects that use RxJava also use the View-based UI system. By using Views, this project better represents the environment developers are likely to encounter when working with existing RxJava codebases.
2. Illustrating RxJava Complexities: The View-based system allows for a clearer illustration of the complexities and nuances of using RxJava with Android UI components. It showcases how RxJava can be used to handle UI events, manage state, and update the UI reactively in a traditional Android app structure.

This choice helps developers understand how to integrate RxJava into existing projects and provides insights into managing asynchronous operations in a View-based UI system.

## Architecture
This app uses a modularized architecture that separates the data layer from the presentation layer. The primary goal is to achieve a clean separation between the user interface, business logic, and services.

### Key components of the architecture:
* View Model: Defines the business logic and data used by the view to show a particular view.
* Repository: Provides content from a store (database). It's abstracted from the view model to focus on view logic.
* Model: The most basic data store in the application. Both view models and repositories manipulate and exchange models.

The app follows the MVVM (Model-View-ViewModel) pattern, which allows for a clean separation of concerns and makes the codebase more maintainable and testable.

### Libraries Used
* **ViewModel**: For managing UI-related data in a lifecycle-conscious way.
* **LiveData**: For building data objects that notify views of database changes.
* **Room**: Provides an abstraction layer over SQLite for robust database access.
* **RxJava**: For composing asynchronous and event-based programs using observable sequences.
* **RxAndroid**: Provides Android-specific bindings for RxJava.
* **RxBinding**: Provides RxJava binding APIs for Android UI widgets.

## Features
* Add, edit, and delete tasks
* Mark tasks as complete
* View task statistics
* Reactive UI updates

## Getting Started
1. Clone the repository
2. Open the project in Android Studio
3. Build and run the app on an emulator or physical device

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.
