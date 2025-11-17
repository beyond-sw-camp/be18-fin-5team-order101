<template>
  <div class="product-registration">
    <h2 class="page-title">신규 상품 등록</h2>

    <div class="form-grid">
      <section class="card">
        <h3 class="card-title">기본 제품 정보</h3>

        <div class="form-row">
          <label>제품명</label>
          <input v-model="form.name" placeholder="제품명을 입력하세요" class="input" />
        </div>

        <div class="form-row">
          <label>상품 코드</label>
          <input v-model="form.code" placeholder="상품 코드를 입력하세요" class="input" />
        </div>

        <div class="form-row">
          <label>대분류</label>
          <select v-model="form.large" class="input">
            <option value="">선택</option>
            <option>생활가전</option>
            <option>전자제품</option>
            <option>식품</option>
          </select>
        </div>

        <div class="form-row">
          <label>중분류</label>
          <select v-model="form.medium" class="input">
            <option value="">선택</option>
            <option>세탁기</option>
            <option>냉장고</option>
          </select>
        </div>

        <div class="form-row">
          <label>소분류</label>
          <select v-model="form.small" class="input">
            <option value="">선택</option>
            <option>드럼세탁기</option>
            <option>통돌이</option>
          </select>
        </div>

        <div class="form-row">
          <label>단위</label>
          <select v-model="form.unit" class="input">
            <option value="개">개</option>
            <option value="박스">박스</option>
          </select>
        </div>

        <div class="form-row">
          <label>공급사</label>
          <select v-model="form.supplier" class="input">
            <option value="">공급사 선택</option>
            <option value="supplier-a">공급사 A</option>
            <option value="supplier-b">공급사 B</option>
          </select>
        </div>

        <div class="form-row">
          <label>공급가</label>
          <input v-model.number="form.price" placeholder="₩ 0" class="input" />
        </div>

        <div class="form-row toggle-row">
          <label>활성 상태</label>
          <input type="checkbox" v-model="form.active" />
        </div>

        <div class="form-row">
          <label>제품 설명</label>
          <textarea
            v-model="form.description"
            class="textarea"
            placeholder="제품 설명을 입력하세요."
          ></textarea>
        </div>
      </section>

      <section class="card image-card">
        <h3 class="card-title">제품 이미지</h3>

        <div class="image-drop">
          <div class="image-placeholder">
            <svg
              width="48"
              height="48"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 16V8"
                stroke="#9CA3AF"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M8 12L12 8L16 12"
                stroke="#9CA3AF"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <rect x="3" y="3" width="18" height="18" rx="2" stroke="#E5E7EB" stroke-width="1.5" />
            </svg>
            <div class="hint">이미지 파일을 여기에 끌어다 놓거나 클릭하여 업로드하세요.</div>
          </div>
          <input type="file" accept="image/*" @change="onFileChange" class="file-input" />
        </div>

        <div class="image-actions">
          <button class="btn-upload" @click="triggerFile">이미지 업로드</button>
          <div v-if="preview" class="preview-box">
            <img :src="preview" alt="preview" />
          </div>
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
import { reactive, ref } from 'vue'

const form = reactive({
  name: '',
  code: '',
  large: '',
  medium: '',
  small: '',
  unit: '개',
  supplier: '',
  price: 0,
  active: true,
  description: '',
})

const fileInput = ref(null)
const preview = ref(null)

function triggerFile() {
  // click the hidden file input
  const el = document.querySelector('.file-input')
  if (el) el.click()
}

function onFileChange(e) {
  const f = e.target.files && e.target.files[0]
  if (!f) return
  const reader = new FileReader()
  reader.onload = (ev) => {
    preview.value = ev.target.result
  }
  reader.readAsDataURL(f)
}

function onSave() {
  if (!form.name || !form.code) {
    alert('제품명과 상품 코드는 필수입니다.')
    return
  }
  // TODO: call API to save product
  console.log('saving product', { ...form })
  alert('저장되었습니다 (샘플).')
}

function onCancel() {
  // reset form
  form.name = ''
  form.code = ''
  form.large = ''
  form.medium = ''
  form.small = ''
  form.unit = '개'
  form.supplier = ''
  form.price = 0
  form.active = true
  form.description = ''
  preview.value = null
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
.image-card {
  width: 360px;
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
.textarea,
select {
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: #fff;
}
.textarea {
  min-height: 120px;
  resize: vertical;
}
.toggle-row {
  display: flex;
  align-items: center;
  gap: 12px;
}
.image-drop {
  border: 1px dashed #e6e6ea;
  padding: 18px;
  border-radius: 8px;
  position: relative;
}
.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 24px 8px;
  color: #9ca3af;
}
.file-input {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}
.image-actions {
  margin-top: 12px;
  display: flex;
  gap: 12px;
  align-items: center;
}
.btn-upload {
  background: #fff;
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
}
.preview-box img {
  max-width: 120px;
  border-radius: 6px;
  border: 1px solid #eee;
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
  .image-card {
    width: 100%;
  }
}
</style>
