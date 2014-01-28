package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.socketClient.SocketClient;

import java.io.IOException;

public class Controller {
    public TextArea inputArea;
    public TextField outputFeld;
    public TextField host;
    public TextField port;
    public TextField myId;
    SocketClient socketClient;
    private String EOL = "\n";
    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public void onConnectClick(ActionEvent actionEvent) throws IOException {
        String hostName = host.getText();
        int portNummer = Integer.valueOf(port.getText());
        String clientId = myId.getText();
        SocketClient socketClient = new SocketClient(hostName, portNummer, clientId);
        setSocketClient(socketClient);
        socketClient.setController(this);
        socketClient.start();
    }

    public void showMessage(String message){
        inputArea.appendText(message + EOL);
    }
    public void onSendClick(ActionEvent actionEvent) {
        String text = outputFeld.getText();
        socketClient.writeMessageInSocket(text);
    }
}
