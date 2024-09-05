# Diplom_2

```markdown

## Описание

Проект является второй частью дипломной работы по автоматическому тестированию, разработанной в рамках курса "Инженер по тестированию: от новичка до автоматизатора" на Яндекс.Практикуме. В этом проекте акцент сделан на разработку и реализацию автоматизированных тестов с использованием инструментов, таких как Rest-Assured для API-тестирования и Allure для визуализации отчетов.

### Технологии:

- Java 11
- Maven
- JUnit 4
- RestAssured
- Allure

## Установка

1. Убедитесь, что у вас установлены [Java JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) и [Maven](https://maven.apache.org/download.cgi).
2. Склонируйте репозиторий на локальный компьютер:

   ```bash
   git clone https://github.com/mir9635/Diplom_2
   cd Diplom_2
   ```

3. Установите зависимости:

   ```bash
   mvn install
   ```

## Использование

Запустите тесты с помощью следующей команды:

```bash
mvn test
```

Сгенерируйте отчет Allure:

```bash
mvn allure:serve
```

## Структура проекта

```plaintext
Diplom_2
│
├── src
│   ├── main
│   │   └── java   
│   │       └── org.springpattern
│   │           ├── orders
│   │           │   ├── OrderAuthorization.java
│   │           │   └── OrderCreate.java
│   │           └── user
│   │               ├── User.java
│   │               ├── UserAuthorization.java
│   │               ├── UserData.java
│   │               └── UserRegistration.java
│   │              
│   │   
│   └── test
│       └── java
│           └── org.springpattern
│               ├── BaseTest.java
│               ├── ChangingUserDataTest.java
│               ├── OrderCreateTest.java
│               ├── ReceivingUserOrdersTest.java
│               ├── UserAuthorizationTest.java
│               └── UserRegistrationTest.java
│
├── pom.xml
└── README.md
```

### pom.xml

Файл `pom.xml` содержит все зависимости проекта и параметры сборки, такие как:

- JUnit4 для тестирования
- RestAssured для работы с RESTful API
- Allure для генерации отчетов о выполнении тестов

