package ru.firefly.game.screens;

import static ru.firefly.game.GameResources.BLACKOUT_MIDDLE_IMG_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.firefly.game.GameResources;
import ru.firefly.game.GameSettings;
import ru.firefly.game.MyGdxGame;
import ru.firefly.game.components.*;
import ru.firefly.game.managers.MemoryManager;


public class SettingsScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;

    MovingBackgroundView backgroundView;
    TextView titleTextView;
    ImageView blackoutImageView;
    ButtonView returnButton;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;

    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        float hWin = GameSettings.SCREEN_HEIGHT;

        titleTextView = new TextView(myGdxGame.largeWhiteFont, 560, hWin - 150, "Settings");
        blackoutImageView = new ImageView(365, 50, BLACKOUT_MIDDLE_IMG_PATH);
        clearSettingView = new TextView(myGdxGame.commonWhiteFont, 450, 250, "clear records");

        musicSettingView = new TextView(
            myGdxGame.commonWhiteFont,
            450, 400,
            "music: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );

        soundSettingView = new TextView(
            myGdxGame.commonWhiteFont,
            450, 325,
            "sound: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );

        returnButton = new ButtonView(
            GameSettings.SCREEN_WIDTH / 2f - 80, 100,
            160, 70,
            myGdxGame.commonBlackFont,
            GameResources.BUTTON_SHORT_BG_IMG_PATH,
            "return"
        );

    }

    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        blackoutImageView.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        clearSettingView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (clearSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                clearSettingView.setText("clear records (cleared)");
            }
            if (musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                myGdxGame.audioManager.updateSoundFlag();
            }
        }
    }

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }
}
