
# 👨‍👩‍👦‍👦 Babpool Backend Server 👨‍👩‍👦‍👦

README 작성 중

[![license](https://img.shields.io/badge/License-AGPL-red)](https://github.com)
[![code](https://img.shields.io/badge/Framework-Spring-green)](https://github.com)
[![member](https://img.shields.io/badge/Project-Member-brown)](https://github.com)
[![DBMS](https://img.shields.io/badge/DBMS-MySQL-blue)](https://github.com)

> 대학생 오프라인 네트워킹 서비스 밥풀 👉 https://bab-pool.com

![babpool_intro_image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/1129c307-937e-4e81-9613-9b66aeedd1b3)

## 📖 Description

---

교내에서 학과나 동아리에 구애받지 않고 
자신의 관심사와 목표에 맞는 사람들에게 밥약(식사 약속)을 신청하여 
1:1로 대화할 수 있는 기회를 제공

## 🔧 Skill Stack

---
![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/9a5dd0ce-58ad-4701-b6b5-d17df27e8e64)

## Getting Started

---

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

<details>
    <summary>
        📦 Dropdown for details
    </summary>

### Prerequisites

- Java 17
  - Gradle
  - IntelliJ IDEA 2023.3.4

### Installing

1. Clone the repository
```bash
git clone https://github.com/proHyundo/your-repository.git
```
2. Navigate into the project directory
```bash
cd your-repository
```
3. Build the project
```bash
./gradlew build
```
4. Run the application
```bash
./gradlew bootRun
```

## Running the tests

To run the tests, use the following command:

```bash
./gradlew test
```

</details>

## 🚀 Deployment

---

This application is set up to be easily deployed using Docker and Docker Compose. For more details, please refer to `.github/workflows/github-actions-server.yaml`.


## Authors

- **proHyundo** - *Initial work* - [proHyundo](https://github.com/proHyundo)

## 🧾 License

This project is licensed under the AGPL License - see the [LICENSE.md](https://github.com/proHyundo/your-repository/blob/main/LICENSE.md) file for details

## 📂 Project Structure

과거 3-Tier 구조로 패키지를 설계하였을 때, 프로젝트 규모가 커질수록 유지보수에 어려움이 있었던 경험이 있었습니다.

특히, 프로젝트의 규모가 커질수록 패키지에 포함된 클래스가 많아지면서 클래스 간의 의존관계를 파악하기 어려웠습니다.
또한, 새로운 관심사가 생기거나 기존의 관심사가 변경될 때 패키지 분리와 코드 리팩토링이 어려웠습니다.

따라서 3-Tier 구조를 개선한 DDD(Domain-Driven Design)의 일부를 적용하여 아래와 같은 패키지 구조를 설계하였습니다.

```
root:.
├─java
│  └─com
│      └─swyp3
│          └─babpool
│              ├─domain
│              │  ├─appointment
│              │  │  ├─api
│              │  │  │  └─request
│              │  │  ├─application
│              │  │  │  └─response
│              │  │  │      └─appointmentdetail
│              │  │  ├─config
│              │  │  ├─dao
│              │  │  ├─domain
│              │  │  └─exception
│              │  │      ├─errorcode
│              │  │      └─handler
│              │  ├─keyword
│              │  │  ├─dao
│              │  │  └─domain
│              │  ├─possibledatetime
│              │  │  ├─dao
│              │  │  └─domain
│              │  ├─profile
│              │  │  ├─api
│              │  │  │  └─request
│              │  │  ├─application
│              │  │  │  └─response
│              │  │  ├─config
│              │  │  ├─dao
│              │  │  ├─domain
│              │  │  └─exception
│              │  │      ├─errorcode
│              │  │      └─handler
│              │  ├─review
│              │  │  ├─api
│              │  │  │  └─request
│              │  │  ├─application
│              │  │  │  └─response
│              │  │  ├─dao
│              │  │  ├─domain
│              │  │  └─exception
│              │  └─user
│              │      ├─api
│              │      │  └─requset
│              │      ├─application
│              │      │  └─response
│              │      ├─dao
│              │      ├─domain
│              │      └─exception
│              │          ├─errorcode
│              │          └─handler
│              ├─global
│              │  ├─common
│              │  │  ├─exception
│              │  │  │  ├─errorcode
│              │  │  │  └─handler
│              │  │  ├─request
│              │  │  └─response
│              │  │      └─config
│              │  ├─config
│              │  ├─jwt
│              │  │  ├─api
│              │  │  ├─application
│              │  │  │  └─response
│              │  │  └─exception
│              │  │      ├─errorcode
│              │  │      └─handler
│              │  ├─logging
│              │  └─uuid
│              │      ├─application
│              │      ├─dao
│              │      ├─domain
│              │      ├─exception
│              │      └─util
│              └─infra
│                  ├─auth
│                  │  ├─dao
│                  │  ├─domain
│                  │  ├─exception
│                  │  │  ├─errorcode
│                  │  │  └─handler
│                  │  ├─kakao
│                  │  ├─response
│                  │  └─service
│                  ├─email
│                  ├─health
│                  │  ├─api
│                  │  ├─application
│                  │  └─dao
│                  ├─redis
│                  │  ├─config
│                  │  ├─dao
│                  │  └─domain
│                  └─s3
│                      ├─application
│                      ├─config
│                      └─exception
└─resources
    ├─mapper
    ├─static
    └─templates
```

## 🔨 Server Architecture

![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/570d5610-64ae-4f77-a1fb-be2832b68da5)

## 📊 Monitoring Architecture

![monitoring_architecture_jpeg](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/2e3c7449-c7a5-45ab-8a6a-259a633c13fc)

## 👨‍💻 BE Contribution

(수정 중)

|   성함    |                                                                      담당 기능                                                                      |
|:-------:|:-----------------------------------------------------------------------------------------------------------------------------------------------:|
| **송현도** | JWT 발급,관리,검증<br/>사용자 로그아웃<br/>사용자 회원탈퇴<br/>실시간 알림 구현<br/>밥 약속 도메인<br/>스케줄러 도입<br/>S3 이미지 관리<br/>클라우드 인프라 구축<br/>도메인 및 인증서 설정<br/>CI/CD 파이프라인 구축 |
| **강연주** |                                         카카오 소셜 로그인<br/>사용자 추가정보 입력 및 회원가입<br/>밥 약속 수락/거절<br/>사용자 정보 수정                                          |

## 소감

![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/098a2e15-9a82-4b0e-aac0-45827670a612)