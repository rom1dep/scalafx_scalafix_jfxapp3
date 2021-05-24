/*
rule = JFXApp3Migration
*/
package fix

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text

object JFXAppDemo extends JFXApp {
  private val priv: String = "private!"
  stage = new PrimaryStage {
    scene = new Scene {
      content = new HBox {
        children = Seq( new Text("Hello World!")  )
      }
    }
  }

  private def aFunction(): Unit = {
    2 + 2
  }
}
