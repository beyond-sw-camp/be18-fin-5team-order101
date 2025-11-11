<template>
  <div class="supplier-list">
    <h2 class="page-title">공급사 조회</h2>

    <div class="controls card">
      <select class="select">
        <option>모든 지역</option>
        <option>중앙</option>
        <option>남부</option>
      </select>
      <input class="search" placeholder="매장 이름 또는 관리자 검색..." />
    </div>

    <div class="list-card card">
      <h3 class="card-title">공급사</h3>
      <table class="supplier-table">
        <thead>
          <tr>
            <th>공급사 ID</th>
            <th>공급사 이름</th>
            <th>주소</th>
            <th>담당자</th>
            <th>연락처</th>
            <th>등록일</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in suppliers" :key="s.id" @click="goDetail(s.id)" class="clickable">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>{{ s.address }}</td>
            <td>{{ s.contact }}</td>
            <td>{{ s.phone }}</td>
            <td>{{ s.registered }}</td>
            <td><button class="more">⋯</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const suppliers = ref([
  {
    id: 'FS001',
    name: '중앙 지점',
    address: '중앙',
    contact: '김민준',
    phone: '010-1234-5678',
    registered: '2022-03-15',
  },
  {
    id: 'FS002',
    name: '남부 지점',
    address: '남부',
    contact: '박서준',
    phone: '010-2345-6789',
    registered: '2021-08-01',
  },
  {
    id: 'FS003',
    name: '동부 지점',
    address: '동부',
    contact: '최지원',
    phone: '010-3456-7890',
    registered: '2023-01-10',
  },
  {
    id: 'FS004',
    name: '서부 지점',
    address: '서부',
    contact: '이지은',
    phone: '010-4567-8901',
    registered: '2023-05-20',
  },
  {
    id: 'FS005',
    name: '북부 지점',
    address: '북부',
    contact: '김유진',
    phone: '010-5678-9012',
    registered: '2022-11-05',
  },
  {
    id: 'FS006',
    name: '제주 지점',
    address: '남부',
    contact: '오세훈',
    phone: '010-6789-0123',
    registered: '2020-09-01',
  },
])

function goDetail(id) {
  router.push({ name: 'hq-supplier-detail', params: { id } })
}
</script>

<style scoped>
.page-title {
  font-size: 20px;
  margin-bottom: 12px;
}
.controls {
  display: flex;
  gap: 12px;
  padding: 14px;
  margin-bottom: 18px;
}
.card {
  background: #fff;
  border: 1px solid #eef2f7;
  border-radius: 8px;
}
.list-card {
  padding: 20px;
}
.supplier-table {
  width: 100%;
  border-collapse: collapse;
}
.supplier-table thead th {
  text-align: left;
  padding: 14px;
  border-bottom: 1px solid #eef2f7;
  background: #fff;
}
.supplier-table tbody td {
  padding: 14px;
  border-top: 1px solid #f7f7f9;
}
.clickable {
  cursor: pointer;
}
.clickable:hover {
  background: #f9fafb;
}
.more {
  background: transparent;
  border: none;
  cursor: pointer;
}
</style>
