<script setup>
import { ref } from 'vue'

const users = ref([
  {
    email: 'john.doe@example.com',
    name: '존 도우',
    role: '가맹점주',
    status: '활성',
    joined: '2023-01-15',
    expiry: 'N/A',
  },
  {
    email: 'alex.wong@example.com',
    name: '알렉스 웡',
    role: '본사 직원',
    status: '활성',
    joined: '2022-11-20',
    expiry: 'N/A',
  },
  {
    email: 'sarah.lee@example.com',
    name: '사라 리',
    role: '본사 관리자',
    status: '비활성화',
    joined: '2023-05-10',
    expiry: '2023-06-10',
  },
  {
    email: 'mike.ross@example.com',
    name: '마이크 로스',
    role: '가맹점주',
    status: '활성',
    joined: '2023-08-01',
    expiry: 'N/A',
  },
])
</script>

<template>
  <div class="users-list">
    <header class="page-header">
      <h1>사용자 관리</h1>
    </header>

    <section class="table-card">
      <div class="table-wrap">
        <table class="users-table">
          <thead>
            <tr>
              <th>이메일</th>
              <th>이름</th>
              <th>역할</th>
              <th>상태</th>
              <th>가입 날짜</th>
              <th>만료 날짜</th>
              <th>동작</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.email">
              <td class="mono">{{ user.email }}</td>
              <td>{{ user.name }}</td>
              <td>{{ user.role }}</td>
              <td>
                <span :class="['status-badge', user.status === '활성' ? 'active' : 'inactive']">{{
                  user.status
                }}</span>
              </td>
              <td>{{ user.joined }}</td>
              <td>{{ user.expiry }}</td>
              <td class="actions"><button class="more">⋯</button></td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 20px;
}
.page-header h1 {
  margin: 0;
  font-size: 22px;
}
.table-card {
  background: #fff;
  border: 1px solid #eef0f3;
  border-radius: 12px;
  padding: 16px;
}
.table-wrap {
  overflow: auto;
}
.users-table {
  width: 100%;
  border-collapse: collapse;
}
.users-table thead th {
  text-align: left;
  padding: 14px;
  background: #fbfbfd;
  color: #6b7280;
  font-weight: 600;
}
.users-table tbody td {
  padding: 18px 14px;
  border-top: 1px solid #f3f4f6;
  vertical-align: middle;
}
.users-table tbody tr:nth-child(even) td {
  background: #fbfbfd;
}
.mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, 'Roboto Mono', monospace;
}
.status-badge {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  font-weight: 700;
  font-size: 12px;
}
.status-badge.active {
  background: #eef2ff;
  color: #4f46e5;
}
.status-badge.inactive {
  background: #f3f4f6;
  color: #9ca3af;
}
.actions {
  text-align: center;
}
.more {
  background: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

@media (max-width: 800px) {
  .users-table thead {
    display: none;
  }
  .users-table tbody td {
    display: block;
    width: 100%;
  }
  .users-table tbody tr {
    margin-bottom: 12px;
    display: block;
    border: 1px solid #eef0f3;
    border-radius: 8px;
    overflow: hidden;
  }
}
</style>
