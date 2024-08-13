package ru.yandex.practicum.filmorate.dal;

public abstract class sqlRequests {
    public static final String INSERT_FILM = "INSERT INTO film (name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_FILM = "UPDATE film SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    public static final String SELECT_BY_ID_FILM = "SELECT * FROM film WHERE id = ?";
    public static final String DELETE_BY_ID_FILM = "DELETE FROM film WHERE id = ?";
    public static final String SELECT_ALL_FILM = "SELECT * FROM film";

    public static final String INSERT_FILM_GENRE = "INSERT INTO film_genre (genre_id, film_id) VALUES(?, ?)";

    public static final String ADD_LIKE = "INSERT INTO film_like (film_id, user_id) VALUES(?, ?)";
    public static final String DELETE_LIKE = "DELETE FROM film_genre WHERE film_id = ? AND user_id = ?";
    public static final String SELECT_LIKES_BY_ID_FILM = "SELECT user_id FROM film_like WHERE film_id = ?";

    public static final String INSERT_USER = "INSERT INTO \"USER\" (email, login, name, birthday)" +
            " VALUES (?, ?, ?, ?)";
    public static final String SELECT_ALL_USER = "SELECT * FROM \"USER\"";
    public static final String UPDATE_USER = "UPDATE \"USER\" SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    public static final String SELECT_BY_ID_USER = "SELECT * FROM \"USER\" WHERE id = ?";
    public static final String DELETE_BY_ID_USER = "DELETE FROM \"USER\" WHERE id = ?";

    public static final String INSERT_FRIEND = "INSERT INTO friend(user_id, friend_id, status)" +
            "VALUES (?, ?, ?)";
    public static final String DELETE_FRIEND = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
    public static final String SELECT_STATUS_FRIENDS = "SELECT status FROM friend WHERE user_id = ? AND friend_id = ?";
    public static final String UPDATE_FRIEND = "UPDATE friend SET status = ? WHERE user_id = ? AND friend_id = ?";
    public static final String SELECT_ALL_FRIEND = "SELECT friend_id FROM friend WHERE user_id = ?";

    public static final String SELECT_All_MPA = "SELECT * FROM mpa";
    public static final String SELECT_MPA = "SELECT * FROM mpa WHERE id = ?";


    public static final String SELECT_ALL_GENRE = "SELECT * FROM genre";
    public static final String SELECT_GENRE = "SELECT * FROM genre WHERE id = ?";
    public static final String SELECT_GENRES_BY_FILM_ID = "SELECT g.id, " +
            "g.name FROM genre AS g JOIN film_genre AS fg ON g.id = " +
            "fg.genre_id WHERE fg.film_id = ? ORDER BY g.id";
}
