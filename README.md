# 📌 구현 범위

## ✅ DB

- 접속 정보: http://localhost:8080/h2-console

> 전체 테이블 구조는 ERD 이미지(`erd.png`)를 참고하세요.
### 📂 테이블 구조 요약

| 테이블명        | 설명                               |
|-----------------|------------------------------------|
| `category`      | 카테고리 정보를 관리하는 테이블     |
| `brand`         | 브랜드 정보를 관리하는 테이블       |
| `product`       | 상품 정보를 관리하는 테이블         |
| `product_rank`  | 상품 랭킹 정보를 저장하는 테이블    |



## ✅ API
> 상세 API 정의는 api-docs 폴더를 참고하세요.<br>
> - /api-docs/html/index.html(openapi를 통해 생성된 html 파일)
> - /api-docs/api-yaml.yaml(원본)
### 📂 API 정의 요약
| HTTP Method | Endpoint                                      | 설명                                                             |
|-------------|-----------------------------------------------|------------------------------------------------------------------|
| GET         | `/categories/lowest`                          | 카테고리별 최저가격 브랜드와 상품 가격, 총액 조회                |
| GET         | `/categories/{categoryId}/lowest-highest`     | 카테고리의 최저/최고가격 브랜드와 상품 가격 조회                 |
| GET         | `/brands/categories/lowest`                   | 단일 브랜드 기준 모든 카테리 상품을 최저가로 구매할 때 정보 조회 |
| POST        | `/brands`                                     | 브랜드 등록                                                      |
| DELETE      | `/brands/{brandId}`                           | 브랜드 삭제                                                      |
| POST        | `/products`                                   | 상품 등록                                                        |
| PUT         | `/products/{productId}`                       | 상품 수정                                                        |
| DELETE      | `/products/{productId}`                       | 상품 삭제                                                        |

### 📂 API Error Code
| 코드 | HTTP 상태코드 | 기본 메시지 | 메시지 포맷 |
|------|----------------|--------------|--------------|
| `INTERNAL_SERVER_ERROR` | 500 (Internal Server Error) | Internal Server Error | *(없음)* |
| `NOT_FOUND`             | 404 (Not Found)             | Not Found             | *(없음)* |
| `BAD_REQUEST`           | 400 (Bad Request)           | Bad Request           | *(없음)* |
| `INVALID_REQUEST`       | 400 (Bad Request)           | Invalid               | `Invalid %s` |
| `ALREADY_EXISTS`        | 400 (Bad Request)           | already exists        | `%s already exists` |
| `REQUEST_TIMEOUT`       | 408 (Request Timeout)       | Request Timeout       | *(없음)* |
| `TOO_MANY_REQUESTS`     | 429 (Too Many Requests)     | Too Many Requests     | *(없음)* |
| `SERVICE_UNAVAILABLE`   | 503 (Service Unavailable)   | Service Unavailable   | *(없음)* |
| `UNAUTHORIZED`          | 401 (Unauthorized)          | Unauthorized          | *(없음)* |
| `FORBIDDEN`             | 403 (Forbidden)             | Forbidden             | *(없음)* |



## ✅ 주요 기능

### 1. 상품 랭킹 초기화
- 어플리케이션 최초 실행 시, 랭킹 데이터를 초기화합니다.
- 초기화 대상:
  - **카테고리별 브랜드 최저가 랭킹**
  - **카테고리별 브랜드 최고가 랭킹**
  - **브랜드별 카테고리 최저가 랭킹**
  - **브랜드별 전체 카테고리 최저가 합산 랭킹**

---

### 2. 상품 랭킹 업데이트
- 상품 정보 변경 시 랭킹에 영향이 있는 경우 업데이트 수행
- **동시성 이슈 방지**를 위해 랭킹은 기획된 등수보다 **버퍼 등수(예: 상위 10등 + 5등 = 15등)** 까지 관리

---

### 3. 상품 랭킹 조회
- 아래 4가지 기준으로 랭킹 정보를 조회 가능:
  - 카테고리별 브랜드 최저가 랭킹
  - 카테고리별 브랜드 최고가 랭킹
  - 브랜드별 카테고리 최저가 랭킹
  - 브랜드별 전체 카테고리 최저가 합산 랭킹

---

### 4. 상품 관리 (등록/수정/삭제/조회)
- 상품 등록 및 수정 시 데이터 유효성 검증 수행
- 상품 정보가 랭킹에 영향을 줄 경우 해당 랭킹 업데이트

---

### 5.  브랜드 관리 (등록/삭제/조회)
- 브랜드 등록 시 데이터 유효성 검증 수행
- 브랜드 정보가 랭킹에 영향을 줄 경우 해당 랭킹 업데이트

---

### 6.  카테고리 조회
- 등록된 카테고리 목록 조회 기능 제공

---

### 7. 캐시 적용 (Caffeine Cache)
- **Caffeine**을 사용한 로컬 캐시 적용
- 조회 성능 개선을 위해 캐싱 사용
- 캐시 갱신(@Cacheable,@CachePut,@CacheEvict)

---

### 8. 에러 응답 처리
- `@ExceptionHandler`를 활용하여 커스텀 에러 응답 처리
- 일관된 형태의 에러 메시지 반환

---

### 9. 객체 매핑 (MapStruct)
- MapStruct를 사용하여 객체 간 변환을 효율적으로 처리

---


## 🛠️ 개발 환경 및 기술 스택

- **Java**: OpenJDK 17
- **Build Tool**:  Maven 3.9.9
- **Framework**: Spring Boot 3.4.5
- **Database**: H2 (In-Memory)
- **ORM**: JPA (Hibernate)
- **Mapper**: MapStruct
- **Cache**: Caffeine Cache


---

# 📌 코드 빌드 및 실행
## ✅ Maven Build
```shell
mvn clean:clean package -Dmaven.test.skip=true
```

## ✅ Run Intellij IDE
- Run Application.main()

## ✅ Run Java 
```shell
# -Xms	초기 힙(heap) 메모리 크기 (처음 할당되는 메모리)
# -Xmx	최대 힙 메모리 크기 (최대 사용 가능 메모리)
# -XX	GC종류 : +UseG1GC,+UseParallelGC,+UseConcMarkSweepGC,+UseZGC,+UseShenandoahGC
java -jar lee-shopping-1.0.jar
```


# 📌 테스트
## ✅ Junit Test
### ✅ BrandControllerTest
| 테스트 메서드명             | 설명                             |
|----------------------------|----------------------------------|
| `getBrandCategoriesLowest` | 브랜드 카테고리 최저가 조회 API 테스트 |
| `registerSuccess`           | 정상적인 브랜드 등록 요청 테스트        |
| `registerFailBlank`         | 빈 문자열 브랜드명 등록 실패 테스트     |
| `registerFailMaxSize`       | 최대 길이 초과 브랜드명 등록 실패 테스트 |
| `registerFailDuplicate`     | 중복 브랜드명 등록 실패 테스트         |
| `deleteSuccess`             | 정상적인 브랜드 삭제 테스트            |
| `deleteFail`                | 존재하지 않는 브랜드 삭제 실패 테스트   |

### ✅ CategoryControllerTest
| 테스트 메서드명           | 설명                                |
|--------------------------|-----------------------------------|
| `getCategoriesLowest`      | 전체 카테고리별 최저가 리스트 조회 테스트        |
| `getCategoryLowestHighest` | 특정 카테고리의 최저가 및 최고가 상품 조회 테스트   |
| `getCategoryLowestHighestFail` | 존재하지 않는 카테고리 조회 시 실패 처리 테스트    |

### ✅ ProductControllerTest
| 테스트 메서드명             | 설명                                    |
|----------------------------|---------------------------------------|
| `registerSuccess`            | 정상 상품 등록 테스트                          |
| `registerFailBrandBlank`     | 브랜드명이 빈 문자열일 때 등록 실패 테스트              |
| `registerFailBrandNotFound`  | 존재하지 않는 브랜드로 등록 실패 테스트               |
| `registerFailCategoryNotFound` | 존재하지 않는 카테고리로 등록 실패 테스트             |
| `registerFailPriceMinValid`  | 가격이 최소값(0) 미만일 때 등록 실패 테스트            |
| `registerFailPriceMaxValid`  | 가격이 최대값(100,000,000) 초과일 때 등록 실패 테스트  |
| `modifySuccess`              | 정상 상품 수정 테스트                          |
| `modifyFailNotFoundBrand`    | 수정 시 존재하지 않는 브랜드로 실패 테스트             |
| `modifyFailNotFoundCategory` | 수정 시 존재하지 않는 카테고리로 실패 테스트           |
| `modifyFailTypeMismatch`     | 상품 ID 타입 오류로 수정 실패 테스트                   |
| `modifyFailInvalidId`        | 존재하지 않는 상품 ID로 수정 실패 테스트                |
| `deleteSuccess`              | 정상 상품 삭제 테스트                          |
| `deleteFail`                 | 존재하지 않는 상품 삭제 실패 테스트                   |

### ✅ ProductRepositoryTest
| 테스트 메서드명          | 설명                                                         |
|-------------------------| ---------------------------------------------------------- |
| `getCategoriesLowestTest`   | 모든 카테고리에서 브랜드별 최저가 상품 조회 테스트                               |
| `getCategoriesHighestTest`  | 모든 카테고리에서 브랜드별 최고가 상품 조회 테스트             |
| `getBrandLowestTest`        | 브랜드별 카테고리 최저가 상품 조회 테스트         |


### ✅ ProductRankServiceTest
| 테스트 메서드명                            | 설명                                                |
|-------------------------------------------|---------------------------------------------------|
| `testRankAddUpdate`                       | - A 브랜드의 상품을 각 카테고리별 최저가로 생성하고 랭킹을 갱신 테스트         |
| `testRankDelUpdate`                       | - 랭킹에 반영된 상품을 삭제한 후 랭킹 갱신 테스트                     |
| `testRankAddReloadTest`                   | - 랭킹 제한(MaxRankSize)이 아닌 경우 상품 테이블 기준 갱신 테스트      |
| `testRankAddWhenRankLimitHighScoreTest`   | - 랭킹 제한(MaxRankSize) 상태에서 높은 점수를 가진 상품이 등록 갱신 테스트 |

## ✅ API Test
> API 정의서를 참고하여 API 테스트 도구를 활용하여 요청 테스트를 합니다.
> <br>Postman 사용시 /api-docs/postman/api.postman_collection.json 파일을 import하여 사용.
 




