import { useEffect, useState } from 'react'
import dayjs from 'dayjs'

export function Countdown({ expireTime }: { expireTime: string }) {
  const [remaining, setRemaining] = useState(() =>
    Math.max(0, dayjs(expireTime).diff(dayjs(), 'second')),
  )

  useEffect(() => {
    setRemaining(Math.max(0, dayjs(expireTime).diff(dayjs(), 'second')))
    const timer = window.setInterval(() => {
      setRemaining((value) => Math.max(0, value - 1))
    }, 1000)
    return () => window.clearInterval(timer)
  }, [expireTime])

  if (remaining <= 0) {
    return <span className="text-destructive">已关闭</span>
  }

  const minutes = Math.floor(remaining / 60)
  const seconds = remaining % 60
  return (
    <span>
      {minutes}:{String(seconds).padStart(2, '0')}
    </span>
  )
}
