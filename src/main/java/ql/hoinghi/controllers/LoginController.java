package ql.hoinghi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import ql.hoinghi.App;
import ql.hoinghi.DAO.NguoiDungDAO;
import ql.hoinghi.models.NguoiDung;

public class LoginController {
    public TextField userName;
    public PasswordField userPass;
    public Button loginButton;

    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    private int validate() {
        if (userName.getText().equals("") || userPass.getText().equals("")) {
            return -1;
        }

        int id = nguoiDungDAO.verifyLogin(userName.getText(), userPass.getText());
        NguoiDung nguoiDung = null;
        if (id != -1) {
            nguoiDung = nguoiDungDAO.getOneById(id);
        }

        if (nguoiDung != null) {
            if (nguoiDung.isBanned()) {
                return -2;
            }
            App.nguoiDungHienTai = nguoiDung;
            return 0;
        }
        else return -3;
    }

    @FXML
    private void switchToHome() {
        int check = validate();
        if (check == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tên tài khoản hoặc mật khẩu không hợp lệ");
            alert.setContentText("Tên tài khoản và mật khẩu không thể bỏ trống");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đăng nhập thất bại");
            alert.setContentText("Tài khoản này đã bị cấm đăng nhập");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tên tài khoản hoặc mật khẩu không tồn tại");
            alert.setContentText("Sai tên tài khoản hoặc mật khẩu");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chúc mừng!!!");
            alert.setContentText("Đăng nhập thành công!!!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            App.isLogin = true;
            App.controller.updateView();
            if (App.controller.currentHoiNghi != null) {
                App.controller.setDangKyButton();
            }

            Stage stage = (Stage)loginButton.getScene().getWindow();
            stage.close();
        }
    }
}