package ql.hoinghi.controllers;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import ql.hoinghi.DAO.NguoiDungDAO;
import ql.hoinghi.models.NguoiDung;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class QLNguoiDungController implements Initializable {
    public TextField searchField;
    public Button submitAllButton;
    public TableView<NguoiDung> nguoiDungTableView = new TableView<>();;
    public ComboBox<String> filterComboBox = new ComboBox<>();

    private ObservableList<NguoiDung> data;
    public List<NguoiDung> dsNguoiDung;

    private Stage qlNguoiDungStage;
    private HashMap<Integer, Boolean> mapValues = new HashMap<>();

    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    private void updateView() {
        dsNguoiDung = nguoiDungDAO.getAllNguoiDung();
        data = FXCollections.observableArrayList(dsNguoiDung);
        nguoiDungTableView.getItems().clear();
        nguoiDungTableView.getItems().addAll(data);
    }

    private void createTableView() {
        data = FXCollections.observableArrayList(dsNguoiDung);

        // Tạo lần lượt các cột cho bảng
        TableColumn<NguoiDung, String> tenNguoiDungCol = new TableColumn<>("Tên người dùng");
        TableColumn<NguoiDung, String> usernameCol = new TableColumn<>("Username");
        TableColumn<NguoiDung, String> emailCol = new TableColumn<>("Email");
        TableColumn<NguoiDung, String> gioiTinhCol = new TableColumn<>("Giới tính");
        TableColumn<NguoiDung, NguoiDung> isBannedCol = new TableColumn<>("Cấm");
        TableColumn<NguoiDung, NguoiDung> submitButtonCol = new TableColumn<>("Xác nhận");

        tenNguoiDungCol.setMinWidth(150);
        usernameCol.setMinWidth(100);
        emailCol.setMinWidth(150);
        gioiTinhCol.setMinWidth(70);
        isBannedCol.setMinWidth(60);
        submitButtonCol.setMinWidth(70);

        gioiTinhCol.setStyle("-fx-alignment: CENTER;");
        isBannedCol.setStyle("-fx-alignment: CENTER;");
        submitButtonCol.setStyle("-fx-alignment: CENTER;");

        tenNguoiDungCol.setCellValueFactory(new PropertyValueFactory<>("tenNguoiDung"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        gioiTinhCol.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));

        isBannedCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        isBannedCol.setCellFactory(param -> new TableCell<NguoiDung, NguoiDung>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(NguoiDung nguoiDung, boolean empty) {
                super.updateItem(nguoiDung, empty);
                if (nguoiDung == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(checkBox);
                checkBox.setSelected(nguoiDung.isBanned());

                checkBox.setOnAction(event -> {
                    mapValues.put(nguoiDung.getIdNguoiDung(), checkBox.isSelected());
                });
            }
        });

        submitButtonCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        submitButtonCol.setCellFactory(param -> new TableCell<NguoiDung, NguoiDung>() {
            private final Button button = new Button("OK");
            @Override
            protected void updateItem(NguoiDung nguoiDung, boolean empty) {
                super.updateItem(nguoiDung, empty);
                if (nguoiDung == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    qlNguoiDungStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    if (!mapValues.isEmpty()) {
                        nguoiDungDAO.setBannedStatus(nguoiDung.getIdNguoiDung(), mapValues.get(nguoiDung.getIdNguoiDung()));

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thay đổi thành công");
                        alert.setContentText("Đã " + (mapValues.get(nguoiDung.getIdNguoiDung()) ? "cấm " : "cho phép ") +
                                nguoiDung.getUsername() + " đăng nhập");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });

                        updateView();
                    }
                });
            }
        });


        nguoiDungTableView.getItems().addAll(data);

        nguoiDungTableView.getColumns().addAll(tenNguoiDungCol, usernameCol, emailCol, gioiTinhCol, isBannedCol, submitButtonCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dsNguoiDung = nguoiDungDAO.getAllNguoiDung();

        // Lọc theo 2 tiêu chí: Giới tính và Tình trạng đăng nhập có bị cấm hay không
        filterComboBox.getItems().addAll("Tất cả", "Nam", "Nữ", "Bị cấm", "Không bị cấm");
        filterComboBox.getSelectionModel().select(0);

        filterComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            filterComboBox.setValue(newValue);
                        }
                    });

                    if (newValue.equals("Tất cả")) {
                        dsNguoiDung = nguoiDungDAO.getAllNguoiDung();
                    }
                    else if (newValue.equals("Nam")) {
                        dsNguoiDung = nguoiDungDAO.getAllNguoiDungNam();
                    }
                    else if (newValue.equals("Nữ")) {
                        dsNguoiDung = nguoiDungDAO.getAllNguoiDungNu();
                    }
                    else if (newValue.equals("Bị cấm")) {
                        dsNguoiDung = nguoiDungDAO.getAllNguoiDungBiCam();
                    }
                    else if (newValue.equals("Không bị cấm")) {
                        dsNguoiDung = nguoiDungDAO.getAllNguoiDungKhongBiCam();
                    }

                    data = FXCollections.observableArrayList(dsNguoiDung);
                    nguoiDungTableView.getItems().clear();
                    nguoiDungTableView.getItems().addAll(data);
                }
            }
        });

        createTableView();
    }

    public void submitAllButtonClicked(ActionEvent actionEvent) {
        mapValues.forEach((key, value) -> nguoiDungDAO.setBannedStatus(key, value));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thay đổi thành công");
        alert.setContentText("Cập nhật thành công tình trạng đăng nhập cho " + mapValues.size() + " người dùng");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        mapValues.clear();

        updateView();
    }
}
