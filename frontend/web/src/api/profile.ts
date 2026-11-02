import http from '@/api/request'

export interface Profile {
  id: number
  phone: string
  nickname: string
  avatar: string
}

export const profileApi = {
  get: () => http.get<Profile>('/user/profile'),
  updateNickname: (nickname: string) =>
    http.post<Profile>('/user/profile/nickname', { nickname }),
  updatePassword: (password: string) =>
    http.post<Profile>('/user/profile/password', { password }),
  uploadAvatar: (file: File) => {
    const form = new FormData()
    form.append('file', file)
    return http.post<Profile>('/user/profile/avatar', form)
  },
}
