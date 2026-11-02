import { createBrowserRouter, Navigate } from 'react-router'
import { RequireAuth } from '@/components/RequireAuth'
import AppLayout from '@/layouts/AppLayout'
import LoginPage from '@/pages/LoginPage'
import HomePage from '@/pages/HomePage'
import MoviePage from '@/pages/MoviePage'
import MovieDetailPage from '@/pages/MovieDetailPage'
import CinemaPage from '@/pages/CinemaPage'
import CinemaDetailPage from '@/pages/CinemaDetailPage'
import SeatSelectionPage from '@/pages/SeatSelectionPage'
import OrderDetailPage from '@/pages/OrderDetailPage'
import ShowPage from '@/pages/ShowPage'
import ShowDetailPage from '@/pages/ShowDetailPage'
import PlayPage from '@/pages/PlayPage'
import PlayDetailPage from '@/pages/PlayDetailPage'
import OrdersPage from '@/pages/OrdersPage'
import ProfilePage from '@/pages/ProfilePage'

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  {
    element: <RequireAuth />,
    children: [
      {
        element: <AppLayout />,
        children: [
          { index: true, element: <Navigate to="/home" replace /> },
          { path: 'home', element: <HomePage /> },
          { path: 'movie', element: <MoviePage /> },
          { path: 'movie/:id', element: <MovieDetailPage /> },
          { path: 'cinema', element: <CinemaPage /> },
          { path: 'cinema/:cinemaId', element: <CinemaDetailPage /> },
          { path: 'screening/:screeningId/seats', element: <SeatSelectionPage /> },
          { path: 'order/:type/:orderNo', element: <OrderDetailPage /> },
          { path: 'show', element: <ShowPage /> },
          { path: 'show/:id', element: <ShowDetailPage /> },
          { path: 'play', element: <PlayPage /> },
          { path: 'play/:id', element: <PlayDetailPage /> },
          { path: 'orders', element: <OrdersPage /> },
          { path: 'profile', element: <ProfilePage /> },
        ],
      },
    ],
  },
  { path: '*', element: <Navigate to="/home" replace /> },
])
