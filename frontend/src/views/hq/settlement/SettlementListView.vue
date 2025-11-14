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
import { ref, onMounted } from 'vue'
import axios from 'axios'

const searchConditions = ref({
  types: ['AR', 'AP'],
  statuses: ['DRAFT', 'ISSUED', 'VOID'],
  period: '2025-06',
  searchText: '',
})

const rows = ref([])

const fetchSettlements = async () => {
  try {

    const url = '/api/v1/settlements';
    const params = {
      ...searchConditions.value,
      page: 0,
      size: 10,
      sort: 'createdAt,desc',
    };

    const response = await axios.get(url, { params });

    const transformedData = response.data.content.map(item => ({
            id: item.settlementNo, // 정산번호를 ID로 사용
            type: item.settlementType === 'AP' ? 'AP' : 'AR', // 유형
            entity: item.supplierName === null ? item.storeName : item.supplierName, // 상점/공급사 이름
            period: '2023-01', // 기간 (YYYY-MM 형식 가정)
            qty: item.settlementQty, // 총 수량
            total: item.settlementAmount, // 총 금액
            net: item.settlementAmount, // 순 금액
            status: mapStatus(item.settlementStatus), // 상태 매핑 함수 사용
            created: item.createdAt,
    }));

    rows.value = transformedData
  } catch (error) {
    console.error('Error fetching settlements:', error)
  }
};
const mapStatus = (backendStatus) => {
    switch (backendStatus) {
        case 'ISSUED': return '발행됨';
        case 'DRAFT': return '초안';
        case 'VOID': return '무효';
        default: return '잠김'; // 백엔드에서 '잠김' 상태에 해당하는 ENUM이 필요
    }
};
onMounted(() => {
    fetchSettlements();
});
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
