package com.example.UserPackage

import com.example.DecisionTreePackage.DecisionTree
import com.example.GameboardPackage.Game
import com.example.ServerPackage.Server

import javax.swing.JTextPane


class Computer(private val game: Game, private val gamePane: JTextPane, private var enemy: Player = null) extends Player() {

  val server = new Server(game, gamePane)

  def setEnemy(user2: Player): Unit = {
    enemy = user2
  }

  override def moveRequest(board: Game): Unit = {
    val decisionTree = new DecisionTree(board)
    decisionTree.createTree()
    Thread.sleep(500)
    server.moveReceived(enemy, decisionTree.findBestMove())
  }
}