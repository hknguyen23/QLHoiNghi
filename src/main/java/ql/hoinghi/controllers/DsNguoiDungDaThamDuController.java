package ql.hoinghi.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import ql.hoinghi.App;
import ql.hoinghi.DAO.ChiTietHoiNghiDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DsNguoiDungDaThamDuController implements Initializable {
    public TableView<Object[]> tableView = new TableView<>();
    public Text tenHoiNghiText;

    private ObservableList<Object[]> data;
    private List<Object[]> dsNguoiDung;

    private ChiTietHoiNghiDAO chiTietHoiNghiDAO = new ChiTietHoiNghiDAO();

    private void createTableView() {
        data = FXCollections.observableArrayList(dsNguoiDung);

        // Tạo lần lượt các cột cho bảng
        TableColumn<Object[], String> tenNguoiDungCol = new TableColumn<>("Tên người dùng");
        TableColumn<Object[], String> usernameCol = new TableColumn<>("Username");
        TableColumn<Object[], String> emailCol = new TableColumn<>("Email");
        TableColumn<Object[], String> gioiTinhCol = new TableColumn<>("Giới tính");

        tenNguoiDungCol.setMinWidth(165);
        usernameCol.setMinWidth(150);
        emailCol.setMinWidth(165);
        gioiTinhCol.setMinWidth(70);

        tenNguoiDungCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[1]));
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[2]));
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[3]));
        gioiTinhCol.setCellValueFactory(cell -> new SimpleStringProperty((String)cell.getValue()[4]));

        tableView.getItems().addAll(data);

        tableView.getColumns().addAll(tenNguoiDungCol, usernameCol, emailCol, gioiTinhCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dsNguoiDung = chiTietHoiNghiDAO.getAllNguoiDungDaThamDuByIDHoiNghi((int)App.controller.currentHoiNghi[0]);
        tenHoiNghiText.setText("Tên hội nghị: " + App.controller.currentHoiNghi[1] + " - Số người: " + dsNguoiDung.size());
        createTableView();
    }
}
