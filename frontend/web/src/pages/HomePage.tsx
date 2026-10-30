import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from '@/components/ui/empty'

export default function HomePage() {
  return (
    <div className="flex min-h-[60vh] items-center justify-center">
      <div className="w-full max-w-lg rounded-lg bg-card p-10 shadow-sm">
        <Empty className="p-0">
          <EmptyHeader>
            <EmptyTitle className="text-lg">首页</EmptyTitle>
            <EmptyDescription>页面开发中，敬请期待</EmptyDescription>
          </EmptyHeader>
        </Empty>
      </div>
    </div>
  )
}
