import { createBrowserRouter, Navigate } from 'react-router'
import { RequireAuth } from '@/components/RequireAuth'
import AppLayout from '@/layouts/AppLayout'
import LoginPage from '@/pages/LoginPage'
import HomePage from '@/pages/HomePage'
import MoviePage from '@/pages/MoviePage'
import CinemaPage from '@/pages/CinemaPage'
import ShowPage from '@/pages/ShowPage'
import PlayPage from '@/pages/PlayPage'
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
          { path: 'cinema', element: <CinemaPage /> },
          { path: 'show', element: <ShowPage /> },
          { path: 'play', element: <PlayPage /> },
          { path: 'orders', element: <OrdersPage /> },
          { path: 'profile', element: <ProfilePage /> },
        ],
      },
    ],
  },
  { path: '*', element: <Navigate to="/home" replace /> },
])
