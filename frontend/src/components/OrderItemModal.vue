<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal">
      <header class="modal-header">
        <h3>품목 추가</h3>
      </header>

      <section class="modal-body">
        <div class="filters">
          <select v-model="major" class="select">
            <option value="all">전체 분류</option>
            <option value="electronics">전자제품</option>
            <option value="home">생활가전</option>
          </select>
          <select v-model="minor" class="select">
            <option value="all">전체</option>
            <option value="A">소분류 A</option>
            <option value="B">소분류 B</option>
          </select>
          <input v-model="q" placeholder="제품 SKU 또는 이름 검색..." class="search" />
        </div>

        <div class="table-wrap">
          <table class="items-table">
            <thead>
              <tr>
                <th>선택</th>
                <th>SKU</th>
                <th>제품명</th>
                <th>단가</th>
                <th>재고</th>
                <th>리드 타임</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in filtered" :key="item.sku">
                <td><input type="checkbox" v-model="selectedMap[item.sku]" /></td>
                <td>{{ item.sku }}</td>
                <td>{{ item.name }}</td>
                <td class="numeric"><Money :value="item.price" /></td>
                <td class="numeric">{{ item.stock }}</td>
                <td class="numeric">{{ item.lead }} 영업일</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <footer class="modal-footer">
        <button class="btn btn-secondary" @click="close">취소</button>
        <button class="btn btn-primary" @click="addSelected">선택된 품목 추가</button>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
const emit = defineEmits(['close', 'add'])

const q = ref('')
const major = ref('all')
const minor = ref('all')

const items = ref([
  { sku: 'RP001', name: '아이폰 16 Pro', price: '1760000', stock: 120, lead: 2 },
  { sku: 'RP002', name: '에어팟 Pro', price: '500000', stock: 80, lead: 5 },
  { sku: 'RP003', name: '노트북', price: '1200000', stock: 50, lead: 1 },
  { sku: 'RP004', name: '헤드셋', price: '3000000', stock: 30, lead: 3 },
  { sku: 'RP005', name: '마우스', price: '150000', stock: 75, lead: 4 },
  { sku: 'RP006', name: '키보드', price: '180000', stock: 40, lead: 3 },
])

const selectedMap = reactive({})

const filtered = computed(() => {
  const term = q.value.trim().toLowerCase()
  return items.value.filter((it) => {
    if (term && !(it.sku.toLowerCase().includes(term) || it.name.toLowerCase().includes(term)))
      return false
    return true
  })
})

function close() {
  emit('close')
}

function addSelected() {
  const selected = items.value.filter((it) => selectedMap[it.sku])
  emit('add', selected)
  Object.keys(selectedMap).forEach((k) => (selectedMap[k] = false))
  emit('close')
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal {
  width: 900px;
  max-width: calc(100% - 40px);
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}
.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eef2f7;
}
.modal-body {
  padding: 16px;
}
.filters {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 12px;
}
.select {
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
}
.search {
  flex: 1;
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
}
.table-wrap {
  max-height: 360px;
  overflow: auto;
  border: 1px solid #eef2f7;
  border-radius: 6px;
}
.items-table {
  width: 100%;
  border-collapse: collapse;
}
.items-table th,
.items-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #f7f7f9;
}
.numeric {
  text-align: right;
}
.modal-footer {
  padding: 12px 16px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  border-top: 1px solid #eef2f7;
}
.btn {
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
}
.btn-primary {
  background: #6b46ff;
  color: #fff;
  border: none;
}
.btn-secondary {
  background: #fff;
  border: 1px solid #e5e7eb;
}
</style>
