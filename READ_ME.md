## 인메모리캐시(in-memory cache) 구현 과제

### 현재 프로젝트 설정 (appication.properties 파일에 설정)
- server.port=8081
- spring.datasource.url=jdbc:sqlite:D:\\task\\TaskProject\\task.db <br>
    - D:\\task\\TaskProject\\task.db' 이 부분을 PC의 .db 파일 위치에 맞게 수정하시면 됩니다.

### API 목록
#### [GET] /category : 카테고리 전체 목록
- **URL Params Required:**  None
- **Success Response:**
    - Code: 200
    - Content: 
    ```json
    {
        "statusCode": 200,
        "responseMessage": "성공",
        "data": [
            {
                "category_no": "1",
                "category_name": "스킨케어",
                "parent_no": null,
                "depth": "1"
            },
            {
                "category_no": "2",
                "category_name": "메이크업",
                "parent_no": null,
                "depth": "1"
            },
            {
                "category_no": "3",
                "category_name": "생활용품",
                "parent_no": null,
                "depth": "1"
            }
            ...
        ]
    }
    ```
- **Sample Call:** <br>
    `http://localhost:8081/category`
---
#### [POST] /category : 카테고리 추가
- **Body Params Required:**
    - category_name
    - depth
- **Body params**
    - parent_no
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
- **Sample Call:** <br>
   `http://localhost:8081/category`
- **Body Params Example:**
   ```json
   {
    "category_name" : "테스트 카테고리", 
    "depth" : 1
   }
   ```
---
#### [PUT] /category : 카테고리 수정
- **Body Params Required:**
   - category_no
- **Body Params:** 아래 파라미터중 1개이상 필수
   - category_name
   - parent_no
   - depth     
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
    OR
    - Code: 204
- **Sample Call:** <br>
   `http://localhost:8081/product`
- **Body Params Example:**
  ```json
  {
    "category_no" : 11,
    "category_name" : "테스트 카테고리2"
  }
  ```
---
#### [DELETE] /category : 카테고리 삭제
- **Body Params Required:**
   - category_no
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
    OR
    - Code: 204
- **Sample Call:** <br>
   `http://localhost:8081/product`
- **Body Params Example:**
  ```json
  {
    "category_no":11
  }
  ```
---
#### [GET] /product : 상품 목록
- **URL Params Required:** 아래 파라미터중 1개 필수
    - category_name
    - product_name
- **Success Response:**
    - Code: 200
    - Content: 
        ```json
        {
            "statusCode": 200,
            "responseMessage": "성공",
            "data": [
                {
                    "product_no": "1",
                    "product_name": "바이탈뷰티(아) 슬리머에스 35EA (16)",
                    "brand_name": "바이탈뷰티(시판)",
                    "product_price": "112500000",
                    "category_no": "1",
                    "category_name": "스킨케어"
                }
            ]
        }
        ```
- **Error Response:**
    - Code: 400
    - Content: 
        ```json
        {
            "statusCode": 400,
            "responseMessage": "올바르지 않은 접근",
            "data": null
        }
        ```
     OR
     - Code: 204
- **Sample Call:** <br>
    `http://localhost:8081/product?product_name=바이탈뷰티(아) 슬리머에스 35EA (16)`
---
#### [POST] /product : 상품 추가
- **Body Params Required:**
    - product_name
    - brand_name
    - product_price
    - category_no
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
- **Sample Call:** <br>
   `http://localhost:8081/product`
- **Body Params Example:**
   ```json
   {
       "product_name" : "테스트 상품",
       "brand_name" : "테스트 브랜드",
       "product_price" : 10000,
       "category_no" : 3
   }
   ```
---
#### [PUT] /product : 상품 수정
- **Body Params Required:**
   - product_no
- **Body Params:** 아래 파라미터중 1개이상 필수
   - product_name
   - brand_name
   - product_price
   - category_no      
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
    OR
    - Code: 204
- **Sample Call:** <br>
   `http://localhost:8081/product`
- **Body Params Example:**
  ```json
  {
      "product_no" : 1001,
      "product_name" : "테스트 상품2",
      "brand_name" : "테스트 브랜드2"
  }
  ```
---
#### [DELETE] /product/delete : 상품 삭제
- **Body Params Required:**
   - product_no
- **Success Response:**
   - Code: 200
   - Content: 
       ```json
       {
           "statusCode": 200,
           "responseMessage": "성공",
           "data": null
       }
       ```
- **Error Response:**
   - Code: 400
   - Content: 
       ```json
       {
           "statusCode": 400,
           "responseMessage": "올바르지 않은 접근",
           "data": null
       }
       ```
    OR
    - Code: 204
- **Sample Call:** <br>
   `http://localhost:8081/product`
- **Body Params Example:**
  ```json
  {
    "product_no":1001
  }
  ```
