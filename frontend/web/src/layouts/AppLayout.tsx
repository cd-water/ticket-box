import { Outlet, useLocation, useNavigate } from 'react-router'
import { BrandMark } from '@/components/BrandMark'
import { UserMenu } from '@/components/UserMenu'
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs'

const NAV_TABS = [
  { value: '/home', label: '首页' },
  { value: '/movie', label: '电影' },
  { value: '/cinema', label: '影院' },
  { value: '/show', label: '演出' },
  { value: '/play', label: '玩乐' },
]

export default function AppLayout() {
  const location = useLocation()
  const navigate = useNavigate()
  const activeTab =
    NAV_TABS.find(
      (tab) => location.pathname === tab.value || location.pathname.startsWith(`${tab.value}/`),
    )?.value ?? location.pathname

  return (
    <div className="flex min-h-svh flex-col bg-background">
      <header className="sticky top-0 z-50 border-b border-border bg-background/95 backdrop-blur">
        <div className="mx-auto flex h-16 w-[min(1180px,calc(100vw-48px))] items-center gap-4 md:gap-8">
          <div className="flex shrink-0 items-center gap-2.5">
            <BrandMark size={34} />
            <span className="hidden text-xl font-semibold tracking-tight whitespace-nowrap text-foreground md:inline">
              TicketBox <span className="font-normal">|</span> 票务盒子
            </span>
          </div>
          <Tabs
            className="min-w-0"
            value={activeTab}
            onValueChange={(value) => navigate(String(value))}
          >
            <TabsList
              variant="line"
              className="max-w-full overflow-x-auto [scrollbar-width:none] [&::-webkit-scrollbar]:hidden [&>*]:flex-none [&>*]:max-md:px-1"
            >
              {NAV_TABS.map((page) => (
                <TabsTrigger key={page.value} value={page.value}>
                  {page.label}
                </TabsTrigger>
              ))}
            </TabsList>
          </Tabs>
          <div className="ml-auto shrink-0">
            <UserMenu />
          </div>
        </div>
      </header>
      <main className="flex-1">
        <div className="mx-auto w-[min(1180px,calc(100vw-48px))] py-9">
          <Outlet />
        </div>
      </main>
    </div>
  )
}
