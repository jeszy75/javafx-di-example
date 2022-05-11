package gui;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.guice.AbstractJdbiDefinitionModule;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.name.Names;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import db.UserDao;
import util.UserGenerator;

public class TableViewApplication extends Application {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    @Named("database")
    private Jdbi jdbi;

    private final GuiceContext context = new GuiceContext(this, () -> List.of(
            new GuiceJdbiModule(),
            binder -> {
                var config = new HikariConfig("/hikari.properties");
                DataSource ds = new HikariDataSource(config);
                binder.bind(DataSource.class).annotatedWith(Names.named("database")).toInstance(ds);
            }
    ));

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/table.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("JavaFX DI Example");
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void init() {
        context.init();
        if (getParameters().getUnnamed().contains("--createDatabase")) {
            createDatabase();
        }
    }

    private void createDatabase() {
        var userGenerator = new UserGenerator();
        jdbi.useExtension(UserDao.class, userDao -> {
            userDao.createTable();
            for (var i = 0; i < 100; i++) {
                var user = userGenerator.randomUser();
                userDao.insert(user);
            }
        });
    }

    static class GuiceJdbiModule extends AbstractJdbiDefinitionModule {

        public GuiceJdbiModule() {
            super(Names.named("database"));
        }

        @Override
        public void configureJdbi() {
            bindPlugin().toInstance(new SqlObjectPlugin());
        }

    }

}
