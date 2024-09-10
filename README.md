
# 👨‍👩‍👦‍👦 Babpool Backend Server 👨‍👩‍👦‍👦

[![license](https://img.shields.io/badge/License-AGPL-red)](https://github.com)
[![code](https://img.shields.io/badge/Framework-Spring-green)](https://github.com)
[![member](https://img.shields.io/badge/Project-Member-brown)](https://github.com)
[![DBMS](https://img.shields.io/badge/DBMS-MySQL-blue)](https://github.com)

> 대학생 오프라인 네트워킹 서비스 밥풀 👉 https://bab-pool.com

![babpool_intro_image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/1129c307-937e-4e81-9613-9b66aeedd1b3)

## 🗂️ Index

1. Description
2. Features

## 📖 Description

- 밥풀 프로젝트는 밥 약속 신청을 통해 관심사와 목표를 공유하는 사람들과 일대일로 대화 할 수 있는 기회를 만들고자 했습니다. 
- 긴밀한 대학생 커뮤니티, 서로 도움을 줄 수 있는 인맥 풀을 만들기 위한 플랫폼 입니다.
- [SWYP](https://swyp.imweb.me/) 3기 프로젝트로 참여하여 1위를 수상 했습 니다.
  
  <details>
      <summary>상장 보기</summary>
      <img src="https://github.com/user-attachments/assets/bff8342c-9122-4145-90b7-b55cb08c81c0" alt="1위_수상_상장" />     
  </details>

## 🔧 BE Skill Stack

![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/9a5dd0ce-58ad-4701-b6b5-d17df27e8e64)

| 구분 | 기술 명 | 선정 이유 |
| :---: | :---: | --- |
| 영속성 프레임워크 | MyBatis | ORM을 사용하더라도 복잡한 조회쿼리는 네이티브 쿼리의 사용은 필요합니다. 이를 염두해 쿼리 작성에 대한 역량을 향상하기 위해 SQL Mapper 프레임워크인 MyBatis를 선택했습니다. |
| API 문서화 | Swagger | RestDocs, Swagger와 RestDocs의 결합, 엑셀문서, 포스트맨 등 여러 API 문서화 도구를 모두 경험해보았습니다. 이를 바탕으로 짧은 기간안에 요구사항을 충족하기 위해 간편하고 생산성이 높은 Swagger를 선택했습니다. |
| 클라우드 | AWS | 프리티어 요금제를 사용하여 비용 부담 없이 다양한 클라우드 서비스를 프로젝트에 적용할 수 있으며 Azure, OCI, GCP 와 비교했을 때 래퍼런스와 커뮤니티 사이즈가 상대적으로 더 크다는 점에서 채택. |
| 스토리지 | AWS S3 | EC2에 저장 시, 인스턴스의 용량이 부족해지고, 파일 관리의 어려움이 있음. 파일 저장에 용이. |
|  | Docker | 컨테이너의 특징에 의해 환경에 구애받지 않고 애플리케이션을 쉽고 안정적으로 관리할 수 있다는 점에서 사용. |
|  | Nginx |  |
| 실시간알림 | Stomp | 사용자가 어느페이지에 존재해도 약속 요청 이벤트가 발생했을 때 실시간 알림 기능을 구현하기 위해, 메시지 기반의 웹소켓 비동기 프로토콜을 간편하게 제공하는 STOMP를 사용. |

## 🔥 BE Challenge & Solution

주요 해결점

## 🔨 Server Architecture

![Architecture](https://github.com/user-attachments/assets/8b97e60c-15fe-4f6b-8d9b-c107d699c24e)

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
