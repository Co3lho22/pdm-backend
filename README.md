# API Endpoints Documentation
1. Favorite Movies and Genres
 
## Add Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: POST

Example Request (json):

    POST /favorite/movies/123
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    
    [1, 2, 3]

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite movies added successfully"

## Get Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: GET

Example Request:

    GET /favorite/movies/123
    Authorization: Bearer <JWT Token>

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    [1, 2, 3]

## Remove Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: DELETE

Example Request (json):

    DELETE /favorite/movies/123
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    
    [1, 2]

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite movies removed successfully"

## Add Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: POST

Example Request:

    POST /favorite/genres/123
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    
    [4, 5]

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite genres added successfully"

## Get Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: GET

Example Request:

    GET /favorite/genres/123
    Authorization: Bearer <JWT Token>

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    [4, 5]

## Remove Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: DELETE

Example Request(json):


    DELETE /favorite/genres/123
    Authorization: Bearer <JWT Token>
    Content-Type: application/json

    [4]

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    "Favorite genres removed successfully"

2. User Authentication
   
## User Login

        Endpoint: /login 
        Method: POST

Example Request (json):


    POST /login
    Authorization: Bearer <JWT Token>
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

## User Registration

    Endpoint: /register
    Method: POST

Example Request (json):


    POST /register
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    
    {
    "username": "new_user",
    "password": "newpassword",
    "email": "newuser@example.com",
    "phone": "21323142134",
    "country": "Portugal"
    }

Example Response (json):


    HTTP/1.1 200 OK
    Content-Type: application/json

    "Registration Successful"

## Refresh Token

    Endpoint: /refresh
    Method: POST

Example Request (json):

    POST /refresh
    Authorization: Bearer <JWT Token>
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
   
## Get Movie by ID

    Endpoint: /movie/{id}
    Method: GET 

Example Request:

    GET /movie/456
    Authorization: Bearer <JWT Token>

Example Response (json):

    HTTP/1.1 200 OK
    Content-Type: application/json

    {
      "id": 456,
      "title": "Movie Title",
      "description": "Description of the movie...",
      // Other movie details
    }

## Get Movies

    Endpoint: /movie
    Method: GET

Example Request:

    GET /movie?minRating=4.5&maxRating=5&startDate=2023-01-01&endDate=2023-12-31&limit=10
    Authorization: Bearer <JWT Token>

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


# Admin Operations
## Add New Movie

    Endpoint: /admin/addMovie
    Method: POST
    Authorization Required: Yes
    Content-Type: application/json

Example Request(json):

    POST /admin/addMovie
    Authorization: Bearer <JWT Token>
    Content-Type: application/json

    {
    "title": "Inception",
    "duration": 148,
    "rating": 8.8,
    "releaseDate": "2010-07-16",
    "description": "A thief who steals corporate secrets through the use of dream-sharing technology..."
    }

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "Movie added successfully"

## Update Existing Movie

    Endpoint: /admin/updateMovie
    Method: PUT
    Authorization Required: Yes
    Content-Type: application/json

Example Request (json):

    PUT /admin/updateMovie
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    
    {
    "id": 123,
    "title": "Inception",
    "duration": 148,
    "rating": 8.8,
    "releaseDate": "2010-07-16",
    "description": "A thief who steals corporate secrets through the use of dream-sharing technology..."
    }

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "Movie updated successfully"

## Delete Movie

    Endpoint: /admin/deleteMovie/{movieId}
    Method: DELETE
    Authorization Required: Yes

Example Request:

    DELETE /admin/deleteMovie/123
    Authorization: Bearer <JWT Token>

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "Movie deleted successfully"

## Add New Genre

    Endpoint: /admin/addGenre
    Method: POST
    Authorization Required: Yes
    Content-Type: text/plain

Example Request:

    POST /admin/addGenre
    Authorization: Bearer <JWT Token>
    Content-Type: text/plain

    Comedy

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "Genre added successfully"

## Remove Genre

    Endpoint: /admin/removeGenre/{genreId}
    Method: DELETE
    Authorization Required: Yes

Example Request:

    DELETE /admin/removeGenre/4
    Authorization: Bearer <JWT Token>

Example Response(json):

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "Genre removed successfully"

## Add User

    Endpoint: /admin/addUser?roleName={userName}
    Method: POST
    Authorization Required: Yes

Note: roleName can be "admin" or "user"

Example Request:

    POST /admin/addUser?roleName=user
    Authorization: Bearer <JWT Token>
    Content-Type: application/json
    {
       "username": "test",
       "password": "test",
       "email": "user@example.com",
       "country": "CountryName",
       "phone": "1234567890"
    }

Example Response:

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "User added successfull"

## Remove User

    Endpoint: /admin/removeUser/{userId}
    Method: DELETE
    Authorization Required: Yes

Example Request:
    
    DELETE /admin/removeUser/456
    Authorization: Bearer <JWT Token>

Example Response:

    HTTP/1.1 200 OK
    Content-Type: application/json
    
    "User removed successfully"

