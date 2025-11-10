<template>
  <div class="stock-view">
    <h2 class="page-title">창고 재고 상태</h2>

    <div class="filter-card">
      <div class="filter-row">
        <select class="select">
          <option>중앙 창고</option>
          <option>지점 A</option>
        </select>
        <select class="select">
          <option>카테고리</option>
          <option>생활가전</option>
          <option>주방가전</option>
        </select>
      </div>
    </div>

    <div class="table-card">
      <h3 class="card-title">현재 재고 개요</h3>
      <table class="stock-table">
        <thead>
          <tr>
            <th>상품 코드</th>
            <th>카테고리</th>
            <th>상품명</th>
            <th>현 재고량</th>
            <th>이동중 수량</th>
            <th>안전재고량</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in rows" :key="r.code">
            <td>{{ r.code }}</td>
            <td>{{ r.category }}</td>
            <td>{{ r.name }}</td>
            <td class="numeric">{{ r.qty }}</td>
            <td class="numeric">{{ r.intransit }}</td>
            <td class="numeric">{{ r.safety }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const rows = ref([
  { code: '97205', category: '영상가전', name: 'TV', qty: 500, intransit: 2, safety: 99 },
  { code: '73102', category: '영상가전', name: '스탠바이미', qty: 120, intransit: 67, safety: 96 },
  { code: '78746', category: '주방가전', name: '식기세척기', qty: 30, intransit: 81, safety: 93 },
  { code: '32202', category: '생활가전', name: '세탁기', qty: 280, intransit: 34, safety: 62 },
  { code: '67202', category: '생활가전', name: '청소기', qty: 700, intransit: 45, safety: 56 },
  { code: '94103', category: '생활가전', name: '드럼세탁기', qty: 150, intransit: 16, safety: 3 },
  { code: '19123', category: '주방가전', name: '식기세척기', qty: 90, intransit: 97, safety: 89 },
  { code: '02119', category: '주방가전', name: '전자레인지', qty: 200, intransit: 96, safety: 45 },
  { code: '95814', category: '웨어러블', name: '애플워치', qty: 350, intransit: 37, safety: 21 },
])

function onImageError(event) {
  event.target.src = 'https://via.placeholder.com/800x400?text=Stock+Diagram+Placeholder'
}
</script>

<style scoped>
.stock-view {
  padding: 16px;
}
.stock-image-wrap {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.stock-image {
  max-width: 100%;
  height: auto;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
}
.caption {
  margin-top: 8px;
  color: #6b7280;
  font-size: 14px;
}
.note {
  margin-top: 16px;
  color: #374151;
}
.page-title {
  font-size: 20px;
  margin: 8px 0 16px;
}
.filter-card {
  background: #fff;
  border: 1px solid #e9eef6;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}
.filter-row {
  display: flex;
  gap: 16px;
}
.select {
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: #fff;
}
.table-card {
  background: #fff;
  border: 1px solid #e9eef6;
  padding: 16px;
  border-radius: 8px;
}
.card-title {
  font-size: 16px;
  margin-bottom: 12px;
}
.stock-table {
  width: 100%;
  border-collapse: collapse;
}
.stock-table thead th {
  background: #f3f4f6;
  text-align: left;
  padding: 14px;
  border-bottom: 1px solid #eef2f7;
}
.stock-table tbody td {
  padding: 18px 14px;
  border-top: 1px solid #f7f7f9;
}
.numeric {
  text-align: left;
}
</style>
