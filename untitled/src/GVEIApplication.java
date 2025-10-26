// GVEIApplication.java
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class GVEIApplication extends Frame {
    private CardLayout cardLayout;
    private Panel mainPanel;
    private User currentUser;
    private UserDAO userDAO;
    private VehicleDAO vehicleDAO;
    private ExchangeOfferDAO offerDAO;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(0, 102, 0); // Dark Green
    private final Color SECONDARY_COLOR = new Color(0, 153, 76); // Medium Green
    private final Color ACCENT_COLOR = new Color(255, 204, 0); // Gold/Yellow
    private final Color BACKGROUND_COLOR = new Color(240, 255, 240); // Light Green
    private final Color PANEL_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(51, 51, 51); // Dark Gray

    // Components for different screens
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private DashboardPanel dashboardPanel;
    private VehicleRegistrationPanel vehicleRegistrationPanel;
    private ExchangeOfferPanel exchangeOfferPanel;
    private AdminDashboardPanel adminDashboardPanel;

    public GVEIApplication() {
        userDAO = new UserDAO();
        vehicleDAO = new VehicleDAO();
        offerDAO = new ExchangeOfferDAO();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Green Vehicle Exchange Initiative - Rwanda");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create different panels
        loginPanel = new LoginPanel();
        registrationPanel = new RegistrationPanel();
        dashboardPanel = new DashboardPanel();
        vehicleRegistrationPanel = new VehicleRegistrationPanel();
        exchangeOfferPanel = new ExchangeOfferPanel();
        adminDashboardPanel = new AdminDashboardPanel();

        // Add panels to main panel
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registrationPanel, "REGISTER");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(vehicleRegistrationPanel, "VEHICLE_REG");
        mainPanel.add(exchangeOfferPanel, "EXCHANGE_OFFER");
        mainPanel.add(adminDashboardPanel, "ADMIN_DASHBOARD");

        add(mainPanel);

        // Window listener for closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
        showLoginScreen();
    }

    // Navigation methods
    public void showLoginScreen() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showRegistrationScreen() {
        cardLayout.show(mainPanel, "REGISTER");
    }

    public void showDashboard() {
        dashboardPanel.refreshData();
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showVehicleRegistration() {
        vehicleRegistrationPanel.clearForm();
        cardLayout.show(mainPanel, "VEHICLE_REG");
    }

    public void showExchangeOffer() {
        exchangeOfferPanel.refreshData();
        cardLayout.show(mainPanel, "EXCHANGE_OFFER");
    }

    public void showAdminDashboard() {
        adminDashboardPanel.refreshData();
        cardLayout.show(mainPanel, "ADMIN_DASHBOARD");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Style utility methods
    private Button createStyledButton(String text, Color bgColor, Color textColor) {
        Button button = new Button(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusable(false);
        return button;
    }

    private Button createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR, Color.WHITE);
    }

    private Button createSecondaryButton(String text) {
        return createStyledButton(text, SECONDARY_COLOR, Color.WHITE);
    }

    private Button createAccentButton(String text) {
        return createStyledButton(text, ACCENT_COLOR, TEXT_COLOR);
    }

    private Panel createStyledPanel() {
        Panel panel = new Panel();
        panel.setBackground(PANEL_COLOR);
        // For AWT, we'll use a simple background color instead of borders
        return panel;
    }

    private Panel createBorderedPanel() {
        Panel panel = new Panel();
        panel.setBackground(PANEL_COLOR);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(PRIMARY_COLOR);
        label.setAlignment(Label.CENTER);
        return label;
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    // Login Panel
    class LoginPanel extends Panel {
        private TextField emailField;
        private TextField passwordField;
        private Button loginButton;
        private Button registerButton;
        private Label messageLabel;

        public LoginPanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            Panel formPanel = createStyledPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(400, 400));

            // Title
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            Label titleLabel = createTitleLabel("Green Vehicle Exchange Initiative - Login");
            formPanel.add(titleLabel, gbc);

            // Spacer
            gbc.gridy = 1;
            formPanel.add(new Label(" "), gbc);

            // Email
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
            Label emailLabel = new Label("Email:");
            emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(emailLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            emailField = new TextField(25);
            emailField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(emailField, gbc);

            // Password
            gbc.gridx = 0; gbc.gridy = 3;
            Label passwordLabel = new Label("Password:");
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(passwordLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 3;
            passwordField = new TextField(25);
            passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
            passwordField.setEchoChar('*');
            formPanel.add(passwordField, gbc);

            // Message Label
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            messageLabel = new Label("");
            messageLabel.setForeground(Color.RED);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(messageLabel, gbc);

            // Buttons
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
            loginButton = createPrimaryButton("Login");
            loginButton.setPreferredSize(new Dimension(120, 35));
            formPanel.add(loginButton, gbc);

            gbc.gridx = 1; gbc.gridy = 5;
            registerButton = createSecondaryButton("Register");
            registerButton.setPreferredSize(new Dimension(120, 35));
            formPanel.add(registerButton, gbc);

            // Footer
            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            Label footerLabel = new Label("Green Vehicle Exchange Initiative - Rwanda");
            footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            footerLabel.setForeground(Color.GRAY);
            footerLabel.setAlignment(Label.CENTER);
            formPanel.add(footerLabel, gbc);

            add(formPanel);

            // Event listeners
            loginButton.addActionListener(e -> login());
            registerButton.addActionListener(e -> showRegistrationScreen());

            // Enter key support for login
            emailField.addActionListener(e -> login());
            passwordField.addActionListener(e -> login());
        }

        private void login() {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            User user = userDAO.login(email, password);
            if (user != null) {
                setCurrentUser(user);
                messageLabel.setText("");
                emailField.setText("");
                passwordField.setText("");

                if (user.getRole().equals("admin")) {
                    showAdminDashboard();
                } else {
                    showDashboard();
                }
            } else {
                messageLabel.setText("Invalid email or password");
            }
        }
    }

    // Registration Panel
    class RegistrationPanel extends Panel {
        private TextField nameField;
        private TextField emailField;
        private TextField passwordField;
        private Button registerButton;
        private Button backButton;
        private Label messageLabel;

        public RegistrationPanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            Panel formPanel = createStyledPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(450, 450));

            // Title
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            Label titleLabel = createTitleLabel("Citizen Registration");
            formPanel.add(titleLabel, gbc);

            // Spacer
            gbc.gridy = 1;
            formPanel.add(new Label(" "), gbc);

            // Name
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
            Label nameLabel = new Label("Full Name:");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(nameLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            nameField = new TextField(25);
            nameField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(nameField, gbc);

            // Email
            gbc.gridx = 0; gbc.gridy = 3;
            Label emailLabel = new Label("Email:");
            emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(emailLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 3;
            emailField = new TextField(25);
            emailField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(emailField, gbc);

            // Password
            gbc.gridx = 0; gbc.gridy = 4;
            Label passwordLabel = new Label("Password:");
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(passwordLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 4;
            passwordField = new TextField(25);
            passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
            passwordField.setEchoChar('*');
            formPanel.add(passwordField, gbc);

            // Message Label
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            messageLabel = new Label("");
            messageLabel.setForeground(Color.RED);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(messageLabel, gbc);

            // Buttons
            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
            registerButton = createPrimaryButton("Register");
            registerButton.setPreferredSize(new Dimension(120, 35));
            formPanel.add(registerButton, gbc);

            gbc.gridx = 1; gbc.gridy = 6;
            backButton = createSecondaryButton("Back to Login");
            backButton.setPreferredSize(new Dimension(120, 35));
            formPanel.add(backButton, gbc);

            add(formPanel);

            // Event listeners
            registerButton.addActionListener(e -> register());
            backButton.addActionListener(e -> showLoginScreen());
        }

        private void register() {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            User user = new User(0, name, email, password, "citizen");
            boolean success = userDAO.registerUser(user);

            if (success) {
                messageLabel.setText("Registration successful! Please login.");
                messageLabel.setForeground(Color.GREEN);

                // Clear fields
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");

                // Auto-switch to login after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showLoginScreen();
                }).start();
            } else {
                messageLabel.setText("Registration failed. Email may already exist.");
            }
        }
    }

    // Dashboard Panel
    class DashboardPanel extends Panel {
        private Label welcomeLabel;
        private Button vehicleRegButton;
        private Button exchangeOfferButton;
        private Button logoutButton;
        private TextArea vehiclesArea;
        private Label statsLabel;

        public DashboardPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            // Header
            Panel headerPanel = createBorderedPanel();
            headerPanel.setPreferredSize(new Dimension(800, 80));

            welcomeLabel = new Label("Welcome to Green Vehicle Exchange Initiative Dashboard");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
            welcomeLabel.setForeground(PRIMARY_COLOR);
            welcomeLabel.setAlignment(Label.CENTER);
            headerPanel.add(welcomeLabel, BorderLayout.CENTER);

            // Stats panel
            statsLabel = new Label("");
            statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            statsLabel.setForeground(SECONDARY_COLOR);
            statsLabel.setAlignment(Label.CENTER);
            headerPanel.add(statsLabel, BorderLayout.SOUTH);

            // Menu buttons panel
            Panel menuPanel = createStyledPanel();
            menuPanel.setLayout(new GridLayout(1, 3, 20, 20));
            menuPanel.setPreferredSize(new Dimension(800, 80));

            vehicleRegButton = createPrimaryButton("Register Vehicle");
            vehicleRegButton.setFont(new Font("Arial", Font.BOLD, 14));

            exchangeOfferButton = createSecondaryButton("Exchange Offers");
            exchangeOfferButton.setFont(new Font("Arial", Font.BOLD, 14));

            logoutButton = createAccentButton("Logout");
            logoutButton.setFont(new Font("Arial", Font.BOLD, 14));

            menuPanel.add(vehicleRegButton);
            menuPanel.add(exchangeOfferButton);
            menuPanel.add(logoutButton);

            // Vehicles display area
            Panel vehiclesPanel = createBorderedPanel();

            Label vehiclesTitle = createHeaderLabel("Your Registered Vehicles:");
            vehiclesPanel.add(vehiclesTitle, BorderLayout.NORTH);

            vehiclesArea = new TextArea(15, 60);
            vehiclesArea.setFont(new Font("Courier New", Font.PLAIN, 12));
            vehiclesArea.setBackground(new Color(248, 248, 248));
            vehiclesArea.setForeground(TEXT_COLOR);
            // Use Panel instead of ScrollPane for AWT compatibility
            Panel vehiclesContainer = new Panel(new BorderLayout());
            vehiclesContainer.add(vehiclesArea, BorderLayout.CENTER);
            vehiclesPanel.add(vehiclesContainer, BorderLayout.CENTER);

            // Main layout
            Panel centerPanel = new Panel(new BorderLayout(10, 10));
            centerPanel.setBackground(BACKGROUND_COLOR);
            centerPanel.add(menuPanel, BorderLayout.NORTH);
            centerPanel.add(vehiclesPanel, BorderLayout.CENTER);

            add(headerPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);

            // Event listeners
            vehicleRegButton.addActionListener(e -> showVehicleRegistration());
            exchangeOfferButton.addActionListener(e -> showExchangeOffer());
            logoutButton.addActionListener(e -> {
                setCurrentUser(null);
                showLoginScreen();
            });
        }

        public void refreshData() {
            if (currentUser != null) {
                welcomeLabel.setText("Welcome, " + currentUser.getName() + "!");

                // Load user's vehicles
                List<Vehicle> vehicles = vehicleDAO.getVehiclesByOwner(currentUser.getUserId());
                StringBuilder sb = new StringBuilder();

                if (vehicles.isEmpty()) {
                    sb.append("No vehicles registered yet.\n\n");
                    sb.append("Click 'Register Vehicle' to add your first vehicle!");
                } else {
                    sb.append(String.format("%-12s %-12s %-10s %-6s %-10s\n",
                            "Plate", "Type", "Fuel", "Year", "Mileage"));
                    sb.append("------------------------------------------------------------\n");

                    for (Vehicle vehicle : vehicles) {
                        sb.append(String.format("%-12s %-12s %-10s %-6d %-10d km\n",
                                vehicle.getPlateNo(),
                                vehicle.getVehicleType(),
                                vehicle.getFuelType(),
                                vehicle.getManufactureYear(),
                                vehicle.getMileage()));
                    }
                }

                vehiclesArea.setText(sb.toString());

                // Update stats
                int totalVehicles = vehicles.size();
                int eligibleVehicles = 0;
                for (Vehicle vehicle : vehicles) {
                    if (vehicleDAO.isVehicleEligible(vehicle)) {
                        eligibleVehicles++;
                    }
                }

                statsLabel.setText("Total Vehicles: " + totalVehicles +
                        " | Eligible for Exchange: " + eligibleVehicles);
            }
        }
    }

    // Vehicle Registration Panel
    class VehicleRegistrationPanel extends Panel {
        private TextField plateNoField;
        private Choice vehicleTypeChoice;
        private Choice fuelTypeChoice;
        private TextField yearField;
        private TextField mileageField;
        private Choice conditionChoice;
        private Button registerButton;
        private Button backButton;
        private Label messageLabel;

        public VehicleRegistrationPanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            Panel formPanel = createStyledPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(500, 500));

            // Title
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            Label titleLabel = createTitleLabel("Vehicle Registration");
            formPanel.add(titleLabel, gbc);

            // Plate Number
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            formPanel.add(new Label("Plate Number:"), gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            plateNoField = new TextField(15);
            plateNoField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(plateNoField, gbc);

            // Vehicle Type
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new Label("Vehicle Type:"), gbc);

            gbc.gridx = 1; gbc.gridy = 2;
            vehicleTypeChoice = new Choice();
            vehicleTypeChoice.add("Car");
            vehicleTypeChoice.add("Bus");
            vehicleTypeChoice.add("Motorcycle");
            vehicleTypeChoice.add("Truck");
            vehicleTypeChoice.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(vehicleTypeChoice, gbc);

            // Fuel Type
            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(new Label("Fuel Type:"), gbc);

            gbc.gridx = 1; gbc.gridy = 3;
            fuelTypeChoice = new Choice();
            fuelTypeChoice.add("Petrol");
            fuelTypeChoice.add("Diesel");
            fuelTypeChoice.add("Electric");
            fuelTypeChoice.add("Hybrid");
            fuelTypeChoice.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(fuelTypeChoice, gbc);

            // Manufacture Year
            gbc.gridx = 0; gbc.gridy = 4;
            formPanel.add(new Label("Manufacture Year:"), gbc);

            gbc.gridx = 1; gbc.gridy = 4;
            yearField = new TextField(4);
            yearField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(yearField, gbc);

            // Mileage
            gbc.gridx = 0; gbc.gridy = 5;
            formPanel.add(new Label("Mileage (km):"), gbc);

            gbc.gridx = 1; gbc.gridy = 5;
            mileageField = new TextField(10);
            mileageField.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(mileageField, gbc);

            // Condition Rating
            gbc.gridx = 0; gbc.gridy = 6;
            formPanel.add(new Label("Condition (1-10):"), gbc);

            gbc.gridx = 1; gbc.gridy = 6;
            conditionChoice = new Choice();
            for (int i = 1; i <= 10; i++) {
                conditionChoice.add(i + " - " + "*".repeat(i));
            }
            conditionChoice.setFont(new Font("Arial", Font.PLAIN, 12));
            conditionChoice.select(4); // Select 5 stars by default
            formPanel.add(conditionChoice, gbc);

            // Message Label
            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            messageLabel = new Label("");
            messageLabel.setForeground(Color.RED);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            formPanel.add(messageLabel, gbc);

            // Buttons
            gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1;
            registerButton = createPrimaryButton("Register Vehicle");
            registerButton.setPreferredSize(new Dimension(150, 35));
            formPanel.add(registerButton, gbc);

            gbc.gridx = 1; gbc.gridy = 8;
            backButton = createSecondaryButton("Back to Dashboard");
            backButton.setPreferredSize(new Dimension(150, 35));
            formPanel.add(backButton, gbc);

            add(formPanel);

            // Event listeners
            registerButton.addActionListener(e -> registerVehicle());
            backButton.addActionListener(e -> showDashboard());
        }

        private void registerVehicle() {
            try {
                String plateNo = plateNoField.getText().trim().toUpperCase();
                String vehicleType = vehicleTypeChoice.getSelectedItem();
                String fuelType = fuelTypeChoice.getSelectedItem();
                int year = Integer.parseInt(yearField.getText());
                int mileage = Integer.parseInt(mileageField.getText());
                int condition = conditionChoice.getSelectedIndex() + 1;

                if (plateNo.isEmpty()) {
                    messageLabel.setText("Please enter plate number");
                    return;
                }

                if (year < 1950 || year > java.time.Year.now().getValue()) {
                    messageLabel.setText("Please enter a valid manufacture year");
                    return;
                }

                Vehicle vehicle = new Vehicle(0, currentUser.getUserId(), plateNo,
                        vehicleType, fuelType, year, mileage, condition);

                boolean success = vehicleDAO.registerVehicle(vehicle);

                if (success) {
                    messageLabel.setText("Vehicle registered successfully!");
                    messageLabel.setForeground(new Color(0, 128, 0));
                    clearForm();
                } else {
                    messageLabel.setText("Registration failed. Plate number may already exist.");
                }

            } catch (NumberFormatException e) {
                messageLabel.setText("Please enter valid numbers for year and mileage");
            }
        }

        public void clearForm() {
            plateNoField.setText("");
            yearField.setText("");
            mileageField.setText("");
            vehicleTypeChoice.select(0);
            fuelTypeChoice.select(0);
            conditionChoice.select(4); // Reset to 5 stars
        }
    }

    // Exchange Offer Panel
    class ExchangeOfferPanel extends Panel {
        private Choice vehicleChoice;
        private Label eligibilityLabel;
        private Label valueLabel;
        private Label subsidyLabel;
        private Button checkEligibilityButton;
        private Button applyOfferButton;
        private Button backButton;
        private TextArea offersArea;
        private Vehicle selectedVehicle;
        private Panel resultsPanel;

        public ExchangeOfferPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            // Main container
            Panel mainContainer = new Panel(new GridLayout(2, 1, 10, 10));
            mainContainer.setBackground(BACKGROUND_COLOR);

            // Top panel for eligibility check
            Panel topPanel = createStyledPanel();
            topPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Title
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            Label titleLabel = createTitleLabel("Exchange Offer Application");
            topPanel.add(titleLabel, gbc);

            // Vehicle Selection
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            Label vehicleLabel = new Label("Select Vehicle:");
            vehicleLabel.setFont(new Font("Arial", Font.BOLD, 12));
            topPanel.add(vehicleLabel, gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            vehicleChoice = new Choice();
            vehicleChoice.setFont(new Font("Arial", Font.PLAIN, 12));
            topPanel.add(vehicleChoice, gbc);

            // Check Eligibility Button
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            checkEligibilityButton = createPrimaryButton("Check Eligibility & Value");
            checkEligibilityButton.setPreferredSize(new Dimension(200, 35));
            topPanel.add(checkEligibilityButton, gbc);

            // Results panel
            resultsPanel = createStyledPanel();
            resultsPanel.setLayout(new GridLayout(3, 2, 10, 10));
            resultsPanel.setVisible(false);

            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            topPanel.add(resultsPanel, gbc);

            Label eligibilityTitle = new Label("Eligibility:");
            eligibilityTitle.setFont(new Font("Arial", Font.BOLD, 12));
            resultsPanel.add(eligibilityTitle);

            eligibilityLabel = new Label("-");
            eligibilityLabel.setFont(new Font("Arial", Font.BOLD, 12));
            resultsPanel.add(eligibilityLabel);

            Label valueTitle = new Label("Exchange Value:");
            valueTitle.setFont(new Font("Arial", Font.BOLD, 12));
            resultsPanel.add(valueTitle);

            valueLabel = new Label("-");
            valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
            valueLabel.setForeground(SECONDARY_COLOR);
            resultsPanel.add(valueLabel);

            Label subsidyTitle = new Label("Subsidy %:");
            subsidyTitle.setFont(new Font("Arial", Font.BOLD, 12));
            resultsPanel.add(subsidyTitle);

            subsidyLabel = new Label("-");
            subsidyLabel.setFont(new Font("Arial", Font.BOLD, 12));
            subsidyLabel.setForeground(SECONDARY_COLOR);
            resultsPanel.add(subsidyLabel);

            // Apply Button
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            applyOfferButton = createSecondaryButton("Apply for Exchange Offer");
            applyOfferButton.setPreferredSize(new Dimension(220, 35));
            applyOfferButton.setEnabled(false);
            topPanel.add(applyOfferButton, gbc);

            // Back Button
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            backButton = createAccentButton("Back to Dashboard");
            backButton.setPreferredSize(new Dimension(180, 35));
            topPanel.add(backButton, gbc);

            // Bottom panel for existing offers
            Panel bottomPanel = createBorderedPanel();

            Label offersTitle = createHeaderLabel("Your Exchange Offers History");
            bottomPanel.add(offersTitle, BorderLayout.NORTH);

            offersArea = new TextArea(10, 60);
            offersArea.setFont(new Font("Courier New", Font.PLAIN, 11));
            offersArea.setBackground(new Color(248, 248, 248));
            offersArea.setEditable(false);
            Panel offersContainer = new Panel(new BorderLayout());
            offersContainer.add(offersArea, BorderLayout.CENTER);
            bottomPanel.add(offersContainer, BorderLayout.CENTER);

            mainContainer.add(topPanel);
            mainContainer.add(bottomPanel);

            add(mainContainer, BorderLayout.CENTER);

            // Event listeners
            checkEligibilityButton.addActionListener(e -> checkEligibility());
            applyOfferButton.addActionListener(e -> applyForOffer());
            backButton.addActionListener(e -> showDashboard());
        }

        public void refreshData() {
            // Refresh vehicle list
            vehicleChoice.removeAll();
            List<Vehicle> vehicles = vehicleDAO.getVehiclesByOwner(currentUser.getUserId());
            for (Vehicle vehicle : vehicles) {
                vehicleChoice.add(vehicle.getPlateNo() + " (" + vehicle.getVehicleType() + ")");
            }

            // Refresh offers display
            StringBuilder sb = new StringBuilder();
            List<Vehicle> userVehicles = vehicleDAO.getVehiclesByOwner(currentUser.getUserId());
            boolean hasOffers = false;

            for (Vehicle vehicle : userVehicles) {
                List<ExchangeOffer> offers = offerDAO.getOffersByVehicle(vehicle.getVehicleId());
                for (ExchangeOffer offer : offers) {
                    hasOffers = true;
                    String statusIcon = offer.getStatus().equals("approved") ? "[APPROVED]" :
                            offer.getStatus().equals("rejected") ? "[REJECTED]" : "[PENDING]";

                    sb.append(statusIcon).append(" Vehicle: ").append(vehicle.getPlateNo())
                            .append(" | Value: RWF ").append(String.format("%,.2f", offer.getExchangeValue()))
                            .append(" | Subsidy: ").append(offer.getSubsidyPercent()).append("%")
                            .append(" | Status: ").append(offer.getStatus())
                            .append("\n");
                }
            }

            if (!hasOffers) {
                offersArea.setText("No exchange offers found.\n\nClick 'Check Eligibility' to see if your vehicles qualify for exchange!");
            } else {
                offersArea.setText(sb.toString());
            }

            // Reset form
            resultsPanel.setVisible(false);
            applyOfferButton.setEnabled(false);
        }

        private void checkEligibility() {
            if (vehicleChoice.getItemCount() == 0) {
                showMessage("No vehicles registered. Please register a vehicle first.");
                return;
            }

            List<Vehicle> vehicles = vehicleDAO.getVehiclesByOwner(currentUser.getUserId());
            selectedVehicle = vehicles.get(vehicleChoice.getSelectedIndex());

            boolean eligible = vehicleDAO.isVehicleEligible(selectedVehicle);

            resultsPanel.setVisible(true);

            if (eligible) {
                double exchangeValue = vehicleDAO.calculateExchangeValue(selectedVehicle);
                double subsidyPercent = calculateSubsidyPercent(selectedVehicle);
                double subsidyAmount = exchangeValue * (subsidyPercent / 100);

                eligibilityLabel.setText("ELIGIBLE");
                eligibilityLabel.setForeground(new Color(0, 128, 0));
                valueLabel.setText("RWF " + String.format("%,.2f", exchangeValue));
                subsidyLabel.setText(String.format("%.1f%% (RWF %,.2f)", subsidyPercent, subsidyAmount));
                applyOfferButton.setEnabled(true);

            } else {
                eligibilityLabel.setText("NOT ELIGIBLE");
                eligibilityLabel.setForeground(Color.RED);
                valueLabel.setText("-");
                subsidyLabel.setText("-");
                applyOfferButton.setEnabled(false);

                // Explain why not eligible
                int currentYear = java.time.Year.now().getValue();
                int vehicleAge = currentYear - selectedVehicle.getManufactureYear();

                if (vehicleAge <= 5) {
                    showMessage("Vehicle is too new (must be older than 5 years). Current age: " + vehicleAge + " years.");
                } else if (!selectedVehicle.getFuelType().equalsIgnoreCase("Petrol") &&
                        !selectedVehicle.getFuelType().equalsIgnoreCase("Diesel")) {
                    showMessage("Vehicle fuel type must be Petrol or Diesel. Current fuel type: " + selectedVehicle.getFuelType());
                }
            }
        }

        private double calculateSubsidyPercent(Vehicle vehicle) {
            int currentYear = java.time.Year.now().getValue();
            int vehicleAge = currentYear - vehicle.getManufactureYear();

            // Base subsidy + additional based on age
            double baseSubsidy = 20.0; // 20% base subsidy
            double ageBonus = Math.min(15.0, (vehicleAge - 5) * 1.5); // 1.5% per year over 5 years

            return baseSubsidy + ageBonus;
        }

        private void applyForOffer() {
            if (selectedVehicle == null) return;

            double exchangeValue = vehicleDAO.calculateExchangeValue(selectedVehicle);
            double subsidyPercent = calculateSubsidyPercent(selectedVehicle);

            ExchangeOffer offer = new ExchangeOffer(0, selectedVehicle.getVehicleId(),
                    exchangeValue, subsidyPercent, "pending", null);

            boolean success = offerDAO.createExchangeOffer(offer);

            if (success) {
                showSuccessDialog("Exchange offer applied successfully!\n\n" +
                        "Vehicle: " + selectedVehicle.getPlateNo() + "\n" +
                        "Exchange Value: RWF " + String.format("%,.2f", exchangeValue) + "\n" +
                        "Subsidy: " + String.format("%.1f%%", subsidyPercent) + "\n\n" +
                        "Please wait for admin approval.");
                refreshData();
            } else {
                showMessage("Failed to apply for exchange offer. Please try again.");
            }
        }

        private void showMessage(String message) {
            Dialog dialog = new Dialog(GVEIApplication.this, "Information", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 150);
            dialog.setLocationRelativeTo(GVEIApplication.this);

            Panel panel = new Panel(new BorderLayout());
            panel.setBackground(PANEL_COLOR);

            Label label = new Label(message);
            label.setAlignment(Label.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(label, BorderLayout.CENTER);

            Button okButton = createPrimaryButton("OK");
            okButton.addActionListener(e -> dialog.dispose());

            Panel buttonPanel = new Panel();
            buttonPanel.add(okButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(panel);
            dialog.setVisible(true);
        }

        private void showSuccessDialog(String message) {
            Dialog dialog = new Dialog(GVEIApplication.this, "Success", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(450, 200);
            dialog.setLocationRelativeTo(GVEIApplication.this);
            dialog.setBackground(new Color(240, 255, 240));

            Panel panel = new Panel(new BorderLayout());
            panel.setBackground(new Color(240, 255, 240));

            Label label = new Label(message);
            label.setAlignment(Label.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(label, BorderLayout.CENTER);

            Button okButton = createPrimaryButton("Great!");
            okButton.addActionListener(e -> dialog.dispose());

            Panel buttonPanel = new Panel();
            buttonPanel.setBackground(new Color(240, 255, 240));
            buttonPanel.add(okButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(panel);
            dialog.setVisible(true);
        }
    }

    // Admin Dashboard Panel (styled version)
    class AdminDashboardPanel extends Panel {
        private TextArea reportsArea;
        private Button refreshButton;
        private Button logoutButton;
        private Button manageOffersButton;
        private Button exportButton;
        private Label statsLabel;

        public AdminDashboardPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(BACKGROUND_COLOR);
            initializeComponents();
        }

        private void initializeComponents() {
            // Header
            Panel headerPanel = createBorderedPanel();
            headerPanel.setPreferredSize(new Dimension(800, 60));

            Label titleLabel = new Label("Admin Dashboard - Green Vehicle Exchange Initiative");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(PRIMARY_COLOR);
            titleLabel.setAlignment(Label.CENTER);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Stats label
            statsLabel = new Label("");
            statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
            statsLabel.setForeground(SECONDARY_COLOR);
            statsLabel.setAlignment(Label.CENTER);
            headerPanel.add(statsLabel, BorderLayout.SOUTH);

            // Menu buttons
            Panel menuPanel = createStyledPanel();
            menuPanel.setLayout(new GridLayout(1, 4, 15, 15));
            menuPanel.setPreferredSize(new Dimension(800, 60));

            refreshButton = createPrimaryButton("Refresh");
            manageOffersButton = createSecondaryButton("Manage Offers");
            exportButton = createAccentButton("Export Report");
            logoutButton = createStyledButton("Logout", Color.LIGHT_GRAY, TEXT_COLOR);

            refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
            manageOffersButton.setFont(new Font("Arial", Font.BOLD, 12));
            exportButton.setFont(new Font("Arial", Font.BOLD, 12));
            logoutButton.setFont(new Font("Arial", Font.BOLD, 12));

            menuPanel.add(refreshButton);
            menuPanel.add(manageOffersButton);
            menuPanel.add(exportButton);
            menuPanel.add(logoutButton);

            // Reports area
            Panel reportsPanel = createBorderedPanel();

            Label reportsTitle = createHeaderLabel("System Reports & Statistics");
            reportsPanel.add(reportsTitle, BorderLayout.NORTH);

            reportsArea = new TextArea(20, 70);
            reportsArea.setFont(new Font("Courier New", Font.PLAIN, 11));
            reportsArea.setBackground(new Color(248, 248, 248));
            reportsArea.setForeground(TEXT_COLOR);
            reportsArea.setEditable(false);
            Panel reportsContainer = new Panel(new BorderLayout());
            reportsContainer.add(reportsArea, BorderLayout.CENTER);
            reportsPanel.add(reportsContainer, BorderLayout.CENTER);

            // Main layout
            Panel centerPanel = new Panel(new BorderLayout(10, 10));
            centerPanel.setBackground(BACKGROUND_COLOR);
            centerPanel.add(menuPanel, BorderLayout.NORTH);
            centerPanel.add(reportsPanel, BorderLayout.CENTER);

            add(headerPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);

            // Event listeners
            refreshButton.addActionListener(e -> refreshData());
            manageOffersButton.addActionListener(e -> showOfferManagementDialog());
            exportButton.addActionListener(e -> exportReports());
            logoutButton.addActionListener(e -> {
                setCurrentUser(null);
                showLoginScreen();
            });
        }

        public void refreshData() {
            StringBuilder report = new StringBuilder();

            // Statistics
            int totalExchanged = offerDAO.getTotalExchangedVehicles();
            double totalSubsidies = offerDAO.getTotalSubsidies();
            double carbonReduction = offerDAO.getEstimatedCarbonReduction();
            int pendingOffers = 0;
            int approvedOffers = 0;
            int rejectedOffers = 0;

            List<ExchangeOffer> allOffers = offerDAO.getAllOffers();
            for (ExchangeOffer offer : allOffers) {
                switch (offer.getStatus()) {
                    case "pending": pendingOffers++; break;
                    case "approved": approvedOffers++; break;
                    case "rejected": rejectedOffers++; break;
                }
            }

            // Update stats label
            statsLabel.setText("Exchanged: " + totalExchanged +
                    " | Subsidies: RWF " + String.format("%,.2f", totalSubsidies) +
                    " | CO2 Reduction: " + String.format("%.1f", carbonReduction) + " tons");

            report.append("=== Green Vehicle Exchange Initiative ADMIN DASHBOARD ===\n");
            report.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");

            report.append("SYSTEM STATISTICS\n");
            report.append("─────────────────────────────────────────────\n");
            report.append("Total Exchanged Vehicles: ").append(totalExchanged).append("\n");
            report.append("Total Subsidies Provided: RWF ").append(String.format("%,.2f", totalSubsidies)).append("\n");
            report.append("Estimated Carbon Reduction: ").append(String.format("%.1f", carbonReduction)).append(" tons CO2/year\n\n");

            report.append("OFFER STATUS OVERVIEW\n");
            report.append("─────────────────────────────────────────────\n");
            report.append("Pending Offers: ").append(pendingOffers).append("\n");
            report.append("Approved Offers: ").append(approvedOffers).append("\n");
            report.append("Rejected Offers: ").append(rejectedOffers).append("\n");
            report.append("Total Offers: ").append(allOffers.size()).append("\n\n");

            report.append("DETAILED OFFER LIST\n");
            report.append("─────────────────────────────────────────────\n");

            if (allOffers.isEmpty()) {
                report.append("No exchange offers in the system.\n");
            } else {
                report.append(String.format("%-8s %-8s %-15s %-10s %-12s %-10s\n",
                        "OfferID", "Vehicle", "Value", "Subsidy%", "Status", "Date"));
                report.append("────────────────────────────────────────────────────────────────────────────\n");

                for (ExchangeOffer offer : allOffers) {
                    String statusIcon = offer.getStatus().equals("approved") ? "[APPROVED]" :
                            offer.getStatus().equals("rejected") ? "[REJECTED]" : "[PENDING]";

                    report.append(String.format("%-8d %-8d RWF %-11.2f %-10.1f %-12s %-10s\n",
                            offer.getOfferId(),
                            offer.getVehicleId(),
                            offer.getExchangeValue(),
                            offer.getSubsidyPercent(),
                            statusIcon,
                            offer.getCreatedDate().toString().substring(0, 10)));
                }
            }

            reportsArea.setText(report.toString());
        }

        private void showOfferManagementDialog() {
            Dialog offerDialog = new Dialog(GVEIApplication.this, "Manage Exchange Offers", true);
            offerDialog.setLayout(new BorderLayout());
            offerDialog.setSize(700, 500);
            offerDialog.setLocationRelativeTo(GVEIApplication.this);
            offerDialog.setBackground(BACKGROUND_COLOR);

            List<ExchangeOffer> offers = offerDAO.getAllOffers();
            java.awt.List offerList = new java.awt.List(15);
            offerList.setFont(new Font("Courier New", Font.PLAIN, 11));

            for (ExchangeOffer offer : offers) {
                String statusIcon = offer.getStatus().equals("approved") ? "[APPROVED]" :
                        offer.getStatus().equals("rejected") ? "[REJECTED]" : "[PENDING]";

                offerList.add(String.format("ID: %-4d | Vehicle: %-6d | Value: RWF %-10.2f | Subsidy: %-5.1f%% | Status: %s",
                        offer.getOfferId(),
                        offer.getVehicleId(),
                        offer.getExchangeValue(),
                        offer.getSubsidyPercent(),
                        statusIcon));
            }

            Panel buttonPanel = new Panel(new FlowLayout());
            buttonPanel.setBackground(PANEL_COLOR);

            Button approveButton = createPrimaryButton("Approve");
            Button rejectButton = createSecondaryButton("Reject");
            Button closeButton = createAccentButton("Close");

            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);
            buttonPanel.add(closeButton);

            Panel contentPanel = createStyledPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(new Label("Select an offer to manage:"), BorderLayout.NORTH);
            contentPanel.add(offerList, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            offerDialog.add(contentPanel);

            approveButton.addActionListener(e -> {
                if (offerList.getSelectedIndex() >= 0) {
                    int offerId = offers.get(offerList.getSelectedIndex()).getOfferId();
                    offerDAO.updateOfferStatus(offerId, "approved");
                    refreshData();
                    offerDialog.dispose();
                    showMessage("Offer approved successfully!");
                }
            });

            rejectButton.addActionListener(e -> {
                if (offerList.getSelectedIndex() >= 0) {
                    int offerId = offers.get(offerList.getSelectedIndex()).getOfferId();
                    offerDAO.updateOfferStatus(offerId, "rejected");
                    refreshData();
                    offerDialog.dispose();
                    showMessage("Offer rejected.");
                }
            });

            closeButton.addActionListener(e -> offerDialog.dispose());

            offerDialog.setVisible(true);
        }

        private void exportReports() {
            List<ExchangeOffer> offers = offerDAO.getAllOffers();
            boolean success = ReportExporter.exportToCSV(offers, "gvei_report_" +
                    java.time.LocalDate.now() + ".csv");

            if (success) {
                reportsArea.append("\n\nReport exported successfully!");
            } else {
                reportsArea.append("\n\nError exporting report");
            }
        }

        private void showMessage(String message) {
            Dialog dialog = new Dialog(GVEIApplication.this, "Success", true);
            dialog.setLayout(new FlowLayout());
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(GVEIApplication.this);

            Label label = new Label(message);
            dialog.add(label);

            Button okButton = createPrimaryButton("OK");
            okButton.addActionListener(e -> dialog.dispose());
            dialog.add(okButton);

            dialog.setVisible(true);
        }
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GVEIApplication();
    }
}