<template>
  <div class="franchise-list">
    <h2 class="page-title">가맹점 관리</h2>
    <div class="filters card">
      <select class="select">
        <option>모든 지역</option>
        <option>중앙</option>
        <option>남부</option>
        <option>북부</option>
      </select>
      <select class="select">
        <option>모든 상태</option>
        <option>활성</option>
        <option>비활성</option>
      </select>
      <input class="search" placeholder="매장 이름 또는 관리자 검색..." />
    </div>
    <div class="list-card card">
      <table class="franchise-table">
        <thead>
          <tr>
            <th>매장 ID</th>
            <th>매장 이름</th>
            <th>주소</th>
            <th>관리자</th>
            <th>연락처</th>
            <th>상태</th>
            <th>등록일</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.address }}</td>
            <td>{{ row.manager }}</td>
            <td>{{ row.phone }}</td>
            <td>
              <span :class="['badge', row.status === '활성' ? 'active' : 'inactive']">{{
                row.status
              }}</span>
            </td>
            <td>{{ row.registered }}</td>
            <td><button class="more">⋯</button></td>
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
    id: 'FS001',
    name: '중앙 지점',
    address: '중앙',
    manager: '김민준',
    phone: '010-1234-5678',
    status: '활성',
    registered: '2022-03-15',
  },
  {
    id: 'FS002',
    name: '남부 지점',
    address: '남부',
    manager: '박서준',
    phone: '010-2345-6789',
    status: '비활성',
    registered: '2021-08-01',
  },
  {
    id: 'FS003',
    name: '동부 지점',
    address: '동부',
    manager: '최지원',
    phone: '010-3456-7890',
    status: '활성',
    registered: '2023-01-10',
  },
  {
    id: 'FS004',
    name: '서부 지점',
    address: '서부',
    manager: '이지은',
    phone: '010-4567-8901',
    status: '활성',
    registered: '2023-05-20',
  },
  {
    id: 'FS005',
    name: '북부 지점',
    address: '북부',
    manager: '김유진',
    phone: '010-5678-9012',
    status: '활성',
    registered: '2022-11-05',
  },
  {
    id: 'FS006',
    name: '제주 지점',
    address: '남부',
    manager: '오세훈',
    phone: '010-6789-0123',
    status: '비활성',
    registered: '2020-09-01',
  },
])
</script>

<style scoped>
.page-title {
  font-size: 20px;
  margin-bottom: 12px;
}
.filters {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 14px;
  margin-bottom: 18px;
}
.select,
.search {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}
.card {
  background: #fff;
  border: 1px solid #eef2f7;
  padding: 16px;
  border-radius: 8px;
}
.list-card {
  padding: 20px;
}
.hero-image {
  width: 100%;
  border-radius: 6px;
  margin-bottom: 16px;
  border: 1px solid #f3f4f6;
}
.franchise-table {
  width: 100%;
  border-collapse: collapse;
}
.franchise-table thead th {
  background: #fff;
  text-align: left;
  padding: 14px;
  border-bottom: 1px solid #eef2f7;
}
.franchise-table tbody td {
  padding: 14px;
  border-top: 1px solid #f7f7f9;
}
.badge {
  padding: 6px 10px;
  border-radius: 999px;
  color: #fff;
  font-weight: 600;
}
.badge.active {
  align-items: center;
  background: #10b981;
}
.badge.inactive {
  align-items: center;
  background: #ef4444;
}
.more {
  background: transparent;
  border: none;
  cursor: pointer;
}
</style>
