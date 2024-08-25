# Stock Market Simulation Game

This project is a Stock Market Simulation Game developed as a part of a course on Informatics - T.P.S.I.T. The project simulates a stock market where players can buy and sell shares in real-time, competing to make the best investment decisions.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Development Team](#development-team)
- [Software Used](#software-used)
- [License](#license)

## Introduction

The project is a simulation game where each player starts with an initial amount of capital to invest in shares. The game ends when a player declares bankruptcy or all other players have done so, leading to the victory of the remaining player. The game can be played in both single-player and multiplayer modes.

## Features

- **Single-player and Multiplayer Modes**: Players can choose to play alone or connect to a server for a real-time multiplayer experience.
- **Real-time Stock Market Simulation**: Stock prices fluctuate according to an algorithm that simulates real-world market behavior.
- **Multiple Client Support**: In multiplayer mode, multiple players can connect to a server, and the game manages turns using a circular list.
- **Graphical User Interface**: The game includes an intuitive GUI for both client and server, making it easy to configure and play.

## Installation

To install the project, follow these steps:

1. Clone the repository to your local machine:
2.	Open the project in Eclipse or any compatible IDE.
3.	Make sure you have the required plugins installed, such as JavaFx.
4.	Run the server application first if you plan to play in multiplayer mode.

## Usage

### Single-Player Mode

	1.	Launch the game and set up your initial configuration, such as the number of players and the starting capital.
	2.	Start the game and begin buying and selling shares based on the fluctuating market prices.
	3.	The game ends when all players but one go bankrupt or a player chooses to leave the game.

### Multiplayer Mode

	1.	Start the server application and configure it with the number of players, initial capital, and list of companies.
	2.	Each player must run the client application, connect to the server by entering the server’s IP address and port number.
	3.	The game proceeds in real-time, with players taking turns to manage their portfolios.

### Project Structure

	•	Server: Manages the game state, stock prices, and player turns.
	•	Client: Allows players to interact with the game, including buying and selling shares.
	•	Model-View-Controller (MVC) Architecture: The project follows the MVC pattern for organizing code.

### Software Used

	•	Eclipse: IDE for Java development.
	•	JavaFx: Used for creating the graphical user interface.
	•	Mockflow: For designing the interface.
	•	Gantt Project: For project management.
	•	Dia: For creating diagrams.
	•	Atom: For text editing.
