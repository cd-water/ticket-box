import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { profileApi, type Profile } from '@/api/profile'
import { useAuthStore } from '@/stores/auth'

export const profileKeys = {
  detail: ['profile'] as const,
}

function syncProfile(profile: Profile) {
  useAuthStore.getState().setProfile({
    nickname: profile.nickname,
    avatar: profile.avatar,
  })
}

export function useProfile() {
  return useQuery({
    queryKey: profileKeys.detail,
    queryFn: () => profileApi.get(),
    staleTime: 30_000,
  })
}

export function useUpdateNickname() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (nickname: string) => profileApi.updateNickname(nickname),
    onSuccess: (profile) => {
      syncProfile(profile)
      queryClient.setQueryData(profileKeys.detail, profile)
    },
  })
}

export function useUploadAvatar() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (file: File) => profileApi.uploadAvatar(file),
    onSuccess: (profile) => {
      syncProfile(profile)
      queryClient.setQueryData(profileKeys.detail, profile)
    },
  })
}

export function useUpdatePassword() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (password: string) => profileApi.updatePassword(password),
    onSuccess: () => {
      useAuthStore.getState().clear()
      queryClient.clear()
    },
  })
}
