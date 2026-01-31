# health-for-u-server

> 식품안전나라 Open API 데이터를 기반으로, 사용자의 질환별 맞춤형 레시피 정보를 제공하는 헬스포유의 핵심 서비스입니다.

---

## 핵심 기능
- **질환 기반 레시피 필터링:** - 특정 질환 선택 시, 해당 질환의 **주의 식품(필터링 키워드)**을 포함하는 레시피를 자동으로 제외하여 안전한 식단을 제안합니다.
- **스마트 레시피 검색:** - 레시피 검색 시 주의 식품 포함 여부를 체크하여, 포함된 경우 사용자에게 **경고 문구**를 함께 제공합니다.
- **질환 및 카테고리 관리:** - 다양한 질환 데이터를 카테고리별로 분류하고 상세 정보를 제공합니다.
- **개인화 기능 (즐겨찾기):** - 마음에 드는 레시피를 저장하여 마이페이지에서 다시 확인할 수 있습니다. (JWT 인증 필요)

---

## API 라우팅 설계


### Disease (질환 관리)
| 컨트롤러명       | 요청 경로 (Path)             | 리턴값                 | 인증(JWT) 여부 |
|:------------|:-------------------------|:--------------------|:----------:|
| **getDiseaseDetail**   | `GET /api/diseases/{diseaseId}`   | `DiseaseResponse`   |     X      |
| **getDiseasesByCategories**  | `GET /api/diseases/`  | `List<CategoryWithDiseasesResponse> `       |     X      |

### Favorites (즐겨찾기)
| 컨트롤러명       | 요청 경로 (Path)             | 리턴값                 | 인증(JWT) 여부 |
|:------------|:-------------------------|:--------------------|:----------:|
| **toggleFavorite**   | `POST /api/favorites/toggle`   | `?`   |     **O**      |
| **getFavoriteRecipes**  | `GET /api/favorites`  | List<FavoriteListResponse>         |     **O**      |

### Recipe (레시피 관리)
| 컨트롤러명       | 요청 경로 (Path)             | 리턴값                 | 인증(JWT) 여부 |
|:------------|:-------------------------|:--------------------|:----------:|
| **getRecipeDetail**   | `GET /api/recipes/{recipeId}`   | `RecipeResponse`   |     X      |
| **getAllRecipes**  | `GET /api/recipes`  | `Page<RecipeResponse>`        |     X      |


---


## 기술 스택
- **Framework:** Java 21, Spring Boot, Spring Data MongoDB, Redis
- **Database:**
    - **MongoDB (Atlas)**: 질환 정보, 레시피 메타데이터, 사용자별 즐겨찾기 데이터 관리
- **Security:** JJWT 0.12.6, Spring Security
- **Build Tool:** Maven

---

## Roadmap & Updates
- [ ] ElasticSearch 도입 및 검색 성능 최적화
- [ ] 서비스 분리 (Disease, Recipe 마이크로서비스화)
- [ ] JUnit5 기반 테스트 코드 작성 및 커버리지 확보
- [ ] 레시피 이미지 캐싱 처리를 통한 응답 속도 개선
