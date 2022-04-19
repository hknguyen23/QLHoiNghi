package ql.hoinghi.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import ql.hoinghi.DAO.DiaDiemDAO;
import ql.hoinghi.models.DiaDiem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QLDiaDiemController implements Initializable {
    public TextField tenDiaDiem;
    public TextField diaChi;
    public TextField sucChua;
    public Text alertText;
    public Button addButton;
    public Button cancelButton;
    public Button saveButton;
    public TableView<DiaDiem> diaDiemTableView = new TableView<>();

    private Stage qlDiaDiemStage;
    private int min = 1;    // Sức chứa tối thiểu của địa điểm
    private DiaDiem selectedDiaDiem;

    private ObservableList<DiaDiem> data;
    public List<DiaDiem> dsDiaDiem;

    private DiaDiemDAO diaDiemDAO = new DiaDiemDAO();

    private void createTableView() {
        data = FXCollections.observableArrayList(dsDiaDiem);

        // Tạo lần lượt các cột cho bảng
        TableColumn<DiaDiem, String> tenDiaDiemCol = new TableColumn<>("Tên địa điểm");
        TableColumn<DiaDiem, String> diaChiCol = new TableColumn<>("Địa chỉ");
        TableColumn<DiaDiem, Integer> sucChuaCol = new TableColumn<>("Sức Chứa");
        TableColumn<DiaDiem, DiaDiem> seeDetailsButtonCol = new TableColumn<>("Xem thông tin");

        tenDiaDiemCol.setMinWidth(225);
        diaChiCol.setMinWidth(225);
        sucChuaCol.setMinWidth(80);
        seeDetailsButtonCol.setMinWidth(100);

        sucChuaCol.setStyle("-fx-alignment: CENTER;");
        seeDetailsButtonCol.setStyle("-fx-alignment: CENTER;");

        tenDiaDiemCol.setCellValueFactory(new PropertyValueFactory<>("tenDiaDiem"));
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        sucChuaCol.setCellValueFactory(new PropertyValueFactory<>("sucChua"));
        seeDetailsButtonCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        seeDetailsButtonCol.setCellFactory(param -> new TableCell<DiaDiem, DiaDiem>() {
            private final Button button = new Button("Xem");
            @Override
            protected void updateItem(DiaDiem diaDiem, boolean empty) {
                super.updateItem(diaDiem, empty);
                if (diaDiem == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    qlDiaDiemStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    selectedDiaDiem = diaDiem;

                    min = diaDiemDAO.getMinSucChuaCoTheSuaDuoc(diaDiem.getIdDiaDiem());
                    if (min == 0) {
                        min = 1;
                    }

                    tenDiaDiem.setText(diaDiem.getTenDiaDiem());
                    diaChi.setText(diaDiem.getDiaChi());
                    sucChua.setText(Integer.toString(diaDiem.getSucChua()));
                    alertText.setText("Sức chứa tối thiểu của địa điểm là " + min);

                    addButton.setDisable(true);
                    saveButton.setDisable(false);
                });
            }
        });


        diaDiemTableView.getItems().addAll(data);

        diaDiemTableView.getColumns().addAll(tenDiaDiemCol, diaChiCol, sucChuaCol, seeDetailsButtonCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dsDiaDiem = diaDiemDAO.getAllDiaDiem();
        createTableView();
    }

    private void updateView() {
        dsDiaDiem = diaDiemDAO.getAllDiaDiem();
        data = FXCollections.observableArrayList(dsDiaDiem);
        diaDiemTableView.getItems().clear();
        diaDiemTableView.getItems().addAll(data);
    }

    private void resetValues() {
        tenDiaDiem.setText("");
        diaChi.setText("");
        sucChua.setText("");
        alertText.setText("Sức chứa tối thiểu của địa điểm là 1");
        min = 1;
    }

    private int checkInput() {
        if (tenDiaDiem.getText().equals("") || diaChi.getText().equals("") || sucChua.getText().equals("")) {
            return -1;
        }
        else if (!sucChua.getText().matches("[0-9]*")) {
            return -2;
        }
        else if (Integer.parseInt(sucChua.getText()) < min) {
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
            alert.setTitle("Sức chứa không hợp lệ");
            alert.setContentText("Sức chứa phải là 1 con số");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sức chứa không phù hợp");
            alert.setContentText("Sức chứa tối thiểu của địa điểm là " + min);
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
            diaDiemDAO.updateDiaDiem(selectedDiaDiem.getIdDiaDiem(), tenDiaDiem.getText(), diaChi.getText(),
                    Integer.parseInt(sucChua.getText()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chỉnh sửa thành công");
            alert.setContentText("Đã chỉnh sửa thông tin của địa điểm");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            resetValues();

            saveButton.setDisable(true);
            addButton.setDisable(false);

            updateView();
        }
    }

    public void cancelButtonClicked(ActionEvent actionEvent) {
        resetValues();
        addButton.setDisable(false);
        saveButton.setDisable(true);
    }

    public void addButtonClicked(ActionEvent actionEvent) {
        int check = checkInput();
        if (check != 0) {
            showAlert(check);
        }
        else {
            diaDiemDAO.addDiaDiem(tenDiaDiem.getText(), diaChi.getText(), Integer.parseInt(sucChua.getText()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thêm thành công");
            alert.setContentText("Đã thêm mới 1 địa điểm");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            resetValues();

            updateView();
        }
    }
}
