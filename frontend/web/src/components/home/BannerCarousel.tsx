import { useEffect, useState } from 'react'
import type { HomeBanner } from '@/api/home'
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
  type CarouselApi,
} from '@/components/ui/carousel'
import { cn } from 'cn'

export function BannerCarousel({ banners }: { banners: HomeBanner[] }) {
  const [api, setApi] = useState<CarouselApi>(undefined)
  const [current, setCurrent] = useState(0)
  const [paused, setPaused] = useState(false)

  useEffect(() => {
    if (!api) return
    const onSelect = () => setCurrent(api.selectedScrollSnap())
    onSelect()
    api.on('select', onSelect)
    return () => {
      api.off('select', onSelect)
    }
  }, [api])

  useEffect(() => {
    if (!api || paused || banners.length < 2) return
    const timer = window.setInterval(() => api.scrollNext(), 4000)
    return () => window.clearInterval(timer)
  }, [api, paused, banners.length])

  return (
    <Carousel
      setApi={setApi}
      opts={{ loop: true }}
      className="w-full"
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
    >
      <CarouselContent>
        {banners.map((banner) => (
          <CarouselItem key={banner.id}>
            <a
              href={banner.linkUrl}
              target="_blank"
              rel="noreferrer"
              className="block overflow-hidden rounded-xl bg-muted"
            >
              <img
                src={banner.imageUrl}
                alt="轮播图"
                className="aspect-[21/7] w-full object-cover"
              />
            </a>
          </CarouselItem>
        ))}
      </CarouselContent>

      {banners.length > 1 ? (
        <>
          <CarouselPrevious />
          <CarouselNext />
          <div className="mt-3 flex justify-center gap-1.5">
            {banners.map((banner, index) => (
              <button
                key={banner.id}
                type="button"
                aria-label={`第 ${index + 1} 张`}
                onClick={() => api?.scrollTo(index)}
                className={cn(
                  'h-1.5 rounded-full transition-all',
                  index === current ? 'w-6 bg-primary' : 'w-1.5 bg-muted-foreground/30',
                )}
              />
            ))}
          </div>
        </>
      ) : null}
    </Carousel>
  )
}
