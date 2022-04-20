// package functionalscalaws

// import functionalscalaws.UserProgram._
// import functionalscalaws.configuration._
// import functionalscalaws.persistence._
// import zio.ZLayer

// object Layers {
//   type AppEnv =  Persistence[User] with UserProgram

//   object live {
//     // private val logLayer                         = rootLayer >+> consoleLogger
//     // private val persistenceLayer                 = logLayer >+> inMemory(Vector.empty)
//     // private val programLayer                     = persistenceLayer >+>  Service.live
    
//     val appLayer: ZLayer[Any, Throwable, AppEnv] =  ZLayer.make[AppEnv](inMemory(Vector.empty),Service.live) // liveConfig ++ programLayer
//   }
// }
