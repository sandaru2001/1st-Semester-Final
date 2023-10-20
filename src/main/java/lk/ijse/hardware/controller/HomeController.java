//package lk.ijse.hardware.controller;
//
//import javafx.application.Platform;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//
//import java.awt.*;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.ResourceBundle;
//
//public class HomeController implements Initializable {
//
//
//    public Label lblTime;
//    public Label lblDate;
//
//    private void TimeNow(){
//        Thread thread = new Thread(() ->{
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//            while (!false){
//                try {
//                    Thread.sleep(1000);
//                }catch (Exception e){
//                    System.out.println(e);
//                }
//                final String timenow = sdf.format(new Date());
//                Platform.runLater(() ->{
//                    lblTime.setText(timenow);
//                });
//            }
//        });
//        thread.start();
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        lblDate.setText(String.valueOf(LocalDate.now()));
//        TimeNow();
//    }
//}
