package com.example.GameboardPackage

import com.example.GameboardPackage.Game._
import com.example.UserPackage.User

import scala.util.Random

object Game {

  private val FIRST_INDEX_PLAYER1 = 0
  private val HOLE_PLAYER1 = 6
  private val FIRST_INDEX_PLAYER2 = 7
  private val HOLE_PLAYER2 = 13
  private val HOLES_NUMBER = 14
  private val PLAYER_1_ROUND = 1
  private val PLAYER_2_ROUND = 2

  def createBoardArray(numberOfStones: Int): Array[Int] = {
    def createBoardArrayInner(currentIndex:Int, numberOfStones:Int): List[Int] = {
      if (currentIndex > HOLE_PLAYER2) Nil
      else if ((currentIndex + 1)%7 == 0) 0 :: createBoardArrayInner(currentIndex + 1,numberOfStones)
      else numberOfStones :: createBoardArrayInner(currentIndex + 1,numberOfStones)
    }
    createBoardArrayInner(FIRST_INDEX_PLAYER1,numberOfStones).toArray
  }

  def getFirstPlayer(): Int = { 1 }
}

class Game(private var board: Array[Int], private var activePlayer: Int){

  // game processing helpers

  def boardClone(): Game = {
    new Game(board.clone(), activePlayer)
  }

  def getBoardHole(index: Int): Int = {
    board(index)
  }


  def getActivePlayerNumber: Int =  {
    activePlayer
  }

  def changeActivePlayer(): Unit ={
    if (activePlayer == PLAYER_1_ROUND) activePlayer = PLAYER_2_ROUND
    else activePlayer = PLAYER_1_ROUND
  }


  def checkIfEnd(): Boolean = {
    if ((board.slice(FIRST_INDEX_PLAYER1, HOLE_PLAYER1).sum == 0 && activePlayer == PLAYER_1_ROUND) || (board.slice(FIRST_INDEX_PLAYER2,HOLE_PLAYER2).sum == 0 && activePlayer == PLAYER_2_ROUND)) true
    else false
  }



  //process game

  def processPlayerMove(chosenField: Int): Unit = {
    if (checkChosenField(chosenField)) {
      val stonesNumber = board(chosenField)
      board(chosenField) = 0

      def processPlayerMoveInner(index: Int, stonesToReplace: Int): Unit =
        if (stonesToReplace > 0) {

          if (activePlayer == PLAYER_1_ROUND) {
            if (index != HOLE_PLAYER2) {
              board(index) = board(index) + 1
              processPlayerMoveInner((index + 1) % HOLES_NUMBER, stonesToReplace - 1)
            } else processPlayerMoveInner(FIRST_INDEX_PLAYER1, stonesToReplace)

          } else {
            if (index != HOLE_PLAYER1) {
              board(index) = board(index) + 1
              processPlayerMoveInner((index + 1) % HOLES_NUMBER, stonesToReplace - 1)
            } else processPlayerMoveInner(FIRST_INDEX_PLAYER2, stonesToReplace)
          }

        } else allStonesMoved(index)

      processPlayerMoveInner(chosenField + 1, stonesNumber)
    }
  }

  def checkChosenField(field: Int): Boolean = {
    if (board(field) == 0) false
    else if (activePlayer == PLAYER_1_ROUND) {
      if (field < FIRST_INDEX_PLAYER1 || field > HOLE_PLAYER1 - 1) false
      else true
    } else {
      if (field < FIRST_INDEX_PLAYER2 || field > HOLE_PLAYER2 - 1) false
      else true
    }
  }

  def allStonesMoved(index: Int): Unit = {
    val lastStoneIndex = if (index == 0) HOLE_PLAYER2 else index - 1
    lastStone(lastStoneIndex)
  }

  private def lastStone(indexOfLastMovedStone: Int): Unit = {
    if (activePlayer == PLAYER_1_ROUND && board(indexOfLastMovedStone) - 1 == 0 && indexOfLastMovedStone >= FIRST_INDEX_PLAYER1 && indexOfLastMovedStone < HOLE_PLAYER1)
        stealStones(HOLE_PLAYER2, HOLE_PLAYER1, indexOfLastMovedStone)
    else if (activePlayer == PLAYER_2_ROUND && board(indexOfLastMovedStone) - 1 == 0 && indexOfLastMovedStone >= FIRST_INDEX_PLAYER2 && indexOfLastMovedStone < HOLE_PLAYER2)
        stealStones(HOLE_PLAYER1, HOLE_PLAYER2, indexOfLastMovedStone)
  }

  def stealStones(index1: Int, index2: Int, indexOfHole: Int): Unit = {
    val indexToStealFrom = 12 - indexOfHole
    board(index2) += board(indexToStealFrom)
    board(indexToStealFrom) = 0
  }



  // get and present results

  def countPlayerResultsDifference(): Int = {
    if (activePlayer == PLAYER_1_ROUND) board(HOLE_PLAYER1) - board(HOLE_PLAYER2)
    else board(HOLE_PLAYER2) - board(HOLE_PLAYER1)
  }

  private def countPlayersResults(): (Int,Int) = {
    def countPlayersResultsInner(index: Int, results: (Int,Int)): (Int,Int) =
      if (index <= HOLE_PLAYER1)
        countPlayersResultsInner(index + 1, (results._1 + board(index), results._2))
      else if (index > HOLE_PLAYER1 && index <= HOLE_PLAYER2)
        countPlayersResultsInner(index + 1, (results._1, results._2 + board(index)))
      else
        results
    countPlayersResultsInner(0, (0,0))
  }

  def printGameResult(): String = {
    val gameResult = countPlayersResults()
    if (gameResult._1 > gameResult._2)
      s"Player 1 won the game - results \n ${gameResult._1} : ${gameResult._2}"
    else if (gameResult._2 > gameResult._1)
      s"Player 2 won the game - results \n ${gameResult._2} : ${gameResult._1}"
    else
      s"There is no winner \n ${gameResult._1} : ${gameResult._2}"
  }

  private def printWhoPlay(): String = {
    if (activePlayer == PLAYER_1_ROUND)
      "          Player 1 play          \n"
    else
      "          Player 2 play          \n"
  }

  def printGameBoard: String = {
    printWhoPlay() +
      s"\n    ${board(12)} | ${board(11)} | ${board(10)} | ${board(9)} | ${board(8)} | ${board(7)}\n" +
      s"    (${board(13)})                                    (${board(6)})\n" +
      s"    ${board(0)} | ${board(1)} | ${board(2)} | ${board(3)} | ${board(4)} | ${board(5)}\n"
  }
}
