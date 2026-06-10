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
                                iconPath.setFill(Color.web("#888888"));
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

        // Setup History Category Filter
        if (historyCategoryFilter != null) {
            historyCategoryFilter.getItems().addAll("All Categories", "Pemasukan", "Pengeluaran", "Food & Drinks", "Transport", "Shopping", "Gift", "Investment", "Subscriptions", "Salary", "Other");
            historyCategoryFilter.getSelectionModel().select("All Categories");
        }

        // Setup Search Listener
        if (searchTransactionField != null) {
            searchTransactionField.textProperty().addListener((obs, oldVal, newVal) -> {
                renderListTransaksi(newVal);
            });
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
            return "M11 9H9V2H7v7H5V2H3v7c0 2.12 1.66 3.84 3.75 3.97V22h2.5v-9.03C11.34 12.84 13 11.12 13 9V2h-2v7zm5-3v8h2.5v8H21V2c-2.76 0-5 2.24-5 4z"; // Cutlery
        } else if (kat.contains("belanja") || kat.contains("shop")) {
            return "M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49A1.003 1.003 0 0 0 20 4H5.21l-.94-2H1zm16 16c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z"; // Cart
        } else if (kat.contains("transport") || kat.contains("mobil") || kat.contains("car")) {
            return "M18.92 6.01C18.72 5.42 18.16 5 17.5 5h-11c-.66 0-1.21.42-1.42 1.01L3 12v8c0 .55.45 1 1 1h1c.55 0 1-.45 1-1v-1h12v1c0 .55.45 1 1 1h1c.55 0 1-.45 1-1v-8l-2.08-5.99zM6.5 16c-.83 0-1.5-.67-1.5-1.5S5.67 13 6.5 13s1.5.67 1.5 1.5S7.33 16 6.5 16zm11 0c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5zM5 11l1.5-4.5h11L19 11H5z"; // Car
        } else if (kat.contains("subs")) {
            return "M20 4H4c-1.11 0-1.99.89-1.99 2L2 18c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V6c0-1.11-.89-2-2-2zm0 14H4V6h16v12zM8 15h8v-2H8v2zm0-4h8V9H8v2z"; // Screen/Subs
        } else if (kat.contains("house") || kat.contains("housing")) {
            return "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"; // House
        } else {
            return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm.31-8.86c-1.77-.45-2.34-.94-2.34-1.67 0-.84.79-1.43 2.1-1.43 1.38 0 1.9.66 1.94 1.64h1.71c-.05-1.34-.87-2.57-2.49-2.97V5H10.9v1.69c-1.51.32-2.72 1.3-2.72 2.81 0 1.79 1.49 2.69 3.66 3.21 1.95.46 2.34 1.15 2.34 1.87 0 .53-.39 1.39-2.1 1.39-1.6 0-2.23-.72-2.32-1.64H8.04c.1 1.7 1.36 2.66 2.86 2.97V19h2.34v-1.67c1.52-.29 2.72-1.16 2.73-2.77-.01-2.2-1.9-2.96-3.66-3.42z"; // Default Dollar
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
        renderListTransaksi(searchTransactionField != null ? searchTransactionField.getText() : "");
    }

    private void renderListTransaksi() {
        renderListTransaksi(searchTransactionField != null ? searchTransactionField.getText() : "");
    }

    private void renderListTransaksi(String query) {
        listRiwayat.getChildren().clear();
        String lowerQuery = query != null ? query.toLowerCase() : "";
        
        String categoryFilter = historyCategoryFilter != null ? historyCategoryFilter.getValue() : "All Categories";

        for (Transaksi t : akun.getRiwayatTransaksi()) {
            if (!"All Categories".equals(categoryFilter)) {
                if ("Pemasukan".equals(categoryFilter) && !(t instanceof model.Pemasukan)) continue;
                if ("Pengeluaran".equals(categoryFilter) && !(t instanceof model.Pengeluaran)) continue;
                if (!"Pemasukan".equals(categoryFilter) && !"Pengeluaran".equals(categoryFilter) && !t.getKategori().equalsIgnoreCase(categoryFilter)) continue;
            }

            if (!lowerQuery.isEmpty()) {
                boolean matchKategori = t.getKategori() != null && t.getKategori().toLowerCase().contains(lowerQuery);
                boolean matchDeskripsi = t.getDeskripsi() != null && t.getDeskripsi().toLowerCase().contains(lowerQuery);
                if (!matchKategori && !matchDeskripsi) {
                    continue; // Skip items that don't match
                }
            }
            
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

            // Amount
            String nominalStr = String.format("%,.0f", t.getNominal());
            Label amountLabel = new Label();
            if (t instanceof Pemasukan) {
                amountLabel.setText("+Rp " + nominalStr);
                amountLabel.getStyleClass().add("item-amount-masuk");
            } else {
                amountLabel.setText("-Rp " + nominalStr);
                amountLabel.getStyleClass().add("item-amount-keluar");
            }
            amountLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            amountLabel.setPrefWidth(120);

            // Add all to row
            row.getChildren().addAll(iconBox, titleBox, dateLabel, spacer, amountLabel);
            
            // Insert at top
            listRiwayat.getChildren().add(0, row);
        }
    }

    @FXML
    public void bukaFormTransaksi(ActionEvent event) {
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
            
            akun.tambahTransaksiDB(t, jenis);
            
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
