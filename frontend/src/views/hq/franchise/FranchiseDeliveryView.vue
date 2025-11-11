<template>
  <div class="page-shell">
    <header class="page-header">
      <h1>배송 조회</h1>
    </header>

    <section class="filters card">
      <div class="filters-row">
        <input placeholder="주문 ID 검색" v-model="filters.q" />
        <select v-model="filters.store">
          <option value="all">모든 가맹점</option>
          <option value="강남점">강남점</option>
          <option value="명동점">명동점</option>
          <option value="부산 해운대점">부산 해운대점</option>
        </select>
        <select v-model="filters.status">
          <option value="all">모든 상태</option>
          <option value="pending">배송 대기중</option>
          <option value="in_transit">배송 중</option>
          <option value="delivered">배송 완료</option>
        </select>
        <button class="btn" @click="applyFilter">필터 적용</button>
        <button class="btn" @click="resetFilter">필터 초기화</button>
      </div>
    </section>

    <section class="card list">
      <div class="table-wrap">
        <table class="delivery-table">
          <thead>
            <tr>
              <th>주문 ID</th>
              <th>가맹점</th>
              <th>배송 창고</th>
              <th class="numeric">총 수량</th>
              <th>처리 상태</th>
              <th>요청 시간</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in filteredRows" :key="r.id">
              <td class="po">{{ r.id }}</td>
              <td>{{ r.store }}</td>
              <td>{{ r.warehouse }}</td>
              <td class="numeric">{{ r.qty }}</td>
              <td>
                <span :class="['chip', statusClass(r.status)]">{{ statusLabel(r.status) }}</span>
              </td>
              <td>{{ r.requestedAt }}</td>
            </tr>
            <tr v-if="filteredRows.length === 0">
              <td colspan="6" class="no-data">조회 결과가 없습니다.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const filters = ref({ q: '', store: 'all', status: 'all' })

const rows = ref([
  {
    id: 'ORD-001',
    store: '강남점',
    warehouse: '수도권 중앙 물류창고',
    qty: 15,
    status: 'pending',
    requestedAt: '22/11/2024',
  },
  {
    id: 'ORD-002',
    store: '명동점',
    warehouse: '수도권 중앙 물류창고',
    qty: 8,
    status: 'in_transit',
    requestedAt: '20/11/2024',
  },
  {
    id: 'ORD-003',
    store: '부산 해운대점',
    warehouse: '영남 지역 물류센터',
    qty: 22,
    status: 'delivered',
    requestedAt: '30/08/2025',
  },
  {
    id: 'ORD-004',
    store: '대전 은행점',
    warehouse: '충청 지역 물류센터',
    qty: 5,
    status: 'pending',
    requestedAt: '05/05/2021',
  },
  {
    id: 'ORD-005',
    store: '제주 시청점',
    warehouse: '제주 허브 물류센터',
    qty: 10,
    status: 'in_transit',
    requestedAt: '17/06/2020',
  },
  {
    id: 'ORD-006',
    store: '광주 상무점',
    warehouse: '호남 지역 물류센터',
    qty: 7,
    status: 'delivered',
    requestedAt: '26/01/2021',
  },
  {
    id: 'ORD-007',
    store: '강남점',
    warehouse: '수도권 중앙 물류창고',
    qty: 12,
    status: 'pending',
    requestedAt: '14/06/2025',
  },
  {
    id: 'ORD-008',
    store: '동대문점',
    warehouse: '수도권 중앙 물류창고',
    qty: 3,
    status: 'in_transit',
    requestedAt: '01/11/2021',
  },
  {
    id: 'ORD-009',
    store: '광화문점',
    warehouse: '수도권 중앙 물류창고',
    qty: 20,
    status: 'delivered',
    requestedAt: '05/09/2021',
  },
  {
    id: 'ORD-010',
    store: '울산 삼산점',
    warehouse: '영남 지역 물류센터',
    qty: 9,
    status: 'pending',
    requestedAt: '07/09/2022',
  },
])

const filteredRows = computed(() => {
  return rows.value.filter((r) => {
    const q = filters.value.q && filters.value.q.toLowerCase()
    if (q && !r.id.toLowerCase().includes(q)) return false
    if (filters.value.store !== 'all' && r.store !== filters.value.store) return false
    if (filters.value.status !== 'all' && r.status !== filters.value.status) return false
    return true
  })
})

function statusClass(s) {
  if (s === 'delivered') return 's-delivered'
  if (s === 'in_transit') return 's-intransit'
  return 's-pending'
}

function statusLabel(s) {
  if (s === 'delivered') return '배송 완료'
  if (s === 'in_transit') return '배송 중'
  return '배송 대기중'
}

function applyFilter() {
  // computed reacts to filters; this is a placeholder in case we want actions
}
function resetFilter() {
  filters.value = { q: '', store: 'all', status: 'all' }
}
</script>

<style scoped>
.page-shell {
  padding: 24px 32px;
}
.page-header {
  margin-bottom: 18px;
}
.card {
  background: #fff;
  border: 1px solid #f0f0f3;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}
.filters-row {
  display: flex;
  gap: 12px;
  align-items: center;
}
.filters-row input,
.filters-row select {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e6e6e9;
}
.btn {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e6e6e9;
  background: white;
  cursor: pointer;
}
.delivery-table {
  width: 100%;
  border-collapse: collapse;
}
.delivery-table th,
.delivery-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #f0f0f3;
  text-align: left;
}
.delivery-table td.numeric {
  text-align: right;
}
.po {
  font-weight: 600;
}
.chip {
  padding: 6px 10px;
  border-radius: 12px;
  color: #fff;
  font-size: 13px;
}
.s-delivered {
  background: #0ea5a4;
}
.s-intransit {
  background: #2563eb;
}
.s-pending {
  background: #6b7280;
}
.no-data {
  text-align: center;
  color: #999;
  padding: 20px;
}
</style>
