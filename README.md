# 2FALogin

**2FALogin** is a Java application implementing **Two-Factor Authentication (2FA)** with **OTP** delivered via **email (SendGrid)** and **SMS (Twilio)**. It can be used as a reference for integrating 2FA into login systems.

## Key Features

* Login using **username and password**
* **2FA with OTP** for enhanced security
* OTP delivery channels:

  * **Email** via **SendGrid API**
  * **WhatsApp** via **Twilio API**
* Built with Gradle
* Docker support for easy deployment

## Architecture & Flow

1. User logs in with **username/password**.
2. The system validates the credentials.
3. The system determines the OTP delivery channel (email or SMS).
4. OTP is sent via:

   * **Email:** using **SendGrid API**
   * **WhatsApp:** using **Twilio API**
5. User enters the OTP to complete the login process.

## Prerequisites

* Java 17+
* Gradle
* **SendGrid account** for email delivery
* **Twilio account** for WhatsApp delivery
* Docker (optional)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/haryokuncoro/2FALogin.git
cd 2FALogin
```

### 2. Configuration

Create `application.properties` or `application.yml` in `src/main/resources`:

```properties
# Database / User storage config
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password

# SendGrid Email Config
SENDGRID_API_KEY=YOUR_SENDGRID_API_KEY
SENDGRID_FROM_EMAIL=your_email@example.com

# Twilio SMS Config
twilio_accountSid=YOUR_TWILIO_SID
twilio_authToken=YOUR_TWILIO_TOKEN
twilio_fromNumber=YOUR_TWILIO_NUMBER
```

### 3. Build & Run

```bash
./gradlew bootRun
```

