<script setup>
import { ref } from 'vue'

const categories = ref(['대분류', '중분류', '소분류', '상품별'])

const items = ref([
  { code: 'P-1001', name: '블루투스 이어폰', stock: 500, incoming: 100 },
  { code: 'P-1002', name: '스마트 워치', stock: 120, incoming: 20 },
  { code: 'P-1003', name: '노트북', stock: 30, incoming: 0 },
  { code: 'P-1004', name: '블루투스 이어폰', stock: 280, incoming: 50 },
  { code: 'C-2001', name: '블루투스 이어폰', stock: 700, incoming: 200 },
  { code: 'C-2002', name: '블루투스 이어폰', stock: 150, incoming: 30 },
  { code: 'F-3001', name: '블루투스 이어폰', stock: 90, incoming: 10 },
  { code: 'F-3002', name: '블루투스 이어폰', stock: 200, incoming: 40 },
  { code: 'H-4001', name: '블루투스 이어폰', stock: 350, incoming: 70 },
])
</script>

<template>
  <div class="store-inventory">
    <header class="page-header">
      <h1>창고 재고 상태</h1>
      <div class="filters">
        <button v-for="cat in categories" :key="cat" class="filter-btn">{{ cat }}</button>
      </div>
    </header>

    <section class="overview-card">
      <h2 class="overview-title">현재 재고 개요</h2>
      <div class="table-wrap">
        <table class="inventory-table">
          <thead>
            <tr>
              <th>코드</th>
              <th>품목명</th>
              <th>재고 수량</th>
              <th>입고 예정 수량</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in items" :key="item.code">
              <td>{{ item.code }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.stock }}</td>
              <td>{{ item.incoming }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<style scoped>
.store-inventory {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.page-header h1 {
  font-size: 20px;
  margin: 0;
}
.filters {
  display: flex;
  gap: 8px;
}
.filter-btn {
  background: #fff;
  border: 1px solid #eef0f3;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
}
.overview-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #eef0f3;
}
.overview-title {
  font-size: 16px;
  margin: 0 0 12px 0;
}
.table-wrap {
  overflow: auto;
}
.inventory-table {
  width: 100%;
  border-collapse: collapse;
}
.inventory-table thead th {
  text-align: left;
  padding: 14px;
  background: #fbfbfd;
  color: #6b7280;
  font-weight: 600;
}
.inventory-table tbody td {
  padding: 18px 14px;
  border-top: 1px solid #f3f4f6;
}
.inventory-table tbody tr:nth-child(odd) td {
  background: #fff;
}
.inventory-table tbody tr:nth-child(even) td {
  background: #fbfbfd;
}

@media (max-width: 768px) {
  .inventory-table thead {
    display: none;
  }
  .inventory-table tbody td {
    display: block;
    width: 100%;
  }
}
</style>
