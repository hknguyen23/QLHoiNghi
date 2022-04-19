package ql.hoinghi.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import ql.hoinghi.App;
import ql.hoinghi.DAO.ChiTietHoiNghiDAO;
import ql.hoinghi.DAO.HoiNghiDAO;
import ql.hoinghi.models.HoiNghi;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DuyetThamDuController implements Initializable {
    public ComboBox<String> hoiNghiComboBox = new ComboBox<>();
    public TableView<Object[]> chiTietHoiNghiTableView = new TableView<>();
    public Button submitAllButton;
    public Button banAllButton;
    public Button deleteAllButton;

    private ObservableList<Object[]> data;
    private List<Object[]> dschiTiet;
    private ObservableList<String> dsHoiNghiData;
    private List<String> dsHoiNghi;

    private HoiNghi selectedHoiNghi;
    private Stage duyetThamDuStage;
    private int soNguoiThamDuHoiNghi;

    private ChiTietHoiNghiDAO chiTietHoiNghiDAO = new ChiTietHoiNghiDAO();
    private HoiNghiDAO hoiNghiDAO = new HoiNghiDAO();

    private void updateView() {
        dschiTiet = chiTietHoiNghiDAO.getAllNguoiDungChoDuyetByIDHoiNghi(selectedHoiNghi.getIdHoiNghi());
        data = FXCollections.observableArrayList(dschiTiet);
        chiTietHoiNghiTableView.getItems().clear();
        chiTietHoiNghiTableView.getItems().addAll(data);
    }

    private void updateHomeView() {
        soNguoiThamDuHoiNghi = hoiNghiDAO.getSoNguoiThamDuHoiNghi(selectedHoiNghi.getIdHoiNghi());

        App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
        App.listHoiNghiCuaNguoiDung = chiTietHoiNghiDAO.getAllHoinghiByIDNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
        App.controller.listView.getItems().clear();
        App.controller.listView.getItems().addAll(App.listHoiNghiWithSoLuong);

        int id = App.controller.currentHoiNghi == null ? 0 : (int)App.controller.currentHoiNghi[0];
        if (id == selectedHoiNghi.getIdHoiNghi()) {
            App.controller.soNguoiThamDuHienTai.setText("Số người tham dự hiện tại: " + soNguoiThamDuHoiNghi + "/" +
                    selectedHoiNghi.getSoNguoiThamDuToiDa());
        }

        if (App.controller.cardView != null) {
            for (int i = 0; i < App.controller.cardView.getChildren().size(); i++) {
                Button button = (Button)App.controller.cardView.getChildren().get(i);
                if (button.getId().equals(Integer.toString(selectedHoiNghi.getIdHoiNghi()))) {
                    GridPane gridPane = (GridPane)button.getGraphic();
                    Label soNguoiThamDuHienTai = (Label)gridPane.getChildren().get(3);

                    soNguoiThamDuHienTai.setText("Số người tham dự hiện tại: " + soNguoiThamDuHoiNghi + "/" +
                            selectedHoiNghi.getSoNguoiThamDuToiDa());

                    break;
                }
            }
        }

        App.controller.setDangKyButton();
    }

    private void createTableView() {
        data = FXCollections.observableArrayList(dschiTiet);

        // Tạo lần lượt các cột cho bảng
        TableColumn<Object[], String> tenNguoiDungCol = new TableColumn<>("Tên người dùng");
        TableColumn<Object[], String> usernameCol = new TableColumn<>("Username");
        TableColumn<Object[], String> emailCol = new TableColumn<>("Email");
        TableColumn<Object[], String> trangThaiCol = new TableColumn<>("Tình trạng");
        TableColumn<Object[], Object[]> yesCol = new TableColumn<>("Đồng ý");
        TableColumn<Object[], Object[]> noCol = new TableColumn<>("Cấm");
        TableColumn<Object[], Object[]> deleteCol = new TableColumn<>("Xóa");

        tenNguoiDungCol.setMinWidth(140);
        usernameCol.setMinWidth(100);
        emailCol.setMinWidth(140);
        trangThaiCol.setMinWidth(100);
        yesCol.setMinWidth(70);
        noCol.setMinWidth(50);
        deleteCol.setMinWidth(50);

        trangThaiCol.setStyle("-fx-alignment: CENTER;");
        yesCol.setStyle("-fx-alignment: CENTER;");
        noCol.setStyle("-fx-alignment: CENTER;");
        deleteCol.setStyle("-fx-alignment: CENTER;");

        tenNguoiDungCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[1]));
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[2]));
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[3]));
        trangThaiCol.setCellValueFactory(cell -> new SimpleStringProperty((cell.getValue()[4]).equals(2) ? "Đang chờ duyệt" : "Bị cấm"));

        yesCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        yesCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("Đồng ý");
            @Override
            protected void updateItem(Object[] obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);

                button.setOnAction(event -> {
                    if (selectedHoiNghi.getSoNguoiThamDuToiDa() == soNguoiThamDuHoiNghi) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Duyệt thất bại");
                        alert.setContentText("Đã đủ số người tham dự hội nghị");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                        return;
                    }

                    duyetThamDuStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    chiTietHoiNghiDAO.capNhatTrangThai(selectedHoiNghi.getIdHoiNghi(), (int)obj[0], 1);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Duyệt thành cônng");
                    alert.setContentText("Đã cho phép người dùng " + obj[2] + " tham gia hội nghị: " + selectedHoiNghi.getTenHoiNghi());
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                    updateView();

                    updateHomeView();
                });
            }
        });

        noCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        noCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("Cấm");
            @Override
            protected void updateItem(Object[] obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);

                button.setOnAction(event -> {
                    duyetThamDuStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    chiTietHoiNghiDAO.capNhatTrangThai(selectedHoiNghi.getIdHoiNghi(), (int)obj[0], 3);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Duyệt thành cônng");
                    alert.setContentText("Đã cấm người dùng " + obj[2] + " tham gia hội nghị: " + selectedHoiNghi.getTenHoiNghi());
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                    updateView();

                    updateHomeView();
                });
            }
        });

        deleteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("Xóa");
            @Override
            protected void updateItem(Object[] obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);

                button.setOnAction(event -> {
                    duyetThamDuStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    chiTietHoiNghiDAO.huyDangKy(selectedHoiNghi.getIdHoiNghi(), (int)obj[0]);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Xóa thành cônng");
                    alert.setContentText("Đã xóa yêu cầu xin tham dự hội nghị " + selectedHoiNghi.getTenHoiNghi() +
                            " của người dùng " + obj[2]);
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                    updateView();

                    updateHomeView();
                });
            }
        });

        chiTietHoiNghiTableView.getItems().addAll(data);

        chiTietHoiNghiTableView.getColumns().addAll(tenNguoiDungCol, usernameCol, emailCol, trangThaiCol, yesCol, noCol, deleteCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dschiTiet = chiTietHoiNghiDAO.getAllNguoiDungChoDuyetByIDHoiNghi(1);
        soNguoiThamDuHoiNghi = hoiNghiDAO.getSoNguoiThamDuHoiNghi(1);
        dsHoiNghi = hoiNghiDAO.getTenHoinghiCol();
        dsHoiNghiData = FXCollections.observableArrayList(dsHoiNghi);
        hoiNghiComboBox.getItems().addAll(dsHoiNghiData);
        hoiNghiComboBox.getSelectionModel().select(0);
        selectedHoiNghi = hoiNghiDAO.getOneById(1);

        hoiNghiComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (newValue != null) {
                    hoiNghiComboBox.setValue(newValue);
                    int id = hoiNghiComboBox.getSelectionModel().getSelectedIndex() + 1;
                    selectedHoiNghi = hoiNghiDAO.getOneById(id);
                    soNguoiThamDuHoiNghi = hoiNghiDAO.getSoNguoiThamDuHoiNghi(selectedHoiNghi.getIdHoiNghi());

                    updateView();
                }
            }
        });

        createTableView();
    }

    public void submitAllButtonClicked(ActionEvent actionEvent) {
        if (dschiTiet.size() == 0) {
            return;
        }

        if (selectedHoiNghi.getSoNguoiThamDuToiDa() < soNguoiThamDuHoiNghi + dschiTiet.size()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Duyệt thất bại");
            alert.setContentText("Số lượng duyệt vượt quá số người tham dự tối đa");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
            return;
        }

        for (Object[] obj : dschiTiet) {
            chiTietHoiNghiDAO.capNhatTrangThai(selectedHoiNghi.getIdHoiNghi(), (int)obj[0], 1);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Duyệt thành cônng");
        alert.setContentText("Đã cho phép tất cả người dùng tham gia hội nghị: " + selectedHoiNghi.getTenHoiNghi());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        updateView();

        updateHomeView();
    }

    public void banAllButtonClicked(ActionEvent actionEvent) {
        if (dschiTiet.size() == 0) {
            return;
        }

        for (Object[] obj : dschiTiet) {
            chiTietHoiNghiDAO.capNhatTrangThai(selectedHoiNghi.getIdHoiNghi(), (int)obj[0], 3);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Duyệt thành cônng");
        alert.setContentText("Đã cấm tất cả người dùng tham gia hội nghị: " + selectedHoiNghi.getTenHoiNghi());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        updateView();

        updateHomeView();
    }

    public void deleteAllButtonClicked(ActionEvent actionEvent) {
        if (dschiTiet.size() == 0) {
            return;
        }

        for (Object[] obj : dschiTiet) {
            chiTietHoiNghiDAO.huyDangKy(selectedHoiNghi.getIdHoiNghi(), (int)obj[0]);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa thành cônng");
        alert.setContentText("Đã xóa tất cả yêu cầu xin tham dự hội nghị " + selectedHoiNghi.getTenHoiNghi());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        updateView();

        updateHomeView();
    }
}
