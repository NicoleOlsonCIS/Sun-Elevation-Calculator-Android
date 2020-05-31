package com.example.sunelevationcalculator;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.sunelevationcalculator.User;

import java.util.List;

@Dao
public interface UserDao {
    //@Query("SELECT * FROM user")
    //List<User> getAll();

    @Query("SELECT * FROM user")
    public User[] loadAllUsers();

    //@Insert void insertOnlySingleUser (User user); // what does this do?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(User... users);

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    //@Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
    //        "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

//@Dao public interface DaoAccess {  @Insert void insertOnlySingleMovie (Movies movies); @Insert void insertMultipleMovies (List<Movies> moviesList); @Query (“SELECT * FROM Movies WHERE movieId = :movieId“) Movies fetchOneMoviesbyMovieId (int movieId); @Update void updateMovie (Movies movies); @Delete void deleteMovie (Movies movies); }