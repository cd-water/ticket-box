import { ChevronDown, LogOut, ShoppingBag, UserRound } from 'lucide-react'
import { useNavigate } from 'react-router'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { useLogout } from '@/hooks/useLogout'
import { useUserInfo } from '@/hooks/useUserInfo'
import { useAuthStore } from '@/stores/auth'

export function UserMenu() {
  const navigate = useNavigate()
  const logoutMutation = useLogout()
  const nickname = useAuthStore((s) => s.nickname)
  const avatar = useAuthStore((s) => s.avatar)
  const { data } = useUserInfo()

  const displayName = nickname || data?.nickname || '用户'

  return (
    <DropdownMenu>
      <DropdownMenuTrigger
        openOnHover
        delay={100}
        closeDelay={120}
        className="flex items-center gap-2 rounded-full py-1 pr-2 pl-1 outline-none hover:bg-muted focus-visible:ring-3 focus-visible:ring-ring/50"
      >
        <Avatar size="sm">
          <AvatarImage src={avatar || undefined} alt={displayName} />
          <AvatarFallback>{displayName.slice(0, 1)}</AvatarFallback>
        </Avatar>
        <span className="hidden max-w-32 truncate text-sm font-medium sm:inline">{displayName}</span>
        <ChevronDown className="hidden size-4 text-muted-foreground sm:block" />
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="min-w-36">
        <DropdownMenuItem onClick={() => navigate('/orders')}>
          <ShoppingBag />
          我的订单
        </DropdownMenuItem>
        <DropdownMenuItem onClick={() => navigate('/profile')}>
          <UserRound />
          个人信息
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          variant="destructive"
          onClick={() => logoutMutation.mutate()}
        >
          <LogOut />
          退出登录
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
