package com.example.UserPackage


import akka.actor.{Actor, ActorRef}
import com.example.GameboardPackage.Gameboard
import com.example.ServerPackage.Server

import java.awt.event.ActionEvent
import javax.swing._

class User(private[this] val name:String, private[this] val button: JButton,
           private[this] val textArea: JTextField) extends Actor {

  private[this] var senderval: ActorRef = _

  button.addActionListener((e: ActionEvent) => {
    val userField = textArea.getText()
    textArea.setText("")
    button.setText(name)
    senderval! Server.UserMoveReceived(Integer.parseInt(userField))}
  )

  override def receive: Receive = {
    case Server.UserMoveRequest(board: Gameboard) =>
      senderval = sender()
      button.setText("Write Move Above!")
  }
}

