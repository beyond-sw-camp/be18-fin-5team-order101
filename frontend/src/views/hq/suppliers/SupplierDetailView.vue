<template>
  <div class="supplier-detail">
    <h2 class="page-title">공급사 상세 정보</h2>

    <div class="card info-card">
      <div class="info-row">
        <div>
          <div class="label">코드</div>
          <div class="value">{{ supplier.code }}</div>
        </div>
        <div>
          <div class="label">이름</div>
          <div class="value">{{ supplier.name }}</div>
        </div>
        <div>
          <div class="label">주소</div>
          <div class="value">{{ supplier.address }}</div>
        </div>
        <div>
          <div class="label">계약일</div>
          <div class="value">{{ supplier.contract }}</div>
        </div>
      </div>

      <div class="divider"></div>

      <div class="contact-row">
        <div>
          <div class="label">담당자</div>
          <div class="value">{{ supplier.manager }}</div>
        </div>
        <div>
          <div class="label">전화번호</div>
          <div class="value">{{ supplier.phone }}</div>
        </div>
      </div>
    </div>

    <div class="card products-card">
      <h3 class="card-title">제품 목록</h3>
      <input class="search" placeholder="제품명/코드 검색..." />

      <table class="products-table">
        <thead>
          <tr>
            <th>제품 ID</th>
            <th>공급사 제품 코드</th>
            <th>제품명</th>
            <th>구매 가격</th>
            <th>리드 타임 (일)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in supplier.products" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.vendorCode }}</td>
            <td>{{ p.name }}</td>
            <td class="numeric">{{ p.price }}원</td>
            <td class="numeric">{{ p.lead }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const id = route.params.id || 'SUP001'

// In a real app, fetch supplier details by id. Here we use a static sample.
const supplier = ref({
  code: id,
  name: '혁신 기술 주식회사',
  address: '서울특별시 강남구 테헤란로 123, 10층',
  contract: '2023-01-15',
  manager: '김현아',
  phone: '010-1234-5678',
  products: [
    { id: 'PROD001', vendorCode: 'STX-A001', name: '초고속 USB-C 허브', price: '45,000', lead: 3 },
    { id: 'PROD002', vendorCode: 'ELX-B005', name: '무선 충전 마우패드', price: '28,000', lead: 5 },
    { id: 'PROD003', vendorCode: 'MNT-C010', name: '인체공학 키보드', price: '72,000', lead: 7 },
    { id: 'PROD004', vendorCode: 'ACC-D022', name: '고급형 월캠', price: '59,000', lead: 4 },
    {
      id: 'PROD005',
      vendorCode: 'PWR-E003',
      name: '휴대용 보조 배터리 10000mAh',
      price: '35,000',
      lead: 2,
    },
  ],
})
</script>

<style scoped>
.page-title {
  font-size: 20px;
  margin-bottom: 12px;
}
.info-card {
  padding: 18px;
  margin-bottom: 18px;
}
.info-row {
  display: flex;
  gap: 24px;
}
.label {
  color: #6b7280;
  font-size: 13px;
}
.value {
  font-weight: 600;
  margin-top: 6px;
}
.divider {
  height: 1px;
  background: #eef2f7;
  margin: 14px 0;
}
.products-card {
  padding: 18px;
}
.products-table {
  width: 100%;
  border-collapse: collapse;
}
.products-table thead th {
  text-align: left;
  padding: 12px;
  border-bottom: 1px solid #eef2f7;
}
.products-table tbody td {
  padding: 12px;
  border-top: 1px solid #f7f7f9;
}
.numeric {
  text-align: right;
}
.search {
  padding: 8px 10px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-bottom: 12px;
}
</style>
