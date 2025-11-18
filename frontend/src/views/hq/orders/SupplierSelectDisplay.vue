<script setup>
const props = defineProps({
  supplier: {
    type: Object,
    default: () => ({ id: null, name: '공급업체 선택' })
  }
});
const emit = defineEmits(['open-modal']);

const openModal = () => {
  emit('open-modal');
};
</script>

<template>
  <div class="supplier-display-container" @click="openModal">
    <span :class="{ 'selected-name': props.supplier.supplierId, 'placeholder': !props.supplier.supplierId }">
      <!-- supplierId가 있으면 이름과 ID를 표시하고, 없으면 "공급업체 선택"을 표시합니다. -->
      <template v-if="props.supplier.supplierId">
        {{ props.supplier.supplierName }} ({{ props.supplier.supplierId }})
      </template>
      <template v-else>
        공급업체 선택
      </template>
    </span>
    <span class="dropdown-icon">▼</span>
  </div>
</template>

<style scoped>
/* (스타일은 1번 OrderCreation.vue에 맞게 작성되어 있습니다.) */
.supplier-display-container {
  padding: 8px 12px;
  border: 1px solid #c0c0c0;
  border-radius: 4px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  width: 100%;
  box-sizing: border-box;
}

.supplier-display-container:hover {
  background-color: #f7f7f7;
}

.selected-name {
  color: #333;
}

.dropdown-icon {
  font-size: 10px;
  color: #666;
  margin-left: 10px;
}
</style>