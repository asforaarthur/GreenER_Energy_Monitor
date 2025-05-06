## Green ER Consumption Project

## 📘 Project Description

This project aims to analyze and compare the **electricity consumption** of the GreenER building as a whole, as well as that of classroom 4A-020 within the same building. The analysis is based on power consumption data collected over several days throughout the year, with the added flexibility for the user to adjust the sample rate.

The main goals are:
- Understand the differences in energy usage;
- Identify periods of overconsumption;
- Raise awareness of energy usage and suggest ways to reduce it.

The project involves processing CSV files, basic statistical analysis, and graphical visualization using Java and the JFreeChart library.

---

## ⚙️ Application Features

The application is built in **Java** and uses the **JFreeChart** library to generate graphical charts. Key features include:

### 📂 Data Loading
- Reads `.csv` files containing power consumption data (in watts);
- Each line contains a timestamp (date and time) and the corresponding measured value.

### 📊 Statistical Analysis
- Automatically computes:
  - daily average consumption;
  - maximum and minimum values;
  - time periods with peak usage.

### 📈 Chart Generation
- Generates **time-series plots** to visualize hour-by-hour consumption;
- Side-by-side comparison between the two rooms;
- Uses `JFreeChart` to:
  - customize axes (hours, dates);
  - export charts as image files.

### 💾 Result Display
- Opens a new window showing the main statistics and charts interactively.

---

## 🗂️ Project Structure

- **`/src`**: Java source code
- **`/lib`**: JFreeChart library for chart rendering
- **`/build/classes`**: Compiled Java classes
- **`/nbproject`**: NetBeans project configuration files
- **`Green_ER_data.csv`**: Consumption data for Room ER
- **`classRoom_4A020_data.csv`**: Consumption data for Room 4A020
- **`Green-Er.jpg` & `Classroom_4A020.png`**: Pictures of the rooms
- **`build.xml`**: Ant build script
- **`manifest.mf`**: Java manifest file

---

## 📊 Key Findings

- 📉 **Room 4A020 generally consumes less energy** than the whole building;
- 🕒 Peak consumption times are correlated with class schedules and room usage;
- 💡 Suggested actions based on the results:
  - Optimize lighting and device usage;
  - Raise user awareness about turning off unused equipment;
  - Evaluate automation options (presence sensors, timers, etc.).

---

## ▶️ Running the Project

### Requirements

- Java JDK 8 or higher
- A Java IDE (such as NetBeans)
- `JFreeChart` library (already included in the `/lib` folder)

### Steps

1. Clone the repository:
    ```bash
    git clone https://github.com/asforarthur/green-er-consumption-project.git
    ```

2. Open the project in **NetBeans**.

3. Run the main file located in the `/src` directory.

---

## 🖼️ Application Screenshots

![image](https://github.com/user-attachments/assets/ce243f90-7cff-4e2f-96f7-301dad36b940)
![image](https://github.com/user-attachments/assets/77d6375e-8d0f-4528-bb03-7459e238fbe8)

---

## 🚀 Author & Acknowledgments

- 👤 Author: **Arthur Asfora**
- 🙏 Thanks to the teaching staff of the *IT Tools and Optimization* course for their guidance.

---

## 📃 License

Academic project completed at Ense3 – free to use for educational purposes.
