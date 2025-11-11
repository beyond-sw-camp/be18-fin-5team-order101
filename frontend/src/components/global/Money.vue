<template>
  <span class="money">{{ formatted }}</span>
</template>

<script setup>
import { computed, inject } from 'vue'
const props = defineProps({
  value: { type: [Number, String], required: true },
  hideSymbol: { type: Boolean, default: false },
})

// prefer injected formatter if plugin provided it, otherwise local fallback
const injected = inject('formatWon', null)

const localFormat = (v) => {
  if (v === null || v === undefined || v === '') return ''
  const s = String(v).replace(/[,\s]/g, '')
  const num = Number(s.toString().replace(/[^0-9.-]/g, ''))
  if (Number.isNaN(num)) return ''
  const sign = num < 0 ? '-' : ''
  const abs = Math.abs(Math.round(num))
  const formatted = new Intl.NumberFormat('ko-KR').format(abs)
  return `${sign}₩${formatted}`
}

const formatted = computed(() => {
  const v = props.value
  const out = injected ? injected(v) : localFormat(v)
  return props.hideSymbol ? out.replace(/^(-?)₩/, '$1') : out
})
</script>

<style scoped>
.money {
  font-variant-numeric: tabular-nums;
}
</style>
