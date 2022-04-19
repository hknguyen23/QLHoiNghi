package ql.hoinghi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ql.hoinghi.App;
import ql.hoinghi.DAO.ChiTietHoiNghiDAO;
import ql.hoinghi.DAO.HoiNghiDAO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class ThongTinHoiNghiController implements Initializable {
    public GridPane mainPane;
    public ImageView hinhAnh;
    public Label tenHoiNghi;
    public Button dangKyButton;
    public Label moTaNgan;
    public TextArea moTaChiTiet;
    public Label thoiGianToChuc;
    public Label tenDiaDiem;
    public Label soNguoiThamDuHienTai;

    public Object[] currentHoiNghi;
    private int soNguoiChoDuyet = 0;
    private ChiTietHoiNghiDAO chiTietHoiNghiDAO = new ChiTietHoiNghiDAO();
    private HoiNghiDAO hoiNghiDAO = new HoiNghiDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentHoiNghi = hoiNghiDAO.getOneHoiNghiWithDiaDiemAndSoLuong((int)App.controller.currentHoiNghi[0]);
        soNguoiChoDuyet = hoiNghiDAO.getSoNguoiDangChoDuyetHoiNghi((int)currentHoiNghi[0]);

        InputStream input = new ByteArrayInputStream((byte[])currentHoiNghi[5]);
        Image img = new Image(input);
        hinhAnh.setImage(img);

        setDangKyButton();

        tenHoiNghi.setText(currentHoiNghi[1].toString());
        moTaNgan.setText(currentHoiNghi[6].toString());
        moTaChiTiet.setText(currentHoiNghi[7].toString());
        thoiGianToChuc.setText("Ngày tổ chức: " + currentHoiNghi[2] + "\tThời gian: " + currentHoiNghi[3] +
                " - " + currentHoiNghi[4]);
        tenDiaDiem.setText("Địa điểm tổ chức: " + currentHoiNghi[10] + "\nĐịa chỉ: " + currentHoiNghi[11]);
        soNguoiThamDuHienTai.setText("Số người tham dự hiện tại: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);
    }

    public void setDangKyButton() {
        if (!App.isLogin) {
            // Nếu quá hạn ngày tổ chức hội nghị
            // new Date() là current date
            if (((Date)currentHoiNghi[2]).before(new Date())) {
                dangKyButton.setText("Quá hạn đăng ký");
                dangKyButton.setDisable(true);
            }
            // Đủ số người tham gia
            else if (Integer.parseInt((String)currentHoiNghi[13]) == (int)currentHoiNghi[8]) {
                dangKyButton.setText("Đủ số lượng");
                dangKyButton.setDisable(true);
            }
            // Đủ số người chờ duyệt
            // Ví dụ: đang có 80/100 người tham gia, có 20 người đang chờ duyệt thì disble nút đăng ký
            else if (Integer.parseInt((String)currentHoiNghi[13]) + soNguoiChoDuyet >= (int)currentHoiNghi[8]) {
                dangKyButton.setText("Đã đủ số lượng chờ duyệt: " + soNguoiChoDuyet);
                dangKyButton.setDisable(true);
            }
            else {
                // Chưa đăng nhập
                dangKyButton.setText("Đăng ký tham dự");
                dangKyButton.setDisable(false);
            }
            dangKyButton.setId("0");
        }
        else {
            // Đã đăng nhập
            Object[] item1 = new Object[] {currentHoiNghi[0], 1};
            Object[] item2 = new Object[] {currentHoiNghi[0], 2};
            Object[] item3 = new Object[] {currentHoiNghi[0], 3};

            // Đã đăng ký tham dự hội nghị
            if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item1))) {
                // Nếu đã đăng ký và hội nghị chưa kết thúc (chưa diễn ra) thì người dùng có thể hủy đăng ký
                if (((Date)currentHoiNghi[2]).before(new Date())) {
                    dangKyButton.setText("Đã đăng ký");
                    dangKyButton.setDisable(true);
                }
                else {
                    dangKyButton.setText("Hủy đăng ký");
                    dangKyButton.setDisable(false);
                    dangKyButton.setId("2");
                }
            }
            // Đang chờ admin duyệt thì người dùng có thể hủy bất cứ lúc nào
            else if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item2))) {
                dangKyButton.setText("Đang chờ admin duyệt ...");
                dangKyButton.setDisable(false);
                dangKyButton.setId("2");
            }
            // Bị cấm tham dự
            else if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item3))) {
                dangKyButton.setText("Bị cấm đăng ký");
                dangKyButton.setDisable(true);
            }
            else {
                // Nếu quá hạn ngày tổ chức hội nghị
                // new Date() là current date
                if (((Date)currentHoiNghi[2]).before(new Date())) {
                    dangKyButton.setText("Quá hạn đăng ký");
                    dangKyButton.setDisable(true);
                }
                else if (Integer.parseInt((String)currentHoiNghi[13]) == (int)currentHoiNghi[8]) {
                    dangKyButton.setText("Đủ số lượng");
                    dangKyButton.setDisable(true);
                }
                else if (Integer.parseInt((String)currentHoiNghi[13]) + soNguoiChoDuyet >= (int)currentHoiNghi[8]) {
                    dangKyButton.setText("Đã đủ số lượng chờ duyệt: " + soNguoiChoDuyet);
                    dangKyButton.setDisable(true);
                }
                else {
                    // Chưa đăng ký tham dự hội nghị này
                    dangKyButton.setText("Đăng ký tham dự");
                    dangKyButton.setDisable(false);
                    dangKyButton.setId("1");
                }
            }
        }
    }

    public void dangKyThamDuHoiNghiButtonClicked(ActionEvent actionEvent) {
        Button button = (Button)actionEvent.getSource();

        // Chưa đăng nhập
        if (button.getId().equals("0")) {
            // Hiển thị Dialog cho người dùng lựa chọn
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            ButtonType signInButton = new ButtonType("Đăng nhập");
            ButtonType signUpButton = new ButtonType("Đăng ký");
            ButtonType cancelButton = new ButtonType("Đóng");

            alert.getButtonTypes().clear();

            alert.getButtonTypes().addAll(signInButton, signUpButton, cancelButton);
            alert.setTitle("Bạn chưa đăng nhập");
            alert.setHeaderText("Để đăng ký tham dự hội nghị, vui lòng chọn các lựa chọn bên dưới");
            alert.setContentText("Nếu đã có tài khoản, chọn đăng nhập, nếu chưa có chọn đăng ký để tạo tài khoản");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == signInButton) {
                try {
                    FXMLLoader loader = App.getFXMLLoader("login");
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 420, 220);
                    Stage stage = new Stage();
                    stage.setTitle("Đăng nhập");
                    stage.setScene(scene);
                    stage.setResizable(false);

                    stage.setOnHiding(e -> {
                        setDangKyButton();
                    });

                    // Wait until Login is complete
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(App.homeStage);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (option.get() == signUpButton) {
                try {
                    FXMLLoader loader = App.getFXMLLoader("signUp");
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 420, 420);
                    Stage stage = new Stage();
                    stage.setTitle("Đăng ký");
                    stage.setScene(scene);
                    stage.setResizable(false);

                    // Wait until this stage is close
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(App.homeStage);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        // Đăng ký
        else if (button.getId().equals("1")) {
            int result = -1;
            // Kiểm tra điều kiện nếu vượt quá số lượng
            if (Integer.parseInt((String)currentHoiNghi[13]) >= (int)currentHoiNghi[8]) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Không thể đăng ký");
                alert.setContentText("Số lượng người tham dự đã đạt mức tối đa");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
            else {
                result = chiTietHoiNghiDAO.dangKy((int)currentHoiNghi[0], App.nguoiDungHienTai.getIdNguoiDung());
                if (result > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Chúc mừng!!!");
                    alert.setContentText("Bạn đã đăng ký tham dự hội nghị thành công, vui lòng chờ admin duyệt");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                    button.setText("Đang chờ admin duyệt ...");
                    button.setDisable(false);
                    button.setId("2");

                    App.listHoiNghiCuaNguoiDung.add(new Object[]{currentHoiNghi[0], 2});
                }
            }
        }
        // Hủy đăng ký
        else if (button.getId().equals("2")) {
            int result = chiTietHoiNghiDAO.huyDangKy((int)currentHoiNghi[0], App.nguoiDungHienTai.getIdNguoiDung());
            if (result > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hủy đăng ký thành công!!!");
                alert.setContentText("Bạn đã hủy đăng ký hội nghị thành công");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });

                button.setText("Đăng ký tham dự");
                button.setDisable(false);
                button.setId("1");

                App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
                App.listHoiNghiCuaNguoiDung = chiTietHoiNghiDAO.getAllHoinghiByIDNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
                currentHoiNghi = hoiNghiDAO.getOneHoiNghiWithDiaDiemAndSoLuong((int)currentHoiNghi[0]);
                App.controller.listView.getItems().clear();
                App.controller.listView.getItems().addAll(App.listHoiNghiWithSoLuong);

                soNguoiThamDuHienTai.setText("Số người tham dự hiện tại: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);

                if (App.controller.cardView != null) {
                    for (int i = 0; i < App.controller.cardView.getChildren().size(); i++) {
                        Button btn = (Button)App.controller.cardView.getChildren().get(i);
                        if (btn.getId().equals(Integer.toString((int)currentHoiNghi[0]))) {
                            GridPane gp = (GridPane)btn.getGraphic();
                            Label lb = (Label)gp.getChildren().get(3);
                            lb.setText("Số người tham dự hiện tại: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);
                            break;
                        }
                    }
                }

            }
        }
    }

    public void listNguoiDungDangKyButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("dsNguoiDungDaThamDu");
        Parent root = loader.load();
        Scene scene = new Scene(root, 620, 420);
        Stage stage = new Stage();
        stage.setTitle("Danh sách người dùng đã tham dự hội nghị");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is close
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }
}
