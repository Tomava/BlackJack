package blackjack;

import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.PeliIkkuna;

public class BlackJackMain extends Application {
    private PeliIkkuna peliIkkuna;

    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        peliIkkuna = new PeliIkkuna(17);
        peliIkkuna.setVisible(true);
        
    }

}
