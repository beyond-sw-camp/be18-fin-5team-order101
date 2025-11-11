<template>
  <div class="franchise-stock">
    <h2 class="page-title">가맹점 재고 현황</h2>

    <div class="controls">
      <select class="select">
        <option>모든 매장</option>
        <option>중앙 지점</option>
        <option>지점 A</option>
      </select>
      <select class="select">
        <option>모든 카테고리</option>
        <option>생활가전</option>
        <option>주방가전</option>
      </select>
      <input class="search" placeholder="검색 SKU 또는 제품명" />

      <div class="spacer"></div>
      <button class="btn-export">CSV 내보내기</button>
    </div>

    <div class="card stock-card">
      <h3 class="card-title">재고 개요</h3>
      <p class="card-sub">모든 제품의 현재 재고 수준입니다.</p>

      <table class="stock-table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>제품명</th>
            <th>재고 수량</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in rows" :key="r.sku">
            <td>{{ r.sku }}</td>
            <td>{{ r.name }}</td>
            <td class="numeric">{{ r.qty }}개</td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button class="page">&lt; Previous</button>
        <button class="page active">1</button>
        <button class="page">2</button>
        <button class="page">3</button>
        <button class="page">Next &gt;</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const rows = ref([
  { sku: 'SKU001', name: '유기농 사과', qty: 500 },
  { sku: 'SKU002', name: '프리미엄 커피 원두', qty: 120 },
  { sku: 'SKU003', name: '수제 베이커리 믹스', qty: 0 },
  { sku: 'SKU004', name: '세척 당근 1kg', qty: 300 },
  { sku: 'SKU005', name: '우유 1L (저지방)', qty: 80 },
])
</script>

<style scoped>
.page-title {
  font-size: 22px;
  margin-bottom: 18px;
}
.controls {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 18px;
}
.select,
.search {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}
.search {
  flex: 1;
}
.spacer {
  flex: 1;
}
.btn-export {
  padding: 8px 12px;
  border-radius: 8px;
  background: #fff;
  border: 1px solid #e5e7eb;
}
.card {
  background: #fff;
  border: 1px solid #eef2f7;
  padding: 20px;
  border-radius: 8px;
}
.stock-card {
  padding: 24px;
}
.card-title {
  font-size: 16px;
  margin-bottom: 8px;
}
.card-sub {
  color: #6b7280;
  margin-bottom: 12px;
}
.stock-table {
  width: 100%;
  border-collapse: collapse;
}
.stock-table thead th {
  text-align: left;
  padding: 14px;
  background: #f8fafc;
  border-bottom: 1px solid #eef2f7;
}
.stock-table tbody td {
  padding: 16px 14px;
  border-top: 1px solid #f7f7f9;
}
.numeric {
  text-align: right;
}
.pagination {
  display: flex;
  gap: 8px;
  justify-content: center;
  margin-top: 18px;
}
.page {
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
  background: #fff;
}
.page.active {
  background: #fff;
  border-color: #c7d2fe;
}

@media (max-width: 1024px) {
  .controls {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
