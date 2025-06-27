package ru.firefly.game.managers;

import com.badlogic.gdx.physics.box2d.*;

import ru.firefly.game.GameSettings;
import ru.firefly.game.objects.BallObject;
import ru.firefly.game.objects.GameObject;
import ru.firefly.game.screens.GameScreen;


public class ContactManager {

    World world;

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDefA = fixA.getFilterData().categoryBits;
                int cDefB = fixB.getFilterData().categoryBits;

                if (cDefB == GameSettings.BALL_BIT && cDefA == GameSettings.PADDLE_BIT) {
// Получаем мировую точку контакта
//                    WorldManifold worldManifold = contact.getWorldManifold();
//                    Vector2 contactPoint = worldManifold.getPoints()[0]; // Получаем первую точку контакта (обычно достаточно)
//                     Теперь у вас есть мировая точка контакта (contactPoint)
//                     Вы можете использовать ее для различных целей (например, для создания эффекта искр)
//                    System.out.println("Contact at: " + contactPoint.x + ", " + contactPoint.y);

                    ((GameObject) fixB.getUserData()).hit();
                    ((GameObject) fixA.getUserData()).hit();
                }

                if (cDefB == GameSettings.BALL_BIT && cDefA == GameSettings.BLOCK_BIT) {
                    ((GameObject) fixA.getUserData()).hit();
                    ((GameObject) fixB.getUserData()).hit();
                }

                if (cDefB == GameSettings.BALL_BIT && cDefA == GameSettings.FRAME_BOTTOM_BIT) {
                    ((BallObject) fixB.getUserData()).hitBottom();
                    GameScreen.isPlaying = true;
                }

            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

    }

}
