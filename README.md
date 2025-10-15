ParkEase – Parking Management App

An Android app for seamless parking management, built with Jetpack Compose, Firebase, and Google Maps API.

Overview:
ParkEase is a parking management solution designed to simplify finding and booking parking spots. Users can register, login, view available parking spaces in real-time on Google Maps, and make bookings with offline support. The app leverages Room Database, LiveData, and StateFlow to ensure real-time updates and a smooth user experience.

Features:
1. User authentication (registration & login) with Firebase
2. Real-time parking spot tracking and updates
3. Google Maps integration for locating nearby parking spots
4. Booking system with offline support using Room Database
5. Enhanced UX with LiveData and StateFlow for responsive updates
6. Navigation implemented with BottomNavigationBar
7. Password change and profile management

Architecture / Workflow:
1. User signs up or logs in via Firebase Authentication
2. App displays nearby parking spots on Google Maps
3. Users can select and book a parking spot
4. Booking information is stored locally in Room Database and synced when online
5. Users can view booking history and manage their profile

Technologies Used:
1. Android: Kotlin, Jetpack Compose
2. Backend/Services: Firebase Authentication, Firebase Firestore
3. Database: Room Database for offline support
4. Maps: Google Maps API
5. State Management: LiveData, StateFlow
6. Networking: Retrofit (for external APIs, e.g., Places API)

Installation / Setup:
1. Clone the repository
2. Open the project in Android Studio
3. Add your Firebase configuration file (google-services.json) in app/
4. Build and run the app on an Android device or emulator

Usage / Demo:
1. Register or log in
2. Browse available parking spots on the map
3. Book a parking spot
4. View booking history and manage profile
5. Offline booking supported via Room Database

Challenges & Learnings:
1. Integrating Google Maps API with real-time updates
2. Handling offline booking and data syncing with Room Database
3. Managing state efficiently with LiveData & StateFlow
4. Implementing secure user authentication with Firebase

Future Enhancements:
1. Push notifications for booking reminders
2. Dynamic pricing based on availability
3.Admin dashboard for managing parking spots
4.Integration with payment gateways
