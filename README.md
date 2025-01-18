# 📝 Todo App

A simple JavaFX-based Todo application that helps you manage your tasks and to-dos. With this app, you can easily add, delete, and mark tasks as completed.

<div align="center">
   <p align="center">
      <img src="https://github.com/user-attachments/assets/f2970a19-d7ff-419f-a79c-d55b71dc700d" height="270" alt="Todo App Main Screen" style="border-radius: 10px;">
   </p>
   <img src="https://github.com/yuvrajsinh5252/TODO-app/assets/117096680/66491b36-ab38-4118-b8b0-1b3a939171aa" height="300" alt="App Screenshot 1" style="border-radius: 10px;">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <img src="https://github.com/yuvrajsinh5252/TODO-app/assets/117096680/d68b498a-b601-4df6-9bbf-78e14297205c" height="300" alt="App Screenshot 2" style="border-radius: 10px;">
</div>

## ✨ Features

- 📌 **Task Management**

  - Add new tasks with titles and descriptions
  - Edit existing tasks
  - Delete unwanted tasks
  - Mark tasks as complete/incomplete
  - Clean and intuitive user interface

- 💾 **Data Persistence**
  - Automatic saving to local database
  - Reliable data storage

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- MySQL Database
- Maven 3.6 or higher
- Your favorite Java IDE

### Installation

1. Clone the repository

   ```bash
   git clone https://github.com/yuvrajsinh5252/TODO-app
   ```

2. Navigate to project directory and build

   ```bash
      cd todo-app
      mvn clean install
   ```

3. Configure the Database

   - Open `DBConnection.java`
   - Update the database password to match your local MySQL setup

4. Run the application
   ```bash
      mvn javafx:run
   ```

## 🛠️ Technical Details

- **Build Tool**: Apache Maven
- **Framework**: JavaFX
- **Database**: MySQL
- **UI Library**: JFoenix
- **Architecture**: MVC Pattern

## 🔮 Future Plans & Enhancements

- [ ] Task Reminders and Notifications
- [ ] Priority Levels for Tasks
- [ ] Task Categories and Tags

## 🤝 Contributing

We love contributions! Here's how you can help:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
