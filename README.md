
# 👨‍👩‍👦‍👦 Babpool Backend Server 👨‍👩‍👦‍👦

[![license](https://img.shields.io/badge/License-AGPL-red)](https://github.com)
[![code](https://img.shields.io/badge/Framework-SpringBoot-green)](https://github.com)
[![DBMS](https://img.shields.io/badge/DBMS-MySQL-blue)](https://github.com)
[![member](https://img.shields.io/badge/Project-Team-brown)](https://github.com)

> 운용중인 페이지 링크 👉 https://bab-pool.com

![babpool_intro_image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/1129c307-937e-4e81-9613-9b66aeedd1b3)

## 🗂️ Index

1. 개요
2. BE 기술 스택
3. BE 챌린지 & 해결
4. ER 다이어그램
5. 서버 아키텍처
6. 시작 가이드
7. CI/CD 파이프라인
8. 기타 문서
9. 프로젝트 구조
10. BE 기여도
11. 소감

## 📖 Outline

소개
- 밥풀 프로젝트는 밥 약속 신청을 통해 관심사와 목표를 공유하는 사람들과 일대일로 대화 할 수 있는 기회를 만들고자 했습니다. 
- 긴밀한 대학생 커뮤니티, 서로 도움을 줄 수 있는 인맥 풀을 만들기 위한 플랫폼 입니다.
- [SWYP](https://swyp.imweb.me/) 3기 프로젝트로 참여하여 1위를 수상 했습 니다.
  <details>
      <summary>상장 보기</summary>
      <img src="https://github.com/user-attachments/assets/bff8342c-9122-4145-90b7-b55cb08c81c0" alt="1위_수상_상장" />     
  </details>

개발 기간
- MVP : 2024.02 ~ 2024.03
- 개선 및 유지보수 : 2024.06 ~ 2024.09

개발 인원
- 기획자 1명, 디자이너 1명, BE 2명, FE 2명

## 🔧 BE Skill Stack

![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/9a5dd0ce-58ad-4701-b6b5-d17df27e8e64)

|    구분     |  기술 명   | 선정 이유                                                                                                                                 |
|:---------:|:-------:|---------------------------------------------------------------------------------------------------------------------------------------|
|    DB     |  MySQL  | RDBMS 중 참고할 수 있는 레퍼런스가 가장 많은 MySQL을 선택했습니다.                                                                                           |
| 영속성 프레임워크 | MyBatis | ORM을 사용하더라도 복잡한 조회쿼리는 네이티브 쿼리의 사용은 필요합니다. 이를 염두해 쿼리 작성에 대한 역량을 향상하기 위해 SQL Mapper 프레임워크인 MyBatis를 선택했습니다.                             |
|  API 문서화  | Swagger | RestDocs, Swagger와 RestDocs의 결합, 엑셀문서, 포스트맨 등 여러 API 문서화 도구를 모두 경험해보았습니다. 이를 바탕으로 짧은 기간안에 요구사항을 충족하기 위해 간편하고 생산성이 높은 Swagger를 선택했습니다. |
|   클라우드    |   AWS   | 프리티어 요금제를 사용하여 비용 부담 없이 다양한 클라우드 서비스를 프로젝트에 적용할 수 있으며 Azure, OCI, GCP 와 비교했을 때 래퍼런스와 커뮤니티 사이즈가 상대적으로 더 크다는 점에서 채택했습니다.                |
|   스토리지    | AWS S3  | 프로필 이미지를 저장하고, 정적 웹 페이지를 응답할 목적으로 사용했습니다. EC2에 저장하지 않아, 메모리 공간을 낭비하지 않고 파일 관리의 용이성을 얻을 수 있었습니다.                                       |
|   가상환경    | Docker  | 컨테이너의 특징에 의해 환경에 구애받지 않고 애플리케이션을 쉽고 안정적으로 관리할 수 있다는 점에서 사용했습니다.                                                                       |
|   실시간알림   |  Stomp  | 사용자가 어느페이지에 존재해도 약속 요청 이벤트가 발생했을 때 실시간 알림 기능을 구현하기 위해, 메시지 기반의 웹소켓 비동기 프로토콜을 간편하게 제공하는 STOMP를 사용했습니다.                                 |

## 🔥 BE Challenge & Solution

1) 밥 약속 동시 요청 발생 시, Race Condition 이 발생했습니다.
  - 해결 : 빈번하게 발생하는 상황은 아니지만, 반드시 정합성이 지켜져야 한다는 점에서, MySQL `SELECT ~ FOR UPDATE` 구문을 사용해 베타적 락을 적용하여 해결했습니다.

2) 데이터가 많아지는 경우, 페이징 쿼리 성능 저하되었습니다.
  - 해결 : 

3) 일일 요청 수 20,000 건 이상 요청되며, AWS 과금이 발생했습니다.
  - 해결 : 

4) AutoIncrement PK 사용으로, 자원에 대한 정보를 추정하기 너무 쉬웠습니다.
  - 해결 :

## 📊 ERD

다음은 데이터베이스의 스키마와 테이블간의 관계를 나타낸 ERD입니다.
- 기존, MVP 구현 기간에 사용된 ERD 의 불편함을 개선하기 위해 반정규화를 진행하였습니다.

**반정규화 전 MVP 기간 ERD**
![ERD_before](https://github.com/user-attachments/assets/29d36984-13a3-4c94-be40-ba080acddef5)


**반정규화 후 최종 ERD**
![ERD_after](https://github.com/user-attachments/assets/c0246525-831c-43e4-a57b-95732853d1dc)



## 🔨 Server Architecture

![Architecture](https://github.com/user-attachments/assets/8b97e60c-15fe-4f6b-8d9b-c107d699c24e)
- 서버의 부하를 분산시키고, 서버의 안정성을 높이기 위해 백엔드와 프론트 서버를 분리한 서버 아키텍처를 구성했습니다.
- 백엔드 서버는 EC2 인스턴스의 도커 환경위에서 컨테이너로 배포하였습니다.
- RDS를 EC2와 동일한 VPC 내에 생성하지만, Private Subnet에 생성하여 EC2 인스턴스에서만 접근할 수 있도록 구성했습니다. 이로 인해 보안성을 높이고, 공개 Ipv4 주소에 의한 비용 발생을 제거했습니다.
- 프론트 서버는 S3에 정적 파일을 호스팅하고, CloudFront 를 통해 CDN 을 적용하여 사용자에게 빠른 응답속도를 제공하고, S3 서비스에 대한 요청 비용을 절감 했습니다.
- 모니터링 서버를 구축하여, 서버의 상태(매트릭)와 로그를 실시간으로 확인하고, 장애 발생 시 빠르게 대응할 수 있도록 구성했습니다.

## 시작 가이드

---

해당 시작 가이드는, 로컬 환경에서 테스트 코드를 실행하기 위한 환경을 구축하는 방법을 안내합니다.

<details>
    <summary>
        📦 상세 내용 더보기
    </summary>

### 사전 준비

- Java JDK 17을 설치합니다.
- JAVA_HOME 환경 변수를 설정합니다.
- IDE 인텔리제이 툴을 권장합니다.

### 프로젝트 클론 및 빌드

1. 저장소 클론
```bash
git clone https://github.com/proHyundo/your-repository.git
```
2. IntelliJ 에서 프로젝트를 열고 Gradle 의존성을 자동으로 불러옵니다.

## Running the tests

To run the tests, use the following command:

```bash
./gradlew test
```

</details>

## 🚀 CI/CD 파이프라인

---

Github Actions와 Docker Compose 를 사용하여 쉽게 CI/CD를 수행할 수 있도록 설정되어 있습니다.
`github-actions-server.yaml` 파일의 변경사항과 함께 main 브랜치에 푸시되면, Github Actions가 동작합니다.
설정 스크립트는 ['.github/workflows/github-actions-server.yaml'(링크)](https://github.com/swyp3-babpool/babpool-backend/blob/main/.github/workflows/github-actions-server.yaml)을 참조하세요.

**주요 단계**

1. 빌드
   - 체크아웃 리포지토리
   - Java 환경 설정
   - 테스트 및 동작에 필요한 각종 파일(application.yml, data.sql, schema.sql, log4jdbc.log4j2.properties)을 Github Secretes 환경변수를 사용해 생성 및 업로드
   - Gradle 설정, `./gradlew clean build -i` 애플리케이션 빌드

2. Docker 작업
   - 비밀번호를 사용하여 DockerHub에 로그인합니다.
   - 애플리케이션에 대한 Docker 이미지를 빌드하고 푸시합니다.

3. 배포 파일 생성 및 복사
    - 시크릿에서 docker-compose.yaml을 생성하여 VM에 복사합니다.
    - app.conf(NGINX 구성)가 생성되어 VM에 복사됩니다.
    - promtail-config.yml이 생성되어 VM에 복사됩니다.

4. VM에 배포
    - VM에 SSH로 접속합니다.
    - 기존 컨테이너와 이미지가 있는 경우 제거합니다.
    - 새 Docker 이미지를 가져옵니다.
    - Docker Compose를 사용하여 애플리케이션을 시작합니다.
    - 사용하지 않는 Docker 이미지를 정리합니다.

5. CI/CD 결과 Slack 알림
   - 워크플로 결과를 Slack에 보냅니다.

  
## 📂 Project Structure

과거 3-Tier 구조로 패키지를 설계하였을 때, 프로젝트 규모가 커질수록 유지보수에 어려움 경험했습니다.

특히, 프로젝트의 규모가 커질수록, 클래스 간의 의존관계를 파악하기 어려웠습니다.
또한, 새로운 관심사가 생기거나 기존의 관심사가 변경될 때 패키지 분리와 코드 리팩토링이 어려웠습니다.

따라서 3-Tier 구조를 개선한 DDD(Domain-Driven Design)의 일부를 적용하여 아래와 같은 패키지 구조를 설계하였습니다.

크게 도메인, 인프라 그리고 전역으로 구분하였습니다.
도메인 패키지는 비즈니스 로직을 담당하며, 인프라 패키지는 외부 시스템과의 연동을 담당합니다.
전역 패키지는 전역적으로 사용되는 패키지를 담당합니다.

서비스 레이어에 추가적인 파사드 패턴을 적용하여, 패키지 순환참조 문제를 해결하였습니다.

<details>
    <summary>
        📦 프로젝트 구조 더보기
    </summary>

```yaml
├── domain
│   ├── appointment
│   │   ├── api
│   │   │   └── request
│   │   ├── application
│   │   │   └── response
│   │   │       └── appointmentdetail
│   │   ├── config
│   │   ├── dao
│   │   ├── domain
│   │   └── exception
│   │       ├── errorcode
│   │       └── handler
│   ├── facade
│   ├── keyword
│   │   ├── application
│   │   ├── dao
│   │   │   └── response
│   │   ├── domain
│   │   └── exception
│   ├── possibledatetime
│   │   ├── api
│   │   │   └── request
│   │   ├── application
│   │   │   └── response
│   │   ├── dao
│   │   ├── domain
│   │   └── exception
│   │       ├── errorcode
│   │       └── handler
│   ├── profile
│   │   ├── api
│   │   │   └── request
│   │   ├── application
│   │   │   └── response
│   │   ├── config
│   │   ├── dao
│   │   ├── domain
│   │   └── exception
│   │       ├── errorcode
│   │       └── handler
│   ├── reject
│   │   ├── api
│   │   ├── application
│   │   │   └── response
│   │   ├── dao
│   │   └── exception
│   ├── review
│   │   ├── api
│   │   │   └── request
│   │   ├── application
│   │   │   └── response
│   │   ├── dao
│   │   ├── domain
│   │   └── exception
│   └── user
│       ├── api
│       │   └── requset
│       ├── application
│       │   └── response
│       ├── dao
│       ├── domain
│       └── exception
│           ├── errorcode
│           └── handler
├── global
│   ├── common
│   │   ├── exception
│   │   │   ├── errorcode
│   │   │   └── handler
│   │   ├── request
│   │   └── response
│   │       └── config
│   ├── config
│   ├── jwt
│   │   ├── api
│   │   ├── application
│   │   │   └── response
│   │   └── exception
│   │       ├── errorcode
│   │       └── handler
│   ├── logging
│   ├── message
│   ├── swagger
│   │   └── exception
│   ├── tsid
│   └── uuid
│       ├── application
│       ├── dao
│       ├── domain
│       ├── exception
│       └── util
└── infra
├── auth
│   ├── dao
│   ├── domain
│   ├── exception
│   │   ├── errorcode
│   │   └── handler
│   ├── kakao
│   ├── response
│   └── service
├── health
│   ├── api
│   ├── application
│   └── dao
├── redis
│   ├── dao
│   └── domain
└── s3
├── application
├── config
└── exception
```

</details>

## 👨‍💻 BE 기여도

#### 송현도

- MVP 기간
  - 프로필 페이징 구현
  - 밥 약속 도메인 설계 및 구현
  - JWT 기반 사용자 인증/인가 처리
  - 소셜 로그아웃, 회원탈퇴 처리
  - 밥 약속 신청 상태 변경을 위한 스케줄러 도입
  - 밥 약속 신청 시, 실시간 알림 구현
  - S3 프로필 이미지 업로드
  - AWS 기반 애플리케이션 아키텍처 설계 및 구축, 도메인 및 인증서 설정
  - CI/CD 파이프라인 구축


- 성능 개선 및 유지보수 기간
  - ERD 반정규화
  - TSID 도입
  - MyBatis 테스트 코드 작성
  - 밥 약속 동시 요청 제어
  - 페이징 쿼리 성능 개선
  - 크롤링 봇 대응

#### 강연주

- MVP 기간
  - 카카오 소셜 로그인 구현
  - 사용자 추가정보 입력 및 회원가입 처리
  - 밥 약속 수락/거절 처리
  - 사용자 정보 수정 처리


## 소감

![image](https://github.com/swyp3-babpool/babpool-backend/assets/128882585/098a2e15-9a82-4b0e-aac0-45827670a612)

## ETC

- *Initial work, Read me* - [proHyundo](https://github.com/proHyundo)
- *License* - This project is licensed under the AGPL License - see the [LICENSE.md](https://github.com/proHyundo/your-repository/blob/main/LICENSE.md) file for details
