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
            App.controller.soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + soNguoiThamDuHoiNghi + "/" +
                    selectedHoiNghi.getSoNguoiThamDuToiDa());
        }

        if (App.controller.cardView != null) {
            for (int i = 0; i < App.controller.cardView.getChildren().size(); i++) {
                Button button = (Button)App.controller.cardView.getChildren().get(i);
                if (button.getId().equals(Integer.toString(selectedHoiNghi.getIdHoiNghi()))) {
                    GridPane gridPane = (GridPane)button.getGraphic();
                    Label soNguoiThamDuHienTai = (Label)gridPane.getChildren().get(3);

                    soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + soNguoiThamDuHoiNghi + "/" +
                            selectedHoiNghi.getSoNguoiThamDuToiDa());

                    break;
                }
            }
        }

        App.controller.setDangKyButton();
    }

    private void createTableView() {
        data = FXCollections.observableArrayList(dschiTiet);

        // T???o l???n l?????t c??c c???t cho b???ng
        TableColumn<Object[], String> tenNguoiDungCol = new TableColumn<>("T??n ng?????i d??ng");
        TableColumn<Object[], String> usernameCol = new TableColumn<>("Username");
        TableColumn<Object[], String> emailCol = new TableColumn<>("Email");
        TableColumn<Object[], String> trangThaiCol = new TableColumn<>("T??nh tr???ng");
        TableColumn<Object[], Object[]> yesCol = new TableColumn<>("?????ng ??");
        TableColumn<Object[], Object[]> noCol = new TableColumn<>("C???m");
        TableColumn<Object[], Object[]> deleteCol = new TableColumn<>("X??a");

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
        trangThaiCol.setCellValueFactory(cell -> new SimpleStringProperty((cell.getValue()[4]).equals(2) ? "??ang ch??? duy???t" : "B??? c???m"));

        yesCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        yesCol.setCellFactory(param -> new TableCell<Object[], Object[]>() {
            private final Button button = new Button("?????ng ??");
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
                        alert.setTitle("Duy???t th???t b???i");
                        alert.setContentText("???? ????? s??? ng?????i tham d??? h???i ngh???");
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
                    alert.setTitle("Duy???t th??nh c??nng");
                    alert.setContentText("???? cho ph??p ng?????i d??ng " + obj[2] + " tham gia h???i ngh???: " + selectedHoiNghi.getTenHoiNghi());
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
            private final Button button = new Button("C???m");
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
                    alert.setTitle("Duy???t th??nh c??nng");
                    alert.setContentText("???? c???m ng?????i d??ng " + obj[2] + " tham gia h???i ngh???: " + selectedHoiNghi.getTenHoiNghi());
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
            private final Button button = new Button("X??a");
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
                    alert.setTitle("X??a th??nh c??nng");
                    alert.setContentText("???? x??a y??u c???u xin tham d??? h???i ngh??? " + selectedHoiNghi.getTenHoiNghi() +
                            " c???a ng?????i d??ng " + obj[2]);
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
            alert.setTitle("Duy???t th???t b???i");
            alert.setContentText("S??? l?????ng duy???t v?????t qu?? s??? ng?????i tham d??? t???i ??a");
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
        alert.setTitle("Duy???t th??nh c??nng");
        alert.setContentText("???? cho ph??p t???t c??? ng?????i d??ng tham gia h???i ngh???: " + selectedHoiNghi.getTenHoiNghi());
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
        alert.setTitle("Duy???t th??nh c??nng");
        alert.setContentText("???? c???m t???t c??? ng?????i d??ng tham gia h???i ngh???: " + selectedHoiNghi.getTenHoiNghi());
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
        alert.setTitle("X??a th??nh c??nng");
        alert.setContentText("???? x??a t???t c??? y??u c???u xin tham d??? h???i ngh??? " + selectedHoiNghi.getTenHoiNghi());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        updateView();

        updateHomeView();
    }
}
