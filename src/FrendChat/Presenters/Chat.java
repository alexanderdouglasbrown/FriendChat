package FrendChat.Presenters;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

public class Chat {
    @FXML
    private WebView webChat;
    @FXML
    private ListView lstUsers;

    String webHTML;

    public void initialize() {
        webChat.setContextMenuEnabled(false);

        webHTML = "<style>" +
                "body{" +
                "font-family: \"Arial\";" +
                "}" +
                "</style>" +
                "<b><font color='blue'>Lorem</font></b> ipsum dolor sit amet, consectetur adipiscing elit. Donec quam lacus, convallis sit amet leo ut, tempus commodo tortor. Pellentesque eu libero lorem. Nulla facilisi. Suspendisse purus diam, tincidunt sit amet eros sit amet, egestas molestie quam. In tincidunt enim felis, maximus suscipit lorem viverra vel. Etiam dolor leo, luctus ut enim in, vehicula molestie eros. Vestibulum eu congue lacus, at dictum nisl. Ut vel nulla ut metus porta ullamcorper.\n" +
                "\n" +
                "Duis vehicula fringilla volutpat. Phasellus mattis feugiat nisl eu porttitor. Ut dapibus consequat luctus. Donec vitae massa rhoncus, feugiat dui porta, maximus est. Vivamus ac maximus dui, vel laoreet mauris. Aliquam erat volutpat. Pellentesque dolor ex, condimentum ut metus in, malesuada porta nunc. Maecenas ligula lorem, lacinia at hendrerit vitae, bibendum ac tellus. Pellentesque condimentum molestie orci ac mollis. Vestibulum lectus metus, faucibus eget placerat ut, auctor eget massa. Etiam eget velit tellus. Donec ullamcorper ut nisi id auctor. Sed eget sollicitudin ante.\n" +
                "\n" +
                "Nam eleifend vel leo sit amet congue. Sed tristique sollicitudin velit vitae eleifend. Nunc rutrum facilisis libero, eget ultricies nunc consectetur ac. Quisque a erat vel augue porta rutrum. Vestibulum ullamcorper sed neque nec semper. Maecenas at felis in dui facilisis rhoncus. Ut magna enim, tincidunt et sollicitudin vitae, egestas ac diam. Sed sagittis massa et lorem rutrum tempor. Phasellus varius ultrices facilisis. Etiam sollicitudin eros sit amet efficitur rhoncus. Quisque gravida lobortis odio, quis commodo turpis dictum et. Suspendisse varius libero lorem, eu mollis justo ornare sit amet. Integer ut accumsan neque, nec interdum massa. In a congue diam. Morbi ornare, augue vitae sodales fringilla, lectus quam sodales diam, eu lacinia est quam a urna.\n" +
                "\n" +
                "Sed pellentesque velit vitae risus egestas pretium non vitae felis. Sed blandit molestie egestas. Sed molestie vitae turpis eu pretium. Fusce iaculis congue lorem at egestas. Curabitur pharetra nisi sit amet leo maximus pretium. Aenean sed vulputate arcu. Aliquam eros nisl, congue vel blandit a, dignissim eu felis. Nam vel elit lobortis, pulvinar orci in, ultrices eros. Nam efficitur nunc in rutrum tempus. Proin fermentum massa ac sagittis lobortis. Donec condimentum velit non pellentesque scelerisque. Aliquam sit amet condimentum tortor, non lobortis lacus. Morbi sodales finibus ipsum, nec accumsan lacus dignissim et. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" +
                "\n" +
                "Sed lacinia, nulla vitae viverra elementum, tortor purus feugiat nunc, sed faucibus orci tortor ut mi. Vivamus nec turpis euismod, lacinia velit et, mattis diam. Vivamus ac convallis nisl, ac imperdiet augue. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ullamcorper velit quis mi tempor faucibus. Cras blandit lorem molestie, vehicula nisl vel, ornare lacus. Pellentesque aliquam ligula in lacus porta hendrerit. Vivamus non dictum diam, at tincidunt nibh.";

        webChat.getEngine().loadContent(webHTML);

        Text user = new Text("Alex");
        user.setFont(Font.font("Arial", FontWeight.BOLD,14));
        user.setFill(Color.TEAL);

        lstUsers.getItems().addAll(user, "Becky", "Killme", "Jojo");
        lstUsers.getItems().remove("Killme");
    }

    public void btnSend() {
        //TODO Escape HTML things (< (&lt;) > (&gt;) "(&quot;) '(&#39;) and &(&amp;))
        String textToAdd = "<div><b><font color='teal'>Alex</font>: </b>Hello World!</div>";
        webChat.getEngine().executeScript("document.body.innerHTML += \"" + textToAdd + "\";");
        webChat.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}
