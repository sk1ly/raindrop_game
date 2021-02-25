package ru.sk1ly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class RaindropGame extends ApplicationAdapter {

//    TODO Разобраться, почему не работает прозрачность у изображений

    Texture raindropImage;
    Texture bucketImage;

    Rectangle bucket;
    Array<Rectangle> raindrops;

    Sound dropSound;
    Music backgroundMusic;

    Vector3 touchPos;
    SpriteBatch batch;
    OrthographicCamera camera;

    long lastDropTime;

    private final static int BUCKET_PX_WIDTH = 128;
    private final static int BUCKET_PX_HEIGH = 83;
    private final static int RAINDROP_PX_WIDTH = 128;
    private final static int RAINDROP_PX_HEIGH = 114;

    @Override
    public void create() {
        raindropImage = new Texture(Gdx.files.internal("raindrop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        bucket = new Rectangle();
        bucket.x = 800 / 2 - BUCKET_PX_WIDTH / 2;
        bucket.y = 20;
        bucket.width = BUCKET_PX_WIDTH;
        bucket.height = BUCKET_PX_HEIGH;

        raindrops = new Array<>();
        spawnRaindrop();

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_song.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        touchPos = new Vector3();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
            batch.draw(raindropImage, raindrop.x, raindrop.y);
        }
        batch.end();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - BUCKET_PX_WIDTH / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 400 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 400 * Gdx.graphics.getDeltaTime();
        }
        if (bucket.x < 0) {
            bucket.x = 0;
        }
        if (bucket.x > 800 - BUCKET_PX_WIDTH) {
            bucket.x = 800 - BUCKET_PX_WIDTH;
        }
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop();
        }
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        dropSound.dispose();
        bucketImage.dispose();
        raindropImage.dispose();
        batch.dispose();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - RAINDROP_PX_WIDTH);
        raindrop.y = 480;
        raindrop.width = RAINDROP_PX_WIDTH;
        raindrop.height = RAINDROP_PX_HEIGH;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
