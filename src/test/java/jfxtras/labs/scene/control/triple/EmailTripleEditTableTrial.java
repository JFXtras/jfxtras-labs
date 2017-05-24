package jfxtras.labs.scene.control.triple;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmailTripleEditTableTrial extends Application
{
    public static void main(String[] args) {
        launch(args);       
    }
    
    @Override
    public void start(Stage primaryStage) {
        EmailHTableEdit e = new EmailHTableEdit();
        List<Email> beanList = new ArrayList<Email>();
        beanList.add(new Email("Work", "me@work.com", true));
        e.setBeanList(beanList);

        Scene scene = new Scene(e, 800, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((ev) -> beanList.forEach(System.out::println));
    }
}
