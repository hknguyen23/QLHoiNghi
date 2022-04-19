package ql.hoinghi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.hibernate.SessionFactory;

import ql.hoinghi.controllers.HomeController;
import ql.hoinghi.models.NguoiDung;
import ql.hoinghi.utils.HibernateUtils;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene scene;
    public static Stage homeStage;
    public static HomeController controller;
    public static boolean isLogin = false;
    public static NguoiDung nguoiDungHienTai = null;

    public static SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

    public static List<Object[]> listHoiNghiWithSoLuong;
    public static List<Object[]> listHoiNghiCuaNguoiDung;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = getFXMLLoader("home");
        Parent root = loader.load();

        controller = loader.getController();

        scene = new Scene(root, 1300, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Trang chủ");
        homeStage = new Stage();
        homeStage = stage;

        stage.show();
    }

    public static FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource("/ql/hoinghi/views/" + fxml + ".fxml"));
    }

    public static void main(String[] args) {
        launch(args);
    }

}