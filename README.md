# 📌 Spring Boot PDF Reader API (LLM-powered)

This is a **Java Spring Boot** API that reads and extracts structured data from CASA statement PDFs using an **LLM (Large Language Model)**. The API parses PDFs to extract key details like **name, email, opening balance, and closing balance**.

🚀 **Live Deployment:** Hosted on Heroku  

---

## 🔧 Features
- 📄 Extracts structured data from CASA statement PDFs.
- 🧠 Utilizes **LLM** for intelligent parsing.
- 🌍 Publicly deployable API.

---

## 🛠️ Tech Stack
- **Backend:** Java, Spring Boot
- **Parsing:** LLM (Large Language Model - Google Gemini)
- **Hosting:** Heroku
- **Version Control:** Git & GitHub

---

## 🛠️ Testing api
- Hit this curl
```
curl --location 'https://casaextracter-98b49cc702e1.herokuapp.com/casa/pdf/parse' \
--form 'file=@"/C:/Users/adars/Downloads/sample_casa_statement.pdf"'
```

- Download the sample pdf from here
```
https://github.com/Adars987h/casaextracter/blob/main/sample_casa_statement.pdf
```

- In postman go to body then form-data and attach this file.

