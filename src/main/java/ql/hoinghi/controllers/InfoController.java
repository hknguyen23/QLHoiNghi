package ql.hoinghi.controllers;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ql.hoinghi.App;
import ql.hoinghi.DAO.ChiTietHoiNghiDAO;
import ql.hoinghi.DAO.HoiNghiDAO;
import ql.hoinghi.DAO.NguoiDungDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    public Text userName;
    public TextField name;
    public TextField email;
    public RadioButton maleButton;
    public RadioButton femaleButton;
    public PasswordField userPass;
    public Label soLuongHoiNghiDaDangKy;
    public Button cancelButton;
    public Button saveButton;
    public Button editButton;
    public Text editableAlert;
    public TableView<Object[]> tableView = new TableView<>();

    private Stage infoStage;
    private String oldName;
    private String oldEmail;
    private boolean oldMaleGender;
    private boolean oldFemaleGender;

    private HoiNghiDAO hoiNghiDAO = new HoiNghiDAO();
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private ChiTietHoiNghiDAO chiTietHoiNghiDAO = new ChiTietHoiNghiDAO();

    private List<Object[]> dsHoiNghiDaDangKy;
    private ObservableList<Object[]> data;

    private void createTableView() {
        data = FXCollections.observableArrayList(dsHoiNghiDaDangKy);

        // Tạo lần lượt các cột cho bảng
        TableColumn<Object[], String> tenHoiNghiCol = new TableColumn<>("Tên hội nghị");
        TableColumn<Object[], Date> ngayToChucCol = new TableColumn<>("Ngày tổ chức");
        TableColumn<Object[], Time> gioBatDauCol = new TableColumn<>("Giờ bắt đầu");
        TableColumn<Object[], Time> gioKetThucCol = new TableColumn<>("Giờ kết thúc");
        TableColumn<Object[], String> trangThaiCol = new TableColumn<>("Trạng thái");
        TableColumn<Object[], Object[]> xemChiTietCol = new TableColumn<>("Xem thông tin");
        TableColumn<Object[], Object[]> huyDangKyCol = new TableColumn<>("Hủy đăng ký");

        tenHoiNghiCol.setMinWidth(150);
        ngayToChucCol.setMinWidth(60);
        gioBatDauCol.setMinWidth(60);
        gioKetThucCol.setMinWidth(60);
        trangThaiCol.setMinWidth(70);
        xemChiTietCol.setMinWidth(90);
        huyDangKyCol.setMinWidth(90);

        ngayToChucCol.setStyle("-fx-alignment: CENTER;");
        gioBatDauCol.setStyle("-fx-alignment: CENTER;");
        gioKetThucCol.setStyle("-fx-alignment: CENTER;");
        trangThaiCol.setStyle("-fx-alignment: CENTER;");
        xemChiTietCol.setStyle("-fx-alignment: CENTER;");
        huyDangKyCol.setStyle("-fx-alignment: CENTER;");

        tenHoiNghiCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[0]));
        ngayToChucCol.setCellValueFactory(cell -> new SimpleObjectProperty<Date>((Date)cell.getValue()[1]));
        gioBatDauCol.setCellValueFactory(cell -> new SimpleObjectProperty<Time>((Time)cell.getValue()[2]));
        gioKetThucCol.setCellValueFactory(cell -> new SimpleObjectProperty<Time>((Time)cell.getValue()[3]));
        trangThaiCol.setCellValueFactory(cell -> new SimpleStringProperty((int)cell.getValue()[5] == 1 ? "Đã đăng ký" : "Đang chờ duyệt"));

        xemChiTietCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        xemChiTietCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("Xem");
            @Override
            protected void updateItem(Object[] obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    infoStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    try {
                        App.controller.currentHoiNghi = hoiNghiDAO.getOneHoiNghiWithDiaDiemAndSoLuong((int)obj[4]);
                        seeDetails();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        huyDangKyCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        huyDangKyCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("Hủy");
            @Override
            protected void updateItem(Object[] obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    infoStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    // Nếu hội nghị đã diễn ra rồi thì không thể hủy đăng ký
                    if (((Date)obj[1]).before(new Date())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Hủy đăng ký hội nghị thất bại");
                        alert.setContentText("Hội nghị này đã kết thúc, không thể hủy đăng ký");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                    }
                    else {
                        chiTietHoiNghiDAO.huyDangKy((int)obj[4], App.nguoiDungHienTai.getIdNguoiDung());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Hủy đăng ký hội nghị thành công");
                        alert.setContentText("Bạn đã hủy đăng ký hội nghị: " + obj[0]);
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });

                        updateView();

                        App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
                        App.listHoiNghiCuaNguoiDung = chiTietHoiNghiDAO.getAllHoinghiByIDNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
                        App.controller.currentHoiNghi = hoiNghiDAO.getOneHoiNghiWithDiaDiemAndSoLuong((int)obj[4]);
                        App.controller.listView.getItems().clear();
                        App.controller.listView.getItems().addAll(App.listHoiNghiWithSoLuong);

                        if ((int)obj[5] == 1) {
                            App.controller.soNguoiThamDuHienTai.setText("Số người tham dự hiện tại: " +
                                    App.controller.currentHoiNghi[13] + "/" + App.controller.currentHoiNghi[8]);

                            if (App.controller.cardView != null) {
                                for (int i = 0; i < App.controller.cardView.getChildren().size(); i++) {
                                    Button btn = (Button)App.controller.cardView.getChildren().get(i);
                                    if (btn.getId().equals(Integer.toString((int)App.controller.currentHoiNghi[0]))) {
                                        GridPane gp = (GridPane)btn.getGraphic();
                                        Label lb = (Label)gp.getChildren().get(3);
                                        lb.setText("Số người tham dự hiện tại: " + App.controller.currentHoiNghi[13] + "/" +
                                                App.controller.currentHoiNghi[8]);
                                        break;
                                    }
                                }
                            }
                        }

                        App.controller.setDangKyButton();
                    }
                });
            }
        });

        tableView.getItems().addAll(data);

        tableView.getColumns().addAll(tenHoiNghiCol, ngayToChucCol, gioBatDauCol, gioKetThucCol, trangThaiCol, xemChiTietCol, huyDangKyCol);
    }

    private void setView() {
        dsHoiNghiDaDangKy = hoiNghiDAO.getAllHoiNghiByIdNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
        userName.setText(App.nguoiDungHienTai.getUsername());
        name.setText(App.nguoiDungHienTai.getTenNguoiDung());
        email.setText(App.nguoiDungHienTai.getEmail());
        soLuongHoiNghiDaDangKy.setText("Số hội nghị đã đăng ký tham dự: " + dsHoiNghiDaDangKy.size());
        oldName = userName.getText();
        oldEmail = email.getText();
        oldMaleGender = maleButton.isSelected();
        oldFemaleGender = femaleButton.isSelected();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        maleButton.setToggleGroup(group);
        femaleButton.setToggleGroup(group);
        if (App.nguoiDungHienTai.getGioiTinh().equals("Nam")) {
            maleButton.setSelected(true);
        }
        else {
            femaleButton.setSelected(true);
        }
        setView();
        createTableView();
    }

    private void updateView() {
        dsHoiNghiDaDangKy = hoiNghiDAO.getAllHoiNghiByIdNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
        data = FXCollections.observableArrayList(dsHoiNghiDaDangKy);
        tableView.getItems().clear();
        tableView.getItems().addAll(data);
        soLuongHoiNghiDaDangKy.setText("Số hội nghị đã đăng ký tham dự: " + dsHoiNghiDaDangKy.size());
    }

    private void seeDetails() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("thongTinHoiNghi");
        Parent root = loader.load();
        Scene scene = new Scene(root, 1020, 620);
        Stage stage = new Stage();
        stage.setTitle("Thông tin chi tiết của hội nghị");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until stage is close
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(infoStage);
        stage.show();

        stage.setOnCloseRequest(e -> {
            updateView();
        });
    }

    private void resetValues() {
        name.setEditable(false);
        email.setEditable(false);
        userPass.setEditable(false);
        editableAlert.setVisible(false);

        maleButton.setDisable(true);
        femaleButton.setDisable(true);
        saveButton.setDisable(true);
        editButton.setDisable(false);
        cancelButton.setDisable(true);

        userPass.setText("");
    }

    public void editButtonClicked(ActionEvent actionEvent) {
        name.setEditable(true);
        email.setEditable(true);
        userPass.setEditable(true);
        editableAlert.setVisible(true);

        maleButton.setDisable(false);
        femaleButton.setDisable(false);
        saveButton.setDisable(false);
        editButton.setDisable(true);
        cancelButton.setDisable(false);

        oldName = name.getText();
        oldEmail = email.getText();
        oldMaleGender = maleButton.isSelected();
        oldFemaleGender = femaleButton.isSelected();
    }

    public void cancelButtonClicked(ActionEvent actionEvent) {
        resetValues();

        name.setText(oldName);
        email.setText(oldEmail);
        maleButton.setSelected(oldMaleGender);
        femaleButton.setSelected(oldFemaleGender);
    }

    private int checkInput() {
        // Ô nhập liệu rỗng
        if (name.getText().equals("") || email.getText().equals("") || userPass.getText().equals("")) {
            return -1;
        }
        // Mật khẩu cũ không khớp
        else if (nguoiDungDAO.verifyLogin(userName.getText(), userPass.getText()) <= 0) {
            return -2;
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
            alert.setTitle("Mật khẩu hiện tại không đúng");
            alert.setContentText("Mật khẩu hiện tại không hợp lệ, vui lòng kiểm tra lại");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    public void saveButtonClicked(ActionEvent actionEvent) {
        int check = checkInput();
        if (check != 0) {
            showAlert(check);
        }
        else {
            String gender = maleButton.isSelected() ? "Nam" : "Nữ";
            nguoiDungDAO.updateWithoutNewPassword(App.nguoiDungHienTai.getIdNguoiDung(), name.getText(), email.getText(), gender);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chúc mừng!!!");
            alert.setContentText("Đã cập nhật thông tin thành công");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            resetValues();

            App.nguoiDungHienTai = nguoiDungDAO.getOneById(App.nguoiDungHienTai.getIdNguoiDung());
            setView();
        }
    }

    public void changePasswordButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("doiMatKhau");
        Parent root = loader.load();
        Scene scene = new Scene(root, 420, 320);
        Stage stage = new Stage();
        stage.setTitle("Đổi mật khẩu");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is close
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }
}
