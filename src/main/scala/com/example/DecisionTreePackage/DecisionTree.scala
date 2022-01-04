package com.example.DecisionTreePackage

import com.example.DecisionTreePackage.DecisionTree.{HOLE_PLAYER1, HOLE2_PLAYER2, MAX_DEEP, INDEX1_PLAYER1, INDEX2_PLAYER2, PLAYER_1, PLAYER_2}
import com.example.GameboardPackage.Game

object DecisionTree {
  private val PLAYER_1 = 1
  private val PLAYER_2 = 2
  private val INDEX1_PLAYER1 = 0
  private val INDEX2_PLAYER2 = 7
  private val HOLE_PLAYER1 = 6
  private val HOLE2_PLAYER2 = 13
  private val MAX_DEEP = 2
}

class DecisionTree(var game: Game) {

  private val root = new Node(game.boardClone(), game.getActivePlayerNumber,0,null,-1, 0)

  def makeTree(): Unit = {
    def makeTreeInner(nodeQueue: List[Node]): Unit =
      nodeQueue match {
        case node :: nextNodes => if (node.getLevel() >= MAX_DEEP) () else makeTreeInner(nextNodes ::: node.findNodeChildren())
        case Nil => ()
      }
    makeTreeInner(List(root))
  }

  def findMaxScoreHole(): Int = {
    def findMaxScoreHoleInner(decisionTree: List[Node], currentBestNode: Node): Node =
      decisionTree match {
        case Nil => currentBestNode
        case (currentNode :: nextNodes) => if (currentNode.getChildren == Nil && currentNode.getAdvantage > currentBestNode.getAdvantage) findMaxScoreHoleInner(nextNodes, currentNode)
                                           else if (currentNode.getChildren == Nil) findMaxScoreHoleInner(nextNodes, currentBestNode)
                                           else findMaxScoreHoleInner(nextNodes ::: currentNode.getChildren, currentBestNode)

      }
    var decisionNode = findMaxScoreHoleInner(List(root), root.getChildren().head)
    while (decisionNode.getParent() != root) decisionNode = decisionNode.getParent()
    decisionNode.getFieldChoice()
  }

  class Node(private val board: Game, private val playerNumber: Int, private val level: Int, private val parent: Node, private val fieldChoice: Int, private val advantage: Int) {

    private var children: List[Node] = Nil

    def getChildren(): List[Node] = {
      children
    }

    def getParent(): Node = {
      parent
    }

    def getAdvantage(): Int = {
      advantage
    }

    def getFieldChoice(): Int = {
      fieldChoice
    }
    def getLevel(): Int = {
      level
    }

    def findNodeChildren(): List[Node] = {
      def findNodeChildrenInner(currentIndex: Int): List[Node] =
        if ((board.getActivePlayerNumber == PLAYER_1 && (currentIndex >= HOLE_PLAYER1 || currentIndex < INDEX1_PLAYER1) || (board.getActivePlayerNumber == PLAYER_2 && (currentIndex >= HOLE2_PLAYER2 || currentIndex < INDEX2_PLAYER2)))) Nil
        else {
          if (!board.checkIfEnd() && board.getBoardHole(currentIndex) != 0) {
            val testBoard = board.boardClone()
            testBoard.processPlayerMove(currentIndex)
            val child = new Node(testBoard, playerNumber, level + 1, this, currentIndex, testBoard.countPlayerResultsDifference())
            child :: findNodeChildrenInner(currentIndex + 1)
          } else {
            if (currentIndex + 1 == HOLE_PLAYER1 || currentIndex + 1 >= HOLE2_PLAYER2) Nil
            else findNodeChildrenInner(currentIndex + 1)
          }
        }
      children = findNodeChildrenInner(if (board.getActivePlayerNumber == PLAYER_1) INDEX1_PLAYER1 else INDEX2_PLAYER2)
      children
    }
  }
}
