<template>
  <div class="page-shell">
    <header class="page-header">
      <h1>발주 관리</h1>
    </header>

    <section class="card controls">
      <div class="controls-row">
        <input class="search" v-model="filters.q" placeholder="PO 번호, 공급업체 등으로 검색..." />

        <div class="controls-right">
          <label class="status-select">
            <select v-model="filters.status">
              <option value="전체">모든 상태</option>
              <option value="대기 중">대기 중</option>
              <option value="승인됨">승인됨</option>
              <option value="거부됨">거부됨</option>
            </select>
          </label>
          <button class="btn">날짜 범위</button>
          <button class="btn" @click="refresh">새로고침</button>
        </div>
      </div>
    </section>

    <section class="card list">
      <div class="table-wrap">
        <table class="orders-table">
          <thead>
            <tr>
              <th>PO 번호</th>
              <th>요청자</th>
              <th>공급업체</th>
              <th>품목 수</th>
              <th class="numeric">금액</th>
              <th>요청일</th>
              <th>상태</th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="row in filteredRowsPaginated"
              :key="row.id"
              class="clickable-row"
              @click="openApproval(row)"
            >
              <td class="po">{{ row.id }}</td>
              <td>{{ row.requester }}</td>
              <td>{{ row.vendor }}</td>
              <td class="numeric">{{ row.items }}</td>
              <td class="numeric">{{ formatMoney(row.amount) }}</td>
              <td>{{ row.requestedAt }}</td>
              <td>
                <span :class="['chip', statusClass(row.status)]">{{ row.status }}</span>
              </td>
              <td class="actions">⋯</td>
            </tr>
            <tr v-if="filteredRows.length === 0">
              <td colspan="8" class="no-data">검색 조건에 맞는 발주가 없습니다.</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination">
        <button @click="prevPage" :disabled="page === 1">‹ Previous</button>
        <div class="pages">
          <button
            v-for="p in totalPages"
            :key="p"
            :class="{ active: p === page }"
            @click="goPage(p)"
          >
            {{ p }}
          </button>
        </div>
        <button @click="nextPage" :disabled="page === totalPages">Next ›</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const filters = ref({ q: '', status: '전체' })
const page = ref(1)
const perPage = ref(7)

const rows = ref([
  {
    id: 'PO-2024-001',
    requester: '김철수',
    vendor: 'ABC 전자',
    items: 10,
    amount: 1500.0,
    requestedAt: '2024-03-10',
    status: '대기 중',
  },
  {
    id: 'PO-2024-002',
    requester: '이영희',
    vendor: 'XYZ 산업',
    items: 5,
    amount: 800.5,
    requestedAt: '2024-03-12',
    status: '승인됨',
  },
  {
    id: 'PO-2024-003',
    requester: '박민준',
    vendor: '테크 솔루션',
    items: 2,
    amount: 2500.75,
    requestedAt: '2024-03-15',
    status: '대기 중',
  },
  {
    id: 'PO-2024-004',
    requester: '최지혜',
    vendor: '글로벌 유통',
    items: 12,
    amount: 320.0,
    requestedAt: '2024-03-18',
    status: '거부됨',
  },
  {
    id: 'PO-2024-005',
    requester: '강동원',
    vendor: '이노베이션랩',
    items: 1,
    amount: 5000.0,
    requestedAt: '2024-03-20',
    status: '대기 중',
  },
  {
    id: 'PO-2024-006',
    requester: '윤서아',
    vendor: '프라임 자재',
    items: 7,
    amount: 650.25,
    requestedAt: '2024-03-22',
    status: '승인됨',
  },
  {
    id: 'PO-2024-007',
    requester: '정우진',
    vendor: '베스트 소프트',
    items: 3,
    amount: 1200.0,
    requestedAt: '2024-03-25',
    status: '대기 중',
  },
  {
    id: 'PO-2024-008',
    requester: '김민지',
    vendor: '모던샵',
    items: 4,
    amount: 420.0,
    requestedAt: '2024-03-27',
    status: '승인됨',
  },
])

const filteredRows = computed(() => {
  const q = (filters.value.q || '').trim().toLowerCase()
  // apply text filter first
  let list = rows.value
  if (q) {
    list = list.filter((r) => {
      return (
        r.id.toLowerCase().includes(q) ||
        r.requester.toLowerCase().includes(q) ||
        r.vendor.toLowerCase().includes(q)
      )
    })
  }
  // apply status filter
  const status = filters.value.status || '전체'
  if (status && status !== '전체') {
    list = list.filter((r) => r.status === status)
  }
  return list
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredRows.value.length / perPage.value)))

const filteredRowsPaginated = computed(() => {
  const start = (page.value - 1) * perPage.value
  return filteredRows.value.slice(start, start + perPage.value)
})

const router = useRouter()

function openApproval(row) {
  // navigate to approval/detail page for the selected PO
  router.push({ name: 'hq-orders-approval-detail', params: { id: row.id } })
}

function goPage(p) {
  page.value = p
}
function prevPage() {
  if (page.value > 1) page.value--
}
function nextPage() {
  if (page.value < totalPages.value) page.value++
}

function refresh() {
  // placeholder: would re-fetch from API
  console.log('refresh')
}

function formatMoney(v) {
  if (v == null) return '-'
  return (
    '$' +
    Number(v).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
  )
}

function statusClass(s) {
  if (!s) return ''
  if (s.includes('승인')) return 's-accepted'
  if (s.includes('대기')) return 's-waiting'
  if (s.includes('거부')) return 's-rejected'
  return ''
}
</script>

<style scoped>
.page-shell {
  padding: 24px 32px;
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
}
.page-header h1 {
  margin: 0;
  font-size: 22px;
}
.card {
  background: #fff;
  border: 1px solid #f0f0f3;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}
.controls-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.search {
  flex: 1;
  padding: 10px 12px;
  border-radius: 10px;
  border: 1px solid #e6e6e9;
}
.controls-right {
  display: flex;
  gap: 8px;
}
.btn {
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px solid #e6e6e9;
  background: white;
  cursor: pointer;
}
.table-wrap {
  margin-top: 12px;
}
.orders-table {
  width: 100%;
  border-collapse: collapse;
}
.orders-table th,
.orders-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #f0f0f3;
  text-align: left;
}
.orders-table td.numeric {
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
.s-accepted {
  background: #16a34a;
}
.s-waiting {
  background: #d97706;
}
.s-rejected {
  background: #ef4444;
}
.actions {
  text-align: center;
}
.no-data {
  text-align: center;
  padding: 26px;
  color: #999;
}
.pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
}
.pages {
  display: flex;
  gap: 8px;
}
.pages button {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #e6e6e9;
  background: white;
}
.pages button.active {
  background: #111827;
  color: white;
}
.clickable-row {
  cursor: pointer;
}
</style>
