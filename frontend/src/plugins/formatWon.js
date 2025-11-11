// Simple plugin to provide a global won formatter and a component
export default {
  install(app) {
    const formatWon = (raw) => {
      if (raw === null || raw === undefined || raw === '') return ''
      // Allow passing numbers or numeric strings
      // Strip non-numeric minus and digits
      const s = String(raw).replace(/[,\s]/g, '')
      // Keep minus and digits and dot
      const num = Number(s.toString().replace(/[^0-9.-]/g, ''))
      if (Number.isNaN(num)) return ''

      const sign = num < 0 ? '-' : ''
      const abs = Math.abs(Math.round(num))
      const formatted = new Intl.NumberFormat('ko-KR').format(abs)
      return `${sign}₩${formatted}`
    }

    app.config.globalProperties.$formatWon = formatWon

    // provide for composition API inject usage
    app.provide('formatWon', formatWon)
  },
}
