package ql.hoinghi.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import ql.hoinghi.App;
import ql.hoinghi.DAO.ChiTietHoiNghiDAO;
import ql.hoinghi.DAO.HoiNghiDAO;

public class HomeController implements Initializable {
    public GridPane mainPane;
    public Label isLoginLabel;
    public HBox naviBar;

    public Button loginButton;
    public Button signUpButton;
    public Button logoutButton;
    public Button infoButton;
    public ComboBox<String> qlComboBox;

    public RadioButton cardViewButton;
    public RadioButton listViewButton;
    public HBox utilsArea;
    public HBox viewArea;
    public TextField searchField;

    public ListView<Object[]> listView;
    public Label tenHoiNghi;
    public Label moTaNgan;
    public TextArea moTaChiTiet;
    public Label thoiGianToChuc;
    public ImageView hinhAnh;
    public Label tenDiaDiem;
    public Label soNguoiThamDuHienTai;

    public GridPane cardView;

    public HBox listViewArea = new HBox();
    public HBox cardViewArea = new HBox();

    public Object[] currentHoiNghi;
    private int soNguoiChoDuyet = 0;
    private Button dangKyButton;
    private Button listNguoiDungDangKyButton;

    private ChiTietHoiNghiDAO chiTietHoiNghiDAO = new ChiTietHoiNghiDAO();
    private HoiNghiDAO hoiNghiDAO = new HoiNghiDAO();

    private void getQLNguoiDungView() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("qlNguoiDung");
        Parent root = loader.load();
        Scene scene = new Scene(root, 720, 420);
        Stage stage = new Stage();
        stage.setTitle("Qu???n l?? ng?????i d??ng");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is complete
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    private void getQLHoiNghiView() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("qlHoiNghi");
        Parent root = loader.load();
        Scene scene = new Scene(root, 1220, 640);
        Stage stage = new Stage();
        stage.setTitle("Qu???n l?? h???i ngh???");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is complete
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    private void getQLDiaDiemView() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("qlDiaDiem");
        Parent root = loader.load();
        Scene scene = new Scene(root, 1120, 420);
        Stage stage = new Stage();
        stage.setTitle("Qu???n l?? ?????a ??i???m");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is complete
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    private void getDuyetThamDuView() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("duyetThamDu");
        Parent root = loader.load();
        Scene scene = new Scene(root, 770, 420);
        Stage stage = new Stage();
        stage.setTitle("Duy???t tham d??? h???i ngh???");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is complete
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();
        cardViewButton.setToggleGroup(group);
        listViewButton.setSelected(true);
        listViewButton.setToggleGroup(group);
        qlComboBox.getItems().addAll("Ng?????i d??ng", "H???i ngh???", "?????a ??i???m", "Duy???t tham d???");

        qlComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            qlComboBox.setValue(newValue);
                        }
                    });

                    if (newValue.equals("Ng?????i d??ng")) {
                        try {
                            getQLNguoiDungView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (newValue.equals("H???i ngh???")) {
                        try {
                            getQLHoiNghiView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (newValue.equals("?????a ??i???m")) {
                        try {
                            getQLDiaDiemView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (newValue.equals("Duy???t tham d???")) {
                        try {
                            getDuyetThamDuView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // T??m ki???m 2 ti??u ch??: t??n h???i ngh??? v?? m?? t??? ng???n c???a h???i ngh???
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                // Press Enter
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithSearchValue(searchField.getText());
                    listView.getItems().clear();
                    listView.getItems().addAll(App.listHoiNghiWithSoLuong);

                    cardViewArea.getChildren().clear();
                    setUpForCardView();
                }
            }
        });


        App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
        viewArea.getChildren().add(listViewArea);
        showView();
    }

    public void updateView() {
        if (!App.isLogin) {
            isLoginLabel.setText("You are not login");
            naviBar.getChildren().addAll(signUpButton, loginButton);
            logoutButton.setVisible(false);
            infoButton.setVisible(false);
            qlComboBox.setVisible(false);
        }
        else {
            App.listHoiNghiCuaNguoiDung = chiTietHoiNghiDAO.getAllHoinghiByIDNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());

            naviBar.getChildren().remove(loginButton);
            naviBar.getChildren().remove(signUpButton);
            logoutButton.setVisible(true);
            infoButton.setVisible(true);

            // N???u ng?????i ????ng nh???p l?? admin
            if (App.nguoiDungHienTai.getLoaiNguoiDung() == 1) {
                qlComboBox.setVisible(true);
            }

            isLoginLabel.setText("You are login");
            infoButton.setText("Hello, " + App.nguoiDungHienTai.getUsername());
        }
    }

    @FXML
    private void login() throws IOException {
        FXMLLoader loader = App.getFXMLLoader("login");
        Parent root = loader.load();
        Scene scene = new Scene(root, 420, 220);
        Stage stage = new Stage();
        stage.setTitle("????ng nh???p");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until Login is complete
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("????ng xu???t");
        alert.setContentText("B???n ???? ????ng xu???t");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });

        App.isLogin = false;
        App.nguoiDungHienTai = null;
        App.listHoiNghiCuaNguoiDung = null;
        App.controller.updateView();

        dangKyButton.setText("????ng k?? tham d???");
        dangKyButton.setDisable(false);
        dangKyButton.setId("0");
    }

    public void info(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("info");
        Parent root = loader.load();
        Scene scene = new Scene(root, 1120, 620);
        Stage stage = new Stage();
        stage.setTitle("Th??ng tin c?? nh??n");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage close
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    public void signUp(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = App.getFXMLLoader("signUp");
        Parent root = loader.load();
        Scene scene = new Scene(root, 420, 420);
        Stage stage = new Stage();
        stage.setTitle("????ng k??");
        stage.setScene(scene);
        stage.setResizable(false);

        // Wait until this stage is close
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.homeStage);
        stage.show();
    }

    public void setDangKyButton() {
        if (!App.isLogin) {
            // N???u qu?? h???n ng??y t??? ch???c h???i ngh???
            // new Date() l?? current date
            if (((Date)currentHoiNghi[2]).before(new Date())) {
                dangKyButton.setText("Qu?? h???n ????ng k??");
                dangKyButton.setDisable(true);
            }
            // ????? s??? ng?????i tham gia
            else if (Integer.parseInt((String)currentHoiNghi[13]) == (int)currentHoiNghi[8]) {
                dangKyButton.setText("????? s??? l?????ng");
                dangKyButton.setDisable(true);
            }
            // ????? s??? ng?????i ch??? duy???t
            // V?? d???: ??ang c?? 80/100 ng?????i tham gia, c?? 20 ng?????i ??ang ch??? duy???t th?? disble n??t ????ng k??
            else if (Integer.parseInt((String)currentHoiNghi[13]) + soNguoiChoDuyet >= (int)currentHoiNghi[8]) {
                dangKyButton.setText("???? ????? s??? l?????ng ch??? duy???t: " + soNguoiChoDuyet);
                dangKyButton.setDisable(true);
            }
            else {
                // Ch??a ????ng nh???p
                dangKyButton.setText("????ng k?? tham d???");
                dangKyButton.setDisable(false);
            }
            dangKyButton.setId("0");
        }
        else {
            // ???? ????ng nh???p
            Object[] item1 = new Object[] {currentHoiNghi[0], 1};
            Object[] item2 = new Object[] {currentHoiNghi[0], 2};
            Object[] item3 = new Object[] {currentHoiNghi[0], 3};

            // ???? ????ng k?? tham d??? h???i ngh???
            if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item1))) {
                // N???u ???? ????ng k?? v?? h???i ngh??? ch??a k???t th??c (ch??a di???n ra) th?? ng?????i d??ng c?? th??? h???y ????ng k??
                if (((Date)currentHoiNghi[2]).before(new Date())) {
                    dangKyButton.setText("???? ????ng k??");
                    dangKyButton.setDisable(true);
                }
                else {
                    dangKyButton.setText("H???y ????ng k??");
                    dangKyButton.setDisable(false);
                    dangKyButton.setId("2");
                }
            }
            // ??ang ch??? admin duy???t th?? ng?????i d??ng c?? th??? h???y b???t c??? l??c n??o
            else if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item2))) {
                dangKyButton.setText("??ang ch??? admin duy???t ...");
                dangKyButton.setDisable(false);
                dangKyButton.setId("2");
            }
            // B??? c???m tham d???
            else if (App.listHoiNghiCuaNguoiDung.stream().anyMatch(x -> Arrays.equals(x, item3))) {
                dangKyButton.setText("B??? c???m ????ng k??");
                dangKyButton.setDisable(true);
            }
            else {
                // N???u qu?? h???n ng??y t??? ch???c h???i ngh???
                // new Date() l?? current date
                if (((Date)currentHoiNghi[2]).before(new Date())) {
                    dangKyButton.setText("Qu?? h???n ????ng k??");
                    dangKyButton.setDisable(true);
                }
                else if (Integer.parseInt((String)currentHoiNghi[13]) == (int)currentHoiNghi[8]) {
                    dangKyButton.setText("????? s??? l?????ng");
                    dangKyButton.setDisable(true);
                }
                else if (Integer.parseInt((String)currentHoiNghi[13]) + soNguoiChoDuyet >= (int)currentHoiNghi[8]) {
                    dangKyButton.setText("???? ????? s??? l?????ng ch??? duy???t: " + soNguoiChoDuyet);
                    dangKyButton.setDisable(true);
                }
                else {
                    // Ch??a ????ng k?? tham d??? h???i ngh??? n??y
                    dangKyButton.setText("????ng k?? tham d???");
                    dangKyButton.setDisable(false);
                    dangKyButton.setId("1");
                }
            }
        }
    }

    public void setUpForListView() {
        listView = new ListView<>();
        listView.setMinHeight(525);
        listView.setMinWidth(250);

        listViewArea.getChildren().add(listView);

        GridPane hoiNghiInfo = new GridPane();
        hoiNghiInfo.setVisible(false);
        hoiNghiInfo.setPrefHeight(550);
        hoiNghiInfo.setPrefWidth(1030);
        hoiNghiInfo.setPadding(new Insets(0, 0, 0, 10));

        hinhAnh = new ImageView();
        hinhAnh.setFitHeight(440);
        hinhAnh.setFitWidth(500);
        hoiNghiInfo.add(hinhAnh, 0, 0);

        tenHoiNghi = new Label();
        tenHoiNghi.setMinHeight(30);
        tenHoiNghi.setMinWidth(200);

        hoiNghiInfo.add(tenHoiNghi, 0, 1);

        moTaNgan = new Label();
        moTaNgan.setMinHeight(25);
        moTaNgan.setMinWidth(200);
        hoiNghiInfo.add(moTaNgan, 0, 2);

        GridPane gridPane = new GridPane();
        gridPane.setPrefHeight(440);
        gridPane.setPrefWidth(500);
        gridPane.setPadding(new Insets(0, 0, 0, 10));

        Label label = new Label();
        label.setText("Th??ng tin chi ti???t c???a h???i ngh???");
        label.setMinHeight(30);
        label.setFont(new Font(18));
        gridPane.add(label, 0, 0);

        thoiGianToChuc = new Label();
        thoiGianToChuc.setMinHeight(25);
        gridPane.add(thoiGianToChuc, 0, 1);

        moTaChiTiet = new TextArea();
        moTaChiTiet.setEditable(false);
        moTaChiTiet.setWrapText(true);
        moTaChiTiet.setMinWidth(510);
        moTaChiTiet.setMinHeight(390);
        gridPane.add(moTaChiTiet, 0, 2);

        tenDiaDiem = new Label();
        tenDiaDiem.setMinHeight(50);
        tenDiaDiem.setMinWidth(200);
        tenDiaDiem.setPadding(new Insets(0, 0, 0, 10));

        GridPane gridPane1 = new GridPane();
        gridPane1.setPrefHeight(150);
        gridPane1.setPrefWidth(500);
        gridPane1.setPadding(new Insets(0, 0, 0, 10));

        soNguoiThamDuHienTai = new Label();
        soNguoiThamDuHienTai.setMinHeight(25);
        soNguoiThamDuHienTai.setMinWidth(200);

        dangKyButton = new Button();
        dangKyButton.setOnAction(e -> {
            Button button = (Button)e.getSource();

            // Ch??a ????ng nh???p
            if (button.getId().equals("0")) {
                // Hi???n th??? Dialog cho ng?????i d??ng l???a ch???n
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                ButtonType signInButton = new ButtonType("????ng nh???p");
                ButtonType signUpButton = new ButtonType("????ng k??");
                ButtonType cancelButton = new ButtonType("????ng");

                alert.getButtonTypes().clear();

                alert.getButtonTypes().addAll(signInButton, signUpButton, cancelButton);
                alert.setTitle("B???n ch??a ????ng nh???p");
                alert.setHeaderText("????? ????ng k?? tham d??? h???i ngh???, vui l??ng ch???n c??c l???a ch???n b??n d?????i");
                alert.setContentText("N???u ???? c?? t??i kho???n, ch???n ????ng nh???p, n???u ch??a c?? ch???n ????ng k?? ????? t???o t??i kho???n");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == signInButton) {
                    try {
                        FXMLLoader loader = App.getFXMLLoader("login");
                        Parent root = loader.load();
                        Scene scene = new Scene(root, 420, 220);
                        Stage stage = new Stage();
                        stage.setTitle("????ng nh???p");
                        stage.setScene(scene);
                        stage.setResizable(false);

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
                        stage.setTitle("????ng k??");
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
            // ????ng k??
            else if (button.getId().equals("1")) {
                int result = -1;
                // Ki???m tra ??i???u ki???n n???u v?????t qu?? s??? l?????ng
                if (Integer.parseInt((String)currentHoiNghi[13]) >= (int)currentHoiNghi[8]) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kh??ng th??? ????ng k??");
                    alert.setContentText("S??? l?????ng ng?????i tham d??? ???? ?????t m???c t???i ??a");
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
                        alert.setTitle("Ch??c m???ng!!!");
                        alert.setContentText("B???n ???? ????ng k?? tham d??? h???i ngh??? th??nh c??ng, vui l??ng ch??? admin duy???t");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });

                        button.setText("??ang ch??? admin duy???t ...");
                        button.setDisable(false);
                        button.setId("2");

                        App.listHoiNghiCuaNguoiDung.add(new Object[]{currentHoiNghi[0], 2});
                    }
                }
            }
            // H???y ????ng k??
            else if (button.getId().equals("2")) {
                int result = chiTietHoiNghiDAO.huyDangKy((int)currentHoiNghi[0], App.nguoiDungHienTai.getIdNguoiDung());
                if (result > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("H???y ????ng k?? th??nh c??ng!!!");
                    alert.setContentText("B???n ???? h???y ????ng k?? h???i ngh??? th??nh c??ng");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                    button.setText("????ng k?? tham d???");
                    button.setDisable(false);
                    button.setId("1");

                    App.listHoiNghiWithSoLuong = hoiNghiDAO.getAllHoiNghiWithDiaDiemAndSoLuong();
                    App.listHoiNghiCuaNguoiDung = chiTietHoiNghiDAO.getAllHoinghiByIDNguoiDung(App.nguoiDungHienTai.getIdNguoiDung());
                    currentHoiNghi = hoiNghiDAO.getOneHoiNghiWithDiaDiemAndSoLuong((int)currentHoiNghi[0]);
                    listView.getItems().clear();
                    listView.getItems().addAll(App.listHoiNghiWithSoLuong);

                    soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);

                    if (cardView != null) {
                        for (int i = 0; i < cardView.getChildren().size(); i++) {
                            Button btn = (Button)cardView.getChildren().get(i);
                            if (btn.getId().equals(Integer.toString((int)currentHoiNghi[0]))) {
                                GridPane gp = (GridPane)btn.getGraphic();
                                Label lb = (Label)gp.getChildren().get(3);
                                lb.setText("S??? ng?????i tham d??? hi???n t???i: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);
                                break;
                            }
                        }
                    }

                }
            }
        });

        listNguoiDungDangKyButton = new Button("Xem danh s??ch nh???ng ng?????i ???? tham gia");
        listNguoiDungDangKyButton.setMinHeight(25);
        listNguoiDungDangKyButton.setMinWidth(200);
        listNguoiDungDangKyButton.setOnAction(e -> {
            try {
                FXMLLoader loader = App.getFXMLLoader("dsNguoiDungDaThamDu");
                Parent root = loader.load();
                Scene scene = new Scene(root, 620, 420);
                Stage stage = new Stage();
                stage.setTitle("Danh s??ch ng?????i d??ng ???? tham d??? h???i ngh???");
                stage.setScene(scene);
                stage.setResizable(false);

                // Wait until this stage is close
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(App.homeStage);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        HBox hBox = new HBox();
        hBox.setMinWidth(510);
        hBox.setMinHeight(25);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(listNguoiDungDangKyButton, dangKyButton);

        gridPane1.add(soNguoiThamDuHienTai, 0, 0);
        gridPane1.add(hBox, 0, 1);

        hoiNghiInfo.add(gridPane, 1, 0);
        hoiNghiInfo.add(tenDiaDiem, 1, 1);
        hoiNghiInfo.add(gridPane1, 1, 2);

        listViewArea.getChildren().add(hoiNghiInfo);

        if (listView.getItems().size() == 0) {
            listView.getItems().addAll(App.listHoiNghiWithSoLuong);

            listView.setMaxWidth(200);

            listView.setCellFactory(lv -> new ListCell<Object[]>() {
                @Override
                public void updateItem(Object[] hoiNghi, boolean empty) {
                    super.updateItem(hoiNghi, empty);
                    if (empty) {
                        setText(null);
                    }
                    else {
                        setText(hoiNghi[1].toString());
                    }
                }
            });

            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object[]>() {
                @Override
                public void changed(
                        ObservableValue<? extends Object[]> observable, Object[] oldValue, Object[] newValue) {
                    if (observable != null && observable.getValue() != null) {
                        hoiNghiInfo.setVisible(true);

                        currentHoiNghi = newValue;
                        soNguoiChoDuyet = hoiNghiDAO.getSoNguoiDangChoDuyetHoiNghi((int)currentHoiNghi[0]);

                        InputStream input = new ByteArrayInputStream((byte[])currentHoiNghi[5]);
                        Image img = new Image(input);
                        hinhAnh.setImage(img);

                        setDangKyButton();

                        tenHoiNghi.setText(currentHoiNghi[1].toString());
                        moTaNgan.setText(currentHoiNghi[6].toString());
                        moTaChiTiet.setText(currentHoiNghi[7].toString());
                        thoiGianToChuc.setText("Ng??y t??? ch???c: " + currentHoiNghi[2] + "\tTh???i gian: " + currentHoiNghi[3] +
                                " - " + currentHoiNghi[4]);
                        tenDiaDiem.setText("?????a ??i???m t??? ch???c: " + currentHoiNghi[10] + "\n?????a ch???: " + currentHoiNghi[11]);
                        soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + currentHoiNghi[13] + "/" + currentHoiNghi[8]);
                    }
                }
            });
        }
    }

    private ScrollPane createPage(int pageIndex) {
        int itemsPerRow = 4;
        int itemsPerPage = 20;

        cardView = new GridPane();

        int page = pageIndex * itemsPerPage;
        for (int i = page; i < page + itemsPerPage; i++) {
            if (i >= App.listHoiNghiWithSoLuong.size()) {
                break;
            }

            Object[] item = App.listHoiNghiWithSoLuong.get(i);
            GridPane gp = new GridPane();

            InputStream input = new ByteArrayInputStream((byte[])item[5]);
            Image img = new Image(input);
            ImageView imageView = new ImageView();
            imageView.setImage(img);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);

            Label tenHoiNghiLabel = new Label();
            tenHoiNghiLabel.setText(item[1].toString());
            tenHoiNghiLabel.setMinWidth(200);
            tenHoiNghiLabel.setMinHeight(25);

            Label moTaNganLabel = new Label();
            moTaNganLabel.setMinHeight(25);
            moTaNganLabel.setMinWidth(200);
            moTaNganLabel.setText(item[6].toString());

            Label soNguoiThamDuHienTai = new Label();
            soNguoiThamDuHienTai.setMinHeight(25);
            soNguoiThamDuHienTai.setMinWidth(200);
            soNguoiThamDuHienTai.setText("S??? ng?????i tham d??? hi???n t???i: " + item[13] + "/" + item[8]);

            Label thoiGianToChuc = new Label();
            thoiGianToChuc.setMinHeight(25);
            thoiGianToChuc.setMinWidth(200);
            thoiGianToChuc.setText("Th???i gian t??? ch???c: " + item[2] + ", " + item[3] + " - " + item[4]);

            gp.add(imageView, 0, 0);
            gp.add(tenHoiNghiLabel, 0, 1);
            gp.add(moTaNganLabel, 0, 2);
            gp.add(soNguoiThamDuHienTai, 0, 3);
            gp.add(thoiGianToChuc, 0, 4);

            Button button = new Button();
            button.setId(Integer.toString((int)item[0]));
            button.setMaxWidth(300);
            button.setGraphic(gp);
            button.setOnAction(e -> {
                currentHoiNghi = item;

                try {
                    FXMLLoader loader = App.getFXMLLoader("thongTinHoiNghi");
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 1020, 620);
                    Stage stage = new Stage();
                    stage.setTitle("Th??ng tin chi ti???t c???a h???i ngh???");
                    stage.setScene(scene);
                    stage.setResizable(false);

                    // Wait until stage is close
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(App.homeStage);
                    stage.show();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            cardView.add(button, i % itemsPerRow, i / itemsPerRow);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(1300, 500);
        AnchorPane.setTopAnchor(scrollPane, 0.);
        AnchorPane.setRightAnchor(scrollPane, 0.);
        AnchorPane.setBottomAnchor(scrollPane, 0.);
        AnchorPane.setLeftAnchor(scrollPane, 0.);

        scrollPane.setContent(cardView);

        return scrollPane;
    }

    public void setUpForCardView() {
        // Ph??n trang
        int itemsPerPage = 20;
        Pagination pagination = new Pagination((App.listHoiNghiWithSoLuong.size() / itemsPerPage) + 1, 0);
        pagination.setPageFactory(this::createPage);

        cardViewArea.getChildren().addAll(pagination);
    }

    public void showView() {
        setUpForListView();
        setUpForCardView();

        if (listViewButton.isSelected()) {
            viewArea.getChildren().clear();
            viewArea.getChildren().add(listViewArea);
        }
        else {
            viewArea.getChildren().clear();
            viewArea.getChildren().add(cardViewArea);
        }
    }

    public void listViewButtonSelected() {
        viewArea.getChildren().clear();
        viewArea.getChildren().add(listViewArea);
    }

    public void cardViewButtonSelected() {
        viewArea.getChildren().clear();
        viewArea.getChildren().add(cardViewArea);
    }
}
