import React from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { App } from './App';
import { NotFoundError } from './NotFoundError';
import { defaultTheme, Provider } from '@adobe/react-spectrum';
import { Login } from './login/Login';
import { Toaster } from 'react-hot-toast';

const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        errorElement: <NotFoundError />,
    },
    {
        path: '/login',
        element: <Login />,
        errorElement: <NotFoundError />, // TODO: Make an authorized error boundary
    },
]);

const container = document.getElementById('root');
const root = createRoot(container);
root.render(
    <Provider theme={defaultTheme}>
        <RouterProvider router={router} />
        <Toaster position="bottom-center" />
    </Provider>,
);
