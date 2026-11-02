import { useEffect, useRef, useState, type ChangeEvent } from 'react'
import { Camera, KeyRound, Save } from 'lucide-react'
import { useNavigate } from 'react-router'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'
import { Input } from '@/components/ui/input'
import { Skeleton } from '@/components/ui/skeleton'
import { toast } from '@/components/ui/toast'
import {
  useProfile,
  useUpdateNickname,
  useUpdatePassword,
  useUploadAvatar,
} from '@/hooks/useProfileQueries'

const DEFAULT_AVATAR = 'https://github.com/cd-water.png'

export default function ProfilePage() {
  const navigate = useNavigate()
  const { data, isPending, isError, refetch } = useProfile()
  const updateNickname = useUpdateNickname()
  const updatePassword = useUpdatePassword()
  const uploadAvatar = useUploadAvatar()

  const fileRef = useRef<HTMLInputElement>(null)
  const [nickname, setNickname] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')

  useEffect(() => {
    if (data) setNickname(data.nickname)
  }, [data])

  if (isPending) {
    return (
      <div className="mx-auto max-w-2xl space-y-4">
        <Skeleton className="h-10 w-1/3" />
        <Skeleton className="h-64 w-full rounded-xl" />
      </div>
    )
  }

  if (isError || !data) {
    return (
      <div className="flex min-h-[50vh] items-center justify-center">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">用户信息加载失败</EmptyTitle>
            <EmptyDescription>请稍后重试</EmptyDescription>
          </EmptyHeader>
          <Button variant="outline" onClick={() => void refetch()}>
            重试
          </Button>
        </Empty>
      </div>
    )
  }

  function saveNickname() {
    const value = nickname.trim()
    if (value.length < 2 || value.length > 20) {
      toast.add({ title: '昵称长度需为 2-20 个字符', type: 'warning' })
      return
    }
    updateNickname.mutate(value, {
      onSuccess: () => toast.add({ title: '昵称已更新', type: 'success' }),
    })
  }

  function selectAvatar(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0]
    event.target.value = ''
    if (!file) return
    if (!['image/jpeg', 'image/png', 'image/webp'].includes(file.type)) {
      toast.add({ title: '仅支持 jpg/png/webp 图片', type: 'warning' })
      return
    }
    if (file.size > 2 * 1024 * 1024) {
      toast.add({ title: '头像不能超过 2MB', type: 'warning' })
      return
    }
    uploadAvatar.mutate(file, {
      onSuccess: () => toast.add({ title: '头像已更新', type: 'success' }),
    })
  }

  function savePassword() {
    if (newPassword.length < 6 || newPassword.length > 20) {
      toast.add({ title: '密码长度需为 6-20 个字符', type: 'warning' })
      return
    }
    if (newPassword !== confirmPassword) {
      toast.add({ title: '两次输入的密码不一致', type: 'warning' })
      return
    }
    updatePassword.mutate(newPassword, {
      onSuccess: () => {
        toast.add({ title: '密码已修改，请重新登录', type: 'success' })
        navigate('/login', { replace: true })
      },
    })
  }

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <h1 className="text-2xl font-semibold text-foreground">个人信息</h1>

      <section className="flex flex-wrap items-center gap-5 rounded-xl border border-border bg-card p-6">
        <button
          type="button"
          aria-label="更换头像"
          className="group relative rounded-full outline-none focus-visible:ring-3 focus-visible:ring-ring/50"
          onClick={() => fileRef.current?.click()}
          disabled={uploadAvatar.isPending}
        >
          <Avatar size="lg" className="size-20">
            <AvatarImage src={data.avatar || DEFAULT_AVATAR} alt={data.nickname} />
            <AvatarFallback>{data.nickname.slice(0, 1)}</AvatarFallback>
          </Avatar>
          <span className="absolute inset-0 flex items-center justify-center rounded-full bg-black/45 text-white opacity-0 transition-opacity group-hover:opacity-100">
            <Camera className="size-5" />
          </span>
        </button>
        <input
          ref={fileRef}
          type="file"
          accept="image/jpeg,image/png,image/webp"
          className="hidden"
          onChange={selectAvatar}
        />
        <div className="space-y-1">
          <p className="text-lg font-semibold text-foreground">{data.nickname}</p>
          <p className="text-sm text-muted-foreground">{data.phone}</p>
          <p className="text-xs text-muted-foreground">
            {uploadAvatar.isPending ? '头像上传中…' : '点击头像更换，支持 jpg/png/webp，不超过 2MB'}
          </p>
        </div>
      </section>

      <section className="space-y-3 rounded-xl border border-border bg-card p-6">
        <h2 className="text-lg font-semibold text-foreground">昵称</h2>
        <div className="flex gap-2">
          <Input
            value={nickname}
            onChange={(event) => setNickname(event.target.value)}
            maxLength={20}
            aria-label="昵称"
            className="h-10 bg-background"
          />
          <Button
            className="h-10 shrink-0"
            disabled={updateNickname.isPending || nickname.trim() === data.nickname}
            onClick={saveNickname}
          >
            <Save />
            保存
          </Button>
        </div>
      </section>

      <section className="space-y-3 rounded-xl border border-border bg-card p-6">
        <h2 className="text-lg font-semibold text-foreground">修改密码</h2>
        <p className="text-xs text-muted-foreground">
          修改成功后所有设备将退出登录，需要重新登录。
        </p>
        <Input
          type="password"
          value={newPassword}
          onChange={(event) => setNewPassword(event.target.value)}
          placeholder="新密码（6-20 位）"
          aria-label="新密码"
          autoComplete="new-password"
          className="h-10 bg-background"
        />
        <Input
          type="password"
          value={confirmPassword}
          onChange={(event) => setConfirmPassword(event.target.value)}
          placeholder="确认新密码"
          aria-label="确认新密码"
          autoComplete="new-password"
          className="h-10 bg-background"
        />
        <Button
          className="h-10"
          disabled={
            updatePassword.isPending || !newPassword || !confirmPassword
          }
          onClick={savePassword}
        >
          <KeyRound />
          {updatePassword.isPending ? '提交中…' : '修改密码'}
        </Button>
      </section>
    </div>
  )
}
