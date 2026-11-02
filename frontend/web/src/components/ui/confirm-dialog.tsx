import { Dialog } from '@base-ui/react/dialog'
import { Button } from '@/components/ui/button'

interface ConfirmDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  title: string
  description: string
  confirmText?: string
  onConfirm: () => void
}

export function ConfirmDialog({
  open,
  onOpenChange,
  title,
  description,
  confirmText = '确认',
  onConfirm,
}: ConfirmDialogProps) {
  return (
    <Dialog.Root open={open} onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <Dialog.Backdrop className="fixed inset-0 z-50 bg-black/50" />
        <Dialog.Popup className="fixed top-1/2 left-1/2 z-50 w-[min(90vw,360px)] -translate-x-1/2 -translate-y-1/2 rounded-xl border border-border bg-card p-5 shadow-lg">
          <Dialog.Title className="text-lg font-semibold text-foreground">{title}</Dialog.Title>
          <Dialog.Description className="mt-2 text-sm text-muted-foreground">
            {description}
          </Dialog.Description>
          <div className="mt-5 flex justify-end gap-2">
            <Button variant="outline" onClick={() => onOpenChange(false)}>
              再想想
            </Button>
            <Button variant="destructive" onClick={onConfirm}>
              {confirmText}
            </Button>
          </div>
        </Dialog.Popup>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
