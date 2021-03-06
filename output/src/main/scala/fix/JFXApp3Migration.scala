package fix

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text

object JFXAppDemo extends JFXApp3 {
  override def start(): Unit = {
    val priv: String = "private!"
    stage = new PrimaryStage {
      scene = new Scene {
        content = new HBox {
          children = Seq(new Text("Hello World!"))
        }
      }
    }
    def aFunction(): Unit = {
      2 + 2
    }
  }
}

object JFXAppDemo2 extends JFXApp3 {
  override def start(): Unit = {
    val priv: String = "private!"
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene {
        content = new HBox {
          children = Seq(new Text("Hello World!"))
        }
      }
    }
    def aFunction(): Unit = {
      2 + 2
    }
  }
}
