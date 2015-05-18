# GridImageSearch
Android app client for Grid image search
This app is developed using the requirements provided at: http://courses.codepath.com/courses/intro_to_android/week/2#!assignment

User stories implemented are:

User can enter a search query that will display a grid of image results from the Google Image API. (see https://developers.google.com/image-search/v1/jsondevguide)<br>
User can click on "settings" which allows selection of advanced search options to filter results<br>
User can configure advanced search filters such as:<br>
Size (small, medium, large, extra-large)<br>
Color filter (black, blue, brown, gray, green, etc...)<br>
Type (faces, photo, clip art, line art)<br>
Site (espn.com)<br>
Subsequent searches will have any filters applied to the search results<br>
User can tap on any image in results to see the image full-screen<br>
User can scroll down “infinitely” to continue loading more image results (up to 8 pages)<br>
The following advanced user stories implemented are optional:

Advanced: Robust error handling, check if internet is available, handle error cases, network failures<br>
Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText<br>
Advanced: User can share an image to their friends or email it to themselves<br>
Advanced: Replace Filter Settings Activity with a lightweight modal overlay<br>
Advanced: Improve the user interface and experiment with image assets and/or styling and coloring<br>
Bonus: Use the StaggeredGridView to display improve the grid of image results. Got an issue with this described below<br>
Bonus: User can zoom or pan images displayed in full-screen detail view<br>

How many hours did it take to complete? 20<br>

Resources included via Gradle:<br>
https://github.com/loopj/android-async-http<br>
https://github.com/square/picasso<br>
https://github.com/etsy/AndroidStaggeredGrid<br>
https://github.com/MikeOrtiz/TouchImageView<br>

GIF walkthrough of all required and optional stories (using LiceCap)<br>





Issue with staggeredview. On scrolling down many images are repeating. Also on scrolling up images are lost. Please let me know what I am missing. Thank you.



