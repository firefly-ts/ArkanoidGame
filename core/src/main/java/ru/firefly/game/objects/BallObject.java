package ru.firefly.game.objects;

import static ru.firefly.game.GameSettings.BALL_BIT;
import static ru.firefly.game.GameSettings.BALL_START_SPEED_X;
import static ru.firefly.game.GameSettings.BALL_START_SPEED_Y;
import static ru.firefly.game.GameSettings.SCALE;
import static ru.firefly.game.GameSettings.SCREEN_HEIGHT;
import static ru.firefly.game.GameSettings.SCREEN_WIDTH;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;


public class BallObject extends GameObject{

    int velocityX, velocityY;
    int livesLeft;

    public BallObject(int x, int y, int width, int height, String texturePath, World world) {
        super(
            texturePath,
            x, y,
            width, height,
            BALL_BIT,
            world
        );

        velocityX = BALL_START_SPEED_X;
        velocityY = BALL_START_SPEED_Y;

        body.setLinearVelocity(new Vector2(velocityX, velocityY));

        livesLeft = 5;
    }


    public void putInFrame() {
        if (getY() >= SCREEN_HEIGHT - height / 2f || getY() <= height / 2f) {
            velocityY = -velocityY;
        }

        if (getX() >= SCREEN_WIDTH - width / 2f || getX() <= (width / 2f)) {
            velocityX = -velocityX;
        }
        body.setLinearVelocity(new Vector2(velocityX, velocityY));
    }

    @Override
    public void hit() {
        velocityY = -velocityY;
        body.setLinearVelocity(new Vector2(velocityX, velocityY));
    }

    public void hitBottom() {
        livesLeft--;
        if (isAlive()) {
            velocityY = -velocityY;
            body.setLinearVelocity(new Vector2(velocityX, velocityY));
        }
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public int getLivesLeft(){
        return livesLeft;
    }

@Override
    protected Body createBody(float x, float y, World world) {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(width * SCALE / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1f;

        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        circleShape.dispose();



        body.setTransform((x + width / 2f) * SCALE, (y + height / 2f) * SCALE, 0);
        return body;
    }

}
