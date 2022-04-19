package ql.hoinghi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import ql.hoinghi.DAO.NguoiDungDAO;
import ql.hoinghi.utils.PasswordUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public Button signUpButton;
    public TextField name;
    public TextField userName;
    public PasswordField userPass;
    public TextField email;
    public RadioButton maleButton;
    public RadioButton femaleButton;

    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        maleButton.setToggleGroup(group);
        femaleButton.setToggleGroup(group);
        maleButton.setSelected(true);
    }

    private int signUp() {
        if (name.getText().equals("") || email.getText().equals("") || userName.getText().equals("") || userPass.getText().equals("")) {
            return -1;
        }

        String hashPass = PasswordUtils.hash(userPass.getText());

        String gender = maleButton.isSelected() ? "Nam" : "Nữ";
        nguoiDungDAO.addOne(name.getText(), userName.getText(), hashPass, email.getText(), gender);

        return 0;
    }

    public void switchToHome(ActionEvent actionEvent) {
        if (signUp() == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tên người dùng hoặc tên tài khoản hoặc mật khẩu hoặc email không hợp lệ");
            alert.setContentText("Tên người dùng hoặc tên tài khoản hoặc mật khẩu hoặc email không thể bỏ trống");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Đăng ký thành công!!!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            Stage stage = (Stage)signUpButton.getScene().getWindow();
            stage.close();
        }
    }
}
