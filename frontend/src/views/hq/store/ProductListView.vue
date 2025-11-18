<template>
  <div class="page-shell">
    <header class="page-header">
      <h1>상품 목록</h1>
      <div class="header-actions">
        <button class="btn-primary">상품 등록</button>
      </div>
    </header>

    <section class="card">
      <div class="filters">
        <select v-model="filters.large">
          <option value="">대분류</option>
          <option>전자제품</option>
          <option>식품</option>
        </select>
        <select v-model="filters.medium">
          <option value="">중분류</option>
          <option>가전</option>
          <option>주방</option>
        </select>
        <select v-model="filters.small">
          <option value="">소분류</option>
          <option>스마트폰</option>
          <option>노트북</option>
        </select>
      </div>

      <h3 class="card-title">모든 상품</h3>

      <div class="table-wrap">
        <table class="product-table">
          <thead>
            <tr>
              <th>상품 코드</th>
              <th>카테고리</th>
              <th>제품명</th>
              <th class="numeric">공급가</th>
              <th>상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in products" :key="p.code" class="clickable-row" @click="openDetail(p)">
              <td class="code">{{ p.code }}</td>
              <td>{{ p.category }}</td>
              <td>{{ p.name }}</td>
              <td class="numeric"><Money :value="p.price" /></td>
              <td>
                <span :class="['chip', p.active ? 's-active' : 's-inactive']">{{
                  p.active ? '활성' : '비활성'
                }}</span>
              </td>
            </tr>
            <tr v-if="products.length === 0">
              <td colspan="5" class="no-data">등록된 상품이 없습니다.</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination">
        <button class="pager">‹ Previous</button>
        <button class="page active">1</button>
        <button class="page">2</button>
        <button class="page">3</button>
        <button class="pager">Next ›</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Money from '@/components/global/Money.vue'

const router = useRouter()
const filters = ref({ large: '', medium: '', small: '' })

const products = ref([
  { code: 'ELEC001', category: '전자제품', name: '스마트폰', price: 20000, active: true },
  { code: 'CLTH005', category: '전자제품', name: '마우스', price: 858000, active: true },
  { code: 'FOOD010', category: '전자제품', name: '키보드', price: 196000, active: false },
  { code: 'OFFC003', category: '전자제품', name: '아이패드', price: 352000, active: true },
  { code: 'BOOK002', category: '전자제품', name: '노트북', price: 980000, active: false },
])

function openDetail(p) {
  router.push({ name: 'hq-product-detail', params: { code: p.code } })
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
.header-actions .btn-primary {
  background: #6b46ff;
  color: #fff;
  border: none;
  padding: 8px 14px;
  border-radius: 8px;
  cursor: pointer;
}
.card {
  background: #fff;
  border: 1px solid #f0f0f3;
  border-radius: 12px;
  padding: 16px;
}
.filters {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}
.filters select {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e6e6ea;
}
.card-title {
  font-size: 16px;
  margin-bottom: 12px;
}
.product-table {
  width: 100%;
  border-collapse: collapse;
}
.product-table th,
.product-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #f0f0f3;
  text-align: left;
}
.product-table td.numeric {
  text-align: right;
}
.code {
  font-weight: 600;
}
.chip {
  padding: 6px 10px;
  border-radius: 12px;
  color: #fff;
  font-size: 13px;
}
.s-active {
  background: #6b46ff;
}
.s-inactive {
  background: #e6e6ea;
  color: #333;
}
.pagination {
  margin-top: 18px;
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: flex-end;
}
.pager {
  background: transparent;
  border: none;
  color: #666;
  cursor: pointer;
}
.page {
  padding: 6px 10px;
  border-radius: 8px;
  border: 1px solid #eee;
  background: #fff;
}
.page.active {
  background: #6b46ff;
  color: #fff;
  border-color: #6b46ff;
}
.no-data {
  text-align: center;
  color: #999;
  padding: 20px;
}
.clickable-row {
  cursor: pointer;
}
</style>
