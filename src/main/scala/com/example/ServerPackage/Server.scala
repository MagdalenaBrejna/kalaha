package com.example.ServerPackage

import akka.util.Timeout
import com.example.GameboardPackage.Game
import com.example.UserPackage.{Player, User}

import java.util.concurrent.{TimeUnit, TimeoutException}
import javax.swing.JTextPane
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future, TimeoutException}
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.{Failure, Success, Try}

class Server(private val game: Game, private val gamePane: JTextPane) {

  implicit val ec = ExecutionContext.global

  def timeoutFuture[A](f: Future[A]): Try[A] =
    Try { Await.result(f, 10.seconds) }

  def stopGame(): Unit = {
    gamePane.setText(game.printGameResult())
  }

  def startGame(): Unit = {
    gamePane.setText(game.printGameBoard)
  }

  def processGame(player: Player): Unit = {
    try {
      val result = Future {player.moveRequest(game); true}
      Await.result(result, 4.second)
    }catch{ case _: TimeoutException => gamePane.setText("30 seconds without a move. Game over")}


    //player.moveRequest(game)
    //FutureUtil.futureWithTimeout(Future{player.moveRequest(game)}, new FiniteDuration(30, TimeUnit.SECONDS))
    //val f = Future{player.moveRequest(game)}
    //try {
    //  timeoutFuture(f)
    //}catch{
    //  case e: TimeoutException => System.out.println("!")
    //}
  }

  def moveReceived(player: Player, userField: Int): Unit = {
    game.processPlayerMove(userField)
    game.changeActivePlayer()
    gamePane.setText(game.printGameBoard)
    if (game.checkIfEnd()) stopGame()
    else processGame(player)
  }

  def processMove(future: Future[Any]): Unit = {
    future onComplete {
      case Success(userField) => userField
      case Failure(_) => gamePane.setText("30 seconds without a move. Game over")
    }
  }
}