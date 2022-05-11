package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.inject.Inject;
import javax.inject.Named;

import org.jdbi.v3.core.Jdbi;

import db.UserDao;
import model.User;

public class TableViewController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    @Named("database")
    private Jdbi jdbi;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Long> id;

    @FXML
    private TableColumn<User, String> username;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        var userDao = jdbi.onDemand(UserDao.class);
        var users = userDao.list();
        ObservableList<User> observableList = FXCollections.observableArrayList();
        observableList.addAll(users);
        tableView.setItems(observableList);
    }

}
