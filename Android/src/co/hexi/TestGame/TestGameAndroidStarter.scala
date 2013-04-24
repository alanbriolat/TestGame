package co.hexi.TestGame

import com.badlogic.gdx.backends.android.{AndroidApplicationConfiguration, AndroidApplication}
import android.os.Bundle
import hexico.testgame.TestGame

class TestGameAndroidStarter extends AndroidApplication {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val cfg = new AndroidApplicationConfiguration
    cfg.useAccelerometer = false
    cfg.useCompass = false
    cfg.useWakelock = false
    cfg.useGL20 = true
    initialize(new TestGame, cfg)
  }
}
