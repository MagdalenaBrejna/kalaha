package com.example.DecisionTreePackage

import com.example.DecisionTreePackage.DecisionTree.{BASE_INDEX_PLAYER1, BASE_INDEX_PLAYER2, COMPUTER_FORESEE_LEVELS, FIRST_INDEX_PLAYER1, FIRST_INDEX_PLAYER2, PLAYER_1_ROUND, PLAYER_2_ROUND}
import com.example.GameboardPackage.Game

import scala.util.control.Breaks.breakable

object DecisionTree {
  private val FIRST_INDEX_PLAYER1 = 0
  private val BASE_INDEX_PLAYER1 = 6
  private val FIRST_INDEX_PLAYER2 = 7
  private val BASE_INDEX_PLAYER2 = 13
  private val PLAYER_1_ROUND = 1
  private val PLAYER_2_ROUND = 2
  private val COMPUTER_FORESEE_LEVELS = 2
}



class DecisionTree(var game: Game) {
/*
  def chooseMove(depth: Int, playerNumber: Int): Int = {
    decisionMinMaxTree(game, depth, true, true, playerNumber)._2
  }

  def decisionMinMaxTree(game: Game, depth: Int, myTurn: Boolean, firstIter: Boolean = false, playerNumber: Int = 1, _alpha: Int = Integer.MIN_VALUE, _beta: Int = Integer.MAX_VALUE): (Int, Int) ={
    if(depth == 0 || game.checkIfEnd()){ // if this is last depth level => returning current bases difference
      if(playerNumber == 1)
        return (game.getBoardHole(6) - game.getBoardHole(13), 0)
      else
        return (game.getBoardHole(7) - game.getBoardHole(0), 0)
    }
    var bestMoveNumber = -1

    val board = game.boardClone()
    var alpha = _alpha
    var beta = _beta

    breakable {
      if (myTurn) {
        var bestValue = Integer.MIN_VALUE

        for (i <- 1 to 6) {
          var value = 0
          if (board.checkChosenField(i)) {
            if (board.processPlayerMove(i)) { // checking for repeated move on copied board
              val board = game.clone() // making next copy to be used in deeper iteration
              board.processPlayerMove(i)
              value = decisionMinMaxTree(board, depth, true, false, playerNumber, alpha, beta)._1
            }
            else { // next move opponent
              val board = game.clone()
              board.make_move(i)
              value = decisionMinMaxTree(board, depth - 1, false, false, playerNumber, alpha, beta)._1
            }

            if (firstIter && value > bestValue) { // every time better value is found => update of best move <- i
              bestMoveNumber = i
            }
            bestValue = value.max(bestValue)
            alpha = alpha.max(bestValue) // if better move was found, updating alpha to new assured score value
            if (alpha >= beta) { // if enemy max score is less than player min score, we dont have to search for result, becouse its the best
              break
            }
          }
        }
        return (bestValue, bestMoveNumber)
      }
      else { // searching for the lowest diffrence between two players bases
        var bestValue = Integer.MAX_VALUE

        for (i <- 1 to 6) {
          var value = 0
          if (board.is_legal_move(i)) {
            if (board.move_repeat(i)) {
              val board = game.clone()
              board.make_move(i)
              value = decisionMinMaxTree(board, depth, false, false, playerNumber, alpha, beta)._1
            }
            else {
              val board = game.clone()
              board.make_move(i)
              value = decisionMinMaxTree(board, depth - 1, true, false, playerNumber, alpha, beta)._1
            }
            bestValue = value.min(bestValue)
            beta = beta.min(bestValue)
            if (alpha >= beta) { // if enemy max score is less than player min score, we dont have to search for result, becouse its the best
              break
            }
          }
        }
        return (bestValue, bestMoveNumber)
      }
    }
    (0, 0)
  }


 */


  private[this] val  root = new Node(game.boardClone(),game.getActivePlayerNumber,0,null,-1)

  class Node(private[this] val board: Game, private[this] val playerNumber: Int, private val level: Int,private val parent: Node, private val fieldChoice: Int) {

    private var children: List[Node] = Nil
    private val advantage = if (board.getActivePlayerNumber == playerNumber) board.countPlayerResultsDifference()
    else {
      board.changeActivePlayer()
      val temp = board.countPlayerResultsDifference()
      board.changeActivePlayer()
      temp
    }

    def calculateChildren(): List[Node] = { //This method is calculating best route for enemy and user at the same time
      def calculateChildrenHelp(currentIndex: Int): List[Node] = {
        if ((board.getActivePlayerNumber == PLAYER_1_ROUND && (currentIndex >= BASE_INDEX_PLAYER1 || currentIndex < FIRST_INDEX_PLAYER1) || (board.getActivePlayerNumber == PLAYER_2_ROUND && (currentIndex >= BASE_INDEX_PLAYER2 || currentIndex < FIRST_INDEX_PLAYER2)))) Nil
        else {
          if (!board.checkIfEnd() && board.getBoardHole(currentIndex) != 0) {
            val testBoard = board.boardClone()
            testBoard.processPlayerMove(currentIndex)
            val child = new Node(testBoard, playerNumber, level + 1, this, currentIndex)
            child :: calculateChildrenHelp(currentIndex + 1)
          } else {
            if (currentIndex + 1 == BASE_INDEX_PLAYER1 || currentIndex + 1 >= BASE_INDEX_PLAYER2) Nil
            else calculateChildrenHelp(currentIndex + 1)
          }
        }
      }
      children = calculateChildrenHelp(if (board.getActivePlayerNumber == PLAYER_1_ROUND) FIRST_INDEX_PLAYER1 else FIRST_INDEX_PLAYER2)
      children
    }

    def getChildren(): List[Node] = {
      children
    }

    def getAdvantage(): Int = {
      advantage
    }

    def getFieldChoice(): Int = {
      fieldChoice
    }

    def getParent(): Node = {
      parent
    }

    def getLevel(): Int = {
      level
    }

    override def toString: String = s"[$advantage $level $fieldChoice]"
  }

  def createTree(): Unit = {
    def createTreeHelp(nodeQueue: List[Node]): Unit = {
      nodeQueue match {
        case h :: t => if (h.getLevel() >= COMPUTER_FORESEE_LEVELS) () else createTreeHelp(t ::: h.calculateChildren())
        case Nil => ()
      }
    }
    createTreeHelp(List(root))
  }

  private def findBestNode(): Node = {
    def findBestNodeHelp(decisionTree: List[Node], currentBestNode: Node): Node = {
      decisionTree match {
        case h :: t => if (h.getChildren == Nil && h.getAdvantage > currentBestNode.getAdvantage) findBestNodeHelp(t, h)
        else if (h.getChildren == Nil) findBestNodeHelp(t, currentBestNode)
        else findBestNodeHelp(t ::: h.getChildren, currentBestNode)
        case Nil => currentBestNode
      }
    }
    findBestNodeHelp(List(root), root.getChildren().head)
  }

  def findBestMove(): Int = {
    val bestMoveNode = findBestNode()
    var decisionNode = bestMoveNode
    while (decisionNode.getParent() != root) {
      decisionNode = decisionNode.getParent()
    }
    decisionNode.getFieldChoice()
  }


}
