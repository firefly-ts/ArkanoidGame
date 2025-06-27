package ru.firefly.game;

import com.badlogic.gdx.graphics.Color;

public class GameSettings {

// ================== ALL =========================

    public static long STARTING_TRASH_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds

    // Device settings

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;


// ================== MY =========================

    public static Color CLEAR = new Color(0, 0.9f, 0.9f, 1);

    // Размеры платформы
    public static final int PADDLE_WIDTH = SCREEN_WIDTH / 6;
    public static final int PADDLE_HEIGHT = PADDLE_WIDTH / 10;
    public static final int PADDLE_START_Y = PADDLE_HEIGHT + 2;
    public static float PADDLE_FORCE_RATIO = 10;


    // Размеры мяча
    public static final int BALL_SIZE = 60;
    public static final int BALL_START_X = 50;
    public static final int BALL_START_Y = 200;
    public static final int BALL_START_SPEED_X = 20;
    public static final int BALL_START_SPEED_Y = -30;

    // Размеры блоков
    public static final int NUM_ROWS = 5;
    public static final int NUM_COLS = 8;
    public static final int BLOCK_PADDING = 20;
    public static final int BLOCK_START_X = 50;
    public static final int BLOCK_START_Y = SCREEN_HEIGHT - 110;
    public static final int BLOCK_WIDTH = (SCREEN_WIDTH - 2 * BLOCK_START_X - (NUM_COLS - 1) * BLOCK_PADDING) / 8;
    public static final int BLOCK_HEIGHT = BLOCK_WIDTH / 3;


    public static final short BLOCK_BIT = 2;
    public static final short PADDLE_BIT = 4;
    public static final short BALL_BIT = 8;
    public static final short FRAME_BOTTOM_BIT = 64;

}
