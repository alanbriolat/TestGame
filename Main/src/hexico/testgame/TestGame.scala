package hexico.testgame

import scala.collection.JavaConversions._
import com.badlogic.gdx.{utils, Gdx, ApplicationListener}
import com.badlogic.gdx.graphics.{GL10, OrthographicCamera, Texture}
import com.badlogic.gdx.audio.{Music, Sound}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.{Vector3, MathUtils, Rectangle}
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.Input.Keys

class TestGame extends ApplicationListener {
  private var dropImage: Texture = _
  private var bucketImage: Texture = _
  private var dropSound: Sound = _
  private var rainMusic: Music = _
  private var camera: OrthographicCamera = _
  private var batch: SpriteBatch = _
  private var bucket: Rectangle = _
  private var raindrops: utils.Array[Rectangle] = _
  private var lastDropTime: Long = _

  override def create() {
    // Load images
    dropImage = new Texture(Gdx.files.internal("drop.png"))
    bucketImage = new Texture(Gdx.files.internal("bucket.png"))

    // Load audio
    dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
    rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))

    // Start background music
    rainMusic.setLooping(true)
    rainMusic.play()

    // Create camera and sprite batch
    camera = new OrthographicCamera
    camera.setToOrtho(false, 800, 480)
    batch = new SpriteBatch

    // Create a rectangle for the bucket
    bucket = new Rectangle
    bucket.x = 800 / 2 - 64 / 2
    bucket.y = 20
    bucket.width = 64
    bucket.height = 64

    // Create raindrops array and spawn first raindrop
    raindrops = new utils.Array[Rectangle]
    spawnRaindrop()
  }

  private def spawnRaindrop() {
    val raindrop = new Rectangle
    raindrop.x = MathUtils.random(0, 800 - 16)
    raindrop.y = 480
    raindrop.width = 16
    raindrop.height = 16
    raindrops.add(raindrop)
    lastDropTime = TimeUtils.nanoTime()
  }

  override def render() {
    // Clear the screen (to blue)
    Gdx.gl.glClearColor(0, 0, 0.2f, 1)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    // Update camera matrices
    camera.update()

    // Tell SpriteBatch to use our camera transformation
    batch.setProjectionMatrix(camera.combined)

    // Draw stuff in the batch
    batch.begin()
    batch.draw(bucketImage, bucket.x, bucket.y)
    for (raindrop: Rectangle <- raindrops)
      batch.draw(dropImage, raindrop.x, raindrop.y)
    batch.end()

    if (Gdx.input.isTouched) {
      val touchPos = new Vector3(Gdx.input.getX, Gdx.input.getY, 0)
      camera.unproject(touchPos)
      bucket.x = touchPos.x - bucket.width / 2
    }

    if (Gdx.input.isKeyPressed(Keys.LEFT))
      bucket.x -= 200 * Gdx.graphics.getDeltaTime
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
      bucket.x += 200 * Gdx.graphics.getDeltaTime

    if (TimeUtils.nanoTime - lastDropTime > 1000000000)
      spawnRaindrop()

    val iter = raindrops.iterator
    while (iter.hasNext) {
      val raindrop = iter.next()
      raindrop.y -= 200 * Gdx.graphics.getDeltaTime
      if (raindrop.y + raindrop.height < 0)
        iter.remove()
      if (raindrop.overlaps(bucket)) {
        dropSound.play()
        iter.remove()
      }
    }
  }

  override def dispose() {
    dropImage.dispose()
    bucketImage.dispose()
    dropSound.dispose()
    rainMusic.dispose()
    batch.dispose()
  }

  override def resize(width: Int, height: Int) {

  }

  override def pause() {

  }

  override def resume() {

  }
}
