<template>
  <div class="franchise-registration">
    <h2 class="page-title">신규 매장 등록</h2>

    <div class="form-grid">
      <section class="card">
        <h3 class="card-title">기본 정보</h3>
        <div class="form-row">
          <label>매장 이름</label>
          <input v-model="form.name" placeholder="매장 이름을 입력하세요" class="input" />
        </div>

        <div class="form-row">
          <label>사업자등록번호</label>
          <input
            v-model="form.businessNo"
            placeholder="사업자등록번호를 입력하세요"
            class="input"
          />
        </div>

        <div class="form-row">
          <label>주소</label>
          <input v-model="form.address" placeholder="주소를 검색하세요" class="input" />
        </div>

        <div class="form-row">
          <label>가맹점 ID</label>
          <input v-model="form.franchiseId" placeholder="가맹점 ID" class="input" />
        </div>
      </section>

      <section class="card">
        <h3 class="card-title">운영 설정</h3>

        <div class="form-row">
          <label>운영 시간</label>
          <input v-model="form.hours" placeholder="예: 09:00 - 22:00" class="input" />
        </div>

        <div class="form-row">
          <label>할인 정책</label>
          <textarea
            v-model="form.discountPolicy"
            placeholder="매장의 할인 정책을 입력하세요."
            class="textarea"
          ></textarea>
        </div>

        <div class="form-row checkbox-row">
          <label><input type="checkbox" v-model="form.aiAutoOrder" /> AI 자동 주문 활성화</label>
        </div>
      </section>
    </div>

    <div class="actions">
      <button class="btn-cancel" @click="onCancel">취소</button>
      <button class="btn-save" @click="onSave">저장</button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const form = reactive({
  name: '',
  businessNo: '',
  address: '',
  franchiseId: '',
  hours: '',
  discountPolicy: '',
  aiAutoOrder: false,
})

function onSave() {
  // basic client-side validation
  if (!form.name || !form.franchiseId) {
    alert('매장 이름과 가맹점 ID를 입력해주세요.')
    return
  }
  // TODO: send to API
  console.log('Saving franchise:', { ...form })
  alert('저장되었습니다 (샘플 동작).')
}

function onCancel() {
  // Reset form or navigate away
  form.name = ''
  form.businessNo = ''
  form.address = ''
  form.franchiseId = ''
  form.hours = ''
  form.discountPolicy = ''
  form.aiAutoOrder = false
}
</script>

<style scoped>
.page-title {
  font-size: 22px;
  margin-bottom: 18px;
}
.form-grid {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}
.card {
  flex: 1;
  background: #fff;
  border: 1px solid #eef2f7;
  padding: 20px;
  border-radius: 8px;
}
.card-title {
  font-size: 16px;
  margin-bottom: 12px;
}
.form-row {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 12px;
}
.input,
.textarea {
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: #fff;
}
.textarea {
  min-height: 120px;
  resize: vertical;
}
.checkbox-row {
  align-items: center;
}
.actions {
  margin-top: 18px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.btn-save {
  background: #6b46ff;
  color: #fff;
  border: none;
  padding: 10px 14px;
  border-radius: 8px;
}
.btn-cancel {
  background: #fff;
  border: 1px solid #e5e7eb;
  padding: 10px 14px;
  border-radius: 8px;
}

@media (max-width: 1024px) {
  .form-grid {
    flex-direction: column;
  }
}
</style>
