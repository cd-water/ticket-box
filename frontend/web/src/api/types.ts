export interface PageResult<T> {
  total: number
  records: T[]
  page: number
  size: number
}
