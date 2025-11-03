<p align="center">
  <img src="https://readme-typing-svg.demolab.com?font=Noto+Sans+KR&weight=900&size=48&duration=2300&pause=900&color=BF00FF&background=00000000&center=true&vCenter=true&width=1000&lines=ORDER101;TEAM+SYNERGY" alt="ORDER101 Typing">
</p>

---

## 👥 팀원 소개

<table>
  <tr align="center">
    <td>조상원</td>
    <td>박진우</td>
    <td>윤석현</td>
    <td>이진구</td>
    <td>최유경</td>
  </tr>
  <tr align="center">
    <td><a target="_blank" href="https://github.com/sangwon5579"><img src="https://avatars.githubusercontent.com/u/81066249?v=4" width="100px"><br>@sangwon5589</a>  </td>
    <td><a target="_blank" href="https://github.com/JINWOO-0715"><img src="https://avatars.githubusercontent.com/u/55976921?v=4" width="100px"><br>@JINWOO-0715</a></td>
    <td><a target="_blank" href="https://github.com/xxiuan"><img src="https://avatars.githubusercontent.com/u/156274066?v=4" width="100px"><br>@xxiuan</a> </td>
    <td><a target="_blank" href="https://github.com/LeeJingu01"><img src="https://avatars.githubusercontent.com/u/174857452?v=4" width="100px"><br>@LeeJingu01</a> </td>
    <td><a target="_blank" href="https://github.com/kyounggg"><img src="https://avatars.githubusercontent.com/u/114654921?v=4" width="100px"><br>@kyounggg</a>  </td>
  </tr>
</table>


## 📚 목차

0. [발표 슬라이드](#0-발표-슬라이드)  
1. [프로젝트 개요](#1-프로젝트-개요)  
2. [요구사항 명세서](#2-요구사항-명세서)  
3. [기술 스택](#3-기술-스택)  
4. [시스템 아키텍처](#4-시스템-아키텍처)  
5. [데이터베이스 설계 (ERD)](#5-데이터베이스-설계-erd)  
6. [테이블 명세서](#6-테이블-명세서)  
7. [API 명세서](#7-api-명세서)
8. .[화면 기능 설계서](#8-화면-기능-설계서)
9. [백엔드 테스트 결과서](#9-백엔드-테스트-결과서)
10. .[프론트엔드 테스트 결과서](#10-프론트엔드-테스트-결과서)
11. .[CICD](#11-CICD)
12. .[트러블 슈팅](#12-트러블-슈팅)
13. [향후 개선 계획](#13-향후-개선-계획)  
14. [회고록](#14-회고록)




<br/>

## <a id="0-발표-슬라이드"></a> 0. 발표 슬라이드

[발표 슬라이드]()
<br>

## <a id="1-프로젝트-개요"></a> 1. 프로젝트 개요  
### 1.1 프로젝트 소개
**ORDER101**은 AI 기반 의사결정 자동화로 공급사–본사–점포의 발주·재고·주문·물류(SCM)를 지능화하여 재고 손실 최소화와 운영 효율 극대화하는 주문 관리 시스템입니다.
<br></br>
### 1.2 프로젝트 배경
- 재고관리 : 수동 및 경험 기반의 발주로 과잉 재고 및 품절 발생 빈번.   
- 운영 효율 : 발주 프로세스가 느리고, 직영점별 관리가 비효율적이며 일관성이 부족함.  
- 경쟁 우위 : 경쟁사들은 이미 데이터 기반 혁신을 가속화하는 추세. 데이터 활용 역량 격차 심화 시 장기적인 리테일 경쟁력 약화 우려.  


<img width="2070" height="584" alt="image" src="https://github.com/user-attachments/assets/8584b2b4-d285-43de-b129-f8dc7a47d311" />
“stockouts (품절)이 전세계 리테일러에 연간 약 1조 달러(약 1,000 조 원) 이상 손실을 안겨주고 있다”  <br>
-> 수요 예측 실패, 재고관리 부정확성, 발주 프로세스 지연 등이 주요 원인  <br>
[출처](https://www.mirakl.com/blog/out-of-stocks-ecommerce-inventory-management-problem?)
<br></br>
<img width="1584" height="463" alt="image" src="https://github.com/user-attachments/assets/9cc90bbf-9f30-4d7c-8d15-c6999f3753c6" />
매장의 과거 판매·발주·재고 데이터 + 외부 변수를 사용하여 최적 발주량을 계산한다.<br>
[출처](https://www.etnews.com/20250331000062?)
<br></br>
<img width="1621" height="406" alt="image" src="https://github.com/user-attachments/assets/d9c56de2-f8d7-4f06-9b1d-62dd6eb8c10c" />
AI가 팔릴 상품 수량 예측하고, 발주까지 자동으로 넣는다.<br>
[출처](https://biz.chosun.com/site/data/html_dir/2020/04/06/2020040602892.html?)

<br></br>
### 1.3 기존 서비스와의 차별점
#### 수요 예측

#### 자동 발주

<br></br>
### 1.4 주요 기능

<details>
  <summary><b>1. 주문 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>주문 생성/승인/내역 조회</li>
      <li>주문 상세 페이지</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>2. 발주 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>발주 생성</li>
      <li>발주 승인 및 관리</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>3. 창고 재고 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>재고 현황</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>4. 상품 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>상품 카탈로그</li>
      <li>상품 등록/수정/상세</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>5. 공급사 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>공급사 조회</li>
      <li>공급사 상세</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>6. 정산 관리</b></summary>
  <div markdown="1">
    <ul>
      <li>정산 관리 리스트</li>
      <li>정산 리포트</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>7. 수요예측</b></summary>
  <div markdown="1">
    <ul>
      <li>AI 예측 모듈</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>8. AI 자동 발주</b></summary>
  <div markdown="1">
    <ul>
      <li>안전 재고 규칙 기반 자동 발주 제안</li>
    </ul>
  </div>
</details>
<details>
  <summary><b>9. 대시보드 및 리포트</b></summary>
  <div markdown="1">
    <ul>
      <li>주문/발주 및 배송 대시보드</li>
      <li>정산 리포트</li>
    </ul>
  </div>
</details>



<br/>

<br/>

## 2. 요구사항 명세서

### 기능 요구사항

#### 2.1 요약

| 요구사항 ID | 대분류 | 중분류 | 소분류 | 상세 설명 | 중요도 |
|-------------|--------|--------|--------|-----------|--------|
|  |  |  |  |  | ★★☆ |
|  |  |  |  |  | ★★☆ |
|  |  |  |  |  | ★★☆ |
|  |  |  |  |  | ★★☆ |
|  |  |  |  |  | ★★☆ |
|  |  |  |  |  | ★★☆ |

#### 2.2 전문
[요구사항 명세서](https://docs.google.com/spreadsheets/d/1vx25t4TzY9Tyu7JUvGRLR6c3iasbg0lG-xEiHt3Kacs/edit?gid=0#gid=0)

</div>
</details>

<br/>


## 3. 기술 스택


<br/>

## 4. 시스템 아키텍처


<br/>


## 5. 데이터베이스 설계 (ERD)

### ERD
[ERD CLOUD](https://www.erdcloud.com/d/BNoDxLbwss3ZbKg8x)
  
![erd]()

<br>


## 6. 테이블 명세서

[테이블 명세서](https://docs.google.com/spreadsheets/d/1L8VvISg4ghGQ_SOjZdJVomAtUtR1-A0axD3hO7-VWj4/edit?gid=1345457380#gid=1345457380)


<br>

## 7. API 명세서

[API 명세서](https://4ktgjt483l.apidog.io)

<br/>

## 8. 화면 기능 설계서 

[화면 기능 설계서](https://app.visily.ai/projects/76f025b6-c743-4fe3-8bea-7c5e958c9d20/boards/2283842)

<br/>

## 9. 백엔드 테스트 결과서

[백엔드 테스트 결과서]()

<br>

## 10. 프론트엔드 테스트 결과서

[백엔드 테스트 결과서]()


## 11. CICD



## 12. 트러블 슈팅



## 13. 향후 개선 계획



<br/>

## 14. 회고록

|   조원 이름	| 회고  	 |
|---	|-------|
| 조상원 | |
| 박진우 | |
| 윤석현 | |
| 이진구 | |
| 최유경 | |
