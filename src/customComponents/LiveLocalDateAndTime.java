/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.swing.Timer;

/**
 *
 * @author Daniel
 */
public class LiveLocalDateAndTime {
//    private String dateTimeLabel = "Current Date and Time: ";
    private LocalDateTime currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
    private LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
    private LocalDate currentDate = LocalDate.now();

    public LiveLocalDateAndTime() {
        Timer timer = new Timer(1000, (ActionEvent e) -> {
            currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
            currentTime = LocalTime.now().withSecond(0).withNano(0);
            currentDate = LocalDate.now();
            
//            dateTimeLabel = "Current Date and Time: " + currentDateTime;
//            System.out.println(dateTimeLabel);
        });

        timer.start();
    }
    
//    public String getDateTimeLabel() {
//        return dateTimeLabel;
//    }
    
    public LocalDateTime getCurrentDateTime(){
        return currentDateTime;
    }
    
    public LocalTime getCurrentTime(){
        return currentTime;
    }
    
    public LocalDate getCurrentDate(){
        return currentDate;
    }
}
