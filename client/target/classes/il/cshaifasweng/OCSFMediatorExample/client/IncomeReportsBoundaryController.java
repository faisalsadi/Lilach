package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class IncomeReportsBoundaryController implements Initializable {
    @FXML private Text incomeTxt;
    @FXML private Button userName;
    @FXML private Button returnBtn;
    @FXML private Label fromL;
    @FXML private DatePicker start;
    @FXML private Label toL;
    @FXML private DatePicker end;
    @FXML private Button generate;
    @FXML private Label priceLabel;
    @FXML private Label finalRes;
    @FXML private BarChart<?, ?> incomeChart;
    @FXML private CategoryAxis categoryA;
    @FXML private NumberAxis numberA;

    private static ArrayList<Order> ordersList = new ArrayList<>();
    private int finalPrice = 0;
    private static String store = "";
    private String Email;

    public static void setOrders(ArrayList<Order> orders, String store) {
        IncomeReportsBoundaryController.ordersList = orders;
        IncomeReportsBoundaryController.store = store;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(EntityHolder.getTable() == -1) {
            userName.setText("Register / Login");
        }
        else {
            if(EntityHolder.getTable() == 0) {
                userName.setText(EntityHolder.getUser().getUserName());
                Email = EntityHolder.getUser().getEmail();
            }
            else if(EntityHolder.getTable() == 1) {
                userName.setText(EntityHolder.getEmployee().getUserName());
                Email = EntityHolder.getEmployee().getEmail();
            }
            else if(EntityHolder.getTable() == 2) {
                userName.setText(EntityHolder.getStoreM().getUserName());
                Email = EntityHolder.getStoreM().getEmail();
            }
            else if(EntityHolder.getTable() == 3) {
                userName.setText(EntityHolder.getChainM().getName());
                Email = EntityHolder.getChainM().getEmail();
            }
        }

        String beforeS = getDate(LocalDateTime.now());
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDate s = LocalDate.parse(beforeS, formatter2);
        end.setValue(s);
        start.setValue(s.minusDays(6));
        try {
            initChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getDetails(ActionEvent event) throws IOException {
        if(userName.getText().equals("Register / Login")) {
            App.setRoot("LoginOrSignupBoundary");
        }
        else {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Message");
            a.setHeaderText("Do you wish to disconnect?");
            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                ArrayList<Object> arr = new ArrayList<>();
                arr.add("#disconnecting");
                arr.add(Email);
                App.getClient().sendToServer(arr);

                App.setRoot("LoginOrSignupBoundary");
                EntityHolder.setTable(-1);
                EntityHolder.setUser(null);
                EntityHolder.setEmployee(null);
                EntityHolder.setStoreM(null);
                EntityHolder.setChainM(null);
                EntityHolder.setID(-1);
                CatalogBoundaryController c = new CatalogBoundaryController();
                c.refreshAfterDisconnect();
            }
        }
    }

    @FXML
    public void returnFunc(ActionEvent event) throws IOException {
        App.setRoot("ManageStore");
    }

    @FXML
    public void generate(ActionEvent actionEvent) throws ParseException {
        initChart();
    }

    public void initChart() throws ParseException {
        int numComp = 0;
        incomeChart.setAnimated(false);
        incomeChart.getData().clear();
        incomeChart.layout();

        long dur= Math.abs(Duration.between(start.getValue().atStartOfDay(), end.getValue().atStartOfDay()).toDays());
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Income");

        int [] reportsPerMonth = new int[(int)dur + 1];

        for(int i = 0; i < ordersList.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date1 = formatter.parse(ordersList.get(i).getDateTime());

            if(ordersList.get(i).getStoreName().equals(store) &&
               date1.after(Date.from(start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
               date1.before(Date.from(end.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                long index = Math.abs(Duration.between(date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
                                                       start.getValue().atStartOfDay()).toDays());;
                reportsPerMonth[(int)index] += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
                numComp += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
            }
        }

        for(int i = 0; i < reportsPerMonth.length; i++) {
            DateFormat dateFormat = new SimpleDateFormat("MMM-dd");
            Date date2 = Date.from(start.getValue().plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
            String strDate = dateFormat.format(date2);
            dataSeries1.getData().add(new XYChart.Data(strDate, reportsPerMonth[i]));
        }

        String price = Integer.toString(numComp) + " $";
        finalRes.setText(price);
        incomeChart.getData().add(dataSeries1);
    }

    public String getDate(LocalDateTime now) {
        int[] getData = new int[5];
        getData[0] = now.getYear();
        getData[1] = now.getMonthValue();
        getData[2] = now.getDayOfMonth();
        getData[3] = now.getHour();
        getData[4] = now.getMinute();

        String[] newData = new String[5];
        for(int i = 0; i < 5; i++) {
            if(getData[i] < 10) {
                newData[i] = "0" + Integer.toString(getData[i]);
            }
            else {
                newData[i] = Integer.toString(getData[i]);
            }
        }

        String t = newData[0] + "-" + newData[1] + "-" + newData[2] + "-" + newData[3] + "-" + newData[4];
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime now1 = LocalDateTime.parse(t, formatter2);
        String now2 = now1.toString().replace("T", "-");
        now2 = now2.replace(":", "-");

        return now2;
    }
}
