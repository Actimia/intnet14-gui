package se.edstrompartner.intnet14;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("test gui");
        stage.setResizable(false);

        Button btn = new Button("testing");
        btn.setId("text");

        TextArea text = new TextArea("hello input");
        text.setEditable(false);
        text.setFocusTraversable(false);
        text.setId("text");
        text.setWrapText(true);

        TextField input = new TextField("hello world");
        input.setId("text");
        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!input.getText().isEmpty()) {
                    text.appendText("\n");
                    text.appendText(input.getText());
                    input.setText("");
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(text);
        root.setBottom(input);
        root.setTop(btn);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Main.class.getResource("chat.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
