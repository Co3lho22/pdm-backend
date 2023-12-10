# Overview of the Backend Structure:
The backend is structured to efficiently manage a movie streaming service. The project is developed using Jersey in Java and interacts with two databases: MariaDB and Cassandra.

### API Layer (api package): 

This layer defines RESTful endpoints in classes like AdminResource, FavoriteResource, and LoginResource. Each class deals with different aspects of the application, such as user authentication and movie streaming.

### Data Access Object (DAO) Layer (dao package): 

The DAO classes, including AdminDAO, GenreDAO, and MovieDAO, are responsible for database interactions. They handle all database logic, maintaining a clean separation of concerns.

### Model Layer (model package): 

This layer consists of classes representing data entities like Movie, Genre, and User. These classes are crucial for data representation and manipulation throughout the application.

### Utility Layer (util package): 

Utilities such as DBConnection for database connections, JwtUtil for handling JWTs, and VideoConverter for video file conversions are located here.

### Database Structure:

#### MariaDB: 

Used for storing user data, movie details, genres, and more. It's a relational database with tables like USERS, MOVIES, and GENRES.

#### Cassandra: 
    
Utilized for storing movie links. It's optimized for handling large data volumes and includes columns like movie_id and resolution.

## Basic Workflow of the Backend:

### User Authentication: 

The system implements user registration, login, and token refresh functionalities. The LoginResource and UserAuthDAO handle these operations using JWTs for security.

### Movie Management: 

In the MovieResource class, endpoints are provided for adding, retrieving, and updating movies. These interact with MovieDAO for database transactions.

### Genre Management: 

Genres are managed through their respective endpoints and DAO classes.

### User Favorites: 

Users can mark movies and genres as favorites, managed through FavoriteResource and UserFavoritesDAO.

### Streaming: 

The StreamResource class is responsible for movie streaming, working with MovieLinksDAO and MovieLinksCassandraDAO to manage movie links and streaming statuses.

### Role-Based Access Control (RBAC): 

The system supports different user roles and permissions, managed through RoleDAO and PermissionDAO.

### Video Conversion: 
The VideoConverter utility handles the conversion of video files to HLS format for streaming.

## Conclusion:

The backend is a robust system designed for a movie streaming service. It's capable of handling user management, movie streaming, and data storage efficiently. By leveraging the strengths of both MariaDB and Cassandra databases, a seamless and scalable service is ensured.