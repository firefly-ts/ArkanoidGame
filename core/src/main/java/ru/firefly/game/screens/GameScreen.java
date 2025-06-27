package ru.firefly.game.screens;

import static ru.firefly.game.GameSettings.BALL_START_X;
import static ru.firefly.game.GameSettings.BALL_START_Y;
import static ru.firefly.game.GameSettings.CLEAR;
import static ru.firefly.game.GameSettings.PADDLE_HEIGHT;
import static ru.firefly.game.GameSettings.PADDLE_START_Y;
import static ru.firefly.game.GameSettings.PADDLE_WIDTH;
import static ru.firefly.game.GameSettings.SCREEN_HEIGHT;
import static ru.firefly.game.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.firefly.game.GameResources;
import ru.firefly.game.GameSession;
import ru.firefly.game.GameSettings;
import ru.firefly.game.GameState;
import ru.firefly.game.MyGdxGame;
import ru.firefly.game.components.ButtonView;
import ru.firefly.game.components.ImageView;
import ru.firefly.game.components.LiveView;
import ru.firefly.game.components.RecordsListView;
import ru.firefly.game.components.TextView;
import ru.firefly.game.managers.ContactManager;
import ru.firefly.game.managers.MemoryManager;
import ru.firefly.game.objects.BlockObject;
import ru.firefly.game.objects.FrameObject;
import ru.firefly.game.objects.PaddleObject;
import ru.firefly.game.objects.BallObject;

public class GameScreen extends ScreenAdapter {
    public static boolean isPlaying = false;

    MyGdxGame myGdxGame;
    GameSession gameSession;
    PaddleObject paddleObject;
    BallObject ballObject;
    private ArrayList<BlockObject> blocks;
    FrameObject frame;

    ContactManager contactManager;

    // PLAY state UI
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        paddleObject = new PaddleObject(
            SCREEN_WIDTH / 2, PADDLE_START_Y,
            PADDLE_WIDTH, PADDLE_HEIGHT,
            GameResources.PADDLE_IMG_PATH,
            myGdxGame.world
        );
        ballObject = new BallObject(
            BALL_START_X, BALL_START_Y,
            GameSettings.BALL_SIZE, GameSettings.BALL_SIZE,
            GameResources.BALL_IMG_PATH,
            myGdxGame.world
        );
        blocks = createBlocks();

        frame = new FrameObject(
            0, 0,
            SCREEN_WIDTH, 8,
            GameResources.FRAME_H_IMG_PATH,
            GameSettings.FRAME_BOTTOM_BIT,
            myGdxGame.world
        );

        createUI();
    }

    private void createUI() {
        topBlackoutView = new ImageView(0, GameSettings.SCREEN_HEIGHT - 50, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(300, GameSettings.SCREEN_HEIGHT - 40);
        scoreTextView = new TextView(myGdxGame.commonBlackFont, 50, GameSettings.SCREEN_HEIGHT - 40);
        pauseButton = new ButtonView(
            SCREEN_WIDTH - 100, SCREEN_HEIGHT - 45,
            35, 40,
            GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);

        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 300, 800, "Pause");

        homeButton = new ButtonView(
            350, 200,
            280, 70,
            myGdxGame.commonBlackFont,
            GameResources.BUTTON_LONG_BG_IMG_PATH,
            "Home"
        );

        continueButton = new ButtonView(
            680, 200,
            280, 70,
            myGdxGame.commonBlackFont,
            GameResources.BUTTON_LONG_BG_IMG_PATH,
            "Continue"
        );

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 400);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 480, 500, "Last records");
        homeButton2 = new ButtonView(
            500, 100,
            280, 70,
            myGdxGame.commonBlackFont,
            GameResources.BUTTON_LONG_BG_IMG_PATH,
            "Home"
        );

    }

    private ArrayList<BlockObject> createBlocks() {
        ArrayList<BlockObject> blocks = new ArrayList<>();
        // Логика создания блоков (по рядам, столбцам)
        for (int row = 0; row < GameSettings.NUM_ROWS; row++) {
            for (int col = 0; col < GameSettings.NUM_COLS; col++) {
                int x = GameSettings.BLOCK_START_X + col * (GameSettings.BLOCK_WIDTH + GameSettings.BLOCK_PADDING);
                int y = GameSettings.BLOCK_START_Y - row * (GameSettings.BLOCK_HEIGHT + GameSettings.BLOCK_PADDING);
                blocks.add(new BlockObject(
                    GameResources.BLOCK_IMG_PATH,
                    x, y,
                    GameSettings.BLOCK_WIDTH, GameSettings.BLOCK_HEIGHT,
                    myGdxGame.world));
            }
        }
        return blocks;
    }

    @Override
    public void show() {
        restartGame();
    }


    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {

            if (!ballObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());   // ============================
            }

            if (isPlaying){
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.upsSound.play(0.2f);
                isPlaying = false;
            }
            updateBlocks();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            liveView.setLeftLives(ballObject.getLivesLeft());

            myGdxGame.stepWorld();
        }
    draw();
}


    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state){

                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    paddleObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(CLEAR);
        ballObject.putInFrame();


        myGdxGame.batch.begin();
            frame.draw(myGdxGame.batch);
            for (BlockObject block : blocks) {
                block.draw(myGdxGame.batch);
            }
            paddleObject.draw(myGdxGame.batch);
            ballObject.draw(myGdxGame.batch);

            topBlackoutView.draw(myGdxGame.batch);
            scoreTextView.draw(myGdxGame.batch);
            liveView.draw(myGdxGame.batch);
            pauseButton.draw(myGdxGame.batch);

            if (gameSession.state == GameState.PAUSED) {
                fullBlackoutView.draw(myGdxGame.batch);
                pauseTextView.draw(myGdxGame.batch);
                homeButton.draw(myGdxGame.batch);
                continueButton.draw(myGdxGame.batch);

            } else if (gameSession.state == GameState.ENDED) {
                fullBlackoutView.draw(myGdxGame.batch);
                recordsTextView.draw(myGdxGame.batch);
                recordsListView.draw(myGdxGame.batch);
                homeButton2.draw(myGdxGame.batch);
            }

        myGdxGame.batch.end();
    }

    private void updateBlocks() {
        for (int i = 0; i < blocks.size(); i++) {

            if (!blocks.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }

            if (blocks.get(i).hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(blocks.get(i).body);
                blocks.remove(i--);
            }
        }
    }


    private void restartGame() {

        for (int i = 0; i < blocks.size(); i++) {
            myGdxGame.world.destroyBody(blocks.get(i).body);
            blocks.remove(i--);
        }

        if (paddleObject != null) {
            myGdxGame.world.destroyBody(paddleObject.body);
        }

        paddleObject = new PaddleObject(
            SCREEN_WIDTH / 2, PADDLE_START_Y,
            PADDLE_WIDTH, PADDLE_HEIGHT,
            GameResources.PADDLE_IMG_PATH,
            myGdxGame.world
        );

        if (ballObject != null) {
            myGdxGame.world.destroyBody(ballObject.body);
        }
        ballObject = new BallObject(
            BALL_START_X, BALL_START_Y,
            GameSettings.BALL_SIZE, GameSettings.BALL_SIZE,
            GameResources.BALL_IMG_PATH,
            myGdxGame.world
        );

        blocks.clear();
        blocks = createBlocks();

        if (frame != null) {
            myGdxGame.world.destroyBody(frame.body);
        }
        frame = new FrameObject(
            0, 0,
            SCREEN_WIDTH, 8,
            GameResources.FRAME_H_IMG_PATH,
            GameSettings.FRAME_BOTTOM_BIT,
            myGdxGame.world
        );

        gameSession.startGame();
    }

}
