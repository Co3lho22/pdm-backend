API Endpoints Documentation
1. Favorite Movies and Genres
 
Add Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: POST

Example Request (json):

    POST /favorite/movies/123
    Content-Type: application/json
    
    [1, 2, 3]

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite movies added successfully"

Get Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: GET

Example Request:

    GET /favorite/movies/123

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    [1, 2, 3]

Remove Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: DELETE

Example Request (json):

    DELETE /favorite/movies/123
    Content-Type: application/json
    
    [1, 2]

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite movies removed successfully"

Add Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: POST

Example Request:

    POST /favorite/genres/123
    Content-Type: application/json
    
    [4, 5]

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite genres added successfully"

Get Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: GET

Example Request:

    GET /favorite/genres/123

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    [4, 5]

Remove Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: DELETE

Example Request(json):


    DELETE /favorite/genres/123
    Content-Type: application/json

    [4]

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite genres removed successfully"

2. User Authentication
   
User Login

        Endpoint: /login 
        Method: POST

Example Request (json):


    POST /login
    Content-Type: application/json
    
    {
    "username": "john_doe",
    "password": "password123"
    }

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    {
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }

User Registration

    Endpoint: /register
    Method: POST

Example Request (json):


    POST /register
    Content-Type: application/json
    
    {
    "username": "new_user",
    "password": "newpassword",
    "email": "newuser@example.com"
    }

Example Response (json):


    HTTP/1.1 200 OK
    Content-Type: application/json

    "Registration Successful"

Refresh Token

    Endpoint: /refresh
    Method: POST

Example Request (json):

    POST /refresh
    Content-Type: application/json
    
    {
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    {
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }

3. Movie Information
   
Get Movie by ID

    Endpoint: /movie/{id}
    Method: GET 

Example Request:

    GET /movie/456

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    {
      "id": 456,
      "title": "Movie Title",
      "description": "Description of the movie...",
      // Other movie details
    }

Get Movies

    Endpoint: /movie
    Method: GET

Example Request:

    GET /movie?minRating=4.5&maxRating=5&startDate=2023-01-01&endDate=2023-12-31&limit=10

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    [
      {
        "id": 123,
        "title": "Movie 1",
        // Other details
      },
      {
        "id": 124,
        "title": "Movie 2",
        // Other details
      }
      // More movies
    ]

