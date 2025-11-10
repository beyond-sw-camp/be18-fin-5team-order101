<script setup>
import { reactive } from 'vue'

const form = reactive({
  memberType: 'franchise',
  loginId: '',
  password: '',
  passwordConfirm: '',
  franchiseId: '',
})

const memberTypes = [
  { label: '본사 직원', value: 'hq' },
  { label: '가맹점 점주', value: 'franchise' },
]

const handleSubmit = () => {
  // TODO: replace with API integration
  console.table(form)
}

const handleReset = () => {
  form.memberType = 'hq'
  form.loginId = ''
  form.password = ''
  form.passwordConfirm = ''
  form.franchiseId = ''
}
</script>

<template>
  <div class="user-registration">
    <header>
      <p class="eyebrow">사용자 관리</p>
      <h1>사용자 등록</h1>
      <p class="subtitle">새로운 관리자 패널 사용자를 위한 계정을 만드세요.</p>
    </header>

    <section class="form-wrapper">
      <form class="form-card" @submit.prevent="handleSubmit">
        <fieldset class="field">
          <legend>회원 유형 <span class="required">*</span></legend>
          <div class="radio-group">
            <label v-for="member in memberTypes" :key="member.value">
              <input
                v-model="form.memberType"
                type="radio"
                name="memberType"
                :value="member.value"
              />
              <span>{{ member.label }}</span>
            </label>
          </div>
        </fieldset>

        <div class="field">
          <label for="loginId">로그인 ID</label>
          <div class="input-wrapper">
            <span class="leading-icon">✉️</span>
            <input
              id="loginId"
              v-model="form.loginId"
              type="text"
              placeholder="사용할 로그인 ID를 입력하세요"
            />
          </div>
        </div>

        <div class="field">
          <label for="password">비밀번호</label>
          <div class="input-wrapper">
            <span class="leading-icon">🔒</span>
            <input
              id="password"
              v-model="form.password"
              type="password"
              placeholder="비밀번호를 입력하세요"
            />
          </div>
        </div>

        <div class="field">
          <label for="passwordConfirm">비밀번호 확인</label>
          <div class="input-wrapper">
            <span class="leading-icon">🔒</span>
            <input
              id="passwordConfirm"
              v-model="form.passwordConfirm"
              type="password"
              placeholder="비밀번호를 다시 입력하세요"
            />
          </div>
        </div>

        <div class="field">
          <label for="franchiseId">프랜차이즈 ID</label>
          <div class="input-wrapper">
            <span class="leading-icon">🏬</span>
            <input
              id="franchiseId"
              v-model="form.franchiseId"
              type="text"
              placeholder="프랜차이즈 고유 ID를 입력하세요"
            />
          </div>
        </div>

        <div class="form-actions">
          <button type="button" class="ghost" @click="handleReset">취소</button>
          <button type="submit" class="primary">저장</button>
        </div>
      </form>
    </section>
  </div>
</template>

<style scoped>
.user-registration {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.eyebrow {
  font-size: 14px;
  color: #6b7280;
  font-weight: 600;
}

h1 {
  font-size: 26px;
  font-weight: 700;
}

.subtitle {
  color: #6b7280;
}

.form-wrapper {
  /* center the card horizontally and give room on the left for the sidebar */
  display: flex;
  justify-content: center;
  padding: 8px 0 40px 0;
}

.form-card {
  background-color: #fff;
  border-radius: 20px;
  padding: 40px;
  border: 1px solid #f1f3f6;
  display: flex;
  flex-direction: column;
  gap: 20px;
  box-shadow: 0 8px 30px rgba(15, 23, 42, 0.04);
  width: 680px; /* fixed card width like the design */
  max-width: calc(100% - 48px);
}

.field {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.field label,
.field legend {
  font-weight: 600;
  color: #111827;
}

.required {
  color: #f87171;
}

.radio-group {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.radio-group label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
}

.input-wrapper {
  display: flex;
  align-items: center;
  border: 1px solid #eef0f3;
  border-radius: 12px;
  padding: 12px 16px;
  background-color: #fff;
  gap: 8px;
}

.leading-icon {
  font-size: 16px;
  opacity: 0.7;
}

input[type='text'],
input[type='password'] {
  border: none;
  flex: 1;
  font-size: 15px;
  background: transparent;
  outline: none;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
}

.form-actions button {
  min-width: 96px;
  padding: 10px 16px;
  border-radius: 12px;
  font-weight: 600;
  border: 1px solid transparent;
  cursor: pointer;
}

.form-actions .ghost {
  border-color: #e5e7eb;
  background-color: #fff;
}

.form-actions .primary {
  background-color: #6b63f6;
  color: #fff;
  border-color: #6b63f6;
}

@media (max-width: 1024px) {
  .form-card {
    width: 100%;
    padding: 28px;
  }

  .form-actions {
    flex-direction: column-reverse;
    align-items: stretch;
  }
}
</style>
