import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

export function formatDateTime(value: string) {
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
}

export function fromNow(value: string) {
  return dayjs(value).fromNow()
}

export function formatTime(value: string) {
  return dayjs(value).format('HH:mm')
}

export function formatShowDate(value: string) {
  return dayjs(value).format('MM月DD日')
}
