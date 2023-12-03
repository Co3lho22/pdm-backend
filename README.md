API Endpoints
1. Favorite Movies and Genres
   Add Favorite Movies

   Endpoint: /favorite/movies/{userId}
   Method: POST
   Consumes: application/json
   Produces: application/json
   Description: Adds a list of favorite movies for a specified user.
   Parameters:
   userId (path parameter): User's ID.
   movieIds (request body): List of movie IDs to add to favorites.

Get Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: GET
    Produces: application/json
    Description: Retrieves a list of favorite movies for a specified user.
    Parameters:
        userId (path parameter): User's ID.

Remove Favorite Movies

    Endpoint: /favorite/movies/{userId}
    Method: DELETE
    Consumes: application/json
    Produces: application/json
    Description: Removes a list of favorite movies for a specified user.
    Parameters:
        userId (path parameter): User's ID.
        movieIds (request body): List of movie IDs to remove from favorites.

Add Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: POST
    Consumes: application/json
    Produces: application/json
    Description: Adds a list of favorite genres for a specified user.
    Parameters:
        userId (path parameter): User's ID.
        genreIds (request body): List of genre IDs to add to favorites.

Get Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: GET
    Produces: application/json
    Description: Retrieves a list of favorite genres for a specified user.
    Parameters:
        userId (path parameter): User's ID.

Remove Favorite Genres

    Endpoint: /favorite/genres/{userId}
    Method: DELETE
    Consumes: application/json
    Produces: application/json
    Description: Removes a list of favorite genres for a specified user.
    Parameters:
        userId (path parameter): User's ID.
        genreIds (request body): List of genre IDs to remove from favorites.

2. User Authentication
   User Login

   Endpoint: /login
   Method: POST
   Consumes: application/json
   Produces: application/json
   Description: Verifies user login credentials and provides access and refresh tokens.
   Parameters:
   user (request body): User object containing username and password.

User Registration

    Endpoint: /register
    Method: POST
    Consumes: application/json
    Produces: application/json
    Description: Registers a new user.
    Parameters:
        user (request body): User object containing user details.

Refresh Token

    Endpoint: /refresh
    Method: POST
    Consumes: application/json
    Produces: application/json
    Description: Refreshes the access token using a valid refresh token.
    Parameters:
        request (request body): TokenRequest object containing the refresh token.

3. Movie Information
   Get Movie by ID

   Endpoint: /movie/{id}
   Method: GET
   Produces: application/json
   Description: Retrieves movie details by movie ID.
   Parameters:
   id (path parameter): Movie's ID.

Get Movies

    Endpoint: /movie
    Method: GET
    Produces: application/json
    Description: Retrieves a list of movies based on various filters.
    Parameters:
        minRating (query parameter): Minimum rating filter.
        maxRating (query parameter): Maximum rating filter.
        startDate (query parameter): Start date filter.
        endDate (query parameter): End date filter.
        limit (query parameter): Limit on the number of movies to retrieve.