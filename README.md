# About this APP `Pop Movies`
 An app to allow users to discover the most popular movies playing.

## As a developer, what I used to build this project:
- Make use of Android Architecture Components (Room, LiveData, ViewModel and Lifecycle). LiveData is used to observe changes in the database and update the UI accordingly
- Create a database using Room to store the names and ids of the user's favorite movies. They are updated whenever the user favorites or unfavorites a movie. Database is not re-queried unnecessarily after rotation, cached LiveData from ViewModel is used instead
- Allow users to mark a movie as a favorite in the details view by tapping a button (star)
- Use RecyclerView(GridLayoutManager) and Adapter to populate Movie list views via a grid of their corresponding movie poster thumbnails by RecyclerView(GridLayoutManager) and Adapter
- Launch detail activity via Intent to allow the user to tap on a movie poster thumbnail and transition to a details screen with additional information
- UI contains a settings menu to toggle the sort order of the movies by: most popular, highest rated. When a user changes the sort criteria via the setting, the main view gets updated correctly. An additional pivot to show the favorites collection is also included. When the `favorites` setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database
- Fetch data from the network services with theMovieDB API
- Use library `Picasso` to fetch images and load them into views
- Use an Intent to open a youtube link in a web browser of choice to view and play movie trailers
- Implement sharing functionality via ShareCompat.IntentBuilder to allow the user to share the first trailer’s  YouTube URL from the movie details screen
- Put object references in an Intent / Bundle with Parcelable mechanism to pass them to activities
- Restore the data using `onSaveInstanceState`/`onRestoreInstanceState` to prevent app crashing when the screen is rotated

## Why this Project
An Android developer must know how to bring particular mobile experiences to life. Specifically how to build clean and compelling user interfaces (UIs), fetch data from network services.

By building this app, I will demonstrate my understanding of the foundational elements of programming for Android. The app will communicate with the Internet and provide a responsive and delightful user experience.

## Overview
- Project overview
<div align="center">
    <img width="300" alt="layout_phone" src="https://github.com/mcf1727/popmovies/blob/master/photos/photo1.jpg"/>     <img width="300" alt="layout_phone" src="https://github.com/mcf1727/popmovies/blob/master/photos/photo2.jpg"/>
</div>

<div align="center">
    <img width="300" alt="layout_phone" src="https://github.com/mcf1727/popmovies/blob/master/photos/photo3.jpg"/>     <img width="300" alt="layout_phone" src="https://github.com/mcf1727/popmovies/blob/master/photos/photo4.jpg"/>
</div>

- Set api_key  
This app use the API from themoviedb.org to fetch popular movies.
To launch the app, you need to follow the follow steps:
1. Go to the website [https://www.themoviedb.org/account/signup](https://www.themoviedb.org/account/signup) to create an account
2. Then request an API Key
< In your request for a key, state that your usage will be for educational/non-commercial use. You will also need to provide some personal information to complete the request. Once you submit your request, you should receive your key via email shortly after.
3. Go to the project:gradle.properties file
4. Fill in your API key for the String API_KEY and add a new reference for API_KEY = “Your API KEY String”
```
# My Secret Key - this is an example, please enter your own key!
API_KEY="Your API KEY String"
```