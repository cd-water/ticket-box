import http from '@/api/request'

export interface LoginResult {
  tokenName: string
  tokenValue: string
  id: number
  username: string
}

export const authApi = {
  login: (username: string, password: string) =>
    http.post<LoginResult>('/admin/login', { username, password }),
  logout: () => http.post<void>('/admin/logout'),
}
