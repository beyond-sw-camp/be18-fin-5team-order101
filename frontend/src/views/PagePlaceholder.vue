<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>
  <div class="placeholder">
    <h1>{{ route.meta.title || '준비 중인 화면' }}</h1>
    <p v-if="route.meta.description">{{ route.meta.description }}</p>
    <p v-else>
      자 여러분들 화이팅~ 라우터만 먼저 연결해두었습니다. 라우터 이름 자유롭게 바꾸시면 됩니다.
      레츠고 오더101
    </p>
  </div>
</template>

<style scoped>
.placeholder {
  min-height: 360px;
  border: 2px dashed #e0e7ff;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  padding: 32px;
  gap: 8px;
  color: #1f2933;
  background-color: #f8faff;
}

h1 {
  font-size: 24px;
  font-weight: 700;
}

p {
  color: #6b7280;
}
</style>
