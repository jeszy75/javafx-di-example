package db;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import model.User;

@RegisterBeanMapper(User.class)
public interface UserDao {

    @SqlUpdate("""
        CREATE TABLE IF NOT EXISTS users (
            id IDENTITY PRIMARY KEY,
            username VARCHAR UNIQUE NOT NULL,
            name VARCHAR NOT NULL,
            email VARCHAR NOT NULL
        )
        """
    )
    void createTable();

    @SqlUpdate("INSERT INTO users (username, name, email) VALUES (:username, :name, :email)")
    @GetGeneratedKeys
    Long insert(@BindBean User user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> findById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> list();

}
