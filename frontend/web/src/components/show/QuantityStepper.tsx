import { Minus, Plus } from 'lucide-react'
import { Button } from '@/components/ui/button'

export function QuantityStepper({
  value,
  min = 1,
  max,
  onChange,
}: {
  value: number
  min?: number
  max: number
  onChange: (value: number) => void
}) {
  return (
    <div className="flex items-center gap-3">
      <Button
        type="button"
        variant="outline"
        size="icon-sm"
        disabled={value <= min}
        onClick={() => onChange(value - 1)}
      >
        <Minus />
      </Button>
      <span className="w-8 text-center text-sm font-medium">{value}</span>
      <Button
        type="button"
        variant="outline"
        size="icon-sm"
        disabled={value >= max}
        onClick={() => onChange(value + 1)}
      >
        <Plus />
      </Button>
    </div>
  )
}
