package fix

import scalafix.v1._
import scala.meta._

class JFXApp3Migration extends SemanticRule("JFXApp3Migration") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      // Imports
      case jfxapp@Importee.Name(Name.Indeterminate("JFXApp"))            => Patch.replaceTree(jfxapp, "JFXApp3")
      case jfxapp_stage@Importer(Term.Select(_, Term.Name("JFXApp")), _) =>
        Patch.replaceTree(jfxapp_stage, "scalafx.application.JFXApp3.PrimaryStage")
      // Main Object
      case mainObj@Defn.Object(_, _, t@Template(_, List(Init(Type.Name("JFXApp"), _, _)), _, st)) =>
        Patch.replaceTree(
          mainObj,
          mainObj
            .copy(
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
                      case privVal@Defn.Val(List(Mod.Private(_)), _, _, _)       => privVal.copy(mods = List.empty)
                      case privDef@Defn.Def(List(Mod.Private(_)), _, _, _, _, _) => privDef.copy(mods = List.empty)
                      // Rewrite PrimaryStage assignment when not explicitly imported
                      case jfxAssgt@Term.Assign(
                      _,
                      trm@Term.NewAnonymous(
                      tpl@Template(_, Init(Type.Select((ref, name)), a, b) :: iniTail, _, _)
                      )
                      ) if ref.syntax == "JFXApp" =>
                        jfxAssgt.copy(rhs =
                          trm.copy(templ =
                            tpl.copy(inits = Init(Type.Select(Term.Name("JFXApp3"), name), a, b) :: iniTail)
                          )
                        )
                      case n                      => n
                    })
                  )
                )
              )
            )
            .syntax
        )
    }.asPatch
  }
}
