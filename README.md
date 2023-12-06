# kanvan - 프로젝트 관리 시스템

<br>

<div align="center">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Boot 3.1.6-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Data JPA-gray?style=for-the-badge&logoColor=white"/></a>

</div>
<div align="center">
<img src="https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/></a>
<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"/></a>
<img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens"/></a>
<img src="https://img.shields.io/badge/swagger-%ffffff.svg?style=for-the-badge&logo=swagger&logoColor=white"/></a>
</div>

<br>

**kanvan**은 프로젝트 및 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상 시키는데 사용됩니다.
<br>
<br>

## 0. 목차
- [1.개발 기간](#1-개발-기간)
- [2.프로젝트 요구사항](#2-프로젝트-요구사항)
- [3.프로젝트 구조](#3-프로젝트-구조)
- [4.ERD](#4-erd)
- [5.API 문서](#5-api-document)

## 1. 개발 기간

2023.11.27 ~ 2023.12.06 (10 days)

## 2.프로젝트 요구사항

- 유저
  - 회원가입 및 로그인 기능 구현.
  - JWT를 이용한 사용자 인증.  
- 팀
  - 팀 생성 기능
  - 팀원 초대 기능 및 초대 의사 결정 기능
  - 팀 전체 조회 및 상세 조회
  - 유저가 받은 초대 목록 조회
  - 팀 생성 및 초대 수락 시 팀 내에 `권한 부여`(팀장, 팀원) 
- 컬럼
  - 컬럼 생성 기능
  - 컬럼 목록 조회 기능
  - 컬럼 제목 변경 기능
  - 컬럼 순서 변경 기능
  - 컬럼 삭제 기능(컬럼 내 티켓이 없는 경우에만)
- 티켓
  - 티켓 생성 기능
  - 티켓 필드 수정 기능
  - 티켓 순서 수정 기능
  - 티켓 삭제 기능
 
## 3. 프로젝트 구조

<details>
    <summary>자세히</summary>

```
└── kanvan
    ├── KanvanApplication.java
    ├── auth
    │   ├── config
    │   ├── controller
    │   ├── dto
    │   ├── filter
    │   ├── jwt
    │   └── service
    ├── column
    │   ├── controller
    │   ├── domain
    │   ├── dto
    │   ├── repository
    │   └── service
    ├── common
    │   ├── config
    │   └── exception
    ├── team
    │   ├── controller
    │   ├── domain
    │   ├── dto
    │   ├── repository
    │   └── service
    ├── ticket
    │   ├── controller
    │   ├── domain
    │   ├── dto
    │   ├── repository
    │   └── service
    └── user
        ├── domain
        ├── dto
        ├── repository
        └── service
```

</details>

## 4. ERD

  <img width="490" alt="스크린샷 2023-12-06 오후 4 07 08" src="https://github.com/cjw9506/kanvan/assets/63503519/0c01e635-4b73-42bf-ba77-b12f74377a68">
  

## 5. API 문서

URL : [`http://server:port/swagger-ui/index.html`](http://15.164.140.158:8080/swagger-ui/index.html)

<img width="500" alt="스크린샷 2023-12-06 오후 3 34 18" src="https://github.com/cjw9506/kanvan/assets/63503519/e1985966-e8a4-49ef-8016-f2f6ec345276">


