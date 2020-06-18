package sample;

import com.sun.xml.internal.ws.commons.xmlutil.Converter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Main extends Application {

    Pane pane;
    public Label text1,text2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pane=new Pane();
        primaryStage.setTitle("SMO Algorithm");
        primaryStage.setScene(new Scene(pane, 250, 150));
        primaryStage.show();
        text1=new Label("");
        text1.setLayoutX(50);
        text1.setLayoutY(50);
        text2=new Label("");
        text2.setLayoutX(50);
        text2.setLayoutY(100);
        pane.getChildren().addAll(text1,text2);

        String[] veri=new String[1372];
        File file=new File("c:/banknot.txt");
        try {
            FileReader fileReader = new FileReader(file);
            String line;
            BufferedReader br = new BufferedReader(fileReader);
            int sayac=0;
            while ((line = br.readLine()) != null) {
                veri[sayac]=line;
                sayac++;
            }
            br.close();
        }catch (Exception e){ }

        Random rnd=new Random();
        String trainingdata="";
        for (int i=0;i<(1372*80/100);i++){  //egitim verisi toplam verinin yüzde 80'i kadar
            trainingdata+=veri[rnd.nextInt(1372)]+"\n";
        }
        String testdata="";
        for (int i=0;i<(1372*20/100);i++){  //test verisi toplam verinin yüzde 20'i kadar
            testdata+=veri[rnd.nextInt(1372)]+"\n";
        }
        double[] gelendegerler=SMO_Algorithm.SVM_test(testdata,SMO_Algorithm.SVM_train(trainingdata,0,0,0));
        int dogrusayisi=0;
        int yanlissayisi=0;
        for (int i=0;i<(1372*20/100);i++){

            if(gelendegerler[i]==-1)
                yanlissayisi++;
            else
                dogrusayisi++;
        }
        double dogrulukorani=dogrusayisi/((1372.0*20.0/100)/100);
        double yanlisorani=yanlissayisi/((1372.0*20.0/100)/100);
        text1.setText("Dogru tahmin oranı = %"+ Double.toString(dogrulukorani).substring(0,5));
        text2.setText("Yanlis tahmin oranı = %"+ Double.toString(yanlisorani).substring(0,5));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
