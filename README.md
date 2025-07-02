# Martian Robots API

This project provides a robust, containerized RESTful API to simulate the movement of robots on a rectangular Martian grid. It is built with Spring Boot and includes features like API documentation, structured logging, and graceful error handling. 

PLEASE NOTE: no in memory database used. The simulation "lives" for the duration of that one request.

---

## Table of Contents
- [Features](#features)
- [Setup and Running the Application](#setup-and-running-the-application)
- [How to Play: API Usage](#how-to-play-api-usage)
  - [The Rules of the Simulation](#the-rules-of-the-simulation)
  - [Example](#example)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Project Structure](#project-structure)

---

## Features

* **RESTful API**: A single endpoint to run the entire simulation.
* **Dockerized**: Packaged with Docker and Docker Compose for easy setup and execution in any environment.
* **JSON API**: Accepts plain text input and returns results in a clean, structured JSON format.
* **API Documentation**: Integrated Swagger UI for interactive API exploration and testing.
* **Graceful Error Handling**: Provides clear, structured JSON error messages for invalid inputs.
* **Structured Logging**: Implements SLF4J and Logback for clear, informative application logs.
* **Externalized Configuration**: Key settings like API info and logging levels are managed in `application.properties`.

---

## Setup and Running the Application

### Prerequisites

* [Docker](https://www.docker.com/products/docker-desktop/) and Docker Compose must be installed on your system.

### Instructions

1.  **Clone the Repository**:
    If this project were on a Git repository, you would clone it. For now, ensure all the project files are in a single directory.

2.  **Build and Run**:
    Open a terminal or command prompt, navigate to the root directory of the project (where the `docker-compose.yml` file is located), and run the following command:

    ```bash
    docker-compose up --build
    ```

    This command will:
    * Build the Docker image for the application.
    * Start a container from that image.
    * The application will be running and accessible on `http://localhost:8080`.

---

## How to Play: API Usage

To run a simulation, you send a `POST` request to the `/simulate` endpoint with your input data.

* **URL**: `http://localhost:8080/simulate`
* **Method**: `POST`
* **Headers**: `Content-Type: text/plain`
* **Body**: The raw text input containing the grid and robot instructions.

### The Rules of the Simulation

The input provided in the request body must follow these rules:

1.  **Grid Definition**: The very first line defines the upper-right coordinates of the rectangular world. The lower-left coordinates are always assumed to be `0, 0`.
    * *Example*: `5 3` creates a grid from (0,0) to (5,3).
    * The maximum value for any coordinate is 50.

2.  **Robot Definitions**: After the grid line, the input consists of pairs of lines for each robot.
    * **Line 1 (Position)**: The robot's initial grid coordinates (X and Y) and its orientation, separated by spaces.
        * *Orientations*: `N` (North), `S` (South), `E` (East), `W` (West).
        * *Example*: `1 1 E`
    * **Line 2 (Instructions)**: A string of characters representing the robot's movement commands.
        * *Example*: `RFRFRFRF`
        * Instruction strings must be less than 100 characters.

3.  **Robot Instructions**:
    * `L`: Turn the robot left 90 degrees.
    * `R`: Turn the robot right 90 degrees.
    * `F`: Move the robot forward one grid point in its current direction.

4.  **Falling Off the Grid**:
    * If a robot moves off an edge of the grid, it is marked as "LOST".
    * A "scent" is left at the last valid grid position the robot occupied.
    * Any future robot that receives an `F` instruction from a scented grid point that would cause it to fall will **ignore that instruction** and remain in its current position.

### Example

#### Sample Input
5 3<br/>
1 1 E<br/>
RFRFRFRF<br/>
3 2 N<br/>
FRRFLLFFRRFLL<br/>
0 3 W<br/>
LLFFFLFLFL<br/>


#### Sample `curl` Command

```bash
curl -X POST http://localhost:8080/simulate \
-H "Content-Type: text/plain" \
-d '5 3
1 1 E
RFRFRFRF
3 2 N
FRRFLLFFRRFLL
0 3 W
LLFFFLFLFL'
```

#### Expected JSON Output
```json
{
  "finalStates": [
    {
      "x": 1,
      "y": 1,
      "orientation": "E",
      "isLost": false
    },
    {
      "x": 3,
      "y": 3,
      "orientation": "N",
      "isLost": true
    },
    {
      "x": 2,
      "y": 3,
      "orientation": "S",
      "isLost": false
    }
  ]
}
```

#### **API Documentation (Swagger)**
The API is fully documented using OpenAPI 3.0. Once the application is running, you can access the interactive Swagger UI in your browser to explore and test the endpoint.

**Swagger UI URL:** http://localhost:8080/swagger-ui/index.html
