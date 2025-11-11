<template>
  <div class="movements">
    <h2 class="page-title">입/출고 목록 조회</h2>

    <div class="tab-row">
      <button class="tab active">입고</button>
      <button class="tab">출고</button>
    </div>

    <div class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <label>제품명</label>
          <input placeholder="제품명 검색" />
        </div>
        <div class="filter-item">
          <label>날짜 범위</label>
          <input placeholder="날짜 범위 선택" />
        </div>
        <div class="filter-actions">
          <button class="btn primary">검색</button>
          <button class="btn">초기화</button>
        </div>
      </div>
    </div>

    <div class="table-card">
      <table class="movements-table">
        <thead>
          <tr>
            <th>입고 번호</th>
            <th>제품명</th>
            <th>수량</th>
            <th>입고일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.code }}</td>
            <td>{{ row.product }}</td>
            <td class="numeric">{{ row.qty }}</td>
            <td>{{ row.date }}</td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button class="page-btn">‹ Previous</button>
        <div class="page-numbers">
          <button class="page current">1</button>
          <button class="page">2</button>
        </div>
        <button class="page-btn">Next ›</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const rows = ref([
  { id: 1, code: 'INB-20230110-010', product: '냉동 베이커리 믹스', qty: 90, date: '2023-01-14' },
  { id: 2, code: 'INB-20230109-009', product: '수제 잼 세트', qty: 60, date: '2023-01-13' },
  { id: 3, code: 'INB-20230108-008', product: '건강 음료 500ml', qty: 200, date: '2023-01-12' },
  { id: 4, code: 'INB-20230106-006', product: '프리미엄 건과류', qty: 70, date: '2023-01-10' },
  { id: 5, code: 'INB-20230106-006', product: '특제 소스 1L', qty: 120, date: '2023-01-10' },
])
</script>

<style scoped>
.page-title {
  font-size: 20px;
  margin: 8px 0 16px;
}
.tab-row {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}
.tab {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid transparent;
  background: transparent;
  cursor: pointer;
}
.tab.active {
  background: #eef2ff;
  border-color: #c7d2fe;
  font-weight: 600;
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
  align-items: end;
}
.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}
.filter-item input {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}
.filter-actions {
  display: flex;
  gap: 8px;
}
.btn {
  padding: 10px 16px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  background: #fff;
  cursor: pointer;
}
.btn.primary {
  background: #6366f1;
  color: #fff;
  border-color: #6366f1;
}
.table-card {
  background: #fff;
  border: 1px solid #e9eef6;
  padding: 0;
  border-radius: 8px;
  overflow: hidden;
}
.movements-table {
  width: 100%;
  border-collapse: collapse;
}
.movements-table thead th {
  background: #f3f4f6;
  text-align: left;
  padding: 16px;
}
.movements-table tbody td {
  padding: 18px 16px;
  border-top: 1px solid #f3f4f6;
}
.numeric {
  text-align: right;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 18px;
}
.page {
  border: 1px solid #e5e7eb;
  padding: 6px 10px;
  border-radius: 6px;
  background: #fff;
}
.page.current {
  background: #f3f4f6;
}
.page-btn {
  background: transparent;
  border: none;
  color: #6b7280;
  cursor: pointer;
}
</style>
