package no.ntnu.idatt1002.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.ntnu.idatt1002.demo.data.Budget;
import no.ntnu.idatt1002.demo.data.BudgetItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Use this class to start the application
 * @author nilstes
 */
public class MyApp extends Application {

    /**
     * Main method for my application
     */
    public static void main(String[] args) throws Exception {
        launch(args);
   }

    @Override
    public void start(Stage stage) throws Exception {
        //Disabled FXML SceneBuilder - Loading the FXML file
        //FXMLLoader fxmlLoader = new FXMLLoader(MyApp.class.getResource("main.fxml"));
        //Setting and displaying the scene (FXML SceneBuilder)
        //Scene scene = new Scene(fxmlLoader.load(), 800, 600);


        //Temprary test data V2
        Budget userOneBudget = new Budget("OlaNordmann");
        userOneBudget.addExpense("Mat", 3500);
        userOneBudget.addExpense("Transport", 600);
        userOneBudget.addExpense("Bolig", 5000);
        userOneBudget.addExpense("Fritid", 500);
        userOneBudget.addExpense("Klær", 1000);
        userOneBudget.addExpense("Helse", 1000);
        userOneBudget.addIncome("Studielån", 8100);
        userOneBudget.addIncome("Deltidsjobb", 3000);


        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: rgba(100,148,76,0.38)");

        String underTitleStyle = (
                "-fx-font-size: 18;" +
                "-fx-font-weight: bold;");

        String welcomeTextStyle = (
                "-fx-font-size: 18;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #ffffff;");

        //HBox for current page title
        HBox titleBox = new HBox();
        Text titleText = new Text();
        titleText.setText("Oversikt");
        titleText.setFill(Color.WHITE);
        titleText.setUnderline(true);

        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle("-fx-background-color: rgba(79,110,57,0.7);" + "-fx-padding: 10;");
        titleBox.getChildren().add(titleText);

        BorderPane topBoxPane = new BorderPane();
        topBoxPane.setPrefHeight(30);
        topBoxPane.setPadding(new Insets(10, 10, 10, 10));

        Text topBoxText = new Text();
        topBoxText.setText("Velkommen, " + userOneBudget.getUsername());
        topBoxText.setFill(Color.BLACK);
        topBoxText.setStyle("-fx-padding: 10");
        topBoxPane.setLeft(topBoxText);

        root.setTop(topBoxPane);

        //StackPane for the different windows (overview, expenses, income, settings)
        StackPane windowPane = new StackPane();


        //overviewWindow- TilePane for content of overview page
        BorderPane overviewWindow = new BorderPane();
        overviewWindow.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");

        //overviewWindow- HBox to contain different graphs
        HBox graphsBox = new HBox();
        graphsBox.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 20px;" +
                "-fx-alignment: center;" +
                "-fx-max-height: 350px;");

        ArrayList<BudgetItem> expenses = userOneBudget.getExpenseList();
        int expenseCount = expenses.size();
        //overviewWindow- PieChart overview of expenses
        ObservableList<PieChart.Data> pieChartExpenses = FXCollections.observableArrayList();

        for (BudgetItem expense : expenses){
            pieChartExpenses.add(new PieChart.Data(expense.getBudgetItemName(), expense.getBudgetItemValue()));
        }


        final PieChart chart = new PieChart(pieChartExpenses);
        chart.setTitle("Dine utgifter");
        chart.setLegendVisible(false);
        chart.setMaxHeight(250);

        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();

        xAxis.setCategories(FXCollections.observableArrayList(
                Arrays.asList("Inntekter", "Utgifter")));

        //Defining the y axis
        NumberAxis yAxis = new NumberAxis();


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inntekter og utgifter");
        barChart.setMaxHeight(250);


        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        //series1.setName("Inntekter og utgifter");
        series1.getData().add(new XYChart.Data<>("Inntekter", userOneBudget.getTotalIncome()));
        series1.getData().add(new XYChart.Data<>("Utgifter", userOneBudget.getTotalExpense()));

        barChart.getData().add(series1);
        barChart.setTitle("Sum inntekt og utgifter");
        barChart.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 10px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");
        barChart.setLegendVisible(false);
        barChart.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        //Pane for tableViews
        BorderPane tablePane = new BorderPane();
        HBox tableHBox = new HBox();
        tablePane.setTop(tableHBox);


        //overviewWindow- TableView for viewing expenses
        TableView<BudgetItem> expensesTableView = new TableView<>();
        ObservableList<BudgetItem> expensesData = FXCollections.observableArrayList(userOneBudget.getExpenseList());

        TableColumn<BudgetItem, String> nameColumn = new TableColumn<>("Navn");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("budgetItemName"));
        TableColumn<BudgetItem, Double> sumColumn = new TableColumn<>("Sum (utgift)");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("budgetItemValue"));
        expensesTableView.setItems(expensesData);
        sumColumn.setMinWidth(250);

        expensesTableView.getColumns().addAll(nameColumn, sumColumn);
        expensesTableView.setMaxHeight(200);
        expensesTableView.setMinWidth(350);



        //overviewWindow- TableView for viewing incomes
        TableView<BudgetItem> incomeTableView = new TableView<>();
        AtomicReference<ObservableList<BudgetItem>> incomeData = new AtomicReference<>(FXCollections.observableArrayList(userOneBudget.getIncomeList()));

        TableColumn<BudgetItem, String> nameIncomeColumn = new TableColumn<>("Navn");
        nameIncomeColumn.setMinWidth(100);
        nameIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("budgetItemName"));

        TableColumn<BudgetItem, Double> sumIncomeColumn = new TableColumn<>("Sum (inntekt)");
        sumIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("budgetItemValue"));

        incomeTableView.setItems(incomeData.get());
        sumIncomeColumn.setMinWidth(250);

        incomeTableView.getColumns().addAll(nameIncomeColumn, sumIncomeColumn);
        incomeTableView.setMaxHeight(200);
        incomeTableView.setMinWidth(350);


        //overviewWindow- Text for top of overview page
        HBox titleOverviewPane = new HBox();
        Text overviewTitle = new Text("\nVelkommen til oversikten, her kan du få et kjapt overblikk over registrert informasjon!\n");
        overviewTitle.setStyle(underTitleStyle);
        titleOverviewPane.setAlignment(Pos.CENTER);
        titleOverviewPane.getChildren().add(overviewTitle);

        //overviewWindow- Text for bottom of overview page
        HBox underOverviewPane = new HBox();
        Text underOverviewText = new Text("\nDersom du ønsker å legge til en utgift eller inntekt, bruk navigasjonsmenyen til venstre.\n");
        underOverviewText.setStyle(
                "-fx-font-size: 15;" +
                "-fx-font-weight: bold;");
        underOverviewPane.getChildren().add(underOverviewText);
        underOverviewPane.setAlignment(Pos.CENTER);

        //overviewWindow- Pane for tableview
        HBox topOverviewPane = new HBox();
        topOverviewPane.setSpacing(50);
        topOverviewPane.autosize();
        topOverviewPane.setStyle("-fx-border-color: #ffffff;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 5px;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");
        Separator graphSeparator = new Separator();
        graphSeparator.setOrientation(Orientation.HORIZONTAL);
        tableHBox.getChildren().addAll(expensesTableView, incomeTableView);
        tableHBox.setSpacing(45);
        tablePane.setTop(tableHBox);
        tablePane.setBottom(graphSeparator);
        graphSeparator.setPadding(new Insets(30, 0, 0, 0));
        tablePane.setPadding(new Insets(10, 10, 10, 10));

        topOverviewPane.getChildren().addAll(tablePane);


        VBox bottomOverviewPane = new VBox();
        bottomOverviewPane.getChildren().addAll(graphsBox, underOverviewPane);

        //overviewWindow- Separator to differentiate between the different charts
        Separator chartSeparator = new Separator();
        chartSeparator.setOrientation(javafx.geometry.Orientation.VERTICAL);

        graphsBox.getChildren().addAll(chart, chartSeparator, barChart);
        overviewWindow.setTop(titleOverviewPane);
        overviewWindow.setCenter(topOverviewPane);
        overviewWindow.setBottom(bottomOverviewPane);
        windowPane.getChildren().addAll(overviewWindow);


        //Pane for income window
        BorderPane incomeWindow = new BorderPane();
        incomeWindow.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");

        //Elements for income window

        //incomeWindow- TableView for viewing incomes
        TableView<BudgetItem> incomePageTableView = new TableView<>();
        AtomicReference<ObservableList<BudgetItem>> incomePageData = new AtomicReference<>(FXCollections.observableArrayList(userOneBudget.getIncomeList()));

        TableColumn<BudgetItem, String> nameIncomePageColumn = new TableColumn<>("Navn");
        nameIncomePageColumn.setMinWidth(100);
        nameIncomePageColumn.setCellValueFactory(new PropertyValueFactory<>("incomeName"));

        TableColumn<BudgetItem, Double> sumIncomePageColumn = new TableColumn<>("Sum (inntekt)");
        sumIncomePageColumn.setCellValueFactory(new PropertyValueFactory<>("incomeValue"));

        incomePageTableView.setItems(incomePageData.get());
        sumIncomePageColumn.setMinWidth(698);

        incomePageTableView.getColumns().addAll(nameIncomePageColumn, sumIncomePageColumn);
        incomePageTableView.setMaxHeight(200);
        incomePageTableView.setMinWidth(350);

        BorderPane incomeWindowElements = new BorderPane();

        HBox fieldBox = new HBox();
        fieldBox.setSpacing(10);
        TextField incomeName = new TextField();
        incomeName.setPrefWidth(250);
        incomeName.setPromptText("Navn på inntekt (eks.: Lønn, Studielån)");
        TextField incomeSum = new TextField();
        incomeSum.setPrefWidth(300);
        incomeSum.setPromptText("Sum på inntekt (eks.: 10000)");
        Button addIncomeButton = new Button("Legg til inntekt");
        addIncomeButton.setStyle(
                "-fx-background-color: #ffffff; " +
                "-fx-border-color: #116c75;" +
                "-fx-text-fill: #116c75;" +
                "-fx-pref-width: 150;" +
                "-fx-highlight-fill: #116c75;" +
                "-fx-alignment: center;");

        fieldBox.getChildren().addAll(incomeName, incomeSum, addIncomeButton);
        fieldBox.setAlignment(Pos.CENTER);

        incomeWindowElements.setCenter(fieldBox);
        incomeWindowElements.setTop(incomePageTableView);


        HBox incomeTitleBox = new HBox();
        Text incomeTitle = new Text("\nVelkommen til inntektsiden, her kan du legge til inntekter!\n");
        incomeTitle.setStyle(underTitleStyle);
        incomeTitleBox.getChildren().add(incomeTitle);
        incomeTitleBox.setAlignment(Pos.CENTER);
        incomeWindow.setTop(incomeTitleBox);
        incomeWindow.setCenter(incomeWindowElements);
        incomeWindow.setVisible(false);
        windowPane.getChildren().addAll(incomeWindow);

        //settingsWindow -Pane for settings window
        BorderPane settingsWindow = new BorderPane();
        settingsWindow.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");

        //settingsWindow -Elements for settings window
        VBox topElementsSettings = new VBox();
        topElementsSettings.setAlignment(Pos.CENTER);

        Text settingsTopText = new Text("\nVelkommen til innstillingsiden, her kan du endre på innstillinger!\n");
        settingsTopText.setStyle(
                "-fx-font-size: 20;" +
                "-fx-font-weight: bold;");

        topElementsSettings.getChildren().addAll(settingsTopText);

        HBox settingsMiddleBox = new HBox();
        settingsMiddleBox.setSpacing(50);
        settingsMiddleBox.setAlignment(Pos.CENTER);
        Button settingsOptionsTextOne = new Button("Ønsker du å endre navn på en bruker?");
        settingsOptionsTextOne.setStyle("-fx-background-color: #ffffff;" +
                "-fx-border-color: #116c75;" +
                "-fx-font-size: 15;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");

        Button settingsOptionsTextTwo = new Button("Ønsker du å slette en bruker?");
        settingsOptionsTextTwo.setStyle("-fx-background-color: #ffffff;" +
                "-fx-border-color: #116c75;" +
                "-fx-font-size: 15;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");
        settingsMiddleBox.getChildren().addAll(settingsOptionsTextOne, settingsOptionsTextTwo);

        //settingsWindow - General settings
        settingsWindow.setTop(topElementsSettings);
        settingsWindow.setCenter(settingsMiddleBox);
        settingsWindow.setVisible(false);
        windowPane.getChildren().addAll(settingsWindow);

        //savingsWindow - Pane for savings window

        BorderPane savingsWindow = new BorderPane();

        savingsWindow.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");

        //savingsWindow - Elements for savings window
        VBox savingsWindowTopBox = new VBox();
        savingsWindowTopBox.setAlignment(Pos.CENTER);
        Text savingsWindowTitle = new Text("Her kan du definere sparemål.");
        savingsWindowTitle.setStyle(underTitleStyle);
        savingsWindowTopBox.getChildren().add(savingsWindowTitle);

        VBox savingsWindowMiddleBox = new VBox();
        savingsWindowMiddleBox.setAlignment(Pos.CENTER);

        HBox exampleSavingsGoal = new HBox();
        ProgressBar savingsProgressBar = new ProgressBar();
        savingsProgressBar.setProgress(0.75);
        savingsProgressBar.setPrefWidth(500);
        exampleSavingsGoal.setAlignment(Pos.CENTER);
        exampleSavingsGoal.getChildren().addAll(new Text("Ny snøskuter: "),savingsProgressBar, new Text(" 75% - 22500/30000"));

        HBox exampleSavingsGoal2 = new HBox();
        ProgressBar savingsProgressBar2 = new ProgressBar();
        savingsProgressBar2.setProgress(0.25);
        savingsProgressBar2.setPrefWidth(500);
        exampleSavingsGoal2.setAlignment(Pos.CENTER);
        exampleSavingsGoal2.getChildren().addAll(new Text("Ny bil: "),savingsProgressBar2, new Text(" 25% - 7500/30000"));

        savingsWindowMiddleBox.getChildren().addAll(exampleSavingsGoal, exampleSavingsGoal2);

        HBox savingsWindowBottomBox = new HBox();
        savingsWindowBottomBox.setSpacing(10);
        savingsWindowBottomBox.setAlignment(Pos.CENTER);
        savingsWindowBottomBox.setPadding(new Insets(10, 10, 200, 10));
        TextField savingsName = new TextField();
        savingsName.setPrefWidth(250);
        savingsName.setPromptText("Navn på sparemål (eks.: Ny snøskuter)");
        TextField savingsSum = new TextField();
        savingsSum.setPrefWidth(300);
        savingsSum.setPromptText("Sum på sparemål (eks.: 30000)");
        Button addSavingsButton = new Button("Legg til sparemål");
        addSavingsButton.setStyle(
                "-fx-background-color: #ffffff; " +
                "-fx-border-color: #116c75;" +
                "-fx-text-fill: #116c75;" +
                "-fx-pref-width: 150;" +
                "-fx-highlight-fill: #116c75;" +
                "-fx-alignment: center;");
        savingsWindowBottomBox.getChildren().addAll(savingsName, savingsSum, addSavingsButton);

        savingsWindow.setTop(savingsWindowTopBox);
        savingsWindow.setCenter(savingsWindowMiddleBox);
        savingsWindow.setBottom(savingsWindowBottomBox);
        savingsWindow.setVisible(false);
        windowPane.getChildren().addAll(savingsWindow);

        //Pane for help window
        BorderPane helpWindow = new BorderPane();
        helpWindow.setStyle("-fx-background-color: #ffffff;" +
                "-fx-padding: 15px;" +
                "-fx-spacing: 10px;" +
                "-fx-alignment: center;");
        VBox helpWindowTopBox = new VBox();
        helpWindowTopBox.setAlignment(Pos.CENTER);
        Text helpWindowTitle = new Text("Her kan du få hjelp til å bruke programmet.");
        helpWindowTitle.setStyle(underTitleStyle);
        helpWindowTopBox.getChildren().add(helpWindowTitle);
        Text helpText = new Text("""
                Ofte stilte spørsmål::
                \s
                - Spørsmål 1: Hvem er dette laget for?\s
                    - Svar 1: Programmet er laget for privat bruk, med mål om å holde styr på privatøkonomien!\s
                    \s
                - Spørsmål 2:\s""");
        helpWindow.setTop(helpWindowTopBox);
        helpWindow.setCenter(helpText);

        VBox helpFeedbackBox = new VBox();
        helpFeedbackBox.setAlignment(Pos.CENTER);
        helpFeedbackBox.setPadding(new Insets(10, 10, 10, 10));
        helpFeedbackBox.setSpacing(10);
        TextField feedbackField = new TextField();
        feedbackField.setPrefHeight(150);
        feedbackField.setMaxWidth(600);
        feedbackField.setPromptText("Har du forslag til utvidet funksjon av programmet, eller har du funnet en bug? \r\rSkriv inn til oss her!");
        Button feedbackButton = new Button("Send tilbakemelding");
        feedbackButton.setStyle("-fx-font-size: 16;" +
                "-fx-background-color: #ffffff; " +
                "-fx-border-color: #116c75;" +
                "-fx-background-radius: 5px;" +
                "-fx-text-fill: #116c75;" +
                "-fx-pref-width: 150;" +
                "-fx-pref-height: 50;" +
                "-fx-highlight-fill: #116c75;" +
                "-fx-alignment: center;");
        feedbackButton.setAlignment(Pos.CENTER);
        feedbackButton.setMinWidth(250);
        feedbackButton.autosize();
        helpFeedbackBox.getChildren().addAll(feedbackField, feedbackButton);


        helpWindow.setBottom(helpFeedbackBox);
        helpWindow.setVisible(false);
        windowPane.getChildren().addAll(helpWindow);


        //VBox for navigation menu on the left side of the window
        VBox navigationMenu = new VBox();
        navigationMenu.setStyle("-fx-background-color: rgba(100,148,76,0.38);" + "-fx-padding: 10;");
        navigationMenu.setSpacing(5);
        String buttonStyle = "-fx-font-size: 16;" +
                "-fx-background-color: #ffffff; " +
                "-fx-background-radius: 5px;" +
                "-fx-text-fill: #116c75;" +
                "-fx-pref-width: 150;" +
                "-fx-pref-height: 50;" +
                "-fx-highlight-fill: #116c75;" +
                "-fx-opacity: 0.5;" +
                "-fx-highlight-text-fill: #ffffff;";

        String buttonHoverStyle = "-fx-font-size: 16;" +
                "-fx-background-color: #ffffff; " +
                "-fx-background-radius: 5px;" +
                "-fx-text-fill: #116c75;" +
                "-fx-pref-width: 150;" +
                "-fx-pref-height: 50;" +
                "-fx-highlight-fill: #116c75;" +
                "-fx-highlight-text-fill: #ffffff;" +
                "-fx-border-color: #116c75;";

        //Buttons for navigating the application
        Button overviewButton = new Button("Oversikt");
        Button accountButton = new Button("Konto");
        Button incomeButton = new Button("Inntekter");
        Button expensesButton = new Button("Utgifter");
        Button savingsButton = new Button("Sparemål");
        Button settingsButton = new Button("Innstillinger");
        Button helpButton = new Button("Hjelp");

        overviewButton.setStyle(buttonStyle);
        overviewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            overviewButton.setStyle(buttonHoverStyle);
            titleText.setText("Oversikt");
            helpWindow.setVisible(false);
            overviewWindow.setVisible(true);
            incomeWindow.setVisible(false);
            settingsWindow.setVisible(false);
            savingsWindow.setVisible(false);

            overviewButton.setStyle(buttonHoverStyle);
            accountButton.setStyle(buttonStyle);
            incomeButton.setStyle(buttonStyle);
            expensesButton.setStyle(buttonStyle);
            savingsButton.setStyle(buttonStyle);
            settingsButton.setStyle(buttonStyle);
            helpButton.setStyle(buttonStyle);
        });
        overviewButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> overviewButton.setStyle(buttonHoverStyle));
        overviewButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!overviewWindow.isVisible()) {
                overviewButton.setStyle(buttonStyle);
            }

        });

        //Initializing to hoverstyle as default
        overviewButton.setStyle(buttonHoverStyle);

        accountButton.setStyle(buttonStyle);
        accountButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> titleText.setText("Konto"));
        accountButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> accountButton.setStyle(buttonHoverStyle));
        accountButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> accountButton.setStyle(buttonStyle));

        incomeButton.setStyle(buttonStyle);
        incomeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            titleText.setText("Inntekter");
            incomeWindow.setVisible(true);
            overviewWindow.setVisible(false);
            helpWindow.setVisible(false);
            settingsWindow.setVisible(false);
            savingsWindow.setVisible(false);

            overviewButton.setStyle(buttonStyle);
            accountButton.setStyle(buttonStyle);
            incomeButton.setStyle(buttonHoverStyle);
            expensesButton.setStyle(buttonStyle);
            savingsButton.setStyle(buttonStyle);
            settingsButton.setStyle(buttonStyle);
            helpButton.setStyle(buttonStyle);

        });
        incomeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> incomeButton.setStyle(buttonHoverStyle));
        incomeButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!incomeWindow.isVisible()) {
                incomeButton.setStyle(buttonStyle);
            }

        });

        expensesButton.setStyle(buttonStyle);
        expensesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> titleText.setText("Utgifter"));
        expensesButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> expensesButton.setStyle(buttonHoverStyle));
        expensesButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> expensesButton.setStyle(buttonStyle));

        savingsButton.setStyle(buttonStyle);
        savingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            titleText.setText("Sparemål");
            overviewWindow.setVisible(false);
            incomeWindow.setVisible(false);
            //expensesWindow.setVisible(false);
            savingsWindow.setVisible(true);
            helpWindow.setVisible(false);
            settingsWindow.setVisible(false);


        });
        savingsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> savingsButton.setStyle(buttonHoverStyle));
        savingsButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> savingsButton.setStyle(buttonStyle));

        settingsButton.setStyle(buttonStyle);
        settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            titleText.setText("Innstillinger");
            overviewWindow.setVisible(false);
            incomeWindow.setVisible(false);
            helpWindow.setVisible(false);
            settingsWindow.setVisible(true);
            savingsWindow.setVisible(false);

            overviewButton.setStyle(buttonStyle);
            accountButton.setStyle(buttonStyle);
            incomeButton.setStyle(buttonStyle);
            expensesButton.setStyle(buttonStyle);
            savingsButton.setStyle(buttonStyle);
            settingsButton.setStyle(buttonHoverStyle);
            helpButton.setStyle(buttonStyle);
        });
        settingsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> settingsButton.setStyle(buttonHoverStyle));
        settingsButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!settingsWindow.isVisible()) {
                settingsButton.setStyle(buttonStyle);
            }

        });

        helpButton.setStyle(buttonStyle);
        helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            titleText.setText("Hjelp");
            helpWindow.setVisible(true);
            overviewWindow.setVisible(false);
            incomeWindow.setVisible(false);
            settingsWindow.setVisible(false);
            savingsWindow.setVisible(false);

            overviewButton.setStyle(buttonStyle);
            accountButton.setStyle(buttonStyle);
            incomeButton.setStyle(buttonStyle);
            expensesButton.setStyle(buttonStyle);
            savingsButton.setStyle(buttonStyle);
            settingsButton.setStyle(buttonStyle);
            helpButton.setStyle(buttonHoverStyle);

        });
        helpButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> helpButton.setStyle(buttonHoverStyle));
        helpButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!helpWindow.isVisible()) {
                helpButton.setStyle(buttonStyle);
            }

        });

        Button loggUtButton = new Button("Logg ut");
        loggUtButton.setStyle(buttonStyle);
        loggUtButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
        loggUtButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> loggUtButton.setStyle(buttonHoverStyle));
        loggUtButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> loggUtButton.setStyle(buttonStyle));

        addIncomeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            userOneBudget.addIncome(incomeName.getText(), Integer.parseInt(incomeSum.getText()));


            barChart.getData().remove(series1);
            series1.getData().clear();
            series1.getData().add(new XYChart.Data<>("Inntekter", userOneBudget.getTotalIncome()));
            series1.getData().add(new XYChart.Data<>("Utgifter", userOneBudget.getTotalExpense()));
            barChart.getData().add(series1);

            incomeData.set(FXCollections.observableArrayList(userOneBudget.getIncomeList()));
            incomeTableView.setItems(incomeData.get());
            incomePageData.set(FXCollections.observableArrayList(userOneBudget.getIncomeList()));
            incomePageTableView.setItems(incomePageData.get());


            incomeSum.setText("");
            incomeName.setText("");

        });

        navigationMenu.isFillWidth();
        navigationMenu.getChildren().addAll(overviewButton, accountButton, incomeButton
        , expensesButton, savingsButton, settingsButton, helpButton, loggUtButton);
        root.setLeft(navigationMenu);
        root.setCenter(windowPane);


        Scene scene = new Scene(root, 1000  , 750);
        stage.setTitle("Budsjettverktøy");
        stage.setScene(scene);
        stage.show();
    }
}
