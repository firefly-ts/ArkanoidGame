package ru.firefly.game.components;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.firefly.game.GameSettings;


public class BackgroundView extends View {

    Texture texture;

    int texture1Y;
    int texture2Y;
    int speed = 2;

    public BackgroundView(String pathToTexture) {
        super(0, 0);
        texture1Y = 0;
        texture2Y = GameSettings.SCREEN_HEIGHT;
        texture = new Texture(pathToTexture);
    }

    public void move() {
        texture1Y -= speed;
        texture2Y -= speed;

        if (texture1Y <= -GameSettings.SCREEN_HEIGHT) {
            texture1Y = GameSettings.SCREEN_HEIGHT;
        }
        if (texture2Y <= -GameSettings.SCREEN_HEIGHT) {
            texture2Y = GameSettings.SCREEN_HEIGHT;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, 0, texture1Y, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        batch.draw(texture, 0, texture2Y, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
