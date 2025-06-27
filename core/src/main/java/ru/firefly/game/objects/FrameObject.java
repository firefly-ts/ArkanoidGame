package ru.firefly.game.objects;


import static ru.firefly.game.GameSettings.SCALE;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class FrameObject extends GameObject{

    public FrameObject(int x, int y, int width, int height, String texturePath, short bit, World world) {
        super(texturePath, x, y, width, height, bit, world);
        body.setLinearDamping(30);
    }

    protected Body createBody(float x, float y, World world) {

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
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

