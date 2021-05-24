package fix

import scalafix.v1._
import scala.meta._

class JFXApp3Migration extends SemanticRule("JFXApp3Migration") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    //    println("Tree.syntax: " + doc.tree.syntax)
    //    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    doc.tree.collect {
      // Imports
      case jfxapp@Importee.Name(Name.Indeterminate("JFXApp")) => Patch.replaceTree(jfxapp, "JFXApp3")
      case jfxapp_stage@Term.Name("JFXApp")                   => Patch.replaceTree(jfxapp_stage, "JFXApp3")
      // Main Object
      case mainObj@Defn.Object(_, _, t@Template(_, List(Init(Type.Name("JFXApp"), _, _)), _, st)) =>
        Patch.replaceTree(mainObj,
          mainObj.copy(
            templ = t.copy(
              // Update extend
              inits = List(Init(Type.Name("JFXApp3"), Name.Anonymous(), List())),
              // Update object body
              stats = List(
                // Def
                Defn.Def(
                  mods = List(Mod.Override()),
                  name = Term.Name("start"),
                  tparams = List(),
                  paramss = List(List()),
                  decltpe = Some(Type.Name("Unit")),
                  body = Term.Block(stats = st.collect {
                    // Drop private accessors
                    case defn@Defn.Val(List(Mod.Private(_)), _, _, _) => defn.copy(mods = List.empty)
                    case n                                            => n
                  })
                )
              )
            )
          ).syntax
        )
    }.asPatch
  }
}

