package com.example.ServerPackage

import scala.concurrent.Promise
import java.util.concurrent.TimeoutException
import java.util.{Timer, TimerTask}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.postfixOps

object FutureUtil {

  val timer: Timer = new Timer(true)

  def futureWithTimeout[T](future : Future[T], timeout : FiniteDuration)(implicit ec: ExecutionContext): Future[T] = {
    val p = Promise[T]

    val timerTask = new TimerTask() {
      def run() : Unit = {
        p.tryFailure(new TimeoutException())
      }
    }

    timer.schedule(timerTask, timeout.toMillis)

    future.map {
      a =>
        if(p.trySuccess(a)) {
          System.out.println("!")
          timerTask.cancel()
        }else
          System.out.println("&")
    }
      .recover {
        case e: Exception =>
          System.out.println("@")
          if(p.tryFailure(e)) {

            timerTask.cancel()
          }
      }

    p.future
  }
}
