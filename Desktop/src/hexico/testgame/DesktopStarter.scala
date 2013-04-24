package hexico.testgame

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object DesktopStarter extends App {
  val cfg = new LwjglApplicationConfiguration
  cfg.title = "TestGame"
  cfg.useGL20 = true
  cfg.width = 800
  cfg.height = 480
  new LwjglApplication(new TestGame, cfg)
}
