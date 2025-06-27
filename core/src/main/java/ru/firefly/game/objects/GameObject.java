package ru.firefly.game.objects;

import static ru.firefly.game.GameSettings.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {

    public short cBits;

    public int width;
    public int height;

    public Body body;
    Texture texture;

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;

        texture = new Texture(texturePath);
        body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture,
            getX() - (width / 2f),
            getY() - (height / 2f),
            width,
            height);
    }

    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public void setX(int x) {
        body.setTransform(x * SCALE, body.getPosition().y, 0);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * SCALE, 0);
    }

    public Rectangle getBounds() {
        return new Rectangle();
    }
    protected Body createBody(float x, float y, World world) {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width * SCALE / 2f , height * SCALE / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1f;

        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        polygonShape.dispose();

        body.setTransform((x + width / 2f) * SCALE, (y + height / 2f) * SCALE, 0);
        return body;
    }

    public void hit() {}

}
