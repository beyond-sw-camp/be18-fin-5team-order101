<template>
  <div class="product-detail">
    <header class="page-header">
      <h1>상품 상세 정보</h1>
      <div class="actions">
        <button class="btn-edit">편집</button>
        <button class="btn-delete">삭제</button>
      </div>
    </header>

    <div class="tabs">
      <button :class="['tab', activeTab === 'basic' ? 'active' : '']" @click="activeTab = 'basic'">
        기본 정보
      </button>
      <button :class="['tab', activeTab === 'stock' ? 'active' : '']" @click="activeTab = 'stock'">
        재고 현황
      </button>
    </div>

    <section v-if="activeTab === 'basic'" class="card basic-card">
      <div class="content-grid">
        <div>
          <div class="field">
            <label>제품명</label>
            <div class="value name">{{ product.name }}</div>
            <span class="chip" v-if="product.active">활성화</span>
          </div>

          <div class="field">
            <label>상품 코드</label>
            <div class="value">{{ product.code }}</div>
          </div>

          <div class="row">
            <div class="field small">
              <label>대분류</label>
              <div class="value">{{ product.large }}</div>
            </div>
            <div class="field small">
              <label>중분류</label>
              <div class="value">{{ product.medium }}</div>
            </div>
            <div class="field small">
              <label>소분류</label>
              <div class="value">{{ product.small }}</div>
            </div>
          </div>

          <div class="field">
            <label>공급가</label>
            <div class="value"><Money :value="product.price" /></div>
          </div>

          <div class="field">
            <label>제품 설명</label>
            <div class="value description">{{ product.description }}</div>
          </div>
        </div>

        <div class="image-col">
          <img src="https://jwiki.kr/wiki/images/d/d1/노진구.png" alt="product" />
        </div>
      </div>
    </section>

    <section v-if="activeTab === 'stock'" class="card stock-card">
      <div class="stock-header">
        <div class="stock-title">
          <div class="code">{{ product.code }}</div>
          <div class="name-lg">{{ product.name }}</div>
        </div>
        <div class="stock-cards">
          <div class="stat">
            현재 재고
            <div class="stat-value">4,750개</div>
          </div>
          <div class="stat">
            입고 예정 재고
            <div class="stat-value">90개</div>
          </div>
          <div class="stat">
            안전 재고
            <div class="stat-value">50개</div>
          </div>
        </div>
      </div>

      <div class="table-wrap">
        <table class="stock-table">
          <thead>
            <tr>
              <th>입출고 번호</th>
              <th>타입</th>
              <th class="numeric">수량</th>
              <th>입고일</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in movements" :key="r.id">
              <td>{{ r.id }}</td>
              <td>{{ r.type }}</td>
              <td class="numeric">{{ r.qty }}</td>
              <td>{{ r.date }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import Money from '@/components/global/Money.vue'

const route = useRoute()
const code = route.params.code || 'UNKNOWN'

const activeTab = ref('basic')

// sample product data, in real app fetch by code
const product = ref({
  code,
  name: 'LG 트롬 세탁기 F21VDW',
  large: '생활가전',
  medium: '세탁기',
  small: '드럼세탁기',
  price: 1590000,
  description:
    '6모션 인버터 DD모터, 스팀살균 기능을 갖춘 대용량 드럼세탁기. 미니워시와 트윈세탁 가능.',
  active: true,
  image: '/assets/sample-washer.png',
})

const movements = ref([
  { id: 'INB-20230110-010', type: '입고', qty: 90, date: '2023-01-14' },
  { id: 'INB-20230109-009', type: '출고', qty: 60, date: '2023-01-13' },
  { id: 'INB-20230108-008', type: '입고', qty: 200, date: '2023-01-12' },
  { id: 'INB-20230106-006', type: '입고', qty: 70, date: '2023-01-10' },
  { id: 'INB-20230106-006', type: '출고', qty: 120, date: '2023-01-10' },
])
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.page-header h1 {
  margin: 0;
}
.page-header .actions {
  display: flex;
  gap: 8px;
}
.btn-edit {
  background: #fff;
  border: 1px solid #e5e7eb;
  padding: 8px 10px;
  border-radius: 8px;
}
.btn-delete {
  background: #ef4444;
  color: #fff;
  border: none;
  padding: 8px 10px;
  border-radius: 8px;
}
.tabs {
  display: inline-flex;
  background: #f3f4f6;
  border-radius: 12px;
  padding: 4px;
  gap: 4px;
  margin-bottom: 18px;
  align-items: center;
}
.tab {
  background: transparent;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  color: #374151;
  font-weight: 500;
  transition: all 0.12s ease-in-out;
}
.tab:hover {
  color: #111827;
}
.tab:focus {
  outline: none;
}
.tab:focus-visible {
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.12);
  border-radius: 8px;
}
.tab.active {
  background: #ffffff;
  color: #111827;
  box-shadow: 0 1px 0 rgba(16, 24, 40, 0.04);
  border: 1px solid #e6e6ea;
}
.card {
  background: #fff;
  border: 1px solid #eef2f7;
  padding: 20px;
  border-radius: 12px;
}
.content-grid {
  display: flex;
  gap: 24px;
}
.image-col img {
  max-width: 280px;
  border-radius: 8px;
}
.field {
  margin-bottom: 12px;
}
.field label {
  display: block;
  color: #6b7280;
  font-size: 13px;
  margin-bottom: 6px;
}
.value {
  background: #f3f4f6;
  padding: 10px;
  border-radius: 8px;
}
.value.name {
  font-weight: 600;
  font-size: 18px;
}
.chip {
  display: inline-block;
  background: #6b46ff;
  color: #fff;
  padding: 6px 10px;
  border-radius: 12px;
  margin-left: 10px;
}
.row {
  display: flex;
  gap: 12px;
}
.field.small {
  flex: 1;
}
.description {
  min-height: 80px;
}
.stock-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.stock-cards {
  display: flex;
  gap: 12px;
}
.stat {
  background: #fff;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #eef2f7;
}
.stat-value {
  font-weight: 700;
  font-size: 20px;
  margin-top: 6px;
}
.table-wrap {
  margin-top: 12px;
}
.stock-table {
  width: 100%;
  border-collapse: collapse;
}
.stock-table th,
.stock-table td {
  padding: 14px 12px;
  border-bottom: 1px solid #f3f4f6;
  text-align: left;
}
.stock-table td.numeric {
  text-align: right;
}

@media (max-width: 1024px) {
  .content-grid {
    flex-direction: column;
  }
  .image-col img {
    max-width: 100%;
  }
}
</style>
