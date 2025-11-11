<template>
  <div class="page-shell">
    <header class="page-header">
      <h1>가맹점 주문 승인 요청 목록</h1>
    </header>

    <section class="filters card">
      <div class="filters-row">
        <select v-model="filter.status">
          <option value="all">모든 상태</option>
          <option value="pending">대기</option>
          <option value="approved">승인</option>
          <option value="rejected">반려</option>
        </select>

        <select v-model="filter.store">
          <option value="all">모든 상점</option>
          <option value="본점">본점</option>
          <option value="지점 A">지점 A</option>
          <option value="지점 B">지점 B</option>
        </select>

        <input type="date" v-model="filter.date" />
      </div>
    </section>

    <section class="card list">
      <div class="table-wrap">
        <table class="approval-table">
          <thead>
            <tr>
              <th>주문 ID</th>
              <th>가맹점</th>
              <th>품목 수</th>
              <th>총 수량</th>
              <th class="numeric">예상 가격</th>
              <th>생성 시간</th>
              <th>승인</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="row in filteredRows"
              :key="row.id"
              class="clickable-row"
              @click="openDetail(row)"
            >
              <td class="po">{{ row.id }}</td>
              <td>{{ row.store }}</td>
              <td class="numeric">{{ row.itemCount }}</td>
              <td class="numeric">{{ row.totalQty }}</td>
              <td class="numeric"><Money :value="row.estimatedPrice" /></td>
              <td>{{ row.createdAt }}</td>
              <td class="actions">
                <button class="btn-accept" @click.stop="approve(row)">승인</button>
                <button class="btn-reject" @click.stop="reject(row)">반려</button>
              </td>
            </tr>
            <tr v-if="filteredRows.length === 0">
              <td colspan="7" class="no-data">승인 요청이 없습니다.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import Money from '@/components/global/Money.vue'

const router = useRouter()

const filter = ref({ status: 'all', store: 'all', date: '' })

const rows = ref([
  {
    id: 'ORD001',
    store: '본점',
    itemCount: 15,
    totalQty: 120,
    estimatedPrice: 250000,
    createdAt: '2024-07-20 10:30',
    status: 'pending',
  },
  {
    id: 'ORD002',
    store: '지점 A',
    itemCount: 8,
    totalQty: 45,
    estimatedPrice: 95000,
    createdAt: '2024-07-19 14:00',
    status: 'pending',
  },
  {
    id: 'ORD003',
    store: '지점 B',
    itemCount: 20,
    totalQty: 200,
    estimatedPrice: 320000,
    createdAt: '2024-07-18 09:15',
    status: 'pending',
  },
  {
    id: 'ORD004',
    store: '본점',
    itemCount: 5,
    totalQty: 30,
    estimatedPrice: 60000,
    createdAt: '2024-07-21 11:45',
    status: 'pending',
  },
  {
    id: 'ORD005',
    store: '지점 A',
    itemCount: 10,
    totalQty: 80,
    estimatedPrice: 150000,
    createdAt: '2024-07-17 16:00',
    status: 'pending',
  },
])

const filteredRows = computed(() => {
  return rows.value.filter((r) => {
    if (filter.value.status !== 'all' && r.status !== filter.value.status) return false
    if (filter.value.store !== 'all' && r.store !== filter.value.store) return false
    if (filter.value.date && !r.createdAt.startsWith(filter.value.date)) return false
    return true
  })
})

function openDetail(row) {
  router.push({ name: 'hq-franchise-approval-detail', params: { id: row.id } })
}

function approve(row) {
  alert(`${row.id} 승인 처리되었습니다.`)
}
function reject(row) {
  alert(`${row.id} 반려 처리되었습니다.`)
}
</script>

<style scoped>
.page-shell {
  padding: 24px 32px;
}
.page-header {
  margin-bottom: 18px;
}
.filters-row {
  display: flex;
  gap: 12px;
  align-items: center;
}
.card {
  background: #fff;
  border: 1px solid #f0f0f3;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}
.table-wrap {
  margin-top: 12px;
}
.approval-table {
  width: 100%;
  border-collapse: collapse;
}
.approval-table th,
.approval-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #f0f0f3;
  text-align: left;
}
.approval-table td.numeric {
  text-align: right;
}
.po {
  font-weight: 600;
}
.actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
.btn-accept {
  background: #6b46ff;
  color: #fff;
  border: none;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
}
.btn-reject {
  background: #fff;
  color: #6b46ff;
  border: 1px solid #c7b8ff;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
}
.clickable-row {
  cursor: pointer;
}
.no-data {
  text-align: center;
  color: #999;
  padding: 20px;
}
</style>
