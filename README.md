# 🎮 BalyGames: Video Game Price Comparison Engine

**BalyGames** is a specialized search engine and price comparison platform designed to help gamers find the best deals on video games across various digital and physical stores. Built on a robust **Client-Server architecture**, the system identifies the cheapest purchase options in real-time by treating stores and games as a weighted graph.

---

## 🚀 Key Features

* **Real-time Price Comparison:** Search for any game via a modern UI and instantly discover the cheapest vendor.
* **Full Inventory View:** A dedicated dashboard to browse the entire database of games, genres, and store-specific prices.
* **Graph-Based Pathfinding:** Implements advanced algorithms to navigate pricing data.
* **Persistent Storage:** Data management via a JSON-based file system using the **DAO (Data Access Object)** pattern.
* **Dynamic UI:** Responsive **JavaFX** interface featuring custom branding and asynchronous network handling.

---

## 🛠️ Technical Architecture

The project is divided into two main modules to ensure separation of concerns:

### **Server-Side (`BalyGamesProject`)**
* **Service Layer:** Contains `PriceComparisonService` and `GameService` for business logic.
* **Algorithms:** Features a custom library implementing **Dijkstra** and **A*** for optimal pathfinding.
* **Data Persistence:** Manages a `games.txt` database using **Google Gson** for serialization.
* **Socket Programming:** A multi-threaded server capable of handling concurrent client requests.

### **Client-Side (`BalyClientSide`)**
* **Frontend:** Developed using **JavaFX** and **FXML**.
* **Communication:** Utilizes a custom `Request/Response` protocol over TCP/IP.
* **UX Design:** Includes "Prompt Text" hints, custom background assets, and real-time alerts.

---

## 💻 Tech Stack

* **Language:** Java 17+
* **GUI Framework:** JavaFX
* **Data Format:** JSON (via Google Gson)
* **Build Tool:** Maven
* **Protocols:** TCP/IP Sockets

---

## ⚙️ Installation & Running

Follow these steps to run the application correctly:

### **1. Prerequisites**
* Ensure **Java JDK 17** or higher is installed.
* Maven must be configured in your IDE (IntelliJ IDEA recommended).

### **2. Start the Server**
1.  Open the `BalyGamesProject`.
2.  Run `Main.java` once to initialize the game database.
3.  Run `ServerDriver.java` to start the socket listener (Default Port: 5000).
    > **Note:** The server must be active before the client can connect.

### **3. Start the Client**
1.  Open the `BalyClientSide`.
2.  Run `HelloApplication.java`.
3.  Use the **"Search & Compare"** tab to find the best price for a specific game.
4.  Use the **"Full Price List"** tab to view all available market data.

---

## 📊 Algorithmic Approach

The core of BalyGames treats the marketplace as a graph where:
* **Nodes** represent Games and Stores.
* **Edges** represent the price (weight) of a game at a specific store.

By feeding this graph into **Dijkstra’s Algorithm**, the system calculates the shortest path (lowest cost) between the product and the vendor, ensuring 100% accuracy in finding the "Best Price."

---

## 👥 Authors

* **Michael Tsegaye** 
* **Roi Baly** 

---

### 📝 Academic Context
This project was developed as a final project for the **Software Engineering/Advanced Java** course. It demonstrates mastery of **Design Patterns (MVC, DAO)**, **Network Programming**, and **Algorithmic Efficiency**.
