package ru.firefly.game.objects;

import static ru.firefly.game.GameSettings.PADDLE_BIT;
import static ru.firefly.game.GameSettings.PADDLE_FORCE_RATIO;
import static ru.firefly.game.GameSettings.PADDLE_START_Y;
import static ru.firefly.game.GameSettings.SCALE;
import static ru.firefly.game.GameSettings.SCREEN_WIDTH;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class PaddleObject extends GameObject {

    public PaddleObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, PADDLE_BIT, world);
        body.setLinearDamping(10);
    }

    public void move(Vector3 vector3) {
        body.applyForceToCenter(new Vector2(
                (vector3.x - getX()) * PADDLE_FORCE_RATIO,
                0),
            true
        );
    }

    private void putInFrame() {

        if (getX() < width / 2) {
            setX(width / 2);
        }
        if (getX() > (SCREEN_WIDTH - width / 2)) {
            setX(SCREEN_WIDTH - width / 2);
        }

        if (getY() != PADDLE_START_Y) {
            setY(PADDLE_START_Y);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        putInFrame();
        super.draw(batch);
    }

    @Override
    public void hit() {

    }

    @Override
    protected Body createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width * SCALE / 2f , height * SCALE / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.1f;  // Не имеет значения для KinematicBody
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // Важно: отсутствие отскока

        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        polygonShape.dispose();

        body.setTransform(x * SCALE, y * SCALE, 0);// Позиция платформы
        body.setFixedRotation(true); // чтобы не вращалась

        return body;

    }

}
