# 🛍️ Farrot 선생님 중고거래 플랫폼
![farrot](https://github.com/user-attachments/assets/dcb62b7f-041a-4bad-be19-4c0d697762f6)

- 발표URL : [Farrot 최종발표/영상 및 자료⭕](https://www.miricanvas.com/v/13opefh)

<br>

## 프로젝트 소개

- Farrot은 교사들이 안전하고 신뢰할 수 있는 환경에서 중고 물품을 거래할 수 있도록 설계된 플랫폼입니다.
- 교사들 간의 원활한 소통과 상호 신뢰를 기반으로 한 거래를 목표로 하며, 교육자들이 필요한 물품을 쉽게 등록하고 검색할 수 있는 기능을 제공합니다.
- 주요 기능으로는 상품 등록 및 검색, 거래 게시판, 교사들 간의 실시간 채팅 및 알림 기능이 포함됩니다.
- 이를 통해 교사들만의 안전한 커뮤니티를 형성하고, 사용자 간의 효율적인 거래를 지원하는 것이 Farrot의 핵심 목적입니다.

#### Farrot will provide a secure platform for trading used goods among teachers.
You can easily register, search, and manage transactions with our user-friendly interface. Our service also includes real-time communication and notification features to enhance the trading experience.

#### Farrot's platform will offer a seamless trading experience.

Our platform supports the following features:
1. Easy product registration and search functionalities.
2. A transaction bulletin board for posting and managing trades.
3. Real-time chat and notification options for effective communication.
4. A secure community exclusively for teachers to support efficient transactions.


<br>

## 👨‍👩‍👦‍👦 웹개발팀 소개

|      김혁진       |          엄진수         |       허상범         |       김다은        |
| :----------------: | :----------------------: | :-----------------: | :----------------: |
|   ![image](https://github.com/user-attachments/assets/c5349ab8-be7a-440c-8335-93bdd1a644ab)   |  ![image](https://github.com/user-attachments/assets/b9e11116-3777-4458-83a8-0c0d188ff458)    |  ![image](https://github.com/user-attachments/assets/f6792437-0999-4464-ad63-3614c3b9231b)  |   ![image](https://github.com/user-attachments/assets/971f010f-1724-475b-86e4-ba8cd34edb78)   |
|   [@hyoekjin](https://github.com/HS-hyeokjin)   |    [@jinsu](https://github.com/Gitdoolgi)  | [@sangbum](https://github.com/tokkaiiii)  | [@daeun](https://github.com/kde0707)  |
| 천재교육 풀스택 6기 | 천재교육 풀스택 6기 | 천재교육 풀스택 6기 | 천재교육 풀스택 6기 |

<br>

## 1. 개발 환경

- **Front-end** : HTML, CSS, JavaScript, Thymeleaf
- **Back-end** : Spring Boot, JPA, MariaDB, SAFERROT(안전 결제 서버)
- **버전 및 이슈관리** : Git, GitHub
- **협업 툴** : Discord, Notion, GitHub, Slack, VScode
- **서비스 배포 환경** : Tomcat

<br>

## 2. 채택한 개발 기술과 브랜치 전략

### 브랜치 전략

- Git-flow 전략을 기반으로 main, dev 브랜치를 운용했습니다.
- main, dev 브랜치로 나누어 개발하였습니다.
    - **main** 브랜치는 배포 단계에서만 사용하는 브랜치입니다.
    - **dev** 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.

<br>

## 3. 프로젝트 구조

```
farrot/
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── public/
│   └── firebase-messaging-sw.js
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hermez/
│   │   │           └── farrot/
│   │   │               ├── admin/
│   │   │               ├── category/
│   │   │               ├── chat/
│   │   │               ├── common/
│   │   │               ├── config/
│   │   │               ├── image/
│   │   │               ├── index/
│   │   │               ├── member/
│   │   │               ├── notification/
│   │   │               ├── oauth/
│   │   │               ├── payment/
│   │   │               ├── product/
│   │   │               ├── report/
│   │   │               ├── util/
│   │   │               └── wishlist/
│   │   └── FarrotApplication.java
│   │
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   ├── img/
│       │   ├── js/
│       │   ├── default-member-image.png
│       │   ├── default-product-image.png
│       │   ├── farrot4-1.png
│       │   └── farrot6.png
│       └── templates/
│           ├── admin/
│           ├── chat/
│           ├── error/
│           ├── index/
│           ├── layout/
│           ├── member/
│           ├── notification/
│           ├── payment/
│           ├── product/
│           └── wishlist/
│
└── test/
    └── java/
        └── com/
            └── hermez/
                └── farrot/
                    └── FarrotApplicationTests.java
```

<br>


<br>

## 4. 역할 분담

### 👻엄진수

- **UI**
    - 페이지 : ⭐채우셈!⭐
    - 공통 컴포넌트 : ⭐채우셈!⭐

- **기능**
    - ⭐채우셈!⭐

<br>
    
### 😎 김혁진

- **UI**
    - 페이지 : ⭐채우셈!⭐ 
    - 공통 컴포넌트 : ⭐채우셈!⭐ 
- **기능**
    - ⭐채우셈!⭐ 

<br>

### 🐬 허상범

- **UI**
    - 페이지 : 채팅, 알림, 찜 
    - 공통 컴포넌트 : 영상제작, 슬라이드 제작
- **기능**
    - 채팅, 알림, 찜 

<br>

### 🍊 김다은
- **UI**
    - 페이지 : ⭐채우셈!⭐ 
    - 공통 컴포넌트 : ⭐채우셈!⭐ 
- **기능**
    - ⭐채우셈!⭐ 

<br>

## 5. 개발 기간 및 작업 관리

### 개발 기간

- 기획 : 2024년 9월 11일 ~ 2024년 9월 12일
- 기능 설계 : 2024년 9월 12일 ~ 2024년 9월 13일
- View 구현 : 2024년 9월 19일 ~ 2024년 9월 20일
- 서버 구현 : 2024년 9월 19일 ~ 2024년 9월 27일
- 발표 : 2024년 9월 23일

<br>

### 작업 관리

- GitHub Projects를 사용하여 진행 상황을 공유했습니다.
- 만나서 회의를 진행하며 작업 순서와 방향성에 대한 고민을 나누고 노션 및 디스코드에 회의 내용을 기록했습니다.

<br>

## 6. 신경 쓴 부분

- ⭐채우셈!⭐ 

<br>

## 7. 페이지별 기능 ⭐움짤로 flone으로 채울 예정 아무튼 채우셈!⭐ 

### [비회원]
- 로그인이 되어 있지 않은 경우 : 로그인 페이지
- 로그인이 되어 있는 경우 : 권한에 맞는 기능

### [홈 화면]
- ⭐채우셈!⭐ 

### [상품 등록] 
- 사용자 본인의 상품을 등록할 수 있는 페이지입니다.
- 제목, 카테고리, 이미지, 가격 및 설명을 입력할 수 있습니다.

### [상품 상세 페이지]
- 사용자가 등록한 상품을 다른 사용자와 거래할 수 있는 페이지입니다.
- 사용자 본인의 상품 수정 및 삭제 기능이 있습니다.

### [채팅]
- 거래 상대와의 실시간 채팅을 지원합니다.
- 기존의 거래 이력과 정보를 확인할 수 있는 공간입니다.

### [관리자]
- ⭐채우셈!⭐ 

<br>

## 8. 클론 및 의존성 설치

### Requirements

- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle 7.5](https://gradle.org/releases/)
- [MariaDB 10.11.8](https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.11.8)


```bash
git clone https://github.com/team-hermez/farrot.git
cd farrot
# Java 및 Gradle 설치 필요
./gradlew build
```

### 실행방법
```
./gradlew bootRun
```

<br>

## 9. 개선 목표

- ⭐채우셈!⭐ 

<br>

## 10. 프로젝트 후기

### 👻 엄진수

이런저런 욕심을 내다보니 꽤 힘든 점도 있었습니다. 그래도 여러 기능을 사용해보기 위해 많이 찾아보면서 직접 로직을 구현했고 실제로 구동이 되는걸 보면서 굉장히 즐거웠습니다. 또한 예상치못한 트러블이 생기는 것을 많이 경험한 프로젝트이기도 합니다. 다음 프로젝트에선 스케쥴링을 좀 더 고려해야 하는 것을 많이 느꼈습니다

<br>

### 😎 김혁진

부족한 부분이 많지만 팀원들과 소통하며 즐겁게 개발했습니다.
트러블 없이 개발하였으며, 개인적인 부분과 팀적인 부분이 적당히 나뉘어져 스스로는 만족할 수 있는 결과를 만든것 같아서 좋았습니다.

<br>

### 🐬 허상범

채팅이란 흥미있는 주제로 프로젝트를 해볼 수 있어서 재밌었습니다. 개발을 하기 전엔 대표적인 채팅 어플인 카카오톡에서 쉬워 보였던 기능들이라든지 왜 아직도 이런 기능이 구현이 안돼있을까 싶은 것들을 조금은 이해하게된 계기가 된 것 같습니다

<br>

### 🍊 김다은

관리자 페이지를 하면서 여러가지 sql문을 경험해볼 수 있었습니다.
처음에만 어려웠지 뒤로 갈수록 jpa에 익숙해지니 마무리가 될때쯤 어렵지 않다고 늦겼습니다. 관리자페이지를 하면서 팀원들과 많이 소통했는데 그 부분이 제일 재미있었던것 같습니다.	
