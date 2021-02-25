package ru.sk1ly.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.sk1ly.RaindropGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Raindrop Game";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new RaindropGame(), config);
    }
}
