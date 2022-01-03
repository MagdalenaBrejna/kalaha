package com.example.KalahaGUI

import com.example.KalahaGUI.ControllerGUI.{pcVsPc, userVsPc, userVsUser}

import java.awt.event.ActionEvent
import java.awt.{BorderLayout, GridLayout}
import javax.swing.{JButton, JFrame, JPanel, JTextField, JTextPane, WindowConstants}
import javax.swing.text.{SimpleAttributeSet, StyleConstants}

class KalahaGUI {
  private val frame = new JFrame
  private val buttonVersion1 = new JButton("PC vs PC")
  private val buttonVersion2 = new JButton("USER vs PC")
  private val buttonVersion3 = new JButton("USER vs USER")
  private val buttonPlayer1 = new JButton("Player 1")
  private val buttonPlayer2 = new JButton("Player 2")
  private val panelWest = new JPanel()
  private val panelNorth = new JPanel()
  private val panelSOUTH = new JPanel()
  private val panelCENTER = new JPanel()
  private val user1TextInput = new JTextField()
  private val user2TextInput = new JTextField()
  private val gameWindow = new JTextPane()

  def run() ={

    panelNorth.setLayout(new GridLayout(1, 3))
    panelNorth.add(buttonVersion1)
    panelNorth.add(buttonVersion2)
    panelNorth.add(buttonVersion3)

    panelWEST.setLayout(new GridLayout(3, 1))

   /* panelWEST.setLayout(new GridLayout(3, 1))
    panelWEST.add(buttonVersion1)
    panelWEST.add(buttonVersion2)
    panelWEST.add(buttonVersion3)
    panelSOUTH.setLayout(new GridLayout(2, 1))
    panelSOUTH.add(user1TextInput)
    panelSOUTH.add(user2TextInput)
    panelSOUTH.add(buttonPlayer1)
    panelSOUTH.add(buttonPlayer2)
    panelCENTER.setLayout(new GridLayout(1, 1))
    panelCENTER.add(gameWindow)
    user1TextInput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER)
    user2TextInput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER)
    buttonVersion1.addActionListener(
      (e: ActionEvent) => pcVsPc(buttonPlayer1, buttonPlayer2, user1TextInput, user2TextInput, gameWindow))
    buttonVersion2.addActionListener(
      (e: ActionEvent) => userVsPc(buttonPlayer1, buttonPlayer2, user1TextInput, user2TextInput, gameWindow))
    buttonVersion3.addActionListener(
      (e: ActionEvent) => userVsUser(buttonPlayer1, buttonPlayer2, user1TextInput, user2TextInput, gameWindow))
    buttonPlayer1.setVisible(false)
    buttonPlayer2.setVisible(false)
    val attribs = new SimpleAttributeSet
    StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER)
    gameWindow.setEditable(false)
    gameWindow.setParagraphAttributes(attribs, true)
    frame.add(panelWEST, BorderLayout.WEST)
    frame.add(panelSOUTH, BorderLayout.SOUTH)
    frame.add(panelCENTER, BorderLayout.CENTER)
    frame.setSize(500, 180)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setVisible(true)
     */

  }


}
