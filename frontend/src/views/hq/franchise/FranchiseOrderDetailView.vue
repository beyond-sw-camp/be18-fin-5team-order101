<template>
  <div class="page-shell">
    <header class="page-header">
      <h1>주문 상세 내역</h1>
    </header>

    <section class="info-cards">
      <div class="card info">
        <label>주문 ID</label>
        <div class="value">{{ order.id }}</div>
      </div>
      <div class="card info">
        <label>상점</label>
        <div class="value">{{ order.store }}</div>
      </div>
      <div class="card info">
        <label>생성 시간</label>
        <div class="value">{{ order.createdAt }}</div>
      </div>
      <div class="card info">
        <label>상태</label>
        <div class="value status-chip">{{ statusLabel(order.status) }}</div>
      </div>
    </section>

    <section class="card items">
      <h3 class="card-title">주문 아이템</h3>
      <table class="items-table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>상품 이름</th>
            <th>현재 재고</th>
            <th>주문 수량</th>
            <th class="numeric">단가</th>
            <th class="numeric">총액</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="it in order.items" :key="it.sku">
            <td>{{ it.sku }}</td>
            <td>{{ it.name }}</td>
            <td class="numeric">{{ it.stock }}</td>
            <td class="numeric">{{ it.qty }}</td>
            <td class="numeric"><Money :value="it.price" /></td>
            <td class="numeric"><Money :value="it.price * it.qty" /></td>
          </tr>
        </tbody>
      </table>

      <div class="items-summary">
        총 수량: {{ totalQty }} | 총 예상 가격: <strong>{{ formatMoney(totalPrice) }}</strong>
      </div>
    </section>

    <section class="card progress">
      <h3 class="card-title">주문 진행 상황</h3>
      <div class="timeline">
        <div class="steps">
          <div v-for="s in orderProgress" :key="s.key" class="step" :class="{ active: s.done }">
            <div class="icon">●</div>
            <div class="label">
              {{ s.label }}
              <div class="sub">{{ s.time }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="statuses">
        <div v-for="s in orderProgress" :key="s.key" class="status-row">
          <div class="status-title">{{ s.label }}</div>
          <div class="status-time">{{ s.time }}</div>
          <div class="status-desc" v-if="s.note">{{ s.note }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { useRoute } from 'vue-router'
import Money from '@/components/global/Money.vue'

const route = useRoute()
const id = route.params.id || 'ORD-UNKNOWN'

// sample order data; replace with API fetch when available
const order = reactive({
  id: id,
  store: '도매상점 본사점',
  createdAt: '2023-10-26 10:30',
  status: 'delivered',
  items: [
    { sku: 'SKU001', name: 'dd', stock: 1200, qty: 100, price: 5000 },
    { sku: 'SKU002', name: 'ss', stock: 800, qty: 70, price: 7500 },
    { sku: 'SKU003', name: 'dd', stock: 2500, qty: 150, price: 4000 },
    { sku: 'SKU004', name: 'ss', stock: 500, qty: 30, price: 1500 },
    { sku: 'SKU005', name: 'dd', stock: 900, qty: 0, price: 6000 },
  ],
})

const totalQty = computed(() => order.items.reduce((s, it) => s + (it.qty || 0), 0))
const totalPrice = computed(() =>
  order.items.reduce((s, it) => s + (it.price || 0) * (it.qty || 0), 0),
)

function formatMoney(v) {
  return v == null ? '-' : Number(v).toLocaleString() + '원'
}

function statusLabel(s) {
  switch (s) {
    case 'submitted':
      return 'SUBMITTED'
    case 'confirmed':
      return 'CONFIRMED'
    case 'waiting':
      return 'WAITING'
    case 'shipped':
      return 'SHIPPED'
    case 'delivered':
      return 'DELIVERED'
    default:
      return s
  }
}

const orderProgress = [
  { key: 'submitted', label: 'SUBMITTED', time: '2023-10-26 10:30', done: true },
  { key: 'confirmed', label: 'CONFIRMED', time: '2023-10-26 14:15', done: true },
  { key: 'waiting', label: 'WAITING', time: '2023-10-27 09:00', done: true },
  {
    key: 'shipped',
    label: 'SHIPPED',
    time: '2023-10-27 16:45',
    done: true,
    note: '배송번호: DPD123456789',
  },
  { key: 'delivered', label: 'DELIVERED', time: '', done: false },
]
</script>

<style scoped>
.page-shell {
  padding: 24px 32px;
}
.page-header {
  margin-bottom: 18px;
}
.info-cards {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
.card.info {
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #eef2f7;
  background: #fff;
  min-width: 180px;
}
.card.info label {
  font-size: 12px;
  color: #6b7280;
}
.card.info .value {
  font-weight: 700;
  margin-top: 8px;
}
.status-chip {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 12px;
  background: #e5e7eb;
}
.items .card-title,
.progress .card-title {
  margin-bottom: 8px;
}
.items-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 8px;
}
.items-table th,
.items-table td {
  padding: 12px;
  border-top: 1px solid #f3f4f6;
  text-align: left;
}
.items-table td.numeric {
  text-align: right;
}
.items-summary {
  text-align: right;
  margin-top: 8px;
  font-weight: 600;
}
.timeline {
  padding: 12px 0;
}
.steps {
  display: flex;
  gap: 16px;
  align-items: center;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #9ca3af;
}
.step.active {
  color: #6b46ff;
}
.step .icon {
  font-size: 18px;
}
.step .label {
  font-size: 12px;
  margin-top: 6px;
  text-align: center;
}
.step .sub {
  font-size: 11px;
  color: #9ca3af;
}
.statuses {
  margin-top: 12px;
}
.status-row {
  border-top: 1px solid #f3f4f6;
  padding: 12px 0;
}
.status-title {
  font-weight: 700;
}
.status-time {
  color: #6b7280;
  font-size: 12px;
  margin-top: 6px;
}
.status-desc {
  margin-top: 8px;
  color: #374151;
}
</style>
