package com.example.UserPackage

import com.example.GameboardPackage.Game
import com.example.ServerPackage.Server

import java.awt.event.{ActionEvent, MouseEvent}
import javax.swing._

class User(private val userName: String, private val userMoveButton: JButton, private val userText: JTextField, private val game: Game, private val gamePane: JTextPane, private var enemy: Player = null) extends Player(){

  val server = new Server(game, gamePane)

  def setEnemy(user2: Player): Unit = {
    enemy = user2
  }

  override def moveRequest(board: Game): Unit = {
      userMoveButton.setText("Your turn!")
  }

  userMoveButton.addActionListener((e: ActionEvent) => {
    val userField = userText.getText()
    userText.setText("")
    userMoveButton.setText("")
    server.moveReceived(enemy, Integer.parseInt(userField))}
  )
}

