# E-Commerce-App

# My Android E-Commerce App
A simple e-commerce app built with Kotlin, featuring product listing, cart management, and mock order submission.
## Features
- Login via Firebase or MockAuth
- Product List with images, name, price
- Add to cart, update quantity, remove items
- Preview product details
- Subtotal, Tax (10%), Grand Total calculations
## Tech Stack
- Kotlin
- Android Jetpack: ViewModel, LiveData/StateFlow, RecyclerView
- Firebase Auth
- Material Components
  Switch Between Firebase & MockAuth

# You can run the app in two modes:

# 1️⃣ MockAuth Mode

This is useful for testing without Firebase.

Use the following static credentials on the login screen:

# Email: test@example.com
# Password: Test@1234


Steps:

Open the Login screen in the app.

Enter the credentials above.

Tap Login — you will be logged in without needing Firebase.

#  ️⃣ Firebase Mode


This mode uses Firebase Email/Password authentication.

Steps:

Add your Firebase project configuration:

Place google-services.json in the app/ folder.

Make sure Email/Password authentication is enabled in Firebase Console.

Run the app.

Use any registered Firebase user to log in.

# Note: You can toggle between MockAuth and Firebase in your LoginViewModel or by a boolean flag like useMockAuth. Set true to use MockAuth or false to use Firebase.
