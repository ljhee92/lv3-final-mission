# API 명세서

## 1. 로그인 및 로그아웃
### 1-1. 로그인
- **request**
```json
GET /login/github
Content-Type: application/json
```

- **response**
```json
HTTP/1.1 302 FOUND
Content-Type: application/json
Location: http://localhost:8080
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 1-2. 인증 정보 확인
- **request**
```json
GET /login/check
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "userId": "brown",
  "name": "브라운",
  "role": "COACH"
}
```

### 1-3. 로그아웃
- **request**
```json
POST /logout
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
```

## 2. 도서 관리
### 2-1. 도서 조회
- **request**
```json
GET /admin/books?keyword=오브젝트
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
        "author": "조영호",
        "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
        "publisher": "위키북스",
        "pubdate": "2019-06-17",
        "isbn": "9791158391409",
        "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴"
    },
    {
        "title": "유튜버 쌤의 코딩 클래스: 엔트리 기초",
        "author": "이산^단^뽀",
        "image": "https://shopping-phinf.pstatic.net/main_3246656/32466562911.20230418163651.jpg",
        "publisher": "하이오브젝트",
        "pubdate": "2021-09-21",
        "isbn": "9791197566509",
        "description": "유튜브로 검색하고 즐기는 지금 이 시대에 가장 적합한 코딩 입문 교재는?\n  \n지금, 어떤 코딩 책을 보고 계신가요? 딱딱한 글의 대학교재 같은 책을 고르시지는 않으신가요?\n  \n지금 시대의 우리는 학습방법이 다르고 코딩능력은 앞으로 길게 가져가야 할 능력인데요.\n과연 흥미있고 쉽게 코딩을 시작할 수 있을까요?\n  \n세계최대기술학회 IEEE의 글로벌 리더 선생님이 코딩을 시작하는 사람들을 위해 세심하게 만들었습니다.\n  \n복잡하고 방대한 자료보다는 알아야할 부분만 간단히 보여주고.\n시트콤 형식의 유튜브 수업으로 흥미, 즐거움과 함께 코딩 역량을 키울 수 있습니다.\n  \n전국 관공서가 극찬한 공증받은 현업 코딩 선생님\n코딩이 필요한 지금. 흥미가 없고, 막막함에 주저하고 있다면 지금 당장 “유튜버 쌤의 코딩 클래스 엔트리 기초”와 함께하세요.\n  \n재미있는 유튜브 시트콤의 이산, 단, 뽀 쌤과 함께 코딩을 시작해 보세요!\n  \n* 위 책은 코딩(프로그래밍) 입문 초등학생을 위한 교양서적입니다.\n\"엔트리\"라는 교육용 코딩 프로그램을 기초부터 시작할 수 있도록 안내해주는 책입니다.\n위 책으로 코딩을 처음 시작하는 사람들이 쉽고 재미있게 코딩의 기초 개념을 학습할 수 있습니다."
    }
]
```

### 2-2. 도서 등록
- **request**
```json
POST /admin/books
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM

{
    "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
    "author": "조영호",
    "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
    "publisher": "위키북스",
    "pubdate": "2019-06-17",
    "isbn": "9791158391409",
    "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴",
    "total_count": "2",
    "regDate": "2025-06-18"
}
```

- **response**
```json
HTTP/1.1 201 CREATED
Content-Type: application/json

{
    "id": "1",
    "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
    "author": "조영호",
    "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
    "publisher": "위키북스",
    "pubdate": "2019-06-17",
    "isbn": "9791158391409",
    "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴",
    "total_count": "2",
    "regDate": "2025-06-18"
}
```

## 3. 예약
### 3-1. 예약 가능 도서 조회
- **request**
```json
GET /reservations/available
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "book_id": "1",
        "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
        "author": "조영호",
        "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
        "publisher": "위키북스",
        "pubdate": "2019-06-17",
        "isbn": "9791158391409",
        "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴",
        "available_count": "2",
        "total_count": "2"
    },
    {
        "book_id": "2",
        "title": "유튜버 쌤의 코딩 클래스: 엔트리 기초",
        "author": "이산^단^뽀",
        "image": "https://shopping-phinf.pstatic.net/main_3246656/32466562911.20230418163651.jpg",
        "publisher": "하이오브젝트",
        "pubdate": "2021-09-21",
        "isbn": "9791197566509",
        "description": "유튜브로 검색하고 즐기는 지금 이 시대에 가장 적합한 코딩 입문 교재는?\n  \n지금, 어떤 코딩 책을 보고 계신가요? 딱딱한 글의 대학교재 같은 책을 고르시지는 않으신가요?\n  \n지금 시대의 우리는 학습방법이 다르고 코딩능력은 앞으로 길게 가져가야 할 능력인데요.\n과연 흥미있고 쉽게 코딩을 시작할 수 있을까요?\n  \n세계최대기술학회 IEEE의 글로벌 리더 선생님이 코딩을 시작하는 사람들을 위해 세심하게 만들었습니다.\n  \n복잡하고 방대한 자료보다는 알아야할 부분만 간단히 보여주고.\n시트콤 형식의 유튜브 수업으로 흥미, 즐거움과 함께 코딩 역량을 키울 수 있습니다.\n  \n전국 관공서가 극찬한 공증받은 현업 코딩 선생님\n코딩이 필요한 지금. 흥미가 없고, 막막함에 주저하고 있다면 지금 당장 “유튜버 쌤의 코딩 클래스 엔트리 기초”와 함께하세요.\n  \n재미있는 유튜브 시트콤의 이산, 단, 뽀 쌤과 함께 코딩을 시작해 보세요!\n  \n* 위 책은 코딩(프로그래밍) 입문 초등학생을 위한 교양서적입니다.\n\"엔트리\"라는 교육용 코딩 프로그램을 기초부터 시작할 수 있도록 안내해주는 책입니다.\n위 책으로 코딩을 처음 시작하는 사람들이 쉽고 재미있게 코딩의 기초 개념을 학습할 수 있습니다.",
        "available_count": "2",
        "total_count": "2"
    }
]
```

### 3-2. 도서 예약
- **request**
```json
POST /reservations
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM

{
    "book_id": "1",
    "reserve_date": "2025-06-18",
    "reserve_time": "13:46:39"
}
```

- **response**
```json
HTTP/1.1 201 CREATED
Content-Type: application/json

{
    "id": "1",
    "name": "듀이",
    "book_id": "1",
    "reserve_date": "2025-06-18",
    "reserve_time": "13:46:39"
}
```

### 3-3. 예약 리스트 조회
- **request**
```json
GET /reservations
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": "1",
        "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
        "author": "조영호",
        "publisher": "위키북스",
        "reserve_date": "2025-06-18",
        "return_date": "2026-06-24"
    },
    {
        "id": "2",
        "title": "유튜버 쌤의 코딩 클래스: 엔트리 기초",
        "author": "이산^단^뽀",
        "publisher": "하이오브젝트",
        "reserve_date": "2025-06-18",
        "return_date": "2026-06-24"
    }
]
```

### 3-4. 예약 상세 정보 조회
- **request**
```json
GET /reservations/{id}
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 200 OK
Content-Type: application/json
        
{
    "id": "1",
    "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
    "author": "조영호",
    "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
    "publisher": "위키북스",
    "pubdate": "2019-06-17",
    "isbn": "9791158391409",
    "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴",
    "reserve_date": "2025-06-18",
    "return_date": "2026-06-24"
}
```

### 3-4. 예약 수정
- **request**
```json
PUT /reservations/{id}
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 201 CREATED
Content-Type: application/json
        
{
    "id": "1",
    "title": "오브젝트 (코드로 이해하는 객체지향 설계)",
    "author": "조영호",
    "image": "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
    "publisher": "위키북스",
    "pubdate": "2019-06-17",
    "isbn": "9791158391409",
    "description": "역할, 책임, 협력을 향해 객체지향적으로 프로그래밍하라!\n\n객체지향으로 향하는 첫걸음은 클래스가 아니라 객체를 바라보는 것에서부터 시작한다. 객체지향으로 향하는 두번째 걸음은 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 존재로 바라보는 것이다. 세번째 걸음을 내디딜 수 있는지 여부는 협력에 참여하는 객체 들에게 얼마나 적절한 역할과 책임을 부여할 수 있느냐에 달려 있다. 객체지향의 마지막 걸음은 앞에서 설명한 개념들을 여러분이 사용하는 프로그래밍 언어라는 틀에 흐트러짐 없이 담아낼 수 있는 기술을 익히는 것이다.\n\n《객체지향의 사실과 오해》가 첫번째 걸음과 두번째 걸음인 객체와 협력에 초점을 맞췄다면 《오브젝트: 코드로 이해하는 객체지향 설계》는 세번째와 네번째 걸음인 책임의 할당과 그 구현에 초점을 맞춘다. 이 책을 읽고 나면 객체에 적절한 역할과 책임을 부여하는 방법과 유연하면서도 요구사항에 적절한 협력을 설계하는 방법을 익히게 될 것이다. 나아가 프로그래밍 언어라는 도구를 이용해 객체지향의 개념과 원칙들을 오롯이 표현할 수 있는 방법 역시 익힐 수 있을 것이다.\n\n★ 이 책에서 다루는 내용 ★ \n\n◎ 역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법\n◎ 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법\n◎ 설계를 유연하게 만드는 다양한 의존성 관리 기법\n◎ 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념\n◎ 다양한 설계 원칙과 디자인 패턴",
    "reserve_date": "2025-06-18",
    "return_date": "2026-07-01"
}
```

### 3-5. 예약 취소
- **request**
```json
DELETE /reservations/{id}
Content-Type: application/json
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

- **response**
```json
HTTP/1.1 204
```