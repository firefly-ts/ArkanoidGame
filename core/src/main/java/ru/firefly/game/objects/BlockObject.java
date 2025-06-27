package ru.firefly.game.objects;

import static ru.firefly.game.GameSettings.SCALE;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ru.firefly.game.GameSettings;

public class BlockObject extends GameObject{

    public boolean wasHit;
    private int livesLeft;

    public BlockObject(String texturePath, int x, int y, int width, int height, World world) {
        super(texturePath, x, y, width, height, GameSettings.BLOCK_BIT, world);

        wasHit = false;
        livesLeft = 1;
    }


    @Override
    public void hit() {
        wasHit = true;
        livesLeft--;
    }

    public int getLivesLeft(){
        return livesLeft;
    }
    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean hasToBeDestroyed() {
        return wasHit;
    }

    protected Body createBody(float x, float y, World world) {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
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

}
