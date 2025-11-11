<template>
  <div class="settlement-list">
    <h2 class="page-title">정산 목록</h2>

    <div class="filter-card card">
      <div class="filter-row">
        <div>
          <label>정산 유형</label>
          <div class="checkboxes">
            <label><input type="checkbox" checked /> AR</label>
            <label><input type="checkbox" checked /> AP</label>
          </div>
        </div>

        <div>
          <label>상태</label>
          <div class="checkboxes">
            <label><input type="checkbox" checked /> 초안</label>
            <label><input type="checkbox" checked /> 발행</label>
            <label><input type="checkbox" checked /> 무효</label>
          </div>
        </div>

        <div>
          <label>기간</label>
          <input type="month" class="input" />
        </div>

        <div class="search-wrapper">
          <label>검색</label>
          <input placeholder="ID 또는 공급업체 검색..." class="input" />
        </div>
      </div>
    </div>

    <div class="card list-card">
      <h3 class="card-title">정산 목록</h3>
      <table class="settlement-table">
        <thead>
          <tr>
            <th>정산 ID</th>
            <th>유형</th>
            <th>상점/공급사</th>
            <th>기간</th>
            <th>총 수량</th>
            <th>총 금액</th>
            <th>순 금액</th>
            <th>상태</th>
            <th>생성일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in rows" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.type }}</td>
            <td>{{ r.entity }}</td>
            <td>{{ r.period }}</td>
            <td class="numeric">{{ r.qty }}</td>
            <td class="numeric"><Money :value="r.total" /></td>
            <td class="numeric"><Money :value="r.net" /></td>
            <td>
              <span
                :class="[
                  'badge',
                  r.status === '발행됨' ? 'published' : r.status === '무효' ? 'invalid' : 'draft',
                ]"
                >{{ r.status }}</span
              >
            </td>
            <td>{{ r.created }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const rows = ref([
  {
    id: 'SETL001',
    type: 'AR',
    entity: '서울 본점',
    period: '2023-01',
    qty: 150,
    total: 1500000,
    net: 1450000,
    status: '발행됨',
    created: '2023-01-05',
  },
  {
    id: 'SETL002',
    type: 'AP',
    entity: '부산 지점',
    period: '2023-01',
    qty: 80,
    total: 800000,
    net: 780000,
    status: '초안',
    created: '2023-01-07',
  },
  {
    id: 'SETL003',
    type: 'AR',
    entity: '대구 지점',
    period: '2023-02',
    qty: 200,
    total: 2000000,
    net: 1900000,
    status: '잠김',
    created: '2023-02-01',
  },
  {
    id: 'SETL004',
    type: 'AP',
    entity: '광주 공급사',
    period: '2023-02',
    qty: 120,
    total: 1200000,
    net: 1180000,
    status: '무효',
    created: '2023-02-10',
  },
  {
    id: 'SETL005',
    type: 'AR',
    entity: '인천 물류',
    period: '2023-03',
    qty: 90,
    total: 900000,
    net: 870000,
    status: '발행됨',
    created: '2023-03-01',
  },
  {
    id: 'SETL006',
    type: 'AR',
    entity: '제주도 가맹점',
    period: '2023-03',
    qty: 180,
    total: 1800000,
    net: 1750000,
    status: '초안',
    created: '2023-03-10',
  },
  {
    id: 'SETL007',
    type: 'AP',
    entity: '대전 협력사',
    period: '2023-04',
    qty: 70,
    total: 700000,
    net: 680000,
    status: '잠김',
    created: '2023-04-01',
  },
])
</script>

<style scoped>
.page-title {
  font-size: 22px;
  margin-bottom: 12px;
}
.filter-card {
  padding: 16px;
  margin-bottom: 18px;
}
.filter-row {
  display: flex;
  gap: 24px;
  align-items: flex-end;
}
.checkboxes label {
  margin-right: 8px;
}
.input {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}
.search-wrapper {
  flex: 1;
}
.list-card {
  padding: 18px;
}
.settlement-table {
  width: 100%;
  border-collapse: collapse;
}
.settlement-table thead th {
  text-align: left;
  padding: 12px;
  border-bottom: 1px solid #eef2f7;
}
.settlement-table tbody td {
  padding: 14px;
  border-top: 1px solid #f7f7f9;
}
.numeric {
  text-align: right;
}
.badge {
  padding: 6px 10px;
  border-radius: 999px;
  color: #fff;
  font-weight: 600;
}
.badge.published {
  background: #4f46e5;
}
.badge.invalid {
  background: #ef4444;
}
.badge.draft {
  background: #9ca3af;
}

@media (max-width: 1024px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
