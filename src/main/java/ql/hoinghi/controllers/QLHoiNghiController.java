package ql.hoinghi.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import ql.hoinghi.App;
import ql.hoinghi.DAO.DiaDiemDAO;
import ql.hoinghi.DAO.HoiNghiDAO;
import ql.hoinghi.models.DiaDiem;
import ql.hoinghi.models.HoiNghi;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class QLHoiNghiController implements Initializable {
    public TextField tenHoiNghi;
    public DatePicker ngayToChuc;
    public ComboBox<String> diaDiemComboBox = new ComboBox<>();
    public TextField gioBatDau;
    public TextField gioKetThuc;
    public TextField moTaNgan;
    public TextArea moTaChiTiet;
    public ImageView hinhAnh;
    public Button chooseImageButton;
    public Button deleteImageButton;
    public TextField soNguoiThamDuToiDa;
    public Button addButton;
    public Button cancelButton;
    public Button saveButton;
    public Text alertText1;
    public Text alertText2;
    public TableView<HoiNghi> hoiNghiTableView = new TableView<>();

    private HoiNghiDAO hoiNghiDAO = new HoiNghiDAO();
    private DiaDiemDAO diaDiemDAO = new DiaDiemDAO();
    private ObservableList<HoiNghi> data;
    private ObservableList<String> dsDiaDiemData;
    private List<HoiNghi> dsHoiNghi;
    private List<String> dsDiaDiem;
    private List<HoiNghi> dsHoiNghiCungThoiDiemToChuc;

    private HoiNghi selectedHoiNghi;
    private DiaDiem selectedDiaDiem;
    private Stage qlHoiNghiStage;
    private int soNguoiThamDuHoiNghi = 0;
    private boolean isNewImageChosen = false;

    private void createTableView() {
        data = FXCollections.observableArrayList(dsHoiNghi);

        // T???o l???n l?????t c??c c???t cho b???ng
        TableColumn<HoiNghi, String> tenHoiNghiCol = new TableColumn<>("T??n h???i ngh???");
        TableColumn<HoiNghi, Date> ngayToChucCol = new TableColumn<>("Ng??y t??? ch???c");
        TableColumn<HoiNghi, Time> gioBatDauCol = new TableColumn<>("Gi??? b???t ?????u");
        TableColumn<HoiNghi, Time> gioKetThucCol = new TableColumn<>("Gi??? k???t th??c");
        TableColumn<HoiNghi, String> moTaNganCol = new TableColumn<>("M?? t??? ng???n");
        TableColumn<HoiNghi, HoiNghi> seeDetailsButtonCol = new TableColumn<>("Xem th??ng tin");

        tenHoiNghiCol.setMinWidth(200);
        ngayToChucCol.setMinWidth(80);
        gioBatDauCol.setMinWidth(80);
        gioKetThucCol.setMinWidth(80);
        moTaNganCol.setMinWidth(150);
        seeDetailsButtonCol.setMinWidth(100);

        ngayToChucCol.setStyle("-fx-alignment: CENTER;");
        gioBatDauCol.setStyle("-fx-alignment: CENTER;");
        gioKetThucCol.setStyle("-fx-alignment: CENTER;");
        seeDetailsButtonCol.setStyle("-fx-alignment: CENTER;");

        tenHoiNghiCol.setCellValueFactory(new PropertyValueFactory<>("tenHoiNghi"));
        ngayToChucCol.setCellValueFactory(new PropertyValueFactory<>("ngayToChuc"));
        gioBatDauCol.setCellValueFactory(new PropertyValueFactory<>("gioBatDau"));
        gioKetThucCol.setCellValueFactory(new PropertyValueFactory<>("gioKetThuc"));
        moTaNganCol.setCellValueFactory(new PropertyValueFactory<>("moTaNgan"));
        seeDetailsButtonCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        ngayToChucCol.setCellFactory(column -> {
            TableCell<HoiNghi, Date> cell = new TableCell<HoiNghi, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    }
                    else {
                        if (item != null) {
                            this.setText(format.format(item));
                        }
                    }
                }
            };
            return cell;
        });

        seeDetailsButtonCol.setCellFactory(param -> new TableCell<HoiNghi, HoiNghi>() {
            private final Button button = new Button("Xem");
            @Override
            protected void updateItem(HoiNghi hoiNghi, boolean empty) {
                super.updateItem(hoiNghi, empty);
                if (hoiNghi == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    qlHoiNghiStage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    isNewImageChosen = false;

                    selectedHoiNghi = hoiNghi;
                    selectedDiaDiem = hoiNghi.getDiaDiem();

                    soNguoiThamDuHoiNghi = hoiNghiDAO.getSoNguoiThamDuHoiNghi(hoiNghi.getIdHoiNghi());

                    tenHoiNghi.setText(hoiNghi.getTenHoiNghi());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                    ngayToChuc.setValue(LocalDate.parse(hoiNghi.getNgayToChuc().toString(), formatter));

                    gioBatDau.setText(hoiNghi.getGioBatDau().toString());
                    gioKetThuc.setText(hoiNghi.getGioKetThuc().toString());
                    moTaNgan.setText(hoiNghi.getMoTaNgan());
                    moTaChiTiet.setText(hoiNghi.getMoTaChiTiet());
                    hinhAnh.setImage(new Image(new ByteArrayInputStream(hoiNghi.getHinhAnh())));
                    soNguoiThamDuToiDa.setText(Integer.toString(hoiNghi.getSoNguoiThamDuToiDa()));

                    alertText1.setText("S??? ng?????i tham d??? l???n nh???t l?? " + selectedDiaDiem.getSucChua());
                    alertText2.setText("S??? ng?????i ???? ????ng k?? tham gia (s??? ng?????i tham d??? nh??? nh???t c???a h???i ngh???): " +
                            (soNguoiThamDuHoiNghi == 0 ? 1 : soNguoiThamDuHoiNghi));

                    diaDiemComboBox.setValue(selectedDiaDiem.getTenDiaDiem());

                    saveButton.setDisable(false);
                    addButton.setDisable(true);
                });
            }
        });

        hoiNghiTableView.getItems().addAll(data);

        hoiNghiTableView.getColumns().addAll(tenHoiNghiCol, ngayToChucCol, gioBatDauCol, gioKetThucCol, moTaNganCol, seeDetailsButtonCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dsHoiNghi = hoiNghiDAO.getAllHoiNghi();
        dsDiaDiem = diaDiemDAO.getTenDiaDiemCol();
        dsDiaDiemData = FXCollections.observableArrayList(dsDiaDiem);
        diaDiemComboBox.getItems().addAll(dsDiaDiemData);

        diaDiemComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (newValue != null) {
                    diaDiemComboBox.setValue(newValue);
                    int id = diaDiemComboBox.getSelectionModel().getSelectedIndex() + 1;
                    selectedDiaDiem = diaDiemDAO.getOneById(id);
                    if (selectedHoiNghi != null) {
                        selectedHoiNghi.setDiaDiem(selectedDiaDiem);
                    }
                    alertText1.setText("S??? ng?????i tham d??? l???n nh???t l?? " + selectedDiaDiem.getSucChua());
                    alertText2.setText("S??? ng?????i ???? ????ng k?? tham gia (s??? ng?????i tham d??? nh??? nh???t c???a h???i ngh???): " +
                            (soNguoiThamDuHoiNghi == 0 ? 1 : soNguoiThamDuHoiNghi));
                }
            }
        });

        createTableView();

        ngayToChuc.setValue(LocalDate.now());
    }

    public void chooseImageButton(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File file = fc.showOpenDialog(qlHoiNghiStage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            hinhAnh.setImage(image);
            isNewImageChosen = true;
        }
    }

    public void deleteImageButton(ActionEvent actionEvent) {
        hinhAnh.setImage(null);
    }

    private void updateHomeView() {
        App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
        App.controller.listView.getItems().clear();
        App.controller.listView.getItems().addAll(App.listHoiNghiWithSoLuong);

        // V?? d???: n???u ??ang xem h???i ngh??? 1, admin v??o ch???nh s???a th??ng tin h???i ngh??? 1 th?? view ph???i thay ?????i
        int id = App.controller.currentHoiNghi == null ? 0 : (int)App.controller.currentHoiNghi[0];
        if (selectedHoiNghi != null) {
            if (id == selectedHoiNghi.getIdHoiNghi()) {
                App.controller.hinhAnh.setImage(hinhAnh.getImage());
                App.controller.tenHoiNghi.setText(tenHoiNghi.getText());
                App.controller.moTaNgan.setText(moTaNgan.getText());
                App.controller.moTaChiTiet.setText(moTaChiTiet.getText());
                App.controller.thoiGianToChuc.setText("Ng??y t??? ch???c: " + ngayToChuc.getValue() + "\tTh???i gian: " +
                        gioBatDau.getText() + " - " + gioKetThuc.getText());
                App.controller.tenDiaDiem.setText("?????a ??i???m t??? ch???c: " + selectedDiaDiem.getTenDiaDiem() + "\n?????a ch???: " +
                        selectedDiaDiem.getDiaChi());
                App.controller.soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + soNguoiThamDuHoiNghi + "/" +
                        soNguoiThamDuToiDa.getText());
            }

            if (App.controller.cardView != null) {
                for (int i = 0; i < App.controller.cardView.getChildren().size(); i++) {
                    Button button = (Button)App.controller.cardView.getChildren().get(i);
                    if (button.getId().equals(Integer.toString(selectedHoiNghi.getIdHoiNghi()))) {
                        GridPane gridPane = (GridPane)button.getGraphic();
                        ImageView imageView = (ImageView)gridPane.getChildren().get(0);
                        Label tenHoiNghiLabel = (Label)gridPane.getChildren().get(1);
                        Label moTaNganLabel = (Label)gridPane.getChildren().get(2);
                        Label soNguoiThamDuHienTai = (Label)gridPane.getChildren().get(3);
                        Label thoiGianToChuc = (Label)gridPane.getChildren().get(4);

                        imageView.setImage(hinhAnh.getImage());
                        tenHoiNghiLabel.setText(tenHoiNghi.getText());
                        moTaNganLabel.setText(moTaNgan.getText());
                        soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + soNguoiThamDuHoiNghi + "/" +
                                soNguoiThamDuToiDa.getText());
                        thoiGianToChuc.setText("Th???i gian t??? ch???c: " + ngayToChuc.getValue() + ", " + gioBatDau.getText() + " - " +
                                gioKetThuc.getText());

                        break;
                    }
                }
            }
        }
    }

    private void updateView() {
        dsHoiNghi = hoiNghiDAO.getAllHoiNghi();
        data = FXCollections.observableArrayList(dsHoiNghi);
        hoiNghiTableView.getItems().clear();
        hoiNghiTableView.getItems().addAll(data);
        dsDiaDiem = diaDiemDAO.getTenDiaDiemCol();
        dsDiaDiemData = FXCollections.observableArrayList(dsDiaDiem);

        updateHomeView();
    }

    private void resetValues() {
        tenHoiNghi.setText("");
        ngayToChuc.setValue(LocalDate.now());
        gioBatDau.setText("");
        gioKetThuc.setText("");
        moTaNgan.setText("");
        moTaChiTiet.setText("");
        hinhAnh.setImage(null);
        soNguoiThamDuToiDa.setText("");
        alertText1.setText("S??? ng?????i tham d??? nh??? nh???t l?? 1");
        alertText2.setText("S??? ng?????i ???? ????ng k?? tham gia (s??? ng?????i tham d??? nh??? nh???t c???a h???i ngh???): 0");
        diaDiemComboBox.setValue(null);
        soNguoiThamDuHoiNghi = 0;
        isNewImageChosen = false;
    }

    public void cancelButtonClicked(ActionEvent actionEvent) {
        resetValues();

        saveButton.setDisable(true);
        addButton.setDisable(false);
    }

    private int checkInput() {
        Pattern timePattern = Pattern.compile("(([0-1]?[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]");
        Date date = Date.from(ngayToChuc.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (tenHoiNghi.getText().equals("") || gioBatDau.getText().equals("") || gioKetThuc.getText().equals("") ||
                moTaNgan.getText().equals("") || moTaChiTiet.getText().equals("") || soNguoiThamDuToiDa.getText().equals("")) {
            return -1;
        }
        else if (diaDiemComboBox.getValue() == null) {
            return -2;
        }
        else if (hinhAnh.getImage() == null) {
            return -3;
        }
        else if (!soNguoiThamDuToiDa.getText().matches("[0-9]*")) {
            return -4;
        }
        else if (!timePattern.matcher(gioBatDau.getText()).matches() || !timePattern.matcher(gioKetThuc.getText()).matches()) {
            return -5;
        }
        else if (Time.valueOf(gioBatDau.getText()).after(Time.valueOf(gioKetThuc.getText())) ||
                gioBatDau.getText().equals(gioKetThuc.getText())) {
            return -6;
        }
        else if (Integer.parseInt(soNguoiThamDuToiDa.getText()) > selectedDiaDiem.getSucChua() ||
                Integer.parseInt(soNguoiThamDuToiDa.getText()) < soNguoiThamDuHoiNghi) {
            return -7;
        }
        // Ki???m tra trong c??ng 1 ng??y, c??ng 1 ?????a ??i???m kh??ng b??? tr??ng ho???c v?????ng gi??? t??? ch???c
        else if ((dsHoiNghiCungThoiDiemToChuc = hoiNghiDAO.getAllHoiNghiCungThoiDiemToChuc(date, Time.valueOf(gioBatDau.getText()),
                Time.valueOf(gioKetThuc.getText()), selectedDiaDiem.getIdDiaDiem())) != null) {
            if (dsHoiNghiCungThoiDiemToChuc.size() != 0) {
                return -8;
            }
        }
        return 0;
    }

    private void showAlert(int check) {
        if (check == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("C??c v??ng nh???p li???u kh??ng h???p l???");
            alert.setContentText("C??c v??ng nh???p li???u kh??ng th??? b??? tr???ng");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("C??c v??ng nh???p li???u kh??ng h???p l???");
            alert.setContentText("B???n ch??a ch???n ?????a ??i???m t??? ch???c h???i ngh???");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("C??c v??ng nh???p li???u kh??ng h???p l???");
            alert.setContentText("B???n ch??a ch???n h??nh ???nh cho h???i ngh???");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("S??? ng?????i tham d??? t???i ??a kh??ng h???p l???");
            alert.setContentText("S??? ng?????i tham d??? t???i ??a ph???i l?? 1 con s???");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th???i gian b???t ?????u v?? k???t th??c kh??ng h???p l???");
            alert.setContentText("Th???i gian b???t ?????u v?? th???i gian k???t th??c ph???i theo ?????nh d???ng HH:mm:ss");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -6) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th???i gian b???t ?????u v?? k???t th??c kh??ng h???p l???");
            alert.setContentText("Th???i gian b???t ?????u ph???i nh??? h??n th???i gian k???t th??c");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -7) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("S??? ng?????i tham d??? t???i ??a kh??ng h???p l???");
            alert.setContentText("S??? ng?????i tham d??? t???i ??a c???a h???i ngh??? ph???i l???n h??n ho???c b???ng " +
                    (soNguoiThamDuHoiNghi == 0 ? 1 : soNguoiThamDuHoiNghi) + " v?? nh??? h??n ho???c b???ng " + selectedDiaDiem.getSucChua());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else if (check == -8) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("L???i v??? th???i gian t??? ch???c");
            alert.setHeaderText("T???i ?????a ??i???m " + selectedDiaDiem.getTenDiaDiem() + "\nNg??y " +
                    ngayToChuc.getValue().toString() + " ???? c?? h???i ngh??? kh??c\nt??? ch???c trong kho???ng th???i gian ???? nh???p");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Th???i gian ???? nh???p: ");
            stringBuilder.append(gioBatDau.getText());
            stringBuilder.append(" - ");
            stringBuilder.append(gioKetThuc.getText());
            stringBuilder.append("\nTh???i gian c???a c??c h???i ngh??? kh??c c??ng ng??y:\n");

            for (HoiNghi hoiNghi : dsHoiNghiCungThoiDiemToChuc) {
                stringBuilder.append("\t+ ");
                stringBuilder.append(hoiNghi.getTenHoiNghi());
                stringBuilder.append(": ");
                stringBuilder.append(hoiNghi.getGioBatDau());
                stringBuilder.append(" - ");
                stringBuilder.append(hoiNghi.getGioKetThuc());
                stringBuilder.append("\n");
            }

            alert.setContentText(stringBuilder.toString());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    public void saveButtonClicked(ActionEvent actionEvent) throws IOException {
        int check = checkInput();
        if (check != 0) {
            showAlert(check);
        }
        else {
            Date date = Date.from(ngayToChuc.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            byte[] bytes = null;
            if (isNewImageChosen) {
                String urlImage = hinhAnh.getImage().getUrl();
                String imgFormatName = urlImage.substring(urlImage.lastIndexOf('.') + 1);
                BufferedImage image = SwingFXUtils.fromFXImage(hinhAnh.getImage(), null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, imgFormatName, baos);
                bytes = baos.toByteArray();
            }
            else {
                bytes = selectedHoiNghi.getHinhAnh();
            }

            hoiNghiDAO.updateHoiNghi(selectedHoiNghi.getIdHoiNghi(), tenHoiNghi.getText(), date, Time.valueOf(gioBatDau.getText()),
                    Time.valueOf(gioKetThuc.getText()), moTaNgan.getText(), moTaChiTiet.getText(), bytes,
                    Integer.parseInt(soNguoiThamDuToiDa.getText()), selectedDiaDiem);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ch???nh s???a th??nh c??ng");
            alert.setContentText("???? ch???nh s???a th??nh c??ng th??ng tin c???a h???i ngh???");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            updateView();

            resetValues();

            saveButton.setDisable(true);
            addButton.setDisable(false);
        }
    }

    public void addButtonClicked(ActionEvent actionEvent) throws IOException {
        int check = checkInput();
        if (check != 0) {
            showAlert(check);
        }
        else {
            Date date = Date.from(ngayToChuc.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            String urlImage = hinhAnh.getImage().getUrl();
            String imgFormatName = urlImage.substring(urlImage.lastIndexOf('.') + 1);
            BufferedImage image = SwingFXUtils.fromFXImage(hinhAnh.getImage(), null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, imgFormatName, baos);
            byte[] bytes = baos.toByteArray();

            hoiNghiDAO.addHoiNghi(tenHoiNghi.getText(), date, Time.valueOf(gioBatDau.getText()), Time.valueOf(gioKetThuc.getText()),
                    moTaNgan.getText(), moTaChiTiet.getText(), bytes, Integer.parseInt(soNguoiThamDuToiDa.getText()), selectedDiaDiem);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??m th??nh c??ng");
            alert.setContentText("???? th??m m???i th??nh c??ng 1 h???i ngh???");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });

            updateView();

            resetValues();
        }
    }

}
