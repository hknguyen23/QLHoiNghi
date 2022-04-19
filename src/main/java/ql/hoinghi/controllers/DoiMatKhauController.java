package ql.hoinghi.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import ql.hoinghi.App;
import ql.hoinghi.DAO.NguoiDungDAO;
import ql.hoinghi.utils.PasswordUtils;

public class DoiMatKhauController {
    public TextField oldPass;
    public TextField newPass;
    public TextField reEnterPass;
    public Button confirmButton;

    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    private int checkInput() {
        if (oldPass.getText().equals("") || newPass.getText().equals("") || reEnterPass.getText().equals("")) {
            return -1;
        }
        else if (!newPass.getText().equals(reEnterPass.getText())) {
            return -2;
        }
        else if (nguoiDungDAO.verifyLogin(App.nguoiDungHienTai.getUsername(), oldPass.getText()) <= 0) {
            return -3;
        }
        return 0;
    }

    private void showAlert(int check) {
        if (check == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Các vùng nhập liệu không hợp lệ");
            alert.setContentText("Các vùng nhập liệu không thể bỏ trống");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mật khẩu không khớp");
            alert.setContentText("Mật khẩu mới và mật khẩu nhập lại không giống nhau");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mật khẩu hiện tại không đúng");
            alert.setContentText("Mật khẩu hiện tại không hợp lệ, vui lòng kiểm tra lại");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    public void confirmButtonClicked(ActionEvent actionEvent) {
        int check = checkInput();
        if (check != 0) {
            showAlert(check);
        }
        else {
            String hashPass = PasswordUtils.hash(newPass.getText());
            nguoiDungDAO.updatePassword(App.nguoiDungHienTai.getIdNguoiDung(), hashPass);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chúc mừng!!!");
            alert.setContentText("Đã thay đổi mật khẩu thành công");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            oldPass.setText("");
            newPass.setText("");
            reEnterPass.setText("");
        }
    }
}
