package com.example.KalahaGUI

import akka.actor.{ActorSystem, Props}
import com.example.ComputerPackage.Computer
import com.example.GameboardPackage.Gameboard
import com.example.ServerPackage.Server
import com.example.UserPackage.User

import javax.swing.{JButton, JTextField, JTextPane}

object ControllerGUI {
  def pcVsPc(userButton1: JButton, userButton2: JButton, user1TextInput: JTextField,
             user2TextInput: JTextField, gameMessageOutput: JTextPane): Unit = {
    userButton1.setVisible(false)
    userButton2.setVisible(false)
    user1TextInput.setVisible(false)
    user2TextInput.setVisible(false)
    val system = ActorSystem("Kalaha")
    val gameBoard = new Gameboard(Gameboard.createBoard(6), Gameboard.drawWhoseMove())
    val player1 = system.actorOf(
      Props(classOf[Computer]), "Computer1")
    val player2 = system.actorOf(
      Props(classOf[Computer]), "Computer2")
    val server = system.actorOf(
      Props(classOf[Server], player1, player2, gameBoard, gameMessageOutput), "com.example.ServerPackage.Server")
    server ! Server.ServerAction()
  }

  def userVsPc(userButton1: JButton, userButton2: JButton, user1TextInput: JTextField,
               user2TextInput: JTextField, gameMessageOutput: JTextPane): Unit = {

    userButton1.setVisible(true)
    userButton2.setVisible(false)
    user1TextInput.setVisible(true)
    user2TextInput.setVisible(false)
    val system = ActorSystem("Kalaha")
    val gameBoard = new Gameboard(Gameboard.createBoard(6), Gameboard.drawWhoseMove())
    val player1 = system.actorOf(
      Props(classOf[User], userButton1.getText, userButton1, user1TextInput), "Player1")
    val player2 = system.actorOf(
      Props(classOf[Computer]), "Computer2")
    val server = system.actorOf(
      Props(classOf[Server], player1, player2, gameBoard, gameMessageOutput), "com.example.ServerPackage.Server")
    server ! Server.ServerAction()
  }

  def userVsUser(userButton1: JButton, userButton2: JButton, user1TextInput: JTextField,
                 user2TextInput: JTextField, gameMessageOutput: JTextPane): Unit = {

    userButton1.setVisible(true)
    userButton2.setVisible(true)
    user1TextInput.setVisible(true)
    user2TextInput.setVisible(true)
    val system = ActorSystem("Kalaha")
    val gameBoard = new Gameboard(Gameboard.createBoard(6), Gameboard.drawWhoseMove())
    val player1 = system.actorOf(
      Props(classOf[User], userButton1.getText, userButton1, user1TextInput), "Player1")
    val player2 = system.actorOf(
      Props(classOf[User], userButton2.getText, userButton2, user2TextInput), "Player2")
    val server = system.actorOf(
      Props(classOf[Server], player1, player2, gameBoard, gameMessageOutput), "com.example.ServerPackage.Server")
    server ! Server.ServerAction()
  }
}
