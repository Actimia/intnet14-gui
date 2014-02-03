package se.edstrompartner.intnet14;

import java.io.IOException;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import se.edstrompartners.intnet14.lab1.ChatClient;
import se.edstrompartners.intnet14.lab1.ChatGUI;

public class Main extends Application {

    private ChatClient cc;
    private WebEngine engine;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("test gui");
        stage.setResizable(false);

        WebView browser = new WebView();
        browser.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                Set<Node> deadSeaScrolls = browser.lookupAll(".scroll-bar");
                for (Node scroll : deadSeaScrolls) {
                    scroll.setVisible(false);
                }
            }
        });
        engine = browser.getEngine();
        engine.load("http://www.edstrompartners.se/base.html");

        TextField input = new TextField("hello input");
        input.setId("text");
        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!input.getText().isEmpty()) {
                    if (cc != null && cc.input(input.getText())) {
                        try {
                            stop();
                            cc = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        input.setStyle("-fx-font-family: Consolas; -fx-font-size: 20px; "
                + "-fx-background-color: #000000; -fx-text-fill: #00ff00; "
                + "-fx-highlight-text-fill: #000000; -fx-highlight-fill: #00ff00;");

        BorderPane root = new BorderPane();
        root.setCenter(browser);
        root.setBottom(input);
        root.setStyle("-fx-background-color: #000000");

        Scene scene = new Scene(root, 800, 588);

        stage.setScene(scene);

        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (cc != null) {
                    cc.close();
                }
            }
        });
        stage.show();

        try {
            cc = ChatClient.connect("localhost", 8080, "Actimia", new ChatGUI() {
                @Override
                public void print(final String type, final String text) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            engine.executeScript(addText(type, text));
                        }
                    });
                }
            });
        } catch (IOException e) {
            engine.executeScript(addText("error",
                    "A connection to the server could not be established."));
        }

    }

    private String addText(String cls, String txt) {
        return String.format("printMessage('%s','%s');", cls, txt);
    }

}
