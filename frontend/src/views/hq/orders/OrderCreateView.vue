<template>
  <div class="order-create">
    <div class="page-inner">
      <div class="left-col">
        <h2 class="title">발주 생성</h2>

        <section class="card">
          <h3 class="card-title">발주 세부 정보</h3>
          <div class="form-row">
            <label>공급업체 선택</label>
            <select class="select">
              <option>싱숭생숭 Inc.</option>
            </select>
          </div>

          <div class="form-row">
            <label>필수 지정일(마감일)</label>
            <input type="date" class="input" />
          </div>

          <div class="actions">
            <button class="btn-primary">구매 주문 제출</button>
          </div>
        </section>

        <section class="card">
          <div class="card-head">
            <h3 class="card-title">품목 세부 정보</h3>
            <button class="btn" @click="openModal">+ 품목 추가</button>
          </div>

          <table class="order-table">
            <thead>
              <tr>
                <th>SKU</th>
                <th>이름</th>
                <th>단가</th>
                <th>주문 수량</th>
                <th>금액</th>
                <th>작업</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, idx) in rows" :key="row.sku + idx">
                <td>{{ row.sku }}</td>
                <td>{{ row.name }}</td>
                <td class="numeric"><Money :value="row.price" /></td>
                <td><input type="number" v-model.number="row.qty" class="qty" min="1" /></td>
                <td class="numeric"><Money :value="row.price * row.qty" /></td>
                <td><button class="btn-delete" @click="removeRow(idx)">삭제</button></td>
              </tr>
              <tr v-if="rows.length === 0">
                <td colspan="6" class="empty">
                  품목이 없습니다. '품목 추가' 버튼을 눌러 추가하세요.
                </td>
              </tr>
            </tbody>
          </table>
        </section>
      </div>

      <aside class="right-col">
        <div class="summary card">
          <h4>발주 금액 요약</h4>
          <div class="summary-row">
            <span>소계:</span><span class="numeric"><Money :value="subtotal" /></span>
          </div>
          <div class="summary-row">
            <span>배송:</span><span class="numeric"><Money :value="shipping" /></span>
          </div>
          <hr />
          <div class="summary-row total">
            <span>총액:</span><span class="numeric"><Money :value="total" /></span>
          </div>
        </div>
      </aside>
    </div>

    <OrderItemModal v-if="showModal" @close="showModal = false" @add="onAddItems" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import OrderItemModal from '../../../components/OrderItemModal.vue'
import Money from '@/components/global/Money.vue'

const showModal = ref(false)
const rows = ref([])

function openModal() {
  showModal.value = true
}

function onAddItems(items) {
  // convert incoming items to order rows
  items.forEach((it) => {
    rows.value.push({ sku: it.sku, name: it.name, price: it.price, qty: 1 })
  })
}

function removeRow(idx) {
  rows.value.splice(idx, 1)
}

function calcAmount(row) {
  // price is string like '15.00 USD' — extract numeric part
  const n = parseFloat(String(row.price).replace(/[^\\d.]/g, '') || '0')
  return (n * (row.qty || 0)).toFixed(2)
}

const subtotal = computed(() => {
  return rows.value
    .reduce((s, r) => s + parseFloat(String(r.price).replace(/[^\d.]/g, '') || 0) * (r.qty || 0), 0)
    .toFixed(2)
})
const shipping = 50000
const total = computed(() => (parseFloat(subtotal.value) + shipping).toFixed(2))
</script>

<style scoped>
.page-inner {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}
.left-col {
  flex: 1;
}
.right-col {
  width: 320px;
}
.title {
  margin: 8px 0 16px;
}
.card {
  background: #fff;
  border: 1px solid #eef2f7;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}
.card-title {
  font-size: 16px;
  margin-bottom: 12px;
}
.form-row {
  margin-bottom: 12px;
  display: flex;
  flex-direction: column;
}
.select,
.input {
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
}
.actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
.btn-primary {
  background: #6b46ff;
  color: #fff;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
}
.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.btn {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
  background: #fff;
}
.order-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}
.order-table th,
.order-table td {
  padding: 12px;
  border-top: 1px solid #f7f7f9;
}
.numeric {
  text-align: right;
}
.qty {
  width: 80px;
}
.btn-delete {
  background: transparent;
  border: none;
  color: #ef4444;
  cursor: pointer;
}
.empty {
  text-align: center;
  color: #9ca3af;
  padding: 18px;
}
.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
}
.total {
  font-weight: 700;
  color: #4f46e5;
}
</style>
