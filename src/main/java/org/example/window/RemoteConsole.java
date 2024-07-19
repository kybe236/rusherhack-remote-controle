package org.example.window;

import org.example.CommandHandler.Both;
import org.example.CommandHandler.Client;
import org.example.CommandHandler.Server;
import org.example.Config;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.window.ResizeableWindow;
import org.rusherhack.client.api.render.graphic.TextureGraphic;
import org.rusherhack.client.api.ui.window.content.ComboContent;
import org.rusherhack.client.api.ui.window.content.component.ButtonComponent;
import org.rusherhack.client.api.ui.window.content.component.TextFieldComponent;
import org.rusherhack.client.api.ui.window.view.RichTextView;
import org.rusherhack.client.api.ui.window.view.TabbedView;
import org.rusherhack.client.api.ui.window.view.WindowView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RemoteConsole extends ResizeableWindow {

    public static RemoteConsole INSTANCE;

    private final TabbedView rootView;
    public final RichTextView messageView;

    public Config config = new Config();

    public RemoteConsole() {
        super("Remote Console", 200, 200);
        RusherHackAPI.getEventBus().subscribe(this);
        INSTANCE = this;

        this.setMinWidth(200);
        this.setMinHeight(200);

        URL image = null;
        try {
            image = new URL("https://mc-heads.net/avatar/kyrill2306");
            InputStream inputStream = image.openStream();
            this.setIcon(new TextureGraphic(inputStream, 64, 64));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.messageView = new RichTextView("Console", this);

        final ComboContent content = new ComboContent(this);

        final TextFieldComponent textField = new TextFieldComponent(this, "Command:", this.getWidth());
        final ButtonComponent buttonComponent = new ButtonComponent(this, "Send", () -> {
            String command = textField.getValue();
            if (command.isEmpty()) {
                return;
            }

            if (command.toLowerCase().contains("server true") || command.toLowerCase().contains("server 1")) {
                config.Server = true;
            } else if (command.toLowerCase().contains("server false") || command.toLowerCase().contains("server 0")) {
                config.Server = false;
            }

            Both.handle(command, this);

            textField.setValue("");
        });

        textField.setReturnCallback((str) -> buttonComponent.onClick());
        content.addContent(textField);
        content.addContent(buttonComponent);

        this.rootView = new TabbedView(this, List.of(this.messageView, content));
    }

    @Override
    public WindowView getRootView() {
        return this.rootView;
    }
}
