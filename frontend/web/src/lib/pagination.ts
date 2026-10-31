export type PageItem = number | 'ellipsis'

export function getPageItems(totalPages: number, current: number): PageItem[] {
  if (totalPages <= 7) {
    return Array.from({ length: totalPages }, (_, index) => index + 1)
  }
  const items: PageItem[] = [1]
  if (current > 4) items.push('ellipsis')
  const start = Math.max(2, current - 1)
  const end = Math.min(totalPages - 1, current + 1)
  for (let page = start; page <= end; page += 1) items.push(page)
  if (current < totalPages - 3) items.push('ellipsis')
  items.push(totalPages)
  return items
}
