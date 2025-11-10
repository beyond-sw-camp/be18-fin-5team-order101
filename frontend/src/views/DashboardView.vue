<script setup>
const orderSummary = [
  { label: '총 주문', value: '150건', description: '지난 24시간 대비 +12%' },
  { label: '주문 처리', value: '20건', description: '예정 4건' },
]

const settlementSummary = [
  { label: '오늘 매출', value: '₩5,000,000' },
  { label: '이번 달 매출', value: '₩120,000,000' },
]

const quickLinks = [
  { title: '새 발주 생성', description: '3 단계로 빠르게 생성', highlight: true },
  { title: '창고 조회', description: '실시간 재고 수량 확인' },
  { title: '주문 조회', description: '가맹점 주문 상태 추적' },
  { title: '수요 예측', description: 'AI 기반 수요 계획' },
]
</script>

<template>
  <div class="dashboard">
    <header>
      <p class="eyebrow">대시보드</p>
      <h1>운영 현황</h1>
      <p class="subtitle">오늘 주문과 정산 흐름을 한눈에 확인하세요.</p>
    </header>

    <section class="card-grid">
      <article class="card">
        <h2>주문 현황</h2>
        <div class="stats">
          <div v-for="item in orderSummary" :key="item.label">
            <p class="label">{{ item.label }}</p>
            <p class="value">{{ item.value }}</p>
            <p class="muted">{{ item.description }}</p>
          </div>
        </div>
      </article>
      <article class="card">
        <h2>일일 정산</h2>
        <div class="stats">
          <div v-for="item in settlementSummary" :key="item.label">
            <p class="label">{{ item.label }}</p>
            <p class="value">{{ item.value }}</p>
          </div>
        </div>
      </article>
      <article class="card">
        <h2>재고 상태</h2>
        <div class="stats compact">
          <div>
            <p class="label">부족 품목</p>
            <p class="value warning">5개</p>
          </div>
          <div>
            <p class="label">총 품목</p>
            <p class="value">1,000개</p>
          </div>
        </div>
      </article>
    </section>

    <section class="quick-actions">
      <h2>빠른 작업</h2>
      <div class="action-grid">
        <button
          v-for="link in quickLinks"
          :key="link.title"
          type="button"
          class="action-card"
          :class="{ highlight: link.highlight }"
        >
          <span class="action-title">{{ link.title }}</span>
          <span class="action-desc">{{ link.description }}</span>
        </button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

header .eyebrow {
  font-size: 14px;
  color: #6366f1;
  font-weight: 600;
  text-transform: uppercase;
}

header h1 {
  font-size: 28px;
  font-weight: 700;
  margin-top: 4px;
}

.subtitle {
  color: #6b7280;
  margin-top: 4px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.card {
  border: 1px solid #edf0f4;
  border-radius: 16px;
  padding: 20px;
  background-color: #fafbff;
}

.card h2 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}

.stats {
  display: flex;
  gap: 12px;
}

.stats div {
  flex: 1;
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #eef1f6;
}

.stats.compact {
  flex-direction: column;
}

.label {
  font-size: 13px;
  color: #6b7280;
}

.value {
  font-size: 20px;
  font-weight: 700;
  margin-top: 6px;
}

.muted {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

.warning {
  color: #f97316;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.action-card {
  border-radius: 16px;
  border: 1px solid #eef1f6;
  padding: 20px;
  text-align: left;
  background-color: #fff;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.action-card.highlight {
  background-color: #ecfdf5;
  border-color: #a7f3d0;
}

.action-title {
  font-weight: 600;
}

.action-desc {
  color: #6b7280;
  font-size: 13px;
}
</style>
