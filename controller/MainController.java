package controller;

import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Akun;
import model.Pemasukan;
import model.Pengeluaran;
import model.Transaksi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

public class MainController {
    @FXML private TextField searchTransactionField;
    @FXML private ComboBox<String> chartDateFilter;
    @FXML private ComboBox<String> historyCategoryFilter;
    @FXML private DatePicker startDateFilter;
    @FXML private DatePicker endDateFilter;
    @FXML private javafx.scene.layout.StackPane logoutModalOverlay;
    @FXML private javafx.scene.layout.Region dummyFocus;

    // Routing Views
    @FXML private ScrollPane homeView;
    @FXML private ScrollPane cardsView;
    @FXML private ScrollPane analyticsView;
    @FXML private ScrollPane profileView;
    @FXML private ScrollPane settingsView;

    // Sidebar Nav Items
    @FXML private HBox navHome;
    @FXML private HBox navAnalytics;
    @FXML private HBox navProfile;
    @FXML private HBox navSettings;

    // Home View Elements
    @FXML private Label greetingLabel;
    @FXML private Label exchangeRateLabel;
    @FXML private VBox homeTotalBalanceCard;
    @FXML private Label homeTotalBalanceTitle;
    @FXML private Label homeTotalBalancePill;
    @FXML private Label saldoLabel;
    @FXML private Label pengeluaranLabel;
    @FXML private Label pemasukanLabel;
    
    @FXML private BarChart<String, Number> spendingChart;
    @FXML private PieChart expensePieChart;
    @FXML private VBox listRiwayat;


    // Analytics Dashboard Elements
    @FXML private DatePicker analyticsStartPicker;
    @FXML private DatePicker analyticsEndPicker;
    @FXML private ComboBox<String> analyticsDateFilter;
    @FXML private Label analyticsIncomeLabel;
    @FXML private Label analyticsExpenseLabel;
    @FXML private Label analyticsNetFlowLabel;
    @FXML private AreaChart<String, Number> analyticsTrendChart;
    @FXML private PieChart analyticsIncomePieChart;

    // Cards View Elements
    @FXML private VBox previewCard;
    @FXML private Label previewCardTitle;
    @FXML private Label previewSaldoLabel;
    @FXML private TextField cardNameField;

    // Modal Elements
    @FXML private StackPane modalOverlay;
    @FXML private ComboBox<String> jenisComboModal;
    @FXML private Label profileStatusLabel;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private Label passwordStatusLabel;
    @FXML private SVGPath profileAvatarIcon;
    @FXML private ImageView profileAvatarImage;
    @FXML private TextField profileFullNameField;
    @FXML private TextField profileEmailField;
    @FXML private TextField profilePhoneField;
    @FXML private TextField nominalFieldModal;
    @FXML private ComboBox<String> kategoriComboModal;
    @FXML private DatePicker tanggalPickerModal;
    @FXML private TextArea deskripsiAreaModal;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterTypeCombo;
    @FXML private ComboBox<String> filterCategoryCombo;
    @FXML private DatePicker filterStartDate;
    @FXML private DatePicker filterEndDate;
    @FXML private TextField filterMinAmount;
    @FXML private TextField filterMaxAmount;

    private int currentEditId = -1;
    private Akun akun;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    // Daftar Kategori
    private final String[] daftarKategori = {
        "Food & Drinks", "Shopping", "Transport", "Subscriptions", 
        "Housing", "Salary", "Investment", "Gift", "Other"
    };

    // Card State
    private String currentCardStyle = "-fx-background-color: linear-gradient(to bottom right, #F39C12, #F1C40F);";
    private String currentCardTitleText = "Total balance";

    @FXML
    public void initialize() {
        currentView = homeView;
        if (model.Session.getInstance().getCurrentUser() != null) {
            greetingLabel.setText("Welcome back, " + model.Session.getInstance().getCurrentUser().getUsername() + "!");
        } else {
            greetingLabel.setText("Welcome back!");
        }

        fetchExchangeRate();

        akun = new Akun(0);
        akun.muatDataDariDatabase();

        // Setup ComboBox Modal
        if (jenisComboModal != null) {
            jenisComboModal.setItems(FXCollections.observableArrayList("Pemasukan", "Pengeluaran"));
            jenisComboModal.setValue("Pengeluaran");
            tanggalPickerModal.setValue(LocalDate.now());

            kategoriComboModal.setItems(FXCollections.observableArrayList(daftarKategori));
            kategoriComboModal.setValue("Food & Drinks");

            kategoriComboModal.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> p) {
                    return new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                HBox hBox = new HBox(10);
                                hBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                                SVGPath iconPath = new SVGPath();
                                iconPath.setContent(getSvgPathForCategory(item));
                                iconPath.setStyle("-fx-fill: transparent; -fx-stroke: #888888; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;");
                                Label label = new Label(item);
                                label.setStyle("-fx-text-fill: white;");
                                hBox.getChildren().addAll(iconPath, label);
                                setGraphic(hBox);
                                setText(null);
                            }
                        }
                    };
                }
            });
            kategoriComboModal.setButtonCell(kategoriComboModal.getCellFactory().call(null));
        }

        if (filterTypeCombo != null) {
            filterTypeCombo.setItems(FXCollections.observableArrayList("All Types", "Income", "Expense"));
            filterTypeCombo.setValue("All Types");
        }
        if (filterCategoryCombo != null) {
            java.util.List<String> cats = new java.util.ArrayList<>();
            cats.add("All Categories");
            cats.addAll(java.util.Arrays.asList(daftarKategori));
            filterCategoryCombo.setItems(FXCollections.observableArrayList(cats));
            filterCategoryCombo.setValue("All Categories");
        }

        // Setup Chart Date Filter
        if (chartDateFilter != null) {
            chartDateFilter.getItems().addAll("Last 7 Days", "Last 30 Days", "This Year", "All Time", "Custom");
            chartDateFilter.getSelectionModel().select("Last 30 Days");
        }
        
        // Setup Analytics Date Filter
        if (analyticsDateFilter != null) {
            analyticsDateFilter.getItems().addAll("Last 7 Days", "Last 30 Days", "This Year", "All Time", "Custom");
            analyticsDateFilter.getSelectionModel().select("Last 30 Days");
        }
        if (analyticsStartPicker != null) {
            analyticsStartPicker.valueProperty().addListener((obs, oldV, newV) -> renderAnalytics());
            analyticsEndPicker.valueProperty().addListener((obs, oldV, newV) -> renderAnalytics());
        }

        updateDashboard();
        applyCardStyle();
    }

    // --- ROUTING LOGIC ---

    private ScrollPane currentView;

    private void switchView(ScrollPane newView) {
        if (currentView == newView) return;
        
        if (currentView != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), currentView);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                currentView.setVisible(false);
                fadeInView(newView);
            });
            fadeOut.play();
        } else {
            homeView.setVisible(false);
            cardsView.setVisible(false);
            analyticsView.setVisible(false);
            profileView.setVisible(false);
            settingsView.setVisible(false);
            fadeInView(newView);
        }
    }

    private void fadeInView(ScrollPane newView) {
        newView.setOpacity(0.0);
        newView.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), newView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        currentView = newView;
    }

    @FXML
    public void navToHome(MouseEvent event) {
        setActiveNav(navHome);
        switchView(homeView);
    }

    @FXML
    public void navToAnalytics(MouseEvent event) {
        setActiveNav(navAnalytics);
        switchView(analyticsView);
    }

    @FXML
    public void navToProfile(MouseEvent event) {
        setActiveNav(navProfile);
        switchView(profileView);
        profileView.setFocusTraversable(true);
        javafx.application.Platform.runLater(() -> {
            profileView.requestFocus();
            // Just in case, try to focus the sidebar button too
            navProfile.requestFocus();
        });
        
        // Populate profile with user data if session exists
        model.User currentUser = model.Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            profileFullNameField.setText(currentUser.getUsername());
            profileEmailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            profilePhoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            
            // Load avatar if exists
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                try {
                    File file = new File(currentUser.getAvatar());
                    if (file.exists()) {
                        profileAvatarImage.setImage(new Image(file.toURI().toString()));
                        profileAvatarImage.setVisible(true);
                        profileAvatarIcon.setVisible(false);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to load avatar: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    public void navToSettings(MouseEvent event) {
        setActiveNav(navSettings);
        switchView(settingsView);
    }

    private void setActiveNav(HBox activeNav) {
        // Reset all
        navHome.getStyleClass().remove("nav-active");
        navAnalytics.getStyleClass().remove("nav-active");
        navProfile.getStyleClass().remove("nav-active");
        navSettings.getStyleClass().remove("nav-active");

        // Set active
        activeNav.getStyleClass().add("nav-active");
    }

    // --- CARD EDITOR LOGIC ---

    @FXML
    public void setThemeCyan(ActionEvent event) {
        currentCardStyle = "-fx-background-color: linear-gradient(to bottom right, #F39C12, #F1C40F);";
        applyCardStyle();
    }

    @FXML
    public void setThemeGreen(ActionEvent event) {
        currentCardStyle = "-fx-background-color: linear-gradient(to bottom right, #00FF7F, #008000);";
        applyCardStyle();
    }

    @FXML
    public void setThemeRed(ActionEvent event) {
        currentCardStyle = "-fx-background-color: linear-gradient(to bottom right, #FF416C, #FF4B2B);";
        applyCardStyle();
    }

    @FXML
    public void setThemeSunset(ActionEvent event) {
        currentCardStyle = "-fx-background-color: linear-gradient(to bottom right, #8A2387, #E94057);";
        applyCardStyle();
    }

    @FXML
    public void setThemeDark(ActionEvent event) {
        currentCardStyle = "-fx-background-color: #1a1a1a; -fx-border-color: #333333; -fx-border-radius: 24;";
        applyCardStyle();
    }

    @FXML
    public void updateCardNamePreview(KeyEvent event) {
        currentCardTitleText = cardNameField.getText();
        if (currentCardTitleText.isEmpty()) {
            currentCardTitleText = "Total balance";
        }
        applyCardStyle();
    }

    private void applyCardStyle() {
        // Apply to Home Card
        homeTotalBalanceCard.setStyle(currentCardStyle);
        homeTotalBalanceTitle.setText(currentCardTitleText);

        // Determine if text should be light or dark based on background
        if (currentCardStyle.contains("#1a1a1a")) {
            homeTotalBalanceTitle.setStyle("-fx-text-fill: white;");
            saldoLabel.setStyle("-fx-text-fill: white;");
            homeTotalBalancePill.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white;");
            previewCardTitle.setStyle("-fx-text-fill: white;");
            previewSaldoLabel.setStyle("-fx-text-fill: white;");
        } else {
            homeTotalBalanceTitle.setStyle("-fx-text-fill: black;");
            saldoLabel.setStyle("-fx-text-fill: black;");
            homeTotalBalancePill.setStyle("-fx-background-color: rgba(0,0,0,0.1); -fx-text-fill: black;");
            previewCardTitle.setStyle("-fx-text-fill: black;");
            previewSaldoLabel.setStyle("-fx-text-fill: black;");
        }

        // Apply to Preview Card
        if (previewCard != null) {
            previewCard.setStyle(currentCardStyle);
            previewCardTitle.setText(currentCardTitleText);
        }
    }


    // --- DATA & DASHBOARD LOGIC ---

    private String getSvgPathForCategory(String kat) {
        kat = kat.toLowerCase();
        if (kat.contains("makan") || kat.contains("food") || kat.contains("drinks")) {
            return "M17 13.23s-.91-.46-1.818-.46c-1.364 0-3.182 1.845-3.182 4.615C12 20.154 14.49 22 17 22s5-1.846 5-4.615s-1.818-4.616-3.182-4.616c-.909 0-1.818.462-1.818.462m0 0c0-1.384.91-3.23 2.727-3.23M10.655 5c.896 0 1.623-.672 1.623-1.5S11.55 2 10.655 2h-5.41c-.896 0-1.622.672-1.622 1.5S4.349 5 5.246 5m5.923-.077c.956 1.766 1.74 3.36 2.22 5.077q.059.21.111.423M10.428 22h-4.1C2.747 22 2 21.31 2 18v-4.223c0-3.4 1.098-5.891 2.705-8.862"; // Cutlery
        } else if (kat.contains("belanja") || kat.contains("shop")) {
            return "M10.5 20.25a.75.75 0 1 1-1.5 0a.75.75 0 0 1 1.5 0m8.5 0a.75.75 0 1 1-1.5 0a.75.75 0 0 1 1.5 0M2 3h.207c1.324 0 1.987 0 2.419.402s.479 1.063.573 2.384l.251 3.519c.142 1.989.213 2.983.515 3.791a6 6 0 0 0 3.931 3.661c.828.243 1.83.243 3.836.243c2.105 0 3.158 0 4.01-.258a6 6 0 0 0 4-4C22 11.89 22 10.843 22 8.75c0-.698 0-1.047-.086-1.33a2 2 0 0 0-1.333-1.334C20.297 6 19.948 6 19.25 6H5.5M16 10v3m-5-3v3"; // Cart
        } else if (kat.contains("transport") || kat.contains("mobil") || kat.contains("car")) {
            return "M22 15.422V18.5c0 .466 0 .699-.076.883a1 1 0 0 1-.541.54c-.184.077-.417.077-.883.077s-.699 0-.883-.076a1 1 0 0 1-.54-.541C19 19.199 19 18.966 19 18.5s0-.699-.076-.883a1 1 0 0 0-.541-.54C18.199 17 17.966 17 17.5 17h-11c-.466 0-.699 0-.883.076a1 1 0 0 0-.54.541C5 17.801 5 18.034 5 18.5s0 .699-.076.883a1 1 0 0 1-.541.54C4.199 20 3.966 20 3.5 20s-.699 0-.883-.076a1 1 0 0 1-.54-.541C2 19.199 2 18.966 2 18.5v-3.078c0-1.202 0-1.803.172-2.37s.505-1.067 1.172-2.067L4 10l.962-2.308c.745-1.79 1.118-2.684 1.874-3.188S8.56 4 10.5 4h3c1.939 0 2.908 0 3.664.504s1.129 1.399 1.874 3.188L20 10l.656.985c.667 1 1 1.5 1.172 2.067S22 14.22 22 15.422 M6.125 14H6m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0m11.875 0H18m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0M2 8.5L4 10l2 .5h12l2-.5l2-1.5"; // Car
        } else if (kat.contains("subs")) {
            return "M12 14a3 3 0 0 1-2.836-2.018C8.984 11.46 8.552 11 8 11c-2.357 0-3.536 0-4.268.732S3 13.643 3 16s0 3.535.732 4.268S5.643 21 8 21h8c2.357 0 3.535 0 4.268-.732C21 19.535 21 18.357 21 16s0-3.536-.732-4.268C19.535 11 18.357 11 16 11c-.552 0-.984.46-1.164.982A3 3 0 0 1 12 14 M21 17v-5c0-2.357 0-3.536-.732-4.268C19.535 7 18.357 7 16 7H8c-2.357 0-3.536 0-4.268.732S3 9.643 3 12v5 M21 13V8c0-2.357 0-3.536-.732-4.268C19.535 3 18.357 3 16 3H8c-2.357 0-3.536 0-4.268.732S3 5.643 3 8v5"; // Screen/Subs
        } else if (kat.contains("house") || kat.contains("housing")) {
            return "m1.5 10.002l5.5-6m0 0l4.311 4.703c.586.639.879.958 1.264 1.128c.384.169.818.169 1.685.169h8.24l-4.311-4.703c-.586-.639-.879-.958-1.264-1.128c-.384-.17-.818-.17-1.685-.17zM11 8.5V20H7c-1.886 0-2.828 0-3.414-.586S3 17.885 3 16V8.5 M11 20h6c1.886 0 2.828 0 3.414-.586S21 17.885 21 16v-6M4 7V4m3.125 7.25H7m.25 0a.25.25 0 1 1-.5 0a.25.25 0 0 1 .5 0M7 20v-4m8-2h2"; // House
        } else {
            return "M20.943 16.835a15.76 15.76 0 0 0-4.476-8.616c-.517-.503-.775-.754-1.346-.986C14.55 7 14.059 7 13.078 7h-2.156c-.981 0-1.472 0-2.043.233c-.57.232-.83.483-1.346.986a15.76 15.76 0 0 0-4.476 8.616C2.57 19.773 5.28 22 8.308 22h7.384c3.029 0 5.74-2.227 5.25-5.165 M7.257 4.443c-.207-.3-.506-.708.112-.8c.635-.096 1.294.338 1.94.33c.583-.009.88-.268 1.2-.638C10.845 2.946 11.365 2 12 2s1.155.946 1.491 1.335c.32.37.617.63 1.2.637c.646.01 1.305-.425 1.94-.33c.618.093.319.5.112.8l-.932 1.359c-.4.58-.599.87-1.017 1.035S13.837 7 12.758 7h-1.516c-1.08 0-1.619 0-2.036-.164S8.589 6.38 8.189 5.8z M13.627 12.919c-.216-.799-1.317-1.519-2.638-.98s-1.53 2.272.467 2.457c.904.083 1.492-.097 2.031.412c.54.508.64 1.923-.739 2.304c-1.377.381-2.742-.214-2.89-1.06m1.984-5.06v.761m0 5.476v.764"; // Default Dollar
        }
    }

    private void updateDashboard() {
        saldoLabel.setText(String.format("Rp %,.0f", akun.getSaldoUtama()));

        // Hitung total tabungan/investasi
        double totalSavings = 0;
        for (Transaksi t : akun.getRiwayatTransaksi()) {
            if ("Investment".equalsIgnoreCase(t.getKategori())) {
                totalSavings += Math.abs(t.getNominal());
            }
        }
        pengeluaranLabel.setText(String.format("Rp %,.0f", totalSavings));
        
        // pemasukanLabel.setText(String.format("Rp %,.0f", akun.getTotalPemasukan())); // Moved to renderChart

        if (previewSaldoLabel != null) {
            previewSaldoLabel.setText(saldoLabel.getText());
        }

        renderChart();
        renderListTransaksi();
        renderAnalytics();
    }

    @javafx.fxml.FXML
    public void handleChartFilterChange(javafx.event.ActionEvent event) {
        String filter = chartDateFilter != null ? chartDateFilter.getValue() : "Last 30 Days";
        boolean isCustom = "Custom".equals(filter);
        if (startDateFilter != null && endDateFilter != null) {
            startDateFilter.setVisible(isCustom);
            startDateFilter.setManaged(isCustom);
            endDateFilter.setVisible(isCustom);
            endDateFilter.setManaged(isCustom);
        }
        renderChart();
    }


    @FXML
    public void handleAnalyticsFilterChange(javafx.event.ActionEvent event) {
        String filter = analyticsDateFilter != null ? analyticsDateFilter.getValue() : "Last 30 Days";
        boolean isCustom = "Custom".equals(filter);
        if (analyticsStartPicker != null && analyticsEndPicker != null) {
            analyticsStartPicker.setVisible(isCustom);
            analyticsStartPicker.setManaged(isCustom);
            analyticsEndPicker.setVisible(isCustom);
            analyticsEndPicker.setManaged(isCustom);
        }
        renderAnalytics();
    }

    private void renderAnalytics() {
        if (analyticsTrendChart == null || analyticsIncomeLabel == null) return;
        
        analyticsTrendChart.getData().clear();
        if (expensePieChart != null) expensePieChart.getData().clear();
        if (analyticsIncomePieChart != null) analyticsIncomePieChart.getData().clear();

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        Map<String, Double> expensesByCategory = new HashMap<>();
        Map<String, Double> incomeByCategory = new HashMap<>();
        
        // Group trends by date
        Map<java.time.LocalDate, Double> dailyIncome = new TreeMap<>();
        Map<java.time.LocalDate, Double> dailyExpense = new TreeMap<>();

        String filter = analyticsDateFilter != null ? analyticsDateFilter.getValue() : "Last 30 Days";
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate startDate = null;
        java.time.LocalDate customStart = null;
        java.time.LocalDate customEnd = null;

        if ("Last 7 Days".equals(filter)) startDate = now.minusDays(7);
        else if ("Last 30 Days".equals(filter)) startDate = now.minusDays(30);
        else if ("This Year".equals(filter)) startDate = now.withDayOfYear(1);
        else if ("All Time".equals(filter)) startDate = null;
        else if ("Custom".equals(filter)) {
            customStart = analyticsStartPicker != null ? analyticsStartPicker.getValue() : null;
            customEnd = analyticsEndPicker != null ? analyticsEndPicker.getValue() : null;
            if (customStart == null || customEnd == null) {
                analyticsIncomeLabel.setText("Rp 0");
                analyticsExpenseLabel.setText("Rp 0");
                analyticsNetFlowLabel.setText("Rp 0");
                analyticsNetFlowLabel.setStyle("-fx-text-fill: #ffffff;");
                return;
            }
        }

        double totalIncome = 0;
        double totalExpense = 0;

        for (model.Transaksi t : akun.getRiwayatTransaksi()) {
            boolean inRange = true;
            if ("Custom".equals(filter)) {
                if (customStart != null && t.getTanggal().isBefore(customStart)) inRange = false;
                if (customEnd != null && t.getTanggal().isAfter(customEnd)) inRange = false;
            } else {
                if (startDate != null && t.getTanggal().isBefore(startDate)) inRange = false;
            }

            if (inRange) {
                if (t instanceof model.Pemasukan) {
                    totalIncome += t.getNominal();
                    incomeByCategory.put(t.getKategori(), incomeByCategory.getOrDefault(t.getKategori(), 0.0) + t.getNominal());
                    dailyIncome.put(t.getTanggal(), dailyIncome.getOrDefault(t.getTanggal(), 0.0) + t.getNominal());
                } else if (t instanceof model.Pengeluaran) {
                    totalExpense += t.getNominal();
                    expensesByCategory.put(t.getKategori(), expensesByCategory.getOrDefault(t.getKategori(), 0.0) + t.getNominal());
                    dailyExpense.put(t.getTanggal(), dailyExpense.getOrDefault(t.getTanggal(), 0.0) + t.getNominal());
                }
            }
        }

        analyticsIncomeLabel.setText(String.format("Rp %,.0f", totalIncome));
        analyticsExpenseLabel.setText(String.format("Rp %,.0f", totalExpense));
        
        double netFlow = totalIncome - totalExpense;
        analyticsNetFlowLabel.setText(String.format("Rp %,.0f", netFlow));
        if (netFlow > 0) {
            analyticsNetFlowLabel.setStyle("-fx-text-fill: #2ecc71;"); // Green
        } else if (netFlow < 0) {
            analyticsNetFlowLabel.setStyle("-fx-text-fill: #e74c3c;"); // Red
        } else {
            analyticsNetFlowLabel.setStyle("-fx-text-fill: #ffffff;"); // White
        }

        // Build Trend Chart
        Set<java.time.LocalDate> allDates = new TreeSet<>(dailyIncome.keySet());
        allDates.addAll(dailyExpense.keySet());
        
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM");
        for (java.time.LocalDate d : allDates) {
            String dateStr = d.format(formatter);
            incomeSeries.getData().add(new XYChart.Data<>(dateStr, dailyIncome.getOrDefault(d, 0.0)));
            expenseSeries.getData().add(new XYChart.Data<>(dateStr, dailyExpense.getOrDefault(d, 0.0)));
        }

        analyticsTrendChart.getData().addAll(incomeSeries, expenseSeries);

        // Build Pie Charts
        if (expensePieChart != null) {
            for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
                expensePieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        if (analyticsIncomePieChart != null) {
            for (Map.Entry<String, Double> entry : incomeByCategory.entrySet()) {
                analyticsIncomePieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
    }

    private void renderChart() {
        spendingChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        Map<String, Double> spendingByCategory = new HashMap<>();
        String filter = chartDateFilter != null ? chartDateFilter.getValue() : "Last 30 Days";
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate startDate = null;
        
        java.time.LocalDate customStart = null;
        java.time.LocalDate customEnd = null;

        if ("Last 7 Days".equals(filter)) {
            startDate = now.minusDays(7);
        } else if ("Last 30 Days".equals(filter)) {
            startDate = now.minusDays(30);
        } else if ("This Year".equals(filter)) {
            startDate = now.withDayOfYear(1);
        } else if ("Custom".equals(filter)) {
            customStart = startDateFilter != null ? startDateFilter.getValue() : null;
            customEnd = endDateFilter != null ? endDateFilter.getValue() : null;
            if (customStart == null || customEnd == null) {
                // Do not render anything if dates are not completely selected
                spendingChart.getData().add(series);
                if (expensePieChart != null) expensePieChart.getData().clear();
                if (pemasukanLabel != null) pemasukanLabel.setText("Rp 0");
                return;
            }
        }

        double filteredIncome = 0;
        for (Transaksi t : akun.getRiwayatTransaksi()) {
            boolean inRange = true;
            if ("Custom".equals(filter)) {
                if (customStart != null && t.getTanggal().isBefore(customStart)) inRange = false;
                if (customEnd != null && t.getTanggal().isAfter(customEnd)) inRange = false;
            } else {
                if (startDate != null && t.getTanggal().isBefore(startDate)) inRange = false;
            }

            if (inRange) {
                if (t instanceof model.Pemasukan) {
                    filteredIncome += t.getNominal();
                } else if (t instanceof model.Pengeluaran) {
                    spendingByCategory.put(t.getKategori(), 
                        spendingByCategory.getOrDefault(t.getKategori(), 0.0) + t.getNominal());
                }
            }
        }
        
        if (pemasukanLabel != null) {
            pemasukanLabel.setText(String.format("Rp %,.0f", filteredIncome));
        }
        
        for (Map.Entry<String, Double> entry : spendingByCategory.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        if (spendingByCategory.isEmpty()) {
            series.getData().add(new XYChart.Data<>("Empty", 0));
        }

        spendingChart.getData().add(series);

        // Update PieChart
        if (expensePieChart != null) {
            expensePieChart.getData().clear();
            for (Map.Entry<String, Double> entry : spendingByCategory.entrySet()) {
                expensePieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
    }

    @FXML
    public void handleHistoryFilterChange(javafx.event.ActionEvent event) {
        String filterCat = historyCategoryFilter != null ? historyCategoryFilter.getValue() : "All Categories";
        java.util.List<Transaksi> filtered = new java.util.ArrayList<>();
        for (Transaksi t : akun.getRiwayatTransaksi()) {
            if ("All Categories".equals(filterCat) || filterCat.equals(t.getKategori())) {
                filtered.add(t);
            }
        }
        renderListTransaksi(filtered);
    }

    @FXML
    public void filterTransactions() {
        if (searchField == null || filterTypeCombo == null || filterCategoryCombo == null) return;
        String query = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        String type = filterTypeCombo.getValue() == null ? "All Types" : filterTypeCombo.getValue();
        String category = filterCategoryCombo.getValue() == null ? "All Categories" : filterCategoryCombo.getValue();

        LocalDate startDate = filterStartDate != null ? filterStartDate.getValue() : null;
        LocalDate endDate = filterEndDate != null ? filterEndDate.getValue() : null;

        Double minAmount = null;
        if (filterMinAmount != null && filterMinAmount.getText() != null && !filterMinAmount.getText().isEmpty()) {
            try { minAmount = Double.parseDouble(filterMinAmount.getText()); } catch (NumberFormatException e) {}
        }

        Double maxAmount = null;
        if (filterMaxAmount != null && filterMaxAmount.getText() != null && !filterMaxAmount.getText().isEmpty()) {
            try { maxAmount = Double.parseDouble(filterMaxAmount.getText()); } catch (NumberFormatException e) {}
        }

        java.util.List<Transaksi> filtered = new java.util.ArrayList<>();
        for (Transaksi t : akun.getRiwayatTransaksi()) {
            boolean matchesSearch = t.getKategori().toLowerCase().contains(query) || 
                                    t.getDeskripsi().toLowerCase().contains(query);
            
            boolean matchesType = "All Types".equals(type) || 
                                 ("Income".equals(type) && t instanceof Pemasukan) || 
                                 ("Expense".equals(type) && t instanceof Pengeluaran);
                                 
            boolean matchesCategory = "All Categories".equals(category) || 
                                      t.getKategori().equals(category);

            boolean matchesStartDate = (startDate == null) || !t.getTanggal().isBefore(startDate);
            boolean matchesEndDate = (endDate == null) || !t.getTanggal().isAfter(endDate);
            
            boolean matchesMinAmount = (minAmount == null) || (t.getNominal() >= minAmount);
            boolean matchesMaxAmount = (maxAmount == null) || (t.getNominal() <= maxAmount);

            if (matchesSearch && matchesType && matchesCategory && matchesStartDate && matchesEndDate && matchesMinAmount && matchesMaxAmount) {
                filtered.add(t);
            }
        }
        renderListTransaksi(filtered);
    }

    private void renderListTransaksi() {
        renderListTransaksi(akun.getRiwayatTransaksi());
    }

    private void renderListTransaksi(java.util.List<Transaksi> transaksiList) {

        listRiwayat.getChildren().clear();
        
        for (Transaksi t : transaksiList) {

            HBox row = new HBox(15);
            row.getStyleClass().add("list-item");
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Icon SVG based on category
            VBox iconBox = new VBox();
            iconBox.getStyleClass().add("item-icon-box");
            iconBox.setAlignment(javafx.geometry.Pos.CENTER);
            SVGPath iconPath = new SVGPath();
            
            iconPath.setContent(getSvgPathForCategory(t.getKategori()));
            iconPath.getStyleClass().add("item-icon-svg");
            iconPath.setStyle("-fx-fill: transparent; -fx-stroke: white; -fx-stroke-width: 1.5; -fx-stroke-line-cap: round; -fx-stroke-line-join: round;");
            iconBox.getChildren().add(iconPath);

            // Title & Subtitle
            VBox titleBox = new VBox(2);
            titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            Label titleLabel = new Label(t.getKategori());
            titleLabel.getStyleClass().add("item-title");
            Label subtitleLabel = new Label(t.getDeskripsi().isEmpty() ? "No Description" : t.getDeskripsi());
            subtitleLabel.getStyleClass().add("item-subtitle");
            titleBox.getChildren().addAll(titleLabel, subtitleLabel);
            titleBox.setPrefWidth(250);

            // Date
            Label dateLabel = new Label(t.getTanggal().format(dateFormatter));
            dateLabel.getStyleClass().add("item-subtitle");
            dateLabel.setPrefWidth(120);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            // Edit Button
            Button editBtn = new Button("Edit");
            editBtn.getStyleClass().add("btn-secondary");
            editBtn.setStyle("-fx-font-size: 11px; -fx-padding: 3 8 3 8; -fx-cursor: hand;");
            editBtn.setOnAction(e -> bukaFormEdit(t));

            // Amount
            Label amountLabel = new Label();
            amountLabel.setText(t.formatTampilan());
            if (t instanceof Pemasukan) {
                amountLabel.getStyleClass().add("item-amount-masuk");
            } else {
                amountLabel.getStyleClass().add("item-amount-keluar");
            }
            amountLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            amountLabel.setPrefWidth(200);

            // Add all to row
            row.getChildren().addAll(iconBox, titleBox, dateLabel, spacer, editBtn, amountLabel);
            
            // Insert at top
            listRiwayat.getChildren().add(0, row);
        }
    }

    @FXML
    public void bukaFormTransaksi(ActionEvent event) {
        currentEditId = -1;
        nominalFieldModal.clear();
        kategoriComboModal.setValue("Food & Drinks");
        deskripsiAreaModal.clear();
        tanggalPickerModal.setValue(LocalDate.now());
        jenisComboModal.setValue("Pengeluaran");
        modalOverlay.setVisible(true);
    }

    public void bukaFormEdit(Transaksi t) {
        currentEditId = t.getId();
        nominalFieldModal.setText(String.format("%.0f", t.getNominal()));
        kategoriComboModal.setValue(t.getKategori());
        deskripsiAreaModal.setText(t.getDeskripsi());
        tanggalPickerModal.setValue(t.getTanggal());
        if (t instanceof Pemasukan) {
            jenisComboModal.setValue("Pemasukan");
        } else {
            jenisComboModal.setValue("Pengeluaran");
        }
        modalOverlay.setOpacity(0.0);

        modalOverlay.setVisible(true);
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), modalOverlay);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    @FXML
    public void tutupFormTransaksi(ActionEvent event) {
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), modalOverlay);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> modalOverlay.setVisible(false));
        ft.play();
    }

    @FXML
    public void simpanDariModal(ActionEvent event) {
        try {
            double nominal = Double.parseDouble(nominalFieldModal.getText());
            String kategori = kategoriComboModal.getValue(); // Mengambil dari ComboBox
            LocalDate tanggal = tanggalPickerModal.getValue();
            String deskripsi = deskripsiAreaModal.getText();
            String jenis = jenisComboModal.getValue();

            if (kategori == null || kategori.trim().isEmpty() || tanggal == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Tolong lengkapi semua data!");
                alert.showAndWait();
                return;
            }

            Transaksi t;
            if ("Pemasukan".equals(jenis)) {
                t = new Pemasukan(nominal, kategori, tanggal, deskripsi);
            } else {
                t = new Pengeluaran(nominal, kategori, tanggal, deskripsi);
            }
            
            if (currentEditId == -1) {
                akun.tambahTransaksiDB(t, jenis);
            } else {
                t.setId(currentEditId);
                akun.updateTransaksiDB(t, jenis);
            }
            
            // Clear input fields
            nominalFieldModal.clear();
            kategoriComboModal.setValue("Food & Drinks");
            deskripsiAreaModal.clear();
            tanggalPickerModal.setValue(LocalDate.now());
            
            // Close modal & refresh
            tutupFormTransaksi(null);
            updateDashboard();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nominal harus berupa angka valid!");
            alert.showAndWait();
        }
    }

    @FXML
    public void clearAllData(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Hapus semua data secara permanen?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            akun.hapusSemuaTransaksiDB();
            updateDashboard();
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Semua data berhasil dihapus!");
            success.showAndWait();
        }
    }

    private void fetchExchangeRate() {
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("https://api.exchangerate-api.com/v4/latest/USD");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();

                String json = content.toString();
                String idrMarker = "\"IDR\":";
                int idrIndex = json.indexOf(idrMarker);
                if (idrIndex != -1) {
                    int commaIndex = json.indexOf(",", idrIndex);
                    int braceIndex = json.indexOf("}", idrIndex);
                    int end = (commaIndex != -1 && commaIndex < braceIndex) ? commaIndex : braceIndex;
                    String rateStr = json.substring(idrIndex + idrMarker.length(), end).trim();
                    double rate = Double.parseDouble(rateStr);
                    javafx.application.Platform.runLater(() -> {
                        if(exchangeRateLabel != null) exchangeRateLabel.setText(String.format("1 USD = Rp %,.0f", rate));
                    });
                }
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    if(exchangeRateLabel != null) exchangeRateLabel.setText("Failed to fetch exchange rate");
                });
            }
        }).start();
    }

    private void showStatus(Label label, String msg, boolean isSuccess) {
        label.setText(msg);
        if (isSuccess) {
            label.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold;");
        } else {
            label.setStyle("-fx-text-fill: #ff4c4c; -fx-font-weight: bold;");
        }
        label.setVisible(true);
        label.setManaged(true);
        
        // Hide after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> {
                    label.setVisible(false);
                    label.setManaged(false);
                });
            } catch (Exception e) {}
        }).start();
    }

    @FXML
    public void changeAvatar(javafx.scene.input.MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Avatar Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Ensure a directory exists to save avatars
                File avatarsDir = new File("avatars");
                if (!avatarsDir.exists()) avatarsDir.mkdir();
                
                File dest = new File(avatarsDir, System.currentTimeMillis() + "_" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                model.User currentUser = model.Session.getInstance().getCurrentUser();
                if (currentUser != null) {
                    currentUser.setAvatar(dest.getPath());
                    // Update UI
                    profileAvatarImage.setImage(new Image(dest.toURI().toString()));
                    profileAvatarImage.setVisible(true);
                    profileAvatarIcon.setVisible(false);
                    showStatus(profileStatusLabel, "Avatar updated!", true);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveProfile(javafx.event.ActionEvent event) {
        model.User currentUser = model.Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.setEmail(profileEmailField.getText().trim());
            currentUser.setPhone(profilePhoneField.getText().trim());
            
            boolean success = database.DatabaseConnection.updateProfile(currentUser);
            if (success) {
                showStatus(profileStatusLabel, "Profile updated successfully!", true);
            } else {
                showStatus(profileStatusLabel, "Failed to update profile.", false);
            }
        }
    }

    @FXML
    public void updatePassword(javafx.event.ActionEvent event) {
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        
        if (oldPass.isEmpty() || newPass.isEmpty()) {
            showStatus(passwordStatusLabel, "Please fill in all fields.", false);
            return;
        }
        
        model.User currentUser = model.Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            boolean success = database.DatabaseConnection.updatePassword(currentUser.getId(), oldPass, newPass);
            if (success) {
                showStatus(passwordStatusLabel, "Password updated successfully!", true);
                oldPasswordField.setText("");
                newPasswordField.setText("");
            } else {
                showStatus(passwordStatusLabel, "Incorrect current password.", false);
            }
        }
    }

    @FXML
    public void logout(javafx.scene.input.MouseEvent event) {
        logoutModalOverlay.setOpacity(0.0);
        logoutModalOverlay.setVisible(true);
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), logoutModalOverlay);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    @FXML
    public void cancelLogout(javafx.event.ActionEvent event) {
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), logoutModalOverlay);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> logoutModalOverlay.setVisible(false));
        ft.play();
    }

    @FXML
    public void confirmLogout(javafx.event.ActionEvent event) {
        model.Session.getInstance().clear();
        try {
            java.net.URL fxmlLocation = getClass().getResource("/view/LoginView.fxml");
            javafx.scene.Parent newRoot = javafx.fxml.FXMLLoader.load(fxmlLocation);
            newRoot.setOpacity(0.0);
            
            javafx.scene.Node sourceNode = (javafx.scene.Node) event.getSource();
            javafx.scene.Scene scene = sourceNode.getScene();
            javafx.scene.Parent currentRoot = scene.getRoot();
            
            scene.setFill(javafx.scene.paint.Color.web("#121212"));
            
            javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), currentRoot);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                scene.setRoot(newRoot);
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
