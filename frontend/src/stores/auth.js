import { reactive } from 'vue'

const auth = reactive({
  isAuthenticated: false,
  role: 'HQ',
})

function init() {
  try {
    const stored = localStorage.getItem('order101_role')
    if (stored) {
      auth.isAuthenticated = true
      auth.role = stored
    }
  } catch (e) {
    // ignore storage errors in some environments
  }
}

/**
 * Login by role — this is a dev stub to be replaced by real API/JWT later.
 * @param {string} role
 */
function login(role) {
  auth.isAuthenticated = true
  auth.role = role
  try {
    localStorage.setItem('order101_role', role)
  } catch (e) {}
}

function logout() {
  auth.isAuthenticated = false
  auth.role = 'HQ'
  try {
    localStorage.removeItem('order101_role')
  } catch (e) {}
}

function setRole(role) {
  auth.role = role
  try {
    localStorage.setItem('order101_role', role)
  } catch (e) {}
}

init()

export { auth, login, logout, setRole }
