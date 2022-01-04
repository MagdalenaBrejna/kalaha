package com.example.DecisionTreePackage

import com.example.DecisionTreePackage.DecisionTree._
import com.example.GameboardPackage.Game

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
  private[this] val  root = new Node(game.boardClone(), game.getActivePlayerNumber,0,null,-1)

  class Node(private[this] val board: Game, private[this] val playerNumber: Int, private val level: Int, private val parent: Node, private val fieldChoice: Int) {

    private var children: List[Node] = Nil
    private val advantage = if (board.getActivePlayerNumber == playerNumber) board.countPlayerResultsDifference()
    else {
      board.changeActivePlayer()
      val temp = board.countPlayerResultsDifference()
      board.changeActivePlayer()
      temp
    }

    def findNodeChildren(): List[Node] = {
      def findChildrenInner(currentIndex: Int): List[Node] =
        if ((board.getActivePlayerNumber == PLAYER_1_ROUND && (currentIndex >= BASE_INDEX_PLAYER1 || currentIndex < FIRST_INDEX_PLAYER1) || (board.getActivePlayerNumber == PLAYER_2_ROUND && (currentIndex >= BASE_INDEX_PLAYER2 || currentIndex < FIRST_INDEX_PLAYER2)))) Nil
        else {
          if (!board.checkIfEnd() && board.getBoardHole(currentIndex) != 0) {
            val testBoard = board.boardClone()
            testBoard.processPlayerMove(currentIndex)
            val child = new Node(testBoard, playerNumber, level + 1, this, currentIndex)
            child :: findChildrenInner(currentIndex + 1)
          } else {
            if (currentIndex + 1 == BASE_INDEX_PLAYER1 || currentIndex + 1 >= BASE_INDEX_PLAYER2) Nil
            else findChildrenInner(currentIndex + 1)
          }
        }
      findChildrenInner(if (board.getActivePlayerNumber == PLAYER_1_ROUND) FIRST_INDEX_PLAYER1 else FIRST_INDEX_PLAYER2)
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

  def createDecisionTree(): Unit = {
    def createTreeHelp(nodeQueue: List[Node]): Unit = {
      nodeQueue match {
        case h :: t => if (h.getLevel() >= COMPUTER_FORESEE_LEVELS) ()
        else createTreeHelp(t ::: h.findNodeChildren())
        case Nil => ()
      }
    }
    createTreeHelp(List(root))
  }

  private def findBestNode(): Node = {
    def findBestNodeHelp(nodeQueue: List[Node], bestNode:Node): Node = {
      nodeQueue match {
        case h :: t  => if (h.getChildren == Nil && h.getAdvantage > bestNode.getAdvantage) findBestNodeHelp(t,h)
        else if (h.getChildren == Nil) findBestNodeHelp(t,bestNode)
        else findBestNodeHelp(t:::h.getChildren,bestNode)
        case Nil => bestNode
      }
    }
    findBestNodeHelp(List(root),root.getChildren().head)
  }

  def findBestMove(): Int = {
    val bestMoveNode = findBestNode()
    var tempNode = bestMoveNode
    while (tempNode.getParent() != root) {
      tempNode = tempNode.getParent()
    }
    tempNode.getFieldChoice()
  }
}
