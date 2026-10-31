import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const tokenValue = ref('')
    const username = ref('')
    const isLoggedIn = computed(() => tokenValue.value !== '')

    async function login(usernameInput: string, password: string) {
      const data = await authApi.login(usernameInput, password)
      tokenValue.value = data.tokenValue
      username.value = data.username
    }

    function reset() {
      tokenValue.value = ''
      username.value = ''
    }

    async function logout() {
      try {
        await authApi.logout()
      } finally {
        reset()
      }
    }

    return { tokenValue, username, isLoggedIn, login, logout, reset }
  },
  { persist: true },
)
